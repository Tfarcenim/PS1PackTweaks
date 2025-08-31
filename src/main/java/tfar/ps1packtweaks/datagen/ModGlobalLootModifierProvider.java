package tfar.ps1packtweaks.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import tfar.ps1packtweaks.DuplicateOutputsLootModifier;
import tfar.ps1packtweaks.Init;
import tfar.ps1packtweaks.PS1PackTweaks;
import tfar.ps1packtweaks.compat.DatapackLootTables;

import java.util.ArrayList;
import java.util.List;

public class ModGlobalLootModifierProvider extends GlobalLootModifierProvider {
    public ModGlobalLootModifierProvider(DataGenerator gen) {
        super(gen, PS1PackTweaks.MOD_ID);
    }

    @Override
    protected void start() {
        add("duplicate_outputs", Init.GlobalLootModifiers.DUPLICATE_OUTPUTS,new DuplicateOutputsLootModifier(new LootItemCondition[]{
                new OrLootTableCondition(whitelist())
        }));
    }

    List<ResourceLocation> whitelist() {
        List<ResourceLocation> list = new ArrayList<>(BuiltInLootTables.all());
        list.addAll(DatapackLootTables.LOCATIONS);
        return list;
    }

}
