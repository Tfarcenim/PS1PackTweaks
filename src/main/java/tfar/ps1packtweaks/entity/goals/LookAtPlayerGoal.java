package tfar.ps1packtweaks.entity.goals;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class LookAtPlayerGoal<T extends PathfinderMob> extends Goal {
      private final T mob;
      @Nullable
      private LivingEntity target;
      public LookAtPlayerGoal(T mob) {
         this.mob = mob;
         this.setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE));
      }

      /**
       * Returns whether execution should begin. You can also read and cache any block necessary for execution in this
       * method as well.
       */
      @Override
      public boolean canUse() {
         this.target = this.mob.getTarget();
          if (this.target instanceof Player) {
            // double d0 = this.target.distanceToSqr(this.mob);
             return true;//this.mob.isLookingAtMe((Player) this.target);
          } else {
             return false;
          }
      }

      /**
       * Execute a one shot task or start executing a continuous task
       */
      @Override
      public void start() {
         this.mob.getNavigation().stop();
      }

      /**
       * Keep ticking a continuous task that has already been started
       */
      @Override
      public void tick() {
         this.mob.getLookControl().setLookAt(this.target.getX(), this.target.getEyeY(), this.target.getZ());
      }
   }