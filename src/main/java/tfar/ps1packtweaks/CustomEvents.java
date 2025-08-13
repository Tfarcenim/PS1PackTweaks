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
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import tfar.ps1packtweaks.entity.HerobrineEntity;
import tfar.ps1packtweaks.util.TreeScanner;

import java.util.*;

public class CustomEvents {

    public static void handleEvents(ServerPlayer player) {
        tickHerobrineSpawn(player);
        tickFallingAnimalSpawn(player);
        tickVanishingLeaves(player);
        tickVanishingLogs(player);
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




    public static TreeScanner treeScanner;

    public static void tickVanishingLeaves(ServerPlayer player) {
        ServerLevel serverLevel = player.getLevel();

        long tick = serverLevel.getGameTime();

        if (treeScanner == null && tick % PS1PackTweaksConfig.SERVER.disappearingLeavesDelay.get() == 0 &&
                player.getRandom().nextDouble() < PS1PackTweaksConfig.SERVER.disappearingLeavesChance.get()) {
            treeScanner = new TreeScanner(player.blockPosition(),state -> state.is(BlockTags.LEAVES)&& !state.getValue(LeavesBlock.PERSISTENT),player.getLookAngle());
        }

        if (treeScanner != null) {
            treeScanner.tick(serverLevel);
            if (treeScanner.isFinished()) {
                treeScanner = null;
            }
        }
    }

    public static void tickVanishingLogs(ServerPlayer player) {
        ServerLevel serverLevel = player.getLevel();

        long tick = serverLevel.getGameTime();

        if (treeScanner == null && tick % PS1PackTweaksConfig.SERVER.disappearingLogsDelay.get() == 0 &&
                player.getRandom().nextDouble() < PS1PackTweaksConfig.SERVER.disappearingLogsChance.get()) {
            treeScanner = new TreeScanner(player.blockPosition(),state -> state.is(BlockTags.LOGS),player.getLookAngle());
        }

        if (treeScanner != null) {
            treeScanner.tick(serverLevel);
            if (treeScanner.isFinished()) {
                treeScanner = null;
            }
        }
    }

    //[Tunnels forming] - 2x2 tunnels should form in the side of mountains and underground when the player is mining.
    // There should occasionally be a redstone torch in these tunnels.

    public static void tickTunnels(ServerPlayer player) {
        if (player.getRandom().nextDouble() < PS1PackTweaksConfig.SERVER.tunnelChance.get()) {
            Direction direction = player.getDirection().getOpposite();
            int length = PS1PackTweaksConfig.SERVER.tunnelSizeMin.get() + player.getRandom().nextInt(PS1PackTweaksConfig.SERVER.tunnelSizeMax.get()
                    - PS1PackTweaksConfig.SERVER.tunnelSizeMin.get());

            boolean withTorch = player.getRandom().nextDouble() < PS1PackTweaksConfig.SERVER.tunnelTorchChance.get();

            ServerLevel level = player.getLevel();

            BlockPos origin = player.blockPosition().relative(direction,2);
            for (int i = 0; i < length;i++) {
                BlockPos relative = origin.relative(direction, i);
                BlockPos relativeAbove = origin.relative(Direction.UP).relative(direction, i);

                BlockPos relativeSide = origin.relative(direction, i).relative(direction.getClockWise());
                BlockPos relativeSideAbove = origin.relative(Direction.UP).relative(direction, i).relative(direction.getClockWise());
                List<BlockPos> toTunnel = List.of(relative,relativeAbove,relativeSide,relativeSideAbove);
                for (BlockPos pos : toTunnel) {
                    BlockState state = level.getBlockState(pos);
                    if (state.getDestroySpeed(level,pos) >= 0) {
                        level.destroyBlock(pos,false);
                        if (withTorch) {
                            level.setBlockAndUpdate(pos,Blocks.REDSTONE_TORCH.defaultBlockState());
                            withTorch = false;
                        }
                    }
                }
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
