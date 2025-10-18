package tfar.ps1packtweaks.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

public abstract class CustomRenderTypes extends RenderType {
    public CustomRenderTypes(String pName, VertexFormat pFormat, VertexFormat.Mode pMode, int pBufferSize, boolean pAffectsCrumbling, boolean pSortOnUpload, Runnable pSetupState, Runnable pClearState) {
        super(pName, pFormat, pMode, pBufferSize, pAffectsCrumbling, pSortOnUpload, pSetupState, pClearState);
    }

    private static final Function<ResourceLocation, RenderType> CUSTOM_EYES = Util.memoize((p_173255_) -> {
        RenderStateShard.TextureStateShard renderstateshard$texturestateshard = new RenderStateShard.TextureStateShard(p_173255_, false, false);
        return RenderType.create("eyes", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false,
                true, RenderType.CompositeState.builder().setShaderState(RenderType.RENDERTYPE_EYES_SHADER)
                        .setTextureState(renderstateshard$texturestateshard).setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
                        .setWriteMaskState(COLOR_WRITE).createCompositeState(false));
    });

    public static RenderType customEyes(ResourceLocation pLocation) {
        return CUSTOM_EYES.apply(pLocation);
    }

}
