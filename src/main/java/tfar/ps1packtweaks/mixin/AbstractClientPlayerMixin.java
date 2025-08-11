package tfar.ps1packtweaks.mixin;

import tfar.ps1packtweaks.AbstractClientPlayerDuck;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfar.ps1packtweaks.client.PS1PackTweaksClient;

import java.util.UUID;

@Mixin(value = AbstractClientPlayer.class,priority = 999)//needs to be before Default Skin
public class AbstractClientPlayerMixin implements AbstractClientPlayerDuck {

    @Unique
    boolean herobrine;

    @Override
    public boolean isHerobrine() {
        return herobrine;
    }

    @Override
    public void setHerobrine(boolean herobrine) {
        this.herobrine = herobrine;
    }

    @Unique
    private static final UUID STEVE_UUID = new UUID(Long.MAX_VALUE, 0L);
    @Unique
    private static final UUID ALEX_UUID = new UUID(Long.MAX_VALUE, 1L);

    public AbstractClientPlayerMixin() {
    }

    @Inject(
            at = {@At("HEAD")},
            method = {"getSkinTextureLocation"},
            cancellable = true
    )
    private void setSkinToHerobrine(CallbackInfoReturnable<ResourceLocation> cir) {
        if (herobrine) {
            cir.setReturnValue(PS1PackTweaksClient.HEROBRINE_SKIN);

        }    }

    @Inject(
            at = {@At("HEAD")},
            method = {"getModelName"},
            cancellable = true
    )
    private void setModelToSteve(CallbackInfoReturnable<String> cir) {
        if (herobrine) {
            cir.setReturnValue(DefaultPlayerSkin.getSkinModelName(STEVE_UUID));
        }
    }
}
