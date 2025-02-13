package tfar.ps1packtweaks.mixin;

import net.minecraft.client.model.HorseModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.AbstractHorseRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.UndeadHorseRenderer;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.ps1packtweaks.client.UndeadHorseArmorLayer;

@Mixin(UndeadHorseRenderer.class)
public abstract class UndeadHorseRendererMixin extends AbstractHorseRenderer<AbstractHorse, HorseModel<AbstractHorse>> {

    @Inject(method = "<init>",at = @At("RETURN"))
    private void onInit(EntityRendererProvider.Context pContext, ModelLayerLocation pLayer, CallbackInfo ci) {
        this.addLayer(new UndeadHorseArmorLayer(this, pContext.getModelSet()));
    }


    public UndeadHorseRendererMixin(EntityRendererProvider.Context pContext, HorseModel pModel, float pScale) {
        super(pContext, pModel, pScale);
    }

}
