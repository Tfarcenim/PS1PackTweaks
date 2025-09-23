package tfar.ps1packtweaks.client;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

public class SimplePlayerRenderer<T extends Mob> extends MobRenderer<T, PlayerModel<T>> {
    private final ResourceLocation texture;

    public SimplePlayerRenderer(EntityRendererProvider.Context context, boolean slim, ResourceLocation texture) {
        super(context, new PlayerModel<>(context.bakeLayer(slim ? ModelLayers.PLAYER_SLIM : ModelLayers.PLAYER), slim), 0.5F);
        this.texture = texture;
        this.addLayer(new HumanoidArmorLayer<>(this, new HumanoidModel<>(context.bakeLayer(slim ? ModelLayers.PLAYER_SLIM_INNER_ARMOR : ModelLayers.PLAYER_INNER_ARMOR)), new HumanoidModel(context.bakeLayer(slim ? ModelLayers.PLAYER_SLIM_OUTER_ARMOR : ModelLayers.PLAYER_OUTER_ARMOR))));
        this.addLayer(new ItemInHandLayer<>(this));
        this.addLayer(new ArrowLayer<>(context, this));
        //this.addLayer(new Deadmau5EarsLayer(this));
        //this.addLayer(new CapeLayer(this));
        this.addLayer(new CustomHeadLayer<>(this, context.getModelSet()));
        this.addLayer(new ElytraLayer<>(this, context.getModelSet()));
        //this.addLayer(new ParrotOnShoulderLayer<>(this, context.getModelSet()));
        this.addLayer(new SpinAttackEffectLayer<>(this, context.getModelSet()));
        this.addLayer(new BeeStingerLayer<>(this));
   //     addLayer(new GenericEyesLayer<>(this,texture));
    }

    @Override
    public ResourceLocation getTextureLocation(T pEntity) {
        return texture;
    }
}
