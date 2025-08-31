package tfar.ps1packtweaks.entity.goals;

import com.nyfaria.nightmare.cap.ExampleHolderAttacher;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import tfar.ps1packtweaks.CustomEvents;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

/**
 * @see net.minecraft.world.entity.ai.goal.FollowMobGoal
 */

public class FollowPlayerGoal extends Goal {
    private final Mob mob;
    private final Predicate<Player> followPredicate;
    @Nullable
    private Player followingMob;
    private final double speedModifier;
    private final PathNavigation navigation;
    private int timeToRecalcPath;
    private final float stopDistance;
    private float oldWaterCost;
    private final float areaSize;

    public FollowPlayerGoal(Mob pMob, double pSpeedModifier, float pStopDistance, float pAreaSize) {
        this.mob = pMob;
        this.followPredicate = this::shouldFollow;
        this.speedModifier = pSpeedModifier;
        this.navigation = pMob.getNavigation();
        this.stopDistance = pStopDistance;
        this.areaSize = pAreaSize;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        if (!(pMob.getNavigation() instanceof GroundPathNavigation) && !(pMob.getNavigation() instanceof FlyingPathNavigation)) {
            throw new IllegalArgumentException("Unsupported mob type for FollowPlayerGoal");
        }
    }

    public boolean shouldFollow(@Nullable Player player) {
        return player != null && player.getPersistentData().getInt(CustomEvents.FOLLOW) > 0;
    }

    public boolean canUse() {
        if (this.mob instanceof TamableAnimal tamableAnimal) {
            if (tamableAnimal.isOrderedToSit()) {
                return false;
            }
        }

        List<Player> list = this.mob.level.getEntitiesOfClass(Player.class, this.mob.getBoundingBox().inflate(this.areaSize), this.followPredicate);
        if (!list.isEmpty()) {

            for (Player mob : list) {
                if (!mob.isInvisible()) {
                    this.followingMob = mob;
                    return true;
                }
            }
        }

        return false;
    }

    public boolean canContinueToUse() {
        return this.followingMob != null && !this.navigation.isDone() && this.mob.distanceToSqr(this.followingMob) > (double) (this.stopDistance * this.stopDistance);
    }

    public void start() {
        this.timeToRecalcPath = 0;
        this.oldWaterCost = this.mob.getPathfindingMalus(BlockPathTypes.WATER);
        this.mob.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
    }

    public void stop() {
        this.followingMob = null;
        this.navigation.stop();
        this.mob.setPathfindingMalus(BlockPathTypes.WATER, this.oldWaterCost);
    }

    public void tick() {
        if (this.followingMob != null && !this.mob.isLeashed()) {
            this.mob.getLookControl().setLookAt(this.followingMob, 10.0F, (float) this.mob.getMaxHeadXRot());
            if (--this.timeToRecalcPath <= 0) {
                this.timeToRecalcPath = this.adjustedTickDelay(10);
                double d0 = this.mob.getX() - this.followingMob.getX();
                double d1 = this.mob.getY() - this.followingMob.getY();
                double d2 = this.mob.getZ() - this.followingMob.getZ();
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                if (!(d3 <= (double) (this.stopDistance * this.stopDistance))) {
                    this.navigation.moveTo(this.followingMob, this.speedModifier);
                } else {
                    this.navigation.stop();
                    if (d3 <= (double) this.stopDistance) {
                        double d4 = this.followingMob.getX() - this.mob.getX();
                        double d5 = this.followingMob.getZ() - this.mob.getZ();
                        this.navigation.moveTo(this.mob.getX() - d4, this.mob.getY(), this.mob.getZ() - d5, this.speedModifier);
                    }
                }
            }
        }
    }
}
