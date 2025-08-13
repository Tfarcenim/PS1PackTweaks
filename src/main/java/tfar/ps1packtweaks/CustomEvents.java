package tfar.ps1packtweaks;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import tfar.ps1packtweaks.entity.HerobrineEntity;
import tfar.ps1packtweaks.util.TreeAOEIterator;

import java.util.*;

public class CustomEvents {

    public static void handleEvents(ServerPlayer player) {
        tickHerobrineSpawn(player);
        tickFallingAnimalSpawn(player);
        tickVanishingLeaves(player);
    }

    static void tickFallingAnimalSpawn(ServerPlayer player) {
        if (player.level.dimension() == Level.OVERWORLD) {
            long time = player.level.getGameTime();
            if (time % PS1PackTweaksConfig.SERVER.minFallingAnimalDelay.get() == 0 &&
                    player.getRandom().nextDouble() < PS1PackTweaksConfig.SERVER.fallingAnimalChance.get()) {
                EntityType<?> type = Registry.ENTITY_TYPE.get(
                        new ResourceLocation(Util.getRandom(PS1PackTweaksConfig.SERVER.fallingAnimalTypes.get(), player.getRandom())));
                Vec3 vec3 = player.position().add(player.getLookAngle().scale(6));
                BlockPos top = new BlockPos(vec3);
                BlockPos dropLocation = player.level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, top);
                int h = 20;
                BlockPos spawnPos = dropLocation.above(h);

                for (int i = h + dropLocation.getY(); i > player.level.getMinBuildHeight(); i--) {
                    BlockPos pos = dropLocation.above(i);
                    BlockState state = player.level.getBlockState(pos);

                    FluidState fluidState = player.level.getFluidState(pos);
                    if (fluidState.is(FluidTags.WATER)) {
                        return;
                    } else if (!state.getCollisionShape(player.level, pos).isEmpty()) {
                        break;
                    }
                }
                Entity spawn = type.spawn(player.getLevel(), null, null, spawnPos,
                        MobSpawnType.EVENT, false, false);
                if (spawn instanceof Mob mob) {
                    mob.setHealth(Float.MIN_VALUE);
                }
            }
        }
    }

    //Herobrine should spawn in the distance when the player isn't looking, and disappear when the player looks.
    // The distance should be far enough that he's partially obscured by the shader fog, but not completely.
    // He should also appear outside of the players windows and disapear when looked at.
    // Sometimes he will not disappear when looked at, and instead start running away from the player.
    // The player should never be able to catch up to him. Sometimes instead of doing either of these,
    // he will teleport to the block directly in front of the player and give the player the blindness effect and then disappear.
    // Sometimes instead of appearing standing still, he should be running away from an invisible entity.
    // The invisible entity should make randomly selected cave sounds and warden noises.


    public static void tickHerobrineSpawn(ServerPlayer player) {
        ServerLevel serverLevel = player.getLevel();

        if (serverLevel.getGameTime() % PS1PackTweaksConfig.SERVER.minHerobrineDelay.get() == 0 && !serverLevel.isDay()) {
            if (!serverLevel.getEntitiesOfClass(HerobrineEntity.class,player.getBoundingBox().inflate(128,100,128)).isEmpty())return;


           // long start = Util.getNanos();

          //  long end = Util.getNanos();

          //  System.out.println("Entity search time: " + (end - start) / 1_000_000d + " ms");

            Random randomSource = serverLevel.random;

            if (randomSource.nextBoolean() && trySpawnBehindGlass(player)) return;


            float yRot = player.getYRot();

            double angle = 180 * randomSource.nextDouble() + yRot + 180;
            double distance = PS1PackTweaksConfig.SERVER.herobrineSpawnDistance.get();
            Vec3 playerPos = player.position();
            Vec2 attempt = addPolar(playerPos, distance, angle);
            int height = serverLevel.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (int) attempt.x, (int) attempt.y);
            BlockPos spawnPos = new BlockPos(attempt.x, height, attempt.y);

            boolean dark = Monster.isDarkEnoughToSpawn(serverLevel, spawnPos, serverLevel.random);
            if (dark) {
                HerobrineEntity herobrine = (HerobrineEntity) Init.ModEntityTypes.HEROBRINE.spawn(serverLevel, null,
                        null, spawnPos, MobSpawnType.EVENT, false, false);
            }
        }
    }

    enum Stage {
        IDLE,SCANNING,BREAKING
    }

    static Stage leavesBreakingStage = Stage.IDLE;
    static List<BlockPos> treeStarts = new ArrayList<>();

    static List<BlockPos> toDestroy = new ArrayList<>();

    public static void tickVanishingLeaves(ServerPlayer player) {
        ServerLevel serverLevel = player.getLevel();

        if (leavesBreakingStage == Stage.BREAKING) {
            long start = Util.getNanos();

            long end = Util.getNanos();
            long elapse = end-start;
            if (PS1PackTweaks.DEV) {
                PS1PackTweaks.LOGGER.info("Breaking took {} ms", elapse / 1_000_000d);
            }
            leavesBreakingStage = Stage.IDLE;
        }

        if (leavesBreakingStage == Stage.SCANNING) {
            long start = Util.getNanos();
            toDestroy.clear();

            for (BlockPos pos : treeStarts) {
                Iterable<BlockPos> iterable = TreeAOEIterator.calculate(serverLevel,pos, block -> block.is(BlockTags.LEAVES) || block.is(BlockTags.LOGS),0,0);
                iterable.forEach(pos1 -> toDestroy.add(pos1));
            }

            long end = Util.getNanos();
            long elapse = end-start;
            if (PS1PackTweaks.DEV) {
                PS1PackTweaks.LOGGER.info("Scanning tree blocks took {} ms, {} blocks scheduled for destruction", elapse / 1_000_000d,toDestroy.size());
            }
            leavesBreakingStage = Stage.BREAKING;
        }

        long tick = serverLevel.getGameTime();

        if (tick % PS1PackTweaksConfig.SERVER.disappearingLeavesDelay.get() == 0 &&
                player.getRandom().nextDouble() < PS1PackTweaksConfig.SERVER.disappearingLeavesChance.get()) {
            leavesBreakingStage = Stage.SCANNING;
            treeStarts.clear();
            long start = Util.getNanos();
            int radius = PS1PackTweaksConfig.SERVER.disappearingLeavesRadius.get();
            BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
            BlockPos playerPos = player.blockPosition();
            int originx = playerPos.getX();
            int originz = playerPos.getZ();
            for (int z = -radius; z < radius; z++) {
                for (int x = -radius; x < radius; x++) {
                    int height = serverLevel.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,originx+x,originz+z);
                    mutableBlockPos.set(originx+x,height-1,originz+z);
                    BlockState state = serverLevel.getBlockState(mutableBlockPos);
                    if (state.is(BlockTags.LOGS) && state.getValue(RotatedPillarBlock.AXIS).isVertical()) {
                        BlockState belowState = serverLevel.getBlockState(mutableBlockPos.below());
                        if (belowState.is(BlockTags.LEAVES)) continue;

                        while (belowState.is(BlockTags.LOGS)) {
                            mutableBlockPos.move(Direction.DOWN);
                            belowState = serverLevel.getBlockState(mutableBlockPos.below());
                        }
                        treeStarts.add(mutableBlockPos.immutable());
                    }
                }
            }
            long end = Util.getNanos();
            long elapse = end-start;
            if (PS1PackTweaks.DEV) {
                PS1PackTweaks.LOGGER.info("Scanning tree starts took {} ms, found {} possible trees", elapse / 1_000_000d,treeStarts.size());
            }
        }

    }

    static final Direction[] h_directions = new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};

    static boolean trySpawnBehindGlass(ServerPlayer player) {
        ServerLevel level = player.getLevel();
        int r = 32;

        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        BlockPos pos = player.blockPosition();
        List<BlockPos> glass = new ArrayList<>();
        for (int y = -8; y < 8; y++) {
            for (int z = -r; z < r; z++) {
                for (int x = -r; x < r; x++) {
                    mutableBlockPos.set(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
                    BlockState state = level.getBlockState(mutableBlockPos);
                    if (state.is(ModTags.HEROBRINE_SPAWNS_BEHIND)) {
                        glass.add(mutableBlockPos.immutable());
                    }
                }
            }
        }


        if (!glass.isEmpty()) {
            Collections.shuffle(glass);
            for (BlockPos pos1 : glass) {
                for (Direction direction : h_directions) {
                    BlockPos offset = pos1.relative(direction).below();

                    boolean entityColliding = level.getEntityCollisions(null, Init.ModEntityTypes.HEROBRINE.getAABB(offset.getX() + 0.5D,
                            offset.getY(), offset.getZ() + 0.5D)).isEmpty();
                    if (entityColliding && level.canSeeSky(offset) && NaturalSpawner.isSpawnPositionOk(SpawnPlacements.Type.ON_GROUND, level, offset,
                            Init.ModEntityTypes.HEROBRINE)) {
                        HerobrineEntity herobrine = (HerobrineEntity) Init.ModEntityTypes.HEROBRINE.spawn(level, null,
                                null, offset, MobSpawnType.EVENT, false, false);
                        return true;
                    }
                }
            }
        }
        return true;
    }

    //x = r cos θ , y = r sin θ
    public static Vec2 addPolar(Vec3 vec3, double radius, double angleDegrees) {
        double x = radius * Math.cos(angleDegrees * Math.PI / 180);
        double z = radius * Math.sin(angleDegrees * Math.PI / 180);

        return new Vec2((float) (x + vec3.x), (float) (z + vec3.z));
    }
}
