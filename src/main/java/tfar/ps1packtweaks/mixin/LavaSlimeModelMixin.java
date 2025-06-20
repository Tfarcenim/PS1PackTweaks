package tfar.ps1packtweaks.mixin;

import net.minecraft.client.model.LavaSlimeModel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.monster.Slime;
import org.checkerframework.checker.units.qual.A;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LavaSlimeModel.class)
public class LavaSlimeModelMixin<T extends Slime> {

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void prepareMobModel(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTick) {
        float f = Mth.lerp(pPartialTick, pEntity.oSquish, pEntity.squish);
        if (f < 0.0F) {
            f = 0.0F;
        }

      //  for(int i = 0; i < this.bodyCubes.length; ++i) {
      //      this.bodyCubes[i].y = (float)(-(4 - i)) * 0 * 1.7F;
      //  }

    }

}
