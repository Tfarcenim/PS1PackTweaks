package tfar.ps1packtweaks.client;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;
import tfar.ps1packtweaks.PS1PackTweaks;
import tfar.ps1packtweaks.entity.Barnacle;

public class BarnacleModel extends AnimatedGeoModel<Barnacle> {

    protected final ResourceLocation animation;
    protected final ResourceLocation model;
    protected final ResourceLocation texture;

    public BarnacleModel() {
        ResourceLocation base = PS1PackTweaks.id("barnacle");
        animation = PS1PackTweaks.id("animations/"+base.getPath()+".animation.json");
        model = PS1PackTweaks.id("geo/"+base.getPath()+".geo.json");
        texture = PS1PackTweaks.id("textures/entity/"+base.getPath()+".barnacle.json");

    }

    @Override
    public ResourceLocation getAnimationFileLocation(Barnacle entity) {
        return animation;
    }

    @Override
    public ResourceLocation getModelLocation(Barnacle entity) {
        return model;
    }

    @Override
    public ResourceLocation getTextureLocation(Barnacle entity) {
        return texture;
    }

    @Override
    public void setLivingAnimations(Barnacle entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone mob = this.getBone("mob");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        mob.setRotationX((extraData.headPitch * ((float) Math.PI / 180F) - (90 * (float) Math.PI / 180)));
        mob.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
    }
}
