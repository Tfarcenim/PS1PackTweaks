package tfar.ps1packtweaks.datagen;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.enderitemc.enderitemod.init.Registration;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.data.loot.EntityLoot;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.LootingEnchantFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import tfar.ps1packtweaks.Init;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class LootTables extends LootTableProvider {
    public LootTables(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationtracker) {
    }

    @Override
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables() {
        return ImmutableList.of(Pair.of(ModEntityLootTables::new, LootContextParamSets.ENTITY),Pair.of(ModBlockLootTables::new, LootContextParamSets.BLOCK));
    }

    public static class ModEntityLootTables extends EntityLoot {
        @Override
        protected Iterable<EntityType<?>> getKnownEntities() {
            List<EntityType<?>> list = new ArrayList<>();
            list.add(Init.ModEntityTypes.BARNACLE);
            return list;
        }

        @Override
        protected void addTables() {
            this.add(Init.ModEntityTypes.BARNACLE, LootTable.lootTable()
                    .withPool(LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1))
                            .add(LootItem.lootTableItem(Init.ModItems.BARNACLE_TOOTH)
                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 1.0F)))
                                    .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                                    .when(LootItemKilledByPlayerCondition.killedByPlayer())))
            );

        }
    }

    public static class ModBlockLootTables extends BlockLoot {
        @Override
        protected Iterable<Block> getKnownBlocks() {
            List<Block> list = new ArrayList<>();
            list.add(Registration.ENDERITE_ORE.get());
            return list;
        }

        @Override
        protected void addTables() {
            dropWhenSilkTouch(Registration.ENDERITE_ORE.get());
        }
    }
}
