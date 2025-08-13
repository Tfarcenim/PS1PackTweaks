package tfar.ps1packtweaks.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.state.BlockState;


/** Logic for iterating over a set of blocks */
    public interface AreaOfEffectIterator {

        /** Interface for loadable area of effect iterators, used for the fallback AOE iterator */
        interface Loadable extends AreaOfEffectIterator{
        }

    /**
         * Gets a list of blocks that the tool can affect.
         *
         * @param context     Context for the original target. Note the hit location is unavailable during block breaking.
         * @param matchType   Type of match
         * @return A list of BlockPos's that the AOE tool can affect. Note these positions will likely be mutable
         */
        Iterable<BlockPos> getBlocks(UseOnContext context, BlockState state, AOEMatchType matchType);

        /** Match types for the AOE getter */
        enum AOEMatchType {
            /** Used when the block is being broken, typically matches only harvestable blocks
             * When using this type, the iteratable should be fetched before breaking the block */
            BREAKING,
            /** Used for right click interactions such as hoeing, typically matches any block (will filter later) */
            TRANSFORM,
            /** Used for wireframe display in world */
            DISPLAY
        }
    }
