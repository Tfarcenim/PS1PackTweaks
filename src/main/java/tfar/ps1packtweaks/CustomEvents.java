package tfar.ps1packtweaks;

import com.mojang.datafixers.util.Pair;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import tfar.ps1packtweaks.entity.HerobrineEntity;
import tfar.ps1packtweaks.entity.InvisibleEntity;
import tfar.ps1packtweaks.util.TreeScanner;

import java.util.*;

public class CustomEvents {

    public static final String FOLLOW = PS1PackTweaks.id("follow").toString();

    public static void handleEvents(ServerPlayer player) {
        tickHerobrineSpawn(player);
        tickFallingAnimalSpawn(player);
        tickVanishingLeaves(player);
        tickVanishingLogs(player);
        tickTunnels(player);
        playRandomSound(player);
        burnWhenLookingUp(player);
        breakLookedAtGlass(player);
        invisibleEntity(player);
        followPlayer(player);
        updateNearby(player);
    }

    static void updateNearby(ServerPlayer player) {
        List<LivingEntity> nearby = player.level.getNearbyEntities(LivingEntity.class, TargetingConditions.DEFAULT, player, player.getBoundingBox().inflate(16));
        for (LivingEntity living : nearby) {
            Init.PLAYER_FOUND_ENTITY.trigger(player, living);
        }
    }

    static void followPlayer(ServerPlayer player) {
        CompoundTag persist = player.getPersistentData();
        if(player.getRandom().nextDouble() <PS1PackTweaksConfig.SERVER.mobsFollowPlayerChance.get()) {
            persist.putInt(FOLLOW,PS1PackTweaksConfig.SERVER.mobsFollowPlayerDuration.get());
        }

        if (persist.getInt(FOLLOW) > 0) {
            persist.putInt(FOLLOW,persist.getInt(FOLLOW) -1);
        }

    }

    static void invisibleEntity(ServerPlayer player) {
        if(player.getRandom().nextDouble() <PS1PackTweaksConfig.SERVER.invisible_entity_chance.get()){
            BlockPos pos = player.blockPosition();
            int attempt = 0;
            BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
            while (attempt <64){
                attempt++;
                int x = pos.getX() + pickNumber(player.getRandom(),16);
                int y = pos.getY() + pickNumber(player.getRandom(),16);
                int z = pos.getZ() + pickNumber(player.getRandom(),16);
                mutableBlockPos.set(x,y,z);
                if (NaturalSpawner.isSpawnPositionOk(SpawnPlacements.Type.ON_GROUND,player.getLevel(),mutableBlockPos,Init.ModEntityTypes.INVISIBLE_ENTITY)) {
                    InvisibleEntity entity = (InvisibleEntity) Init.ModEntityTypes.INVISIBLE_ENTITY.spawn(player.getLevel(), null, null,
                            mutableBlockPos,MobSpawnType.EVENT,false,false);
                    //entity.setInvisible(true);
                    entity.setInvulnerable(true);
                    break;
                }
            }
        }

    }

    static void breakLookedAtGlass(ServerPlayer player) {
        BlockHitResult trace = (BlockHitResult) player.pick(5,0,false);
        BlockPos pos = trace.getBlockPos();
        BlockState state = player.level.getBlockState(pos);
        if (state.is(ModTags.HEROBRINE_SPAWNS_BEHIND) && player.getRandom().nextDouble() < PS1PackTweaksConfig.SERVER.break_glass_looked_at_chance.get()) {
            player.level.destroyBlock(pos,true);
        }
    }

    static void burnWhenLookingUp(ServerPlayer player) {
        boolean isEligibleToBurn = isSunBurnTick(player);
        if (isEligibleToBurn) {
            player.setSecondsOnFire(1);
        }
    }

    protected static boolean isSunBurnTick(LivingEntity entity) {
        if (entity.getXRot() < -60 && entity.getRandom().nextDouble() < PS1PackTweaksConfig.SERVER.look_up_burn_chance.get() /*&& entity.level.isDay()*/ && !entity.level.isClientSide) {
            float f = entity.getBrightness();
            BlockPos blockpos = new BlockPos(entity.getX(), entity.getEyeY(), entity.getZ());
            boolean flag = entity.isInWaterRainOrBubble() || entity.isInPowderSnow || entity.wasInPowderSnow;
            return /*f > 0.5F && entity.getRandom().nextFloat() * 30.0F < (f - 0.4F) * 2.0F &&*/ !flag && entity.level.canSeeSky(blockpos);
        }

        return false;
    }


    // Doors opening, player taking damage, player falling, item pickup, footsteps, block breaking.
    enum RandomSoundEvent{
        DOOR_OPEN,PLAYER_HURT,PLAYER_FALL,ITEM_PICKUP,FOOTSTEPS,BLOCK_BREAK;
        static final RandomSoundEvent[] VALUES = values();
    }

    static void playRandomSound(ServerPlayer player) {
        ServerLevel level = player.getLevel();
        if (level.dimension() == Level.OVERWORLD) {
            long time = level.getGameTime();
            if (time % PS1PackTweaksConfig.SERVER.randomSoundDelay.get() == 0) {
                Random random = player.getRandom();
                RandomSoundEvent randomSoundEvent = RandomSoundEvent.VALUES[random.nextInt(RandomSoundEvent.VALUES.length)];
                switch (randomSoundEvent) {
                    case DOOR_OPEN -> {
                        int attempt = 0;
                        int r = 16;
                        while (attempt < 64) {
                            attempt++;
                            BlockPos pos = getRandomInCube(player.blockPosition(),r,random);
                            BlockState state = level.getBlockState(pos);
                            if (state.getBlock() instanceof DoorBlock doorBlock) {
                                doorBlock.playSound(level,pos,!state.getValue(DoorBlock.OPEN));
                                break;
                            } else if (state.getBlock() instanceof TrapDoorBlock trapDoorBlock) {
                                trapDoorBlock.playSound(player,level,pos,state.getValue(TrapDoorBlock.OPEN));
                                break;
                            } else {
                                continue;
                            }
                        }
                        //oh well
                    }
                    case FOOTSTEPS -> {
                        int attempt = 0;
                        int r = 16;
                        while (attempt < 64) {
                            attempt++;
                            BlockPos pos = getRandomInCube(player.blockPosition(), r, random);
                            BlockState state = level.getBlockState(pos);
                            if (!state.getCollisionShape(level,pos).isEmpty() && !state.getMaterial().isLiquid()) {
                                playStepSound(player,pos,state);
                                break;
                            }
                        }
                    }
                    case PLAYER_HURT -> {
                        int attempt = 0;
                        int r = 16;
                        while (attempt < 64) {
                            attempt++;
                            BlockPos pos = getRandomInCube(player.blockPosition(), r, random);
                            BlockState state = level.getBlockState(pos);
                            if (!state.getCollisionShape(level,pos).isEmpty() && !state.getMaterial().isLiquid()) {
                                player.level.playSound(null,pos, SoundEvents.PLAYER_HURT,player.getSoundSource(), 1,1);
                                break;
                            }
                        }
                    }
                    case PLAYER_FALL -> {
                        int attempt = 0;
                        int r = 16;
                        while (attempt < 64) {
                            attempt++;
                            BlockPos pos = getRandomInCube(player.blockPosition(), r, random);
                            BlockState state = level.getBlockState(pos);
                            if (!state.getCollisionShape(level,pos).isEmpty() && !state.getMaterial().isLiquid()) {
                                playBlockFallSound(player,pos);
                                break;
                            }
                        }
                    }
                    case BLOCK_BREAK -> {
                        int attempt = 0;
                        int r = 16;
                        while (attempt < 64) {
                            attempt++;
                            BlockPos pos = getRandomInCube(player.blockPosition(), r, random);
                            BlockState state = level.getBlockState(pos);
                            if (!state.getCollisionShape(level,pos).isEmpty() && !state.getMaterial().isLiquid()) {
                                level.levelEvent(player, LevelEvent.PARTICLES_DESTROY_BLOCK, pos, Block.getId(state));
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Plays the fall sound for the block landed on
     */
    protected static void playBlockFallSound(ServerPlayer player, BlockPos pos) {
        if (!player.isSilent()) {
            int i = Mth.floor(pos.getX());
            int j = Mth.floor(pos.getY() - (double)0.2F);
            int k = Mth.floor(pos.getZ());
            pos = new BlockPos(i, j, k);
            BlockState blockstate = player.level.getBlockState(pos);
            if (!blockstate.isAir()) {
                SoundType soundtype = blockstate.getSoundType(player.level, pos, player);
                player.level.playSound(null,pos, soundtype.getFallSound(),player.getSoundSource(), soundtype.getVolume() * 0.5F, soundtype.getPitch() * 0.75F);
                //this.playSound(soundtype.getFallSound(), soundtype.getVolume() * 0.5F, soundtype.getPitch() * 0.75F);
            }
        }
    }

    protected static void playStepSound(ServerPlayer player,BlockPos pPos, BlockState pState) {
        if (!pState.getMaterial().isLiquid()) {
            BlockState blockstate = player.level.getBlockState(pPos.above());
            SoundType soundtype = blockstate.is(Blocks.SNOW) ? blockstate.getSoundType(player.level, pPos, player) : pState.getSoundType(player.level, pPos, player);
            player.level.playSound(null,pPos,soundtype.getStepSound(),player.getSoundSource(), soundtype.getVolume() * 0.15F, soundtype.getPitch());
        }
    }


    static BlockPos getRandomInCube(BlockPos pos,int r,Random random) {
        int x = pos.getX()+ pickNumber(random,r);
        int y = pos.getY()+ pickNumber(random,r);
        int z = pos.getZ()+ pickNumber(random,r);
        return new BlockPos(x,y,z);
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
    // The distance should be far enough that he's partially obscured by the advancement fog, but not completely.
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
        ServerLevel serverLevel = player.getLevel();

        long tick = serverLevel.getGameTime();
        if (tick % PS1PackTweaksConfig.SERVER.tunnelDelay.get() == 0 && player.getRandom().nextDouble() < PS1PackTweaksConfig.SERVER.tunnelChance.get()) {
            Random random =player.getRandom();
            Pair<BlockPos, Direction> tunnelPos = pickRandomTunnelStartPos(serverLevel, random, player.blockPosition(),PS1PackTweaksConfig.SERVER.tunnelYMax.get());

            if (tunnelPos != null) {
                Direction direction = tunnelPos.getSecond().getOpposite();
                int length = PS1PackTweaksConfig.SERVER.tunnelSizeMin.get() + player.getRandom().nextInt(PS1PackTweaksConfig.SERVER.tunnelSizeMax.get()
                        - PS1PackTweaksConfig.SERVER.tunnelSizeMin.get());

                boolean withTorch = player.getRandom().nextDouble() < PS1PackTweaksConfig.SERVER.tunnelTorchChance.get();

                ServerLevel level = player.getLevel();

                BlockPos origin = tunnelPos.getFirst();
                BlockState torchState =  Blocks.REDSTONE_TORCH.defaultBlockState();
                for (int i = 0; i < length; i++) {
                    BlockPos relative = origin.relative(direction, i);
                    BlockPos relativeAbove = origin.relative(Direction.UP).relative(direction, i);

                    BlockPos relativeSide = origin.relative(direction, i).relative(direction.getClockWise());
                    BlockPos relativeSideAbove = origin.relative(Direction.UP).relative(direction, i).relative(direction.getClockWise());
                    List<BlockPos> toTunnel = List.of(relative, relativeAbove, relativeSide, relativeSideAbove);
                    for (BlockPos pos : toTunnel) {
                        BlockState state = level.getBlockState(pos);
                        if (state.getDestroySpeed(level, pos) >= 0) {
                            level.destroyBlock(pos, false);
                            if (withTorch && torchState.canSurvive(level,pos)) {
                                level.setBlockAndUpdate(pos,torchState);
                                withTorch = false;
                            }
                        }
                    }
                }
            }
        }
    }

    static Pair<BlockPos,Direction> pickRandomTunnelStartPos(ServerLevel level, Random random, BlockPos playerPos, int maxY) {
        int attempt = 0;
        int r = 128;
        int xOrigin = playerPos.getX();
        int zOrigin = playerPos.getZ();
        int minTunnelHeight = level.getMinBuildHeight();
        int maxTunnelHeight =Math.min(maxY,level.getMaxBuildHeight())-1;
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        while (attempt < 128) {
            attempt++;
            int x = xOrigin + pickNumber(random,r);
            int y = minTunnelHeight + random.nextInt(maxTunnelHeight - minTunnelHeight+1);
            int z = zOrigin + pickNumber(random,r);
            pos.set(x,y,z);
            if (pos.distSqr(playerPos) < 1024) continue;

            if (!level.getBlockState(pos).is(ModTags.CAN_TUNNEL_THROUGH)) continue;

            for (Direction direction : h_directions) {
                boolean adjacent = level.getBlockState(pos.relative(direction)).isAir();
                if (!adjacent)continue;
                return Pair.of(pos,direction);
            }

        }
        return null;
    }

    static int pickNumber(Random random,int r) {
        return random.nextInt(2*r+1)-r;
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
