package tfar.ps1packtweaks.entity.goals;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import tfar.ps1packtweaks.entity.CanLookAt;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class LookforPlayerGoal<T extends PathfinderMob & CanLookAt> extends NearestAttackableTargetGoal<Player> {
    private final T pathfinderMob;
    /** The player */
    @Nullable
    private Player pendingTarget;
    private int aggroTime;
    private final TargetingConditions startAggroTargetConditions;
    private final TargetingConditions continueAggroTargetConditions = TargetingConditions.forCombat().ignoreLineOfSight();

    public LookforPlayerGoal(T pathfinderMob, @Nullable Predicate<LivingEntity> pSelectionPredicate) {
        super(pathfinderMob, Player.class, 10, false, false, pSelectionPredicate);
        this.pathfinderMob = pathfinderMob;
        this.startAggroTargetConditions = TargetingConditions.forCombat().range(this.getFollowDistance()).selector(living -> true);
    }

    /**
     * Returns whether execution should begin. You can also read and cache any block necessary for execution in this
     * method as well.
     */
    @Override
    public boolean canUse() {
        this.pendingTarget = this.pathfinderMob.level.getNearestPlayer(this.startAggroTargetConditions, this.pathfinderMob);
        return this.pendingTarget != null;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void start() {
        this.aggroTime = this.adjustedTickDelay(5);
    }

    /**
     * Reset the task's internal block. Called when this task is interrupted by another one
     */
    @Override
    public void stop() {
        this.pendingTarget = null;
        super.stop();
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean canContinueToUse() {
        if (this.pendingTarget != null) {
            return false;
        } else {
            return this.target != null && this.continueAggroTargetConditions.test(this.pathfinderMob, this.target) || super.canContinueToUse();
        }
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    @Override
    public void tick() {
        if (this.pathfinderMob.getTarget() == null) {
            super.setTarget(null);
        }

        if (this.pendingTarget != null) {
            this.target = this.pendingTarget;
            this.pendingTarget = null;
            super.start();
        } else {
            super.tick();
        }
    }

    public LivingEntity getTarget() {
        return target;
    }


}
