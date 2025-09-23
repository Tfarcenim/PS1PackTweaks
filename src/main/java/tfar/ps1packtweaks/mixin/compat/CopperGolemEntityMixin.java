package tfar.ps1packtweaks.mixin.compat;

import com.faboslav.friendsandfoes.init.ModBlocks;
import com.minecraftserverzone.coppergolem.CopperGolemEntity;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CopperGolemEntity.class)
public class CopperGolemEntityMixin {
    @Shadow
    Block[] buttonblocks;

    @Inject(method = "<init>",at = @At("RETURN"))
    private void limitButtons(EntityType p_30369_, Level p_30370_, CallbackInfo ci) {
        this.buttonblocks = new Block[]{ModBlocks.COPPER_BUTTON.get(),
                ModBlocks.EXPOSED_COPPER_BUTTON.get(),
        ModBlocks.WEATHERED_COPPER_BUTTON.get(),
        ModBlocks.OXIDIZED_COPPER_BUTTON.get(),
        ModBlocks.WAXED_COPPER_BUTTON.get(),
        ModBlocks.WAXED_EXPOSED_COPPER_BUTTON.get(),
        ModBlocks.WAXED_WEATHERED_COPPER_BUTTON.get(),
        ModBlocks.WAXED_OXIDIZED_COPPER_BUTTON.get()};
    }
}
