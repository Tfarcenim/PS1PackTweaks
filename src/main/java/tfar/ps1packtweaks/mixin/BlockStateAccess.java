package tfar.ps1packtweaks.mixin;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BlockBehaviour.BlockStateBase.class)
public interface BlockStateAccess {

    @Accessor @Mutable
    void setDestroySpeed(float destroySpeed);
    @Accessor @Mutable
    void setRequiresCorrectToolForDrops(boolean needsTool);
    @Accessor @Mutable
    void setMaterial(Material material);

    @Accessor @Mutable
    void setCanOcclude(boolean canOcclude);

}
