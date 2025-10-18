package tfar.ps1packtweaks.entity;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;
import tfar.ps1packtweaks.Init;
import tfar.ps1packtweaks.PS1PackTweaks;

public class AbstractHerobrineEntity extends PathfinderMob {

    protected static final EntityDataAccessor<ResourceLocation> DATA_TEXTURE = SynchedEntityData.defineId(AbstractHerobrineEntity.class, Init.ModEntityDataSerializers.RESOURCELOCATION);


    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(DATA_TEXTURE, PS1PackTweaks.id("textures/entity/herobrine.png"));
    }

    public void setTexture(ResourceLocation texture) {
        entityData.set(DATA_TEXTURE,texture);
    }

    public ResourceLocation getTexture() {
        return entityData.get(DATA_TEXTURE);
    }

    protected AbstractHerobrineEntity(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
}
