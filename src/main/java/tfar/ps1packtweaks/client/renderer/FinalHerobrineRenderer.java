package tfar.ps1packtweaks.client.renderer;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import tfar.ps1packtweaks.PS1PackTweaks;
import tfar.ps1packtweaks.client.layers.HerobrineEyesLayer;
import tfar.ps1packtweaks.entity.FinalHerobrineEntity;

public class FinalHerobrineRenderer extends SimplePlayerRenderer<FinalHerobrineEntity> {
    public FinalHerobrineRenderer(EntityRendererProvider.Context context, boolean slim) {
        super(context, slim);
        addLayer(new HerobrineEyesLayer(this, PS1PackTweaks.id("textures/entity/blank_eyes.png")));
    }
}
