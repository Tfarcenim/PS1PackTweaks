package tfar.ps1packtweaks.client;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class BasicGeoModel<T extends Entity & IAnimatable> extends AnimatedGeoModel<T> {
    private final ResourceLocation animationFile;
    private final ResourceLocation modelFile;
    private final ResourceLocation texture;

    public BasicGeoModel(ResourceLocation animationFile, ResourceLocation modelFile, ResourceLocation texture) {
        this.animationFile = animationFile;
        this.modelFile = modelFile;
        this.texture = texture;
    }

    @Override
    public ResourceLocation getAnimationFileLocation(T entity) {
        return animationFile;
    }

    @Override
    public ResourceLocation getModelLocation(T entity) {
        return modelFile;
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return texture;
    }

    @Override
    public void setCustomAnimations(T animatable, int instanceId, AnimationEvent animationEvent) {
        super.setCustomAnimations(animatable, instanceId, animationEvent);
        IBone head = this.getAnimationProcessor().getBone("head");
        EntityModelData extraData = (EntityModelData)animationEvent.getExtraDataOfType(EntityModelData.class).get(0);
        AnimationData manager = animatable.getFactory().getOrCreateAnimationData(instanceId);
        int unpausedMultiplier = Minecraft.getInstance().isPaused() && !manager.shouldPlayWhilePaused ? 0 : 1;
        head.setRotationX(head.getRotationX() + extraData.headPitch * 0.017453292F * (float)unpausedMultiplier);
        head.setRotationY(head.getRotationY() + extraData.netHeadYaw * 0.017453292F * (float)unpausedMultiplier);
    }
}
