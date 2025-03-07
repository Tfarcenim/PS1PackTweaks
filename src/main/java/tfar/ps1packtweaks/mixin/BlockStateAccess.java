package tfar.ps1packtweaks.mixin;

import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Debug(export = true)
@Mixin(BlockBehaviour.BlockStateBase.class)
public interface BlockStateAccess {

    @Accessor @Mutable
    void setDestroySpeed(float destroySpeed);
    @Accessor @Mutable
    void setRequiresCorrectToolForDrops(boolean needsTool);

}
