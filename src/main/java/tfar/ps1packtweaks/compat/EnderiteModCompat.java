package tfar.ps1packtweaks.compat;

import net.enderitemc.enderitemod.init.Registration;
import net.minecraft.world.level.block.Blocks;
import tfar.ps1packtweaks.PS1PackTweaks;

public class EnderiteModCompat {
    public static void setup(){
        PS1PackTweaks.setDestroySpeed(Registration.ENDERITE_ORE.get(),66f);
        PS1PackTweaks.setDestroySpeed(Blocks.DEEPSLATE,2);
        PS1PackTweaks.setRequiresCorrectToolForDrops(Registration.ENDERITE_ORE.get(),true);
        PS1PackTweaks.setDefaultLootTable(Registration.ENDERITE_ORE.get());
    }
}
