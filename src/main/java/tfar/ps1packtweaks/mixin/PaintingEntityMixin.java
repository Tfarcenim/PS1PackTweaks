package tfar.ps1packtweaks.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.Motive;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import tfar.ps1packtweaks.Init;
import tfar.ps1packtweaks.PS1PackTweaksConfig;
import tfar.ps1packtweaks.PaintingEntityDuck;

import javax.annotation.Nullable;
import java.util.List;

@Mixin(Painting.class)

public abstract class PaintingEntityMixin extends Entity implements PaintingEntityDuck {

    @Shadow public Motive motive;
    
    @Unique
    boolean rendering;
    @Unique @Nullable Motive original;

    @Unique boolean wasRendering;

    @Unique int displayTime;

    public PaintingEntityMixin(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "<init>(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;)V",at = @At("RETURN")
    ,locals = LocalCapture.CAPTURE_FAILHARD)
    private void noCursed(Level pLevel, BlockPos pPos, Direction pFacingDirection, CallbackInfo ci, List<Motive> list) {
        if (motive == Init.ModPaintings.CURSED_COURBET) {
            list.remove(motive);
            this.motive = list.get(this.random.nextInt(list.size()));
        }
    }

    @Override
    public boolean isRendering() {
        return rendering;
    }

    @Override
    public void setRendering(boolean rendering) {
        this.rendering = rendering;
    }

    @Override
    public void tick() {
        super.tick();
        if (level.isClientSide) {
            if (rendering && !wasRendering) {
                if (this.motive == Motive.COURBET && random.nextDouble() < PS1PackTweaksConfig.SERVER.courbetReplaceChance.get()) {
                    original = motive;
                    motive = Init.ModPaintings.CURSED_COURBET;
                    displayTime = 12;
                }
            }

            if (rendering && original != null) {
                displayTime--;
                if (displayTime == 0) {
                    motive = original;
                    original = null;
                }
            }

            /*if (!rendering && wasRendering && original != null) {
                motive = original;
                original = null;
            }*/

            wasRendering = rendering;
        }
    }
}
