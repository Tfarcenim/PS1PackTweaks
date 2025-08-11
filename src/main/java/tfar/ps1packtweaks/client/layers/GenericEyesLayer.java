package tfar.ps1packtweaks.client.layers;

import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class GenericEyesLayer<T extends LivingEntity, M extends PlayerModel<T>> extends EyesLayer<T, M> {

    private final RenderType eyes;
    public GenericEyesLayer(RenderLayerParent<T,M> $$0, ResourceLocation base) {
        super($$0);
        String s = base.getPath();
        String s1 = s.substring(0,s.length() - ".png".length());
        eyes = RenderType.eyes(new ResourceLocation(base.getNamespace(),s1+"_eyes.png"));
    }

    @Override
    public RenderType renderType() {
        return eyes;
    }
}
