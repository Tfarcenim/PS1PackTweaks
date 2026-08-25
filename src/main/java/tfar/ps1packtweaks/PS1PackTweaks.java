package tfar.ps1packtweaks;

import com.Apothic0n.StarryEnd.core.objects.StarryEndBlocks;
import com.github.alexthe666.alexsmobs.effect.AMEffectRegistry;
import com.github.alexthe666.alexsmobs.misc.ItemsForEmeraldsTrade;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Multimap;
import com.mojang.authlib.GameProfile;
import com.mojang.logging.LogUtils;
import com.natamus.naturallychargedcreepers.forge.events.ForgeCreeperChargeEvent;
import com.spawnerhead.entity.EntitySpawnEvent;
import com.weathersettings.event.EventHandler;
import corgitaco.enhancedcelestials.server.commands.LunarForecastCommand;
import crumbs.trueherobrine.init.TrueHerobrineModEntities;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.mcreator.goldenmelons.init.GoldenmelonsModItems;
import net.mcreator.goldenmelons.item.GoldenMelonItem;
import net.mcreator.midnightlurker.init.MidnightlurkerModEntities;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.*;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.Container;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.decoration.Motive;
import net.minecraft.world.entity.monster.Evoker;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
import net.minecraft.world.level.levelgen.structure.StructureSpawnOverride;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityMobGriefingEvent;
import net.minecraftforge.event.entity.living.LivingChangeTargetEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LootingLevelEvent;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.EventBus;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.IEventListener;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import rejected.init.RejectedModItems;
import rejected.item.PumpkinpieItem;
import tfar.ps1packtweaks.client.PS1PackTweaksClient;
import tfar.ps1packtweaks.client.WorldLocker;
import tfar.ps1packtweaks.compat.BrewingCauldronCompat;
import tfar.ps1packtweaks.compat.EnderiteModCompat;
import tfar.ps1packtweaks.compat.ModIntegration;
import tfar.ps1packtweaks.compat.MoreHorseArmorCompat;
import tfar.ps1packtweaks.datagen.ModDataGenerator;
import tfar.ps1packtweaks.datagen.OrLootTableCondition;
import tfar.ps1packtweaks.duck.EnchantmentDuck;
import tfar.ps1packtweaks.entity.Barnacle;
import tfar.ps1packtweaks.entity.EventHerobrineEntity;
import tfar.ps1packtweaks.entity.InvisibleEntity;
import tfar.ps1packtweaks.entity.ScriptedMidnightLurker;
import tfar.ps1packtweaks.entity.goals.FollowPlayerGoal;
import tfar.ps1packtweaks.mixin.*;
import tfar.ps1packtweaks.mixin.compat.alexmobs.ItemsForEmeraldsTradeAccessor;
import tfar.ps1packtweaks.network.ForgePacketHandler;
import tfar.ps1packtweaks.network.PacketHandler;
import tfar.ps1packtweaks.network.client.S2CAdvancementPacket;
import tfar.ps1packtweaks.network.client.S2CEventPacket;
import tfar.ps1packtweaks.network.client.S2CShaderPacket;
import tfar.ps1packtweaks.worldgen.ModConfiguredFeatures;
import tfar.ps1packtweaks.worldgen.ModPlacedFeatures;
import tfar.ps1packtweaks.worldgen.ModStructureFeatures;
import tfar.ps1packtweaks.worldgen.ModTreeFeatures;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(PS1PackTweaks.MOD_ID)
public class PS1PackTweaks {
    public static final String MOD_ID = "ps1packtweaks";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final boolean TRIGGER_BANNER_CRASH = false;

    public static final boolean DEV = !FMLLoader.isProduction();

    //Nether: -159 50 -98
    //End: 7900 66 5439

    public static final GameRules.Key<GameRules.BooleanValue> RULE_NETHER_SPAWN = GameRules.register
            ("spawnInNether", GameRules.Category.UPDATES, GameRules.BooleanValue.create(false, (server, booleanValue) -> {
                boolean value = booleanValue.get();
                if (value) {
                    server.overworld().setDefaultSpawnPos(new BlockPos(-159,50,-98),0);
                }
            }));

    public static final GameRules.Key<GameRules.BooleanValue> RULE_END_SPAWN = GameRules.register
            ("spawnInEnd", GameRules.Category.UPDATES, GameRules.BooleanValue.create(false, (server, booleanValue) -> {
                boolean value = booleanValue.get();
                if (value) {
                    server.overworld().setDefaultSpawnPos(new BlockPos(7900,66,5439),0);
                }
            }));

    public PS1PackTweaks() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, PS1PackTweaksConfig.SERVER_SPEC);
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        // Register the setup method for modloading
        bus.addListener(this::setup);
        bus.addListener(this::changeAttributes);

        if (FMLEnvironment.dist.isClient()) {
            PS1PackTweaksClient.init(bus);
        }

        bus.addGenericListener(Block.class, this::registerBlocks);
        bus.addGenericListener(ParticleType.class, this::registerParticleTypes);
        bus.addGenericListener(Item.class, this::registerItems);
        bus.addGenericListener(EntityType.class, this::registerEntities);
        bus.addGenericListener(SoundEvent.class, this::registerSounds);
        bus.addGenericListener(Feature.class, this::registerFeatures);
        bus.addGenericListener(MenuType.class, this::registerMenus);
        bus.addGenericListener(Motive.class, this::registerMotives);
        bus.addGenericListener(StructureFeature.class, this::registerStructures);
        bus.addGenericListener(GlobalLootModifierSerializer.class, this::registerGLMs);
        bus.addGenericListener(RecipeSerializer.class, this::registerRecipeSerializers);

        bus.addListener(ModDataGenerator::gatherData);
        bus.addListener(PS1PackTweaksConfig::configUpdate);
        MinecraftForge.EVENT_BUS.addListener(this::sleepCheck);
        bus.addListener(this::onAttributeCreate);
        MinecraftForge.EVENT_BUS.addListener(this::rightClickEntity);
        MinecraftForge.EVENT_BUS.addListener(this::playerTick);
        //MinecraftForge.EVENT_BUS.addListener(this::breakBlock);
        MinecraftForge.EVENT_BUS.addListener(this::biomeLoading);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.LOW,this::lateBiomeLoading);
        MinecraftForge.EVENT_BUS.addListener(this::entityJoinWorld);
        MinecraftForge.EVENT_BUS.addListener(this::onKill);
        MinecraftForge.EVENT_BUS.addListener(this::adjustLooting);
        MinecraftForge.EVENT_BUS.addListener(this::afterSleep);
        MinecraftForge.EVENT_BUS.addListener(this::advancementGet);
        MinecraftForge.EVENT_BUS.addListener(this::itemCrafted);
        MinecraftForge.EVENT_BUS.addListener(this::manageVillagerTrades);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.LOWEST,this::listVillagerTrades);
        MinecraftForge.EVENT_BUS.addListener(this::rightClickBlock);
        MinecraftForge.EVENT_BUS.addListener(this::preventColor);
        MinecraftForge.EVENT_BUS.addListener(FinalHerobrine::useFlintAndSteel);
        MinecraftForge.EVENT_BUS.addListener(FinalHerobrine::levelTick);
        MinecraftForge.EVENT_BUS.addListener(this::serverStarted);
        MinecraftForge.EVENT_BUS.addListener(this::commands);
        MinecraftForge.EVENT_BUS.addListener(this::dimensions);
        MinecraftForge.EVENT_BUS.addListener(this::onTargetSet);
        MinecraftForge.EVENT_BUS.addListener(this::livingDamage);
        MinecraftForge.EVENT_BUS.addListener(this::onDatapackReload);
        MinecraftForge.EVENT_BUS.addListener(this::levelLoad);
    }

    public void levelLoad(WorldEvent.Load event) {

    }

    public void registerRecipeSerializers(RegistryEvent.Register<RecipeSerializer<?>> event) {
        event.getRegistry().register(Init.ModRecipeSerializers.NBT_COPY_CRAFTING_SHAPED.setRegistryName("nbt_copy_crafting_shaped"));
    }

    public void onDatapackReload(OnDatapackSyncEvent event) {
        MinecraftServer server = event.getPlayerList().getServer();
        Registry<ConfiguredStructureFeature<?, ?>> registry = server.registryAccess().registryOrThrow(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY);

        ConfiguredStructureFeature<?, ?> shipwreck = registry.get(BuiltinStructures.SHIPWRECK);

        if (shipwreck != null) {
            addSpawnToStructure(shipwreck, StructureSpawnOverride.BoundingBoxType.STRUCTURE, MobCategory.MONSTER,
                    new MobSpawnSettings.SpawnerData(Init.ModEntityTypes.BARNACLE, 15, 1, 4));
        }
    }

    //thanks Alex Mobs
    private static void addSpawnToStructure(ConfiguredStructureFeature<?,?> feature, StructureSpawnOverride.BoundingBoxType bbType,
                                            MobCategory category, MobSpawnSettings.SpawnerData spawn) {
        if (!feature.spawnOverrides.isEmpty() && feature.spawnOverrides.get(category) != null) {
            StructureSpawnOverride previous = feature.spawnOverrides.get(category);
            List<MobSpawnSettings.SpawnerData> l = new ArrayList<>(previous.spawns().unwrap());
            boolean contained = false;

            for(MobSpawnSettings.SpawnerData data : l) {
                if (data.type.equals(spawn.type)) {
                    contained = true;
                    break;
                }
            }

            if (!contained) {
                l.add(spawn);
            }

            WeightedRandomList<MobSpawnSettings.SpawnerData> spawns = WeightedRandomList.create(l);
            StructureSpawnOverride override = new StructureSpawnOverride(previous.boundingBox(), spawns);
            HashMap<MobCategory, StructureSpawnOverride> newMap = new HashMap<>(feature.spawnOverrides);


            newMap.put(category, override);
            feature.spawnOverrides = ImmutableMap.copyOf(newMap);
        } else {
            WeightedRandomList<MobSpawnSettings.SpawnerData> spawns = WeightedRandomList.create(spawn);
            StructureSpawnOverride override = new StructureSpawnOverride(bbType, spawns);
            HashMap<MobCategory, StructureSpawnOverride> newMap = new HashMap<>(feature.spawnOverrides);
            newMap.put(category, override);
            feature.spawnOverrides = ImmutableMap.copyOf(newMap);
        }
    }


    void livingDamage(LivingDamageEvent event) {
        Entity attacker = event.getSource().getEntity();
        LivingEntity target = event.getEntityLiving();
        if (attacker!= null && attacker.getType().is(ModTags.MIDNIGHT_LURKERS) && target instanceof ServerPlayer serverPlayer) {
            if (!encounteredLurkers.get(serverPlayer.getUUID()).contains(attacker.getUUID())) {
                encounteredLurkers.get(serverPlayer.getUUID()).add(attacker.getUUID());
                S2CEventPacket.PLAY_LURKER_JUMPSCARE.send(serverPlayer);
            }
        }
    }

    void dimensions(PlayerEvent.PlayerChangedDimensionEvent event) {
        ResourceKey<Level> eventTo = event.getTo();
        ServerPlayer player = (ServerPlayer) event.getPlayer();
        MinecraftServer server = player.server;
        GameRules gameRules = server.getGameRules();
        if (eventTo == Level.OVERWORLD) {
            if (gameRules.getBoolean(RULE_NETHER_SPAWN)) {
                GameRules.BooleanValue booleanValue = gameRules.getRule(RULE_NETHER_SPAWN);
                booleanValue.set(false, server);
            }
        }
    }

    static final Multimap<UUID,UUID> encounteredLurkers = HashMultimap.create();

    void onTargetSet(LivingChangeTargetEvent event) {

    }

    void changeAttributes(EntityAttributeModificationEvent event) {
        event.add(EntityType.GHAST, Attributes.FOLLOW_RANGE,27);
        event.add(EntityType.BLAZE, Attributes.FOLLOW_RANGE,27);
    }

    void commands(RegisterCommandsEvent event) {
        ModCommands.register(event.getDispatcher());
    }

    public static FinalHerobrine finalHerobrine;

    void serverStarted(ServerStartedEvent event) {
        MinecraftServer server = event.getServer();
        ServerLevel overworld = server.overworld();
        finalHerobrine = overworld.getDataStorage().computeIfAbsent((p_184095_) -> FinalHerobrine.loadStatic(overworld, p_184095_),
                () -> new FinalHerobrine(overworld), "final_herobrine");

        boolean pure = WorldLocker.isPure();


        if (!server.isDedicatedServer()) {
            String levelName = server.getWorldData().getLevelName();
            if (!WorldLocker.isFixedWeather(levelName)) {
                server.getGameRules().getRule(GameRules.RULE_WEATHER_CYCLE).set(true,server);
            }
        }

        if (pure) {

            if (!finalHerobrine.hasPurgedBloodMoons) {
                LunarForecastCommand.recompute(server.createCommandSourceStack());
                finalHerobrine.hasPurgedBloodMoons = true;
                finalHerobrine.setDirty();
            }

            //disable spawns
            Map<EntityType<?>, SpawnPlacements.Data> dataByType = SpawnPlacementsAccess.getDATA_BY_TYPE();

            MidnightlurkerModEntities.REGISTRY.getEntries().forEach(entityTypeRegistryObject -> {
                dataByType.remove(entityTypeRegistryObject.get());
            });

            dataByType.remove(TrueHerobrineModEntities.HEROBRINE.get());
            removeEventsAfterPurification();
        }


        //LOGGER.info("World is pure according to file: {}", pure);
    }

    static boolean shouldDisableEvent(Object o) {
        if (o instanceof Class<?> clas) {
            if (clas == EventHandler.class) {
                return true;
            }


            String name = clas.getName();
            return name.contains("mcreator.midnightlurker") || name.contains("weirdandwonderous")
                    || name.contains("untrustedlife.liminalstairs") || name.contains("crumbs.trueherobrine");
        } else return o instanceof EntitySpawnEvent || o instanceof ForgeCreeperChargeEvent;
    }

    public void listVillagerTrades(VillagerTradesEvent event) {
        if (!PS1PackTweaksConfig.SERVER.listVillagerTrades.get()) return;
        VillagerProfession type = event.getType();

        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();

        List<String> lines = new ArrayList<>();


        lines.add(LONG_TRIPLE_BAR);

        lines.add("Villager Profession: "+ type);
        Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();

        for (int i = 1; i <=5 ;i++) {
            lines.add("Villager Trade Rank #"+ i);
            List<VillagerTrades.ItemListing> itemListings = trades.get(i);
            if (itemListings != null) {
                for (VillagerTrades.ItemListing itemListing : itemListings) {
                    lines.add(LONG_DASHES);
                    if (itemListing instanceof VillagerTrades.EmeraldForItems emeraldForItems) {
                        lines.add("Villager Trade Type: "+emeraldForItems.getClass());

                        lines.add("Buying Item: "+emeraldForItems.item);
                        lines.add("Cost: "+emeraldForItems.cost);
                        lines.add("Max Uses: "+emeraldForItems.maxUses);
                        lines.add("Xp Value: "+emeraldForItems.villagerXp);
                        lines.add("Price Multiplier: "+emeraldForItems.priceMultiplier);
                    } else if (itemListing instanceof VillagerTrades.ItemsForEmeralds itemsForEmeralds) {
                        lines.add("Villager Trade Type: "+itemsForEmeralds.getClass());

                        lines.add("Selling Item: "+itemsForEmeralds.itemStack);
                        lines.add("Emerald Count: "+itemsForEmeralds.emeraldCost);
                        lines.add("Selling Item Count: "+itemsForEmeralds.numberOfItems);
                        lines.add("Max Uses: "+itemsForEmeralds.maxUses);
                        lines.add("Xp Value: "+itemsForEmeralds.villagerXp);
                        lines.add("Price Multiplier: "+itemsForEmeralds.priceMultiplier);

                    } else if (itemListing instanceof ItemsForEmeraldsTrade itemsForEmeraldsTrade) {
                        ItemsForEmeraldsTradeAccessor itemsForEmeraldsTradeAccessor = (ItemsForEmeraldsTradeAccessor) itemsForEmeraldsTrade;
                        lines.add("Villager Trade Type: "+itemsForEmeraldsTrade.getClass());

                        lines.add("Selling Item: "+itemsForEmeraldsTradeAccessor.getSellingItem());
                        lines.add("Emerald Count: "+itemsForEmeraldsTradeAccessor.getEmeraldCount());
                        lines.add("Selling Item Count: "+itemsForEmeraldsTradeAccessor.getSellingItemCount());
                        lines.add("Max Uses: "+itemsForEmeraldsTradeAccessor.getMaxUses());
                        lines.add("Xp Value: "+itemsForEmeraldsTradeAccessor.getXpValue());
                        lines.add("Price Multiplier: "+itemsForEmeraldsTradeAccessor.getPriceMultiplier());

                    } else {
                        lines.add("Villager Trade: "+ itemListing.toString());
                    }
                }
            }
            lines.add(LONG_EQUALS);
        }

        try (FileWriter fileWriter = new FileWriter(FMLPaths.CONFIGDIR.get().resolve("trades.txt").toFile(),true)) {
            for (String line : lines) {
                fileWriter.write(line);
                fileWriter.write("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static final String LONG_DASHES = "-".repeat(40);
    public static final String LONG_EQUALS = "=".repeat(40);
    public static final String LONG_TRIPLE_BAR = "≡".repeat(40);

    // Can you remove shields from villager trades,
    // replace the glistering melon trade with goldenmelons:golden_melon and rank 5
    // replace the pumpkin pie trade with rejected:pumpkinpie rank 2
    public void manageVillagerTrades(VillagerTradesEvent event) {
        VillagerProfession type = event.getType();
        if (type == VillagerProfession.LIBRARIAN) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
            List<VillagerTrades.ItemListing> itemListings = trades.get(4);
            itemListings.removeIf(this::shouldRemove);
        } else if (type == VillagerProfession.ARMORER) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
            List<VillagerTrades.ItemListing> itemListings = trades.get(4);
            itemListings.removeIf(this::shouldRemove);
        } else if (type == VillagerProfession.FARMER) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
            List<VillagerTrades.ItemListing> itemListings2 = trades.get(2);
            List<VillagerTrades.ItemListing> itemListings5 = trades.get(5);
            if (ModIntegration.goldenmelons.loaded) {
                replaceTrade(Items.GLISTERING_MELON_SLICE, GoldenmelonsModItems.GOLDEN_MELON.get(), itemListings5);
            }
            if (ModIntegration.rejected.loaded) {
                replaceTrade(Items.PUMPKIN_PIE, RejectedModItems.PUMPKINPIE.get(), itemListings2);
            }
        }
       // try {
            //Files.delete(FMLPaths.CONFIGDIR.get().resolve("trades.txt"));
     //   } catch (IOException e) {
            //throw new RuntimeException(e);
       // }
    }

    public void replaceTrade(Item itemToReplace,Item replacement, List<VillagerTrades.ItemListing> itemListings) {
        for (Iterator<VillagerTrades.ItemListing> iterator = itemListings.iterator(); iterator.hasNext(); ) {
            VillagerTrades.ItemListing itemListing = iterator.next();
            if (itemListing instanceof VillagerTrades.ItemsForEmeralds itemsForEmeralds) {
                ItemStack item = itemsForEmeralds.itemStack;
                if (item.is(itemToReplace)) {
                    VillagerTrades.ItemsForEmeralds newTrade = new VillagerTrades.ItemsForEmeralds(
                            new ItemStack(replacement,item.getCount()),itemsForEmeralds.emeraldCost,
                            itemsForEmeralds.numberOfItems,itemsForEmeralds.maxUses,itemsForEmeralds.villagerXp);
                    iterator.remove();
                    itemListings.add(newTrade);
                    break;
                }
            }
        }
    }

    public boolean shouldRemove(VillagerTrades.ItemListing trade) {
        if (trade instanceof VillagerTrades.EmeraldForItems emeraldForItems) {
            Item item = emeraldForItems.item;
            if (item == Items.WRITABLE_BOOK) {
                return true;
            }
        } else if (trade instanceof VillagerTrades.ItemsForEmeralds itemsForEmeralds) {
            ItemStack itemStack = itemsForEmeralds.itemStack;
            if (itemStack.is(Items.SHIELD)) {
                return true;
            }
        }
        return true;
    }

    void itemCrafted(PlayerEvent.ItemCraftedEvent event) {
        Player player = event.getPlayer();
        if (player instanceof ServerPlayer serverPlayer) {
            ItemStack stack = event.getCrafting();
            Container container = event.getInventory();
            if (container instanceof CraftingContainer craftingContainer) {
                if (craftingContainer.getContainerSize() == 9) {
                    Init.ITEM_CRAFTED.trigger(serverPlayer, stack);
                }
            }
            serverPlayer.giveExperiencePoints(2);
        }
    }

    void advancementGet(AdvancementEvent e) {
        Advancement advancement = e.getAdvancement();
        Player player = e.getPlayer();
        ForgePacketHandler.sendToClient(new S2CAdvancementPacket(advancement.getId()), (ServerPlayer) player);
    }

    void afterSleep(PlayerWakeUpEvent event) {
        Player player = event.getPlayer();
        if (!WorldLocker.isPure() && player instanceof ServerPlayer && player.getRandom().nextDouble() < PS1PackTweaksConfig.SERVER.wakeupSurpriseChance.get()) {
            EntityType<? extends Monster> type = player.getRandom().nextBoolean() ? EntityType.ZOMBIE : EntityType.SKELETON;
            type.spawn((ServerLevel) player.level, null, null, player.blockPosition(), MobSpawnType.EVENT, false, false);
        }
    }

    void adjustLooting(LootingLevelEvent event) {
        DamageSource damageSource = event.getDamageSource();
        if (damageSource.getEntity() instanceof LivingEntity living) {
            MobEffectInstance luckEffect = living.getEffect(MobEffects.LUCK);
            if (luckEffect != null) {
                event.setLootingLevel(event.getLootingLevel() + luckEffect.getAmplifier() + 1);
            }
        }
    }

    //Killing a mob will turn the whole screen black and white
    void onKill(LivingDeathEvent event) {
        DamageSource source = event.getSource();
        Entity attacker = source.getEntity();
        if (attacker instanceof ServerPlayer player && !WorldLocker.isPure() && player.getRandom().nextDouble() < PS1PackTweaksConfig.SERVER.blackAndWhiteKillChance.get()) {
            ForgePacketHandler.sendToClient(new S2CShaderPacket(id("shaders/post/noir.json"), PS1PackTweaksConfig.SERVER.blackAndWhiteKillTime.get()), player);
        }

        LivingEntity entity = event.getEntityLiving();
        if (entity instanceof TamableAnimal tamableAnimal) {
            LivingEntity owner = tamableAnimal.getOwner();
            if (owner instanceof ServerPlayer serverPlayerOwner) {
                Init.PLAYER_PET_KILLED.trigger(serverPlayerOwner, tamableAnimal);
            }
        }
    }

    public static void onDragonKilled(EnderDragon dragon) {
        //disable gamerule when dragon is killed
        if (dragon.level instanceof ServerLevel serverLevel) {
            MinecraftServer server =serverLevel.getServer();
            GameRules gameRules = server.getGameRules();
            if (gameRules.getBoolean(RULE_END_SPAWN)) {
                GameRules.BooleanValue booleanValue = gameRules.getRule(RULE_END_SPAWN);
                booleanValue.set(false, server);
            }
        }
    }

    ////[Items appearing in chests] - Redstone torch, leaves, logs, rotten flesh. These items should randomly appear in player placed chests.
    public static final List<Item> items = List.of(Items.REDSTONE_TORCH, Items.OAK_LEAVES, Items.OAK_LOG, Items.ROTTEN_FLESH);

    public static void onRandomTick(BlockBehaviour block, BlockState pState, ServerLevel pLevel, BlockPos pPos, Random pRandom) {
        if (/*pLevel.getGameRules().getBoolean(RULE_CREEPY_EVENTS)*/ !WorldLocker.isPure() && (block == Blocks.TRAPPED_CHEST || block == Blocks.CHEST)) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (pRandom.nextDouble() < PS1PackTweaksConfig.SERVER.randomItemsInChestChance.get() &&
                    blockEntity instanceof ChestBlockEntity chestBlockEntity && blockEntity.getTileData().getBoolean("ps1packtweaks:player_placed")) {
                Item item = items.get(pRandom.nextInt(items.size()));
                chestBlockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(iItemHandler -> {
                    int slots = iItemHandler.getSlots();
                    ItemStack stack = item.getDefaultInstance();
                    for (int i = 0; i < slots; i++) {
                        stack = iItemHandler.insertItem(i, stack, false);
                        if (stack.isEmpty()) break;
                    }
                });
            }
        }
    }

    public static List<Motive> getCongruent(Motive motive) {
        return Registry.MOTIVE.stream().filter(motive1 -> motive1.getHeight() == motive.getHeight() && motive1.getWidth() == motive.getWidth()).toList();
    }

    public void breakBlock(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        /*if (player != null) {
            MobEffectInstance luckEffect = player.getEffect(MobEffects.LUCK);
            if (luckEffect != null) {
               // event.get(event.getLootingLevel()+luckEffect.getAmplifier()+1);
            }
        }*/
    }

    public static void onStatAwarded(Player player, ResourceLocation pStat, int pIncrement) {
        if (player instanceof ServerPlayer && !WorldLocker.isPure()) {
            if (pStat == Stats.WALK_ONE_CM || pStat == Stats.SPRINT_ONE_CM) {
                double leavesLogChance = PS1PackTweaksConfig.SERVER.leaves_and_logs_chance.get() * pIncrement;
                if (player.getRandom().nextDouble() < leavesLogChance) {
                    BlockPos pos = player.blockPosition();
                    int yMax = player.level.getHeight(Heightmap.Types.MOTION_BLOCKING, pos.getX(), pos.getZ());
                    while (pos.getY() < yMax) {
                        pos = pos.above();
                        BlockState state = player.level.getBlockState(pos);
                        if (state.isCollisionShapeFullBlock(player.level, pos)) {
                            if (state.is(BlockTags.LEAVES) && state.getBlock() instanceof LeavesBlock && !state.getValue(LeavesBlock.PERSISTENT)) {
                                ItemLike itemLike;
                                if (player.getRandom().nextBoolean()) {
                                    itemLike = Blocks.OAK_LOG;
                                } else {
                                    itemLike = Blocks.OAK_LEAVES;
                                }
                                NonNullList<ItemStack> items = player.getInventory().items;
                                for (int i = 9; i < 36; i++) {
                                    ItemStack stack = items.get(i);
                                    if (stack.isEmpty()) {
                                        items.set(i, itemLike.asItem().getDefaultInstance());
                                        break;
                                    } else if (itemLike.asItem() == stack.getItem() && stack.getCount() < stack.getMaxStackSize()) {
                                        stack.grow(1);
                                        break;
                                    }
                                }
                            }
                            break;
                        }
                    }
                }
            }
        }
    }

    void playerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START && event.side == LogicalSide.SERVER) {
            ServerPlayer player = (ServerPlayer) event.player;
            CustomEvents.handleEvents(player);
        }
    }

    void rightClickEntity(PlayerInteractEvent.EntityInteract event) {
    }

    void rightClickBlock(PlayerInteractEvent.RightClickBlock event) {
    }

    void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(
                Init.ModBlocks.EBONY_SIGN.setRegistryName("ebony_sign"),
                Init.ModBlocks.EBONY_WALL_SIGN.setRegistryName("ebony_wall_sign"),
                Init.ModBlocks.ENDERVIOLET_SIGN.setRegistryName("enderviolet_sign"),
                Init.ModBlocks.ENDERVIOLET_WALL_SIGN.setRegistryName("enderviolet_wall_sign"),
                Init.ModBlocks.ENDERSHROOM.setRegistryName("endershroom"),
                Init.ModBlocks.ENDERSHROOM_BLOCK.setRegistryName("endershroom_block")
        );
    }

    void preventColor(EntityMobGriefingEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Evoker) {
            event.setResult(Event.Result.DENY);
        }
    }

    void registerParticleTypes(RegistryEvent.Register<ParticleType<?>> event) {
        event.getRegistry().registerAll(Init.ModParticleTypes.ENDERMAN.setRegistryName("enderman"),
                Init.ModParticleTypes.BLUE_ENDERMAN.setRegistryName("blue_enderman"),
                Init.ModParticleTypes.FINAL_HEROBRINE.setRegistryName("final_herobrine")
        );
    }

    void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(
                Init.ModItems.BARNACLE_TOOTH.setRegistryName("barnacle_tooth"),
                Init.ModItems.PRISMARINE_ROD.setRegistryName("prismarine_rod"),
                Init.ModItems.EBONY_SIGN.setRegistryName("ebony_sign"),
                Init.ModItems.ENDERVIOLET_SIGN.setRegistryName("enderviolet_sign"),
                Init.ModItems.EBONY_BOAT.setRegistryName("ebony_boat"),
                Init.ModItems.ENDERVIOLET_BOAT.setRegistryName("enderviolet_boat"),
                Init.ModItems.ENDERSHROOM.setRegistryName("endershroom"),
                Init.ModItems.ENDERSHROOM_BLOCK.setRegistryName("endershroom_block")
        );
    }

    void registerEntities(RegistryEvent.Register<EntityType<?>> event) {
        event.getRegistry().registerAll(Init.ModEntityTypes.BARNACLE.setRegistryName("barnacle"),
                Init.ModEntityTypes.HEROBRINE.setRegistryName("herobrine"),
                Init.ModEntityTypes.FINAL_HEROBRINE.setRegistryName("final_herobrine"),
                Init.ModEntityTypes.SCRIPTED_MIDNIGHT_LURKER.setRegistryName("scripted_midnight_lurker"),
                Init.ModEntityTypes.INVISIBLE_ENTITY.setRegistryName("invisible_entity")
        );
    }

    void registerSounds(RegistryEvent.Register<SoundEvent> event) {
        event.getRegistry().registerAll(Init.ModSounds.BARNACLE_AMBIENT.setRegistryName("barnacle_ambient"),
                Init.ModSounds.BARNACLE_HURT.setRegistryName("barnacle_hurt"), Init.ModSounds.BARNACLE_DEATH.setRegistryName("barnacle_death"),
                Init.ModSounds.BARNACLE_FLOP.setRegistryName("barnacle_flop"), Init.ModSounds.SCREEN.setRegistryName("screen"),
                Init.ModSounds.LURKER_JUMPSCARE.setRegistryName("lurker"));
    }

    void registerFeatures(RegistryEvent.Register<Feature<?>> event) {
        event.getRegistry().registerAll(Init.ModFeatures.TUNNEL.setRegistryName("tunnel"), Init.ModFeatures.SIGN.setRegistryName("sign"),
                Init.ModFeatures.PYRAMID.setRegistryName("pyramid"), Init.ModFeatures.HUGE_ENDERSHROOM.setRegistryName("huge_endershroom"));
    }

    void registerStructures(RegistryEvent.Register<StructureFeature<?>> event) {
        event.getRegistry().registerAll(ModStructureFeatures.END_VILLAGE.setRegistryName("end_village"));
    }

    void registerMenus(RegistryEvent.Register<MenuType<?>> event) {
        event.getRegistry().registerAll(Init.ModMenus.FLETCHING_TABLE.setRegistryName("fletching_table"));
    }

    void registerMotives(RegistryEvent.Register<Motive> event) {
        event.getRegistry().registerAll(Init.ModPaintings.CURSED_COURBET.setRegistryName("cursed_courbet"));
    }

    void registerGLMs(RegistryEvent.Register<GlobalLootModifierSerializer<?>> event) {
        Registry.register(Registry.LOOT_CONDITION_TYPE, id("or_loot_table_id"), OrLootTableCondition.OR_LOOT_TABLE_ID);
        event.getRegistry().registerAll(Init.GlobalLootModifiers.DUPLICATE_OUTPUTS.setRegistryName("duplicate_outputs"),
                Init.GlobalLootModifiers.ADD_ITEM.setRegistryName("add_item")
        );
    }


    void sleepCheck(SleepingTimeCheckEvent event) {
        if (event.getResult() == Event.Result.DENY) return;

        Player player = event.getPlayer();
        Level level = player.level;
        if (level.dimensionType().hasFixedTime()) return;

        int skyDarken = getSkyDarken(level);

        if (skyDarken < 4) {
            event.setResult(Event.Result.DENY);
        }
    }

    public static final Set<ResourceLocation> GENERATE_BIOMES = Set.of(new ResourceLocation("forest"),
            new ResourceLocation("windswept_hills"), new ResourceLocation("taiga"),
            new ResourceLocation("birch_forest"), new ResourceLocation("windswept_forest"), new ResourceLocation("windswept_gravelly_hills"),
            new ResourceLocation("snowy_taiga"), new ResourceLocation("desert"));

    void biomeLoading(BiomeLoadingEvent event) {

        boolean pure = WorldLocker.isPure();
        BiomeGenerationSettingsBuilder generation = event.getGeneration();
        ResourceLocation biomeName = event.getName();
        if (!pure) {

            if (GENERATE_BIOMES.contains(biomeName)) {
                generation.addFeature(GenerationStep.Decoration.UNDERGROUND_STRUCTURES, ModPlacedFeatures.PLACED_TUNNEL);
                generation.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, ModPlacedFeatures.PLACED_TUNNEL);
                generation.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, ModPlacedFeatures.COBBLE_ROCK);
            }


            Biome.BiomeCategory category = event.getCategory();

            switch (category) {
                case SAVANNA -> {
                    addIfNotPresent(generation, GenerationStep.Decoration.SURFACE_STRUCTURES, ModPlacedFeatures.PLACED_OAK_SIGN);
                    addIfNotPresent(generation, GenerationStep.Decoration.SURFACE_STRUCTURES, ModPlacedFeatures.PLACED_ACACIA_SIGN);
                }

                case JUNGLE -> {
                    addIfNotPresent(generation, GenerationStep.Decoration.SURFACE_STRUCTURES, ModPlacedFeatures.PLACED_JUNGLE_SIGN);

                }
                case FOREST -> {
                    addIfNotPresent(generation, GenerationStep.Decoration.SURFACE_STRUCTURES, ModPlacedFeatures.PLACED_OAK_SIGN);
                    addIfNotPresent(generation, GenerationStep.Decoration.SURFACE_STRUCTURES, ModPlacedFeatures.PLACED_BIRCH_SIGN);
                    addIfNotPresent(generation, GenerationStep.Decoration.SURFACE_STRUCTURES, ModPlacedFeatures.PLACED_DARK_OAK_SIGN);
                }
                case TAIGA -> {
                    addIfNotPresent(generation, GenerationStep.Decoration.SURFACE_STRUCTURES, ModPlacedFeatures.PLACED_SPRUCE_SIGN);
                }
                case OCEAN -> {
              /*  generation.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, ModPlacedFeatures.PLACED_PYRAMID);
                event.getSpawns().getSpawner(MobCategory.MONSTER).add(new MobSpawnSettings.SpawnerData(Init.ModEntityTypes.BARNACLE,
                        1000, 1, 3));*/
                }
                case THEEND -> {

                }
            }
        }


        if (Objects.equals(Biomes.END_BARRENS.location(), biomeName) || Objects.equals(Biomes.END_HIGHLANDS.location(), biomeName)) {
            generation.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, ModPlacedFeatures.HUGE_ENDERSHROOM);
        }
    }

    void lateBiomeLoading(BiomeLoadingEvent event) {
        boolean pure = WorldLocker.isPure();
        if (pure) {
            for (MobCategory category : MobCategory.values()) {
                List<MobSpawnSettings.SpawnerData> spawner = event.getSpawns().getSpawner(category);
                List<MobSpawnSettings.SpawnerData> remove = new ArrayList<>();
                for (MobSpawnSettings.SpawnerData data : spawner) {
                    EntityType<?> type = data.type;
                    ResourceLocation id = Registry.ENTITY_TYPE.getKey(type);
                    String modid = id.getNamespace();
                    if (ModIntegration.shouldRemoveMobs(modid)) {
                        remove.add(data);
                    }
                }
                spawner.removeAll(remove);
            }
        }
    }

    //can run clientside
    void entityJoinWorld(EntityJoinWorldEvent event) {
        Entity entity = event.getEntity();
        Level level = entity.level;
        if (!level.isClientSide) {
            boolean pure = WorldLocker.isPure();

            if (pure) {

                if (entity.getType() == Init.ModEntityTypes.INVISIBLE_ENTITY || entity.getType() == Init.ModEntityTypes.SCRIPTED_MIDNIGHT_LURKER ||
                        entity.getType() == Init.ModEntityTypes.HEROBRINE) {
                    event.setCanceled(true);
                    return;
                }

                ResourceLocation id = Registry.ENTITY_TYPE.getKey(entity.getType());
                String modid = id.getNamespace();

                if (ModIntegration.shouldRemoveMobs(modid)) {
                    event.setCanceled(true);
                }
            } else {
                if (entity instanceof PathfinderMob pathfinderMob) {
                    if (pathfinderMob.getNavigation() instanceof GroundPathNavigation || pathfinderMob.getNavigation() instanceof FlyingPathNavigation) {
                        pathfinderMob.goalSelector.addGoal(0, new FollowPlayerGoal(pathfinderMob, 1.0, 4.0F, 30.0F));
                    }
                }
            }
        }
    }

    static boolean hasFeature(List<Holder<PlacedFeature>> features, Holder<PlacedFeature> feature) {
        return features.stream().anyMatch(f -> f.is(feature.unwrapKey().get()));
    }

    static void addIfNotPresent(BiomeGenerationSettingsBuilder generation, GenerationStep.Decoration step, Holder<PlacedFeature> feature) {
        if (!hasFeature(generation.getFeatures(step), feature)) {
            generation.addFeature(step, feature);
        }
    }

    void onAttributeCreate(EntityAttributeCreationEvent event) {
        event.put(Init.ModEntityTypes.BARNACLE, Barnacle.setCustomAttributes().build());
        event.put(Init.ModEntityTypes.SCRIPTED_MIDNIGHT_LURKER, ScriptedMidnightLurker.createAttributes().build());
        event.put(Init.ModEntityTypes.HEROBRINE, EventHerobrineEntity.createAttributes().build());
        event.put(Init.ModEntityTypes.FINAL_HEROBRINE, EventHerobrineEntity.createAttributes().build());
        event.put(Init.ModEntityTypes.INVISIBLE_ENTITY, InvisibleEntity.createAttributes().build());
    }

    public int getSkyDarken(Level level) {
        double d0 = 1.0D - (level.getRainLevel(1.0F) * 5.0F) / 16.0D;
        double d2 = 0.5D + 2.0D * Mth.clamp(Mth.cos(level.getTimeOfDay(1.0F) * ((float) Math.PI * 2F)), -0.25D, 0.25D);
        return (int) ((1.0D - d2 * d0) * 11.0D);
    }
    //Potion of luck can be brewed with an ender clover from End's Phantasm mod

    private void setup(final FMLCommonSetupEvent event) {

        PacketHandler.registerPackets();
        event.enqueueWork(() -> {
            WorldLocker.loadKeysFromFile();

            Blocks.CHEST.getStateDefinition().getPossibleStates().forEach(state -> ((BlockStateAccess)state).setCanOcclude(false));
            Blocks.TRAPPED_CHEST.getStateDefinition().getPossibleStates().forEach(state -> ((BlockStateAccess)state).setCanOcclude(false));
            Blocks.ENDER_CHEST.getStateDefinition().getPossibleStates().forEach(state -> ((BlockStateAccess)state).setCanOcclude(false));

            Init.ModEntityDataSerializers.init();
            setCanOcclude(Blocks.ICE, true);
            ModTreeFeatures.init();
            Init.init();
            PotionBrewing.addMix(Potions.AWKWARD, StarryEndBlocks.ENDER_CLOVER.get().asItem(), Potions.LUCK);
            ModConfiguredFeatures.init();
            if (ModIntegration.morehorsearmor.loaded) {
                MoreHorseArmorCompat.setup();
            }
            if (ModIntegration.enderitemod.loaded) {
                EnderiteModCompat.setup();
            }
            changePOI(PoiType.TOOLSMITH, Set.of(Blocks.ANVIL, Blocks.CHIPPED_ANVIL, Blocks.DAMAGED_ANVIL));
            if (ModIntegration.brewingcauldron.loaded) {
                BrewingCauldronCompat.setup();
            }
            ((BlockAccess) Blocks.CHEST).setIsRandomlyTicking(true);
            ((BlockAccess) Blocks.TRAPPED_CHEST).setIsRandomlyTicking(true);
            SpawnPlacements.register(Init.ModEntityTypes.BARNACLE, SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    Barnacle::canSpawn);

            setEffectColor(AMEffectRegistry.ENDER_FLU, 0x4cff4c);

            //0x6836aa
            for (Enchantment enchantment : Registry.ENCHANTMENT) {
                if (enchantment.getRegistryName().getNamespace().equals(ModIntegration.alexsmobs.name())) {
                    ((EnchantmentDuck) enchantment).setDiscoverable(false);
                    ((EnchantmentDuck) enchantment).setTradeable(false);
                }
            }

            if (WorldLocker.isPure()) {
                removeEventsAfterPurification();
            }
        });

    }

    public static void removeEventsAfterPurification() {

        //disable events
        EventBus gameEvents = (EventBus) MinecraftForge.EVENT_BUS;


        try {

            VarHandle PRIVATE_TEST_VARIABLE = MethodHandles
                    .privateLookupIn(EventBus.class, MethodHandles.lookup())
                    .findVarHandle(EventBus.class, "listeners", ConcurrentHashMap.class);


            ConcurrentHashMap<Object, List<IEventListener>> listeners = (ConcurrentHashMap<Object, List<IEventListener>>) PRIVATE_TEST_VARIABLE.get(gameEvents);

            List<Object> disableEvents = listeners.keySet().stream().filter(PS1PackTweaks::shouldDisableEvent).toList();

            disableEvents.forEach(listeners::remove);

        } catch (NoSuchFieldException e) {
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }


    public static void skipGlassRendering(BlockAndTintGetter pLevel, BlockPos pPos, FluidState pFluidState,
                                          BlockState pBlockState, Direction pSide, FluidState pNeighborFluid, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) return;
        BlockState otherState = pLevel.getBlockState(pPos.relative(pSide));
        if (pSide != Direction.UP && otherState.getBlock() instanceof HalfTransparentBlock) {
            cir.setReturnValue(false);
        }
    }

    public static void changePOI(PoiType poiType, Set<Block> blocks) {
        Set<BlockState> oldStates = poiType.getBlockStates();
        for (BlockState oldState : oldStates) {
            PoiAccess.getTYPE_BY_STATE().remove(oldState);
        }
        Set<BlockState> newStates = blocks.stream().flatMap(block -> block.getStateDefinition().getPossibleStates().stream()).collect(Collectors.toSet());
        poiType.matchingStates = newStates;
        for (BlockState state : newStates) {
            PoiAccess.getTYPE_BY_STATE().put(state, poiType);
        }
        PoiType.ALL_STATES = new ObjectOpenHashSet<>(PoiAccess.getTYPE_BY_STATE().keySet());
    }

    public static void setEffectColor(MobEffect effect, int color) {
        ((MobEffectAccess) effect).setColor(color);
    }

    public static void setDestroySpeed(Block block, float v) {
        ImmutableList<BlockState> possibleStates = block.getStateDefinition().getPossibleStates();
        possibleStates.forEach(state -> ((BlockStateAccess) state).setDestroySpeed(v));
    }

    public static void setMaterial(Block block, Material material) {
        ImmutableList<BlockState> possibleStates = block.getStateDefinition().getPossibleStates();
        possibleStates.forEach(state -> ((BlockStateAccess) state).setMaterial(material));
    }

    public static void setCanOcclude(Block block, boolean canOcclude) {
        ImmutableList<BlockState> possibleStates = block.getStateDefinition().getPossibleStates();
        possibleStates.forEach(state -> ((BlockStateAccess) state).setCanOcclude(canOcclude));
    }


    public static void setRequiresCorrectToolForDrops(Block block, boolean v) {
        ImmutableList<BlockState> possibleStates = block.getStateDefinition().getPossibleStates();
        possibleStates.forEach(state -> ((BlockStateAccess) state).setRequiresCorrectToolForDrops(v));
    }

    public static void setLootTable(Block block, ResourceLocation lootTable) {
        BlockAccess blockAccess = (BlockAccess) block;
        blockAccess.setDrops(lootTable);
        blockAccess.setLootTableSupplier(() -> lootTable);
    }

    public static void setDefaultLootTable(Block block) {
        ResourceLocation r = new ResourceLocation(block.getRegistryName().getNamespace(), "blocks/" + block.getRegistryName().getPath());
        setLootTable(block, r);
    }

    //borrowed from https://github.com/Commoble/respawn/blob/1.20.1/src/main/java/commoble/respawn/RespawnMod.java which is not on 1.18.2

    public static ResourceKey<Level> redirectPlayerListPlaceNewPlayerGetOverworld(PlayerList playerList) {
        MinecraftServer server = playerList.getServer();
        if (server.getGameRules().getBoolean(RULE_NETHER_SPAWN)) {
            return Level.NETHER;
        }

        if (server.getGameRules().getBoolean(RULE_END_SPAWN)) {
            return Level.END;
        }
        return Level.OVERWORLD;
    }

    public static void onPlayerListGetPlayerForLogin(PlayerList playerList, GameProfile profile, CallbackInfoReturnable<ServerPlayer> cir) {

        ResourceKey<Level> levelKey = redirectPlayerListPlaceNewPlayerGetOverworld(playerList);
        MinecraftServer server = playerList.getServer();
            ServerLevel serverLevel = server.getLevel(levelKey);
            if (serverLevel == null) {
                LOGGER.error("Invalid level key {}", levelKey.location());
            }
            else
            {
                cir.setReturnValue(new ServerPlayer(server, serverLevel, profile));
            }
        }

    public static ServerLevel redirectPlayerListRespawnGetServerOverworld(MinecraftServer server)
    {
        var levelKey = redirectPlayerListPlaceNewPlayerGetOverworld(server.getPlayerList());
        ServerLevel serverLevel = server.getLevel(levelKey);
        if (serverLevel == null)
        {
            LOGGER.error("Invalid level key {}", levelKey.location());
        }
        else
        {
            return serverLevel;
        }
        return server.overworld(); // if we don't want to redirect, fall back to vanilla
    }



    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
