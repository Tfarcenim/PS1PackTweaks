package tfar.ps1packtweaks.client.layers;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import tfar.ps1packtweaks.client.CustomRenderTypes;

import java.util.function.Function;

public class GenericEyesLayer<T extends LivingEntity, M extends PlayerModel<T>> extends EyesLayer<T, M> {

    private final RenderType eyes;
    public GenericEyesLayer(RenderLayerParent<T,M> $$0, ResourceLocation texture) {
        super($$0);
        eyes = CustomRenderTypes.customEyes(texture);
    }

    @Override
    public RenderType renderType() {
        return eyes;
    }


}
