package tfar.ps1packtweaks.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import tfar.ps1packtweaks.Init;

public class FinalHerobrineEntity extends AbstractHerobrineEntity{

    public FinalHerobrineEntity(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public boolean noClip;

    public int duration = 30;
    public int start = 150;

    @Override
    public void tick() {
        if (noClip) {
            this.noPhysics = true;
        }
        super.tick();
        if (noClip) {
            this.noPhysics = false;
        }
        if (level.isClientSide && tickCount > start && tickCount < start + duration){
            double width = getBbWidth();
            for (int i = 0; i < 5;i++) {
                level.addParticle(Init.ModParticleTypes.FINAL_HEROBRINE, false, getX() + width / 2 * (2 * Math.random() - 1),
                        getY()+ getBbHeight()/2,
                        getZ() + width / 2 * (2 * Math.random() - 1), 0, 0.05, 0);
            }
        }
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        Player player = this.level.getNearestPlayer(this,10);
        if (player != null) {
            double d0 = player.getEyeY();
            getLookControl().setLookAt(player.getX(), d0, player.getZ());
        }
    }

    @Override
    protected void pushEntities() {

    }
}
