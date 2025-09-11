package tfar.ps1packtweaks.datagen.tags;

import net.mcreator.midnightlurker.init.MidnightlurkerModEntities;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import tfar.ps1packtweaks.ModTags;
import tfar.ps1packtweaks.PS1PackTweaks;

public class ModEntityTypeTagProvider extends EntityTypeTagsProvider {
    public ModEntityTypeTagProvider(DataGenerator p_126517_,@Nullable ExistingFileHelper existingFileHelper) {
        super(p_126517_, PS1PackTweaks.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        tag(ModTags.MIDNIGHT_LURKERS).add(MidnightlurkerModEntities.MIDNIGHT_LURKER_AGGRESSIVE.get(),
                MidnightlurkerModEntities.MIDNIGHT_LURKER_RUNAWAY.get(),
                MidnightlurkerModEntities.MIDNIGHT_LURKER_STALKING.get(),
                MidnightlurkerModEntities.MIDNIGHT_LURKER_STARE.get(),
                MidnightlurkerModEntities.MIDNIGHT_LURKER_UNPROVOKED.get(),
                MidnightlurkerModEntities.MIDNIGHTLURKER_NE.get()
        );
    }
}
//# AGGRESSIVE
//effect give @e[type=midnightlurker:midnight_lurker_aggressive,nbt=!{ActiveEffects:[{Id:11b}]}] minecraft:resistance 10 2 true
//
//# RUNAWAY
//effect give @e[type=midnightlurker:midnight_lurker_runaway,nbt=!{ActiveEffects:[{Id:11b}]}] minecraft:resistance 10 2 true
//
//# STALKING
//effect give @e[type=midnightlurker:midnight_lurker_stalking,nbt=!{ActiveEffects:[{Id:11b}]}] minecraft:resistance 10 2 true
//
//# STARE
//effect give @e[type=midnightlurker:midnight_lurker_stare,nbt=!{ActiveEffects:[{Id:11b}]}] minecraft:resistance 10 2 true
//
//# UNPROVOKED
//effect give @e[type=midnightlurker:midnight_lurker_unprovoked,nbt=!{ActiveEffects:[{Id:11b}]}] minecraft:resistance 10 2 true
//
//# NE
//effect give @e[type=midnightlurker:midnightlurker_ne,nbt=!{ActiveEffects:[{Id:11b}]}] minecraft:resistance 10 2 true