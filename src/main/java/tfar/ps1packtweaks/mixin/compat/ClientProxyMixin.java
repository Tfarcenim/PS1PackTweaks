package tfar.ps1packtweaks.mixin.compat;

import com.github.alexthe666.alexsmobs.client.model.ModelMimicube;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ModelMimicube.class)
public class ClientProxyMixin {

    @Shadow @Final public AdvancedModelBox body;

    @Inject(method = "<init>",at = @At("RETURN"))
    void fixModel(CallbackInfo ci) {
        this.body.cubeList.clear();
        this.body.addBox(-8, -16, -8, 16, 16, 16, 0, false);
    }

}
