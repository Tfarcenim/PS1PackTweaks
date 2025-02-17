package tfar.ps1packtweaks.compat;

import net.onvoid.morehorsearmor.common.MoreHorseArmorItems;
import tfar.ps1packtweaks.mixin.ItemAccess;

public class MoreHorseArmorCompat {

    public static void setup() {
        ((ItemAccess) MoreHorseArmorItems.NETHERITE_HORSE_ARMOR.get()).setIsFireResistant(true);
        ((ItemAccess) MoreHorseArmorItems.ENDERITE_HORSE_ARMOR.get()).setIsFireResistant(true);
    }
}
