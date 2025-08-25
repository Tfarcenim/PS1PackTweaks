package tfar.ps1packtweaks.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

public class InvisibleEntity extends PathfinderMob {
    public InvisibleEntity(EntityType<? extends PathfinderMob> $$0, Level level) {
        super($$0, level);
    }

    long age;
    long lifespan = 1200;


    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 24).add(Attributes.MOVEMENT_SPEED, 0.3);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1));
    }

    @Override
    public void tick() {
        super.tick();
        age++;
        if (age >= lifespan) {
            despawn();
        }
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
    }

    void despawn() {
        if (!level.isClientSide) {
            discard();
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putLong("age", age);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        age = tag.getLong("age");
    }

}
