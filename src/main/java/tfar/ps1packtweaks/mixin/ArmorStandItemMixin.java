package tfar.ps1packtweaks.mixin;

import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.ArmorStandItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Random;

@Mixin(ArmorStandItem.class)
public class ArmorStandItemMixin {

    /**
     * @author
     * @reason
     */
    @Overwrite
    private void randomizePose(ArmorStand pArmorStand, Random pRandom) {
    }
}
