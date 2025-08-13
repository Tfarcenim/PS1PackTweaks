package tfar.ps1packtweaks.util;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import tfar.ps1packtweaks.PS1PackTweaks;
import tfar.ps1packtweaks.PS1PackTweaksConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class TreeScanner {


    private final BlockPos origin;
    private final Predicate<BlockState> eligibleForDestruction;
    private final Vec3 look;
    List<BlockPos> treeStarts = new ArrayList<>();


    protected int treeStartScanSpeed = 100;
    protected int treeStartScanPos;
    List<BlockPos> toDestroy = new ArrayList<>();

    protected enum Stage {
        SCANNING_TREE_STARTS, SCANNING_TREE_BLOCKS, BREAKING,FINISHED
    }

    protected Stage stage = Stage.SCANNING_TREE_STARTS;

    public TreeScanner(BlockPos origin, Predicate<BlockState> eligibleForDestruction, Vec3 look) {
        this.origin = origin;
        this.eligibleForDestruction = eligibleForDestruction;
        this.look = look;
    }

    public boolean isBehind(BlockPos pos) {

        if (pos.distSqr(origin) < 64) {
            return false;
        }

        BlockPos subtract = pos.subtract(origin);



        double degrees = Mth.atan2(subtract.getX() - look.x, subtract.getZ() - look.z) * 180 / Math.PI;
        System.out.println(degrees);
        return false;
    }

    public void tick(ServerLevel level) {
        switch (stage) {
            case SCANNING_TREE_STARTS -> {
                long start = Util.getNanos();
                int radius = PS1PackTweaksConfig.SERVER.disappearingLeavesRadius.get();
                BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
                int originx = origin.getX();
                int originz = origin.getZ();
                for (int z = -radius; z < radius; z++) {
                    for (int x = -radius; x < radius; x++) {
                        int height = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, originx + x, originz + z);
                        mutableBlockPos.set(originx + x, height - 1, originz + z);
                        BlockState state = level.getBlockState(mutableBlockPos);
                        if (state.is(BlockTags.LOGS) && state.getValue(RotatedPillarBlock.AXIS).isVertical()) {
                            BlockState belowState = level.getBlockState(mutableBlockPos.below());
                            if (belowState.is(BlockTags.LEAVES)) continue;

                            while (belowState.is(BlockTags.LOGS)) {
                                mutableBlockPos.move(Direction.DOWN);
                                belowState = level.getBlockState(mutableBlockPos.below());
                            }
                            if (isBehind(mutableBlockPos)) {
                                treeStarts.add(mutableBlockPos.immutable());
                            }
                        }
                    }
                }
                long end = Util.getNanos();
                stage = Stage.SCANNING_TREE_BLOCKS;
                long elapse = end - start;
                if (PS1PackTweaks.DEV) {
                    PS1PackTweaks.LOGGER.info("Scanning tree starts took {} ms, found {} possible trees", elapse / 1_000_000d, treeStarts.size());
                }
            }
            case SCANNING_TREE_BLOCKS -> {
                long start = Util.getNanos();

                int endPos = Math.min(treeStarts.size(),treeStartScanPos+treeStartScanSpeed);
                boolean reachEnd = endPos == treeStarts.size();
                for (int i = treeStartScanPos; i < endPos; i++) {
                    treeStartScanPos++;
                    BlockPos pos = treeStarts.get(i);
                    Iterable<BlockPos> iterable = TreeAOEIterator.calculate(level, pos, block -> block.is(BlockTags.LEAVES) || block.is(BlockTags.LOGS), 0, 0);
                    iterable.forEach(pos1 -> {
                        BlockState state = level.getBlockState(pos1);
                        if (eligibleForDestruction.test(state)) {
                            toDestroy.add(pos1);
                        }
                    });
                }

                long end = Util.getNanos();
                long elapse = end-start;
                if (PS1PackTweaks.DEV) {
                    PS1PackTweaks.LOGGER.info("Scanning {} tree blocks took {} ms, {} blocks scheduled for destruction",endPos, elapse / 1_000_000d,toDestroy.size());
                }
                if (reachEnd) {
                    stage = Stage.BREAKING;
                }
            }

            case BREAKING -> {
                long start = Util.getNanos();

                for (BlockPos pos : toDestroy) {
                    level.destroyBlock(pos,false);
                }

                long end = Util.getNanos();
                long elapse = end-start;
                if (PS1PackTweaks.DEV) {
                    PS1PackTweaks.LOGGER.info("Breaking took {} ms", elapse / 1_000_000d);
                }
                stage = Stage.FINISHED;
            }
        }
    }

    public boolean isFinished() {
        return stage == Stage.FINISHED;
    }

}
