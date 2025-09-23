package tfar.ps1packtweaks.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootTableIdCondition;
import tfar.ps1packtweaks.compat.ModIntegration;
import tfar.ps1packtweaks.loot.AddItemLootModifier;
import tfar.ps1packtweaks.loot.DuplicateOutputsLootModifier;
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

        add("add_map_to_village",Init.GlobalLootModifiers.ADD_ITEM,new AddItemLootModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(ModIntegration.kazs_end_village.id("chests/cartographer")).build()
        }));
    }

    List<ResourceLocation> whitelist() {
        List<ResourceLocation> list = new ArrayList<>(BuiltInLootTables.all());
        list.addAll(DatapackLootTables.LOCATIONS);
        return list;
    }

}
