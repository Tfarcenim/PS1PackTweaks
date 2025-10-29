package tfar.ps1packtweaks.mixin.compat;

import com.minecraftserverzone.coppergolem.NewLightningRodBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;
import java.util.function.Predicate;

@Mixin(NewLightningRodBlock.class)
public class NewLightningRodBlockMixin {

    @Shadow(remap = false) @Nullable private BlockPattern ironGolemFull;

    @Shadow(remap = false) @Final private static Predicate<BlockState> LIGHTNING_ROD_PREDICATE;

    @Shadow(remap = false) @Final private static Predicate<BlockState> COPPER_BLOCKS_PREDICATE;

    /**
     * @author
     * @reason
     */
    @Overwrite(remap = false)
    private BlockPattern getOrCreateIronGolemFull() {
        if (this.ironGolemFull == null) {
            this.ironGolemFull = BlockPatternBuilder.start().aisle("^", "x", "#").where('^', BlockInWorld.hasState(LIGHTNING_ROD_PREDICATE)).where('x', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.CARVED_PUMPKIN))).where('#', BlockInWorld.hasState(COPPER_BLOCKS_PREDICATE)).build();
        }

        return this.ironGolemFull;
    }

}