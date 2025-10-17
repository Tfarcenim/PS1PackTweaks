package tfar.ps1packtweaks;

import com.Apothic0n.StarryEnd.core.objects.StarryEndBlocks;
import com.google.common.collect.ImmutableList;
import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.*;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.village.poi.PoiType;
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
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityMobGriefingEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LootingLevelEvent;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.items.CapabilityItemHandler;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfar.ps1packtweaks.client.PS1PackTweaksClient;
import tfar.ps1packtweaks.compat.BrewingCauldronCompat;
import tfar.ps1packtweaks.compat.EnderiteModCompat;
import tfar.ps1packtweaks.compat.ModIntegration;
import tfar.ps1packtweaks.compat.MoreHorseArmorCompat;
import tfar.ps1packtweaks.datagen.ModDataGenerator;
import tfar.ps1packtweaks.datagen.OrLootTableCondition;
import tfar.ps1packtweaks.duck.EnchantmentDuck;
import tfar.ps1packtweaks.entity.Barnacle;
import tfar.ps1packtweaks.entity.HerobrineEntity;
import tfar.ps1packtweaks.entity.InvisibleEntity;
import tfar.ps1packtweaks.entity.ScriptedMidnightLurker;
import tfar.ps1packtweaks.entity.goals.FollowPlayerGoal;
import tfar.ps1packtweaks.mixin.BlockAccess;
import tfar.ps1packtweaks.mixin.BlockStateAccess;
import tfar.ps1packtweaks.mixin.PoiAccess;
import tfar.ps1packtweaks.network.ForgePacketHandler;
import tfar.ps1packtweaks.network.PacketHandler;
import tfar.ps1packtweaks.network.client.S2CAdvancementPacket;
import tfar.ps1packtweaks.network.client.S2CShaderPacket;
import tfar.ps1packtweaks.worldgen.ModConfiguredFeatures;
import tfar.ps1packtweaks.worldgen.ModPlacedFeatures;
import tfar.ps1packtweaks.worldgen.ModStructureFeatures;
import tfar.ps1packtweaks.worldgen.ModTreeFeatures;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(PS1PackTweaks.MOD_ID)
public class PS1PackTweaks {
    public static final String MOD_ID = "ps1packtweaks";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final boolean TRIGGER_BANNER_CRASH = false;

    public static final boolean DEV = !FMLLoader.isProduction();

    public static final GameRules.Key<GameRules.BooleanValue> RULE_CREEPY_EVENTS = GameRules.register
            ("doCreepyEvents", GameRules.Category.UPDATES, GameRules.BooleanValue.create(true));

    public PS1PackTweaks() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, PS1PackTweaksConfig.SERVER_SPEC);
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        // Register the setup method for modloading
        bus.addListener(this::setup);
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

        bus.addListener(ModDataGenerator::gatherData);
        bus.addListener(PS1PackTweaksConfig::configUpdate);
        MinecraftForge.EVENT_BUS.addListener(this::sleepCheck);
        bus.addListener(this::onAttributeCreate);
        MinecraftForge.EVENT_BUS.addListener(this::rightClickEntity);
        MinecraftForge.EVENT_BUS.addListener(this::playerTick);
        //MinecraftForge.EVENT_BUS.addListener(this::breakBlock);
        MinecraftForge.EVENT_BUS.addListener(this::biomeLoading);
        MinecraftForge.EVENT_BUS.addListener(this::entityJoinWorld);
        MinecraftForge.EVENT_BUS.addListener(this::onKill);
        MinecraftForge.EVENT_BUS.addListener(this::adjustLooting);
        MinecraftForge.EVENT_BUS.addListener(this::afterSleep);
        MinecraftForge.EVENT_BUS.addListener(this::advancementGet);
        MinecraftForge.EVENT_BUS.addListener(this::itemCrafted);
        MinecraftForge.EVENT_BUS.addListener(this::manageVillagerTrades);
        MinecraftForge.EVENT_BUS.addListener(this::rightClickBlock);
        MinecraftForge.EVENT_BUS.addListener(this::preventColor);
        MinecraftForge.EVENT_BUS.addListener(FinalHerobrine::useFlintAndSteel);
    }

    public void manageVillagerTrades(VillagerTradesEvent event) {
        VillagerProfession type = event.getType();
        if (type == VillagerProfession.LIBRARIAN) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
            List<VillagerTrades.ItemListing> itemListings = trades.get(4);
            itemListings.removeIf(this::shouldRemove);
        }
    }

    public boolean shouldRemove(VillagerTrades.ItemListing trade) {
        if (trade instanceof VillagerTrades.EmeraldForItems emeraldForItems) {
            Item item = emeraldForItems.item;
            if (item == Items.WRITABLE_BOOK) {
                return true;
            }
        }
        return true;
    }

    public static boolean isWorldPure(WorldGenLevel level) {
        if (level instanceof WorldGenRegion worldGenRegion) {
            return !worldGenRegion.getLevel().getGameRules().getBoolean(RULE_CREEPY_EVENTS);
        } else if (level instanceof ServerLevel serverLevel) {
            return !serverLevel.getGameRules().getBoolean(RULE_CREEPY_EVENTS);
        }
        return false;
    }

    void itemCrafted(PlayerEvent.ItemCraftedEvent event) {
        Player player = event.getPlayer();
        if (player instanceof ServerPlayer serverPlayer) {
            ItemStack stack = event.getCrafting();
            Container container = event.getInventory();
            if (container instanceof CraftingContainer craftingContainer) {
                if (craftingContainer.getContainerSize() == 9){
                    Init.ITEM_CRAFTED.trigger(serverPlayer,stack);
                }
            }
        }
    }

    void advancementGet(AdvancementEvent e) {
        Advancement advancement = e.getAdvancement();
        Player player = e.getPlayer();
        ForgePacketHandler.sendToClient(new S2CAdvancementPacket(advancement.getId()),(ServerPlayer) player);
    }

    void afterSleep(PlayerWakeUpEvent event) {
        Player player = event.getPlayer();
        if (player.level.getGameRules().getBoolean(RULE_CREEPY_EVENTS) && player instanceof ServerPlayer && player.getRandom().nextDouble() < PS1PackTweaksConfig.SERVER.wakeupSurpriseChance.get()) {
            EntityType<? extends Monster> type = player.getRandom().nextBoolean() ? EntityType.ZOMBIE : EntityType.SKELETON;
            type.spawn((ServerLevel) player.level, null, null, player.blockPosition(), MobSpawnType.EVENT, false, false);
        }
    }

    void adjustLooting(LootingLevelEvent event) {
        DamageSource damageSource = event.getDamageSource();
        if (damageSource.getEntity() instanceof LivingEntity living) {
            MobEffectInstance luckEffect = living.getEffect(MobEffects.LUCK);
            if (luckEffect != null) {
                event.setLootingLevel(event.getLootingLevel()+luckEffect.getAmplifier()+1);
            }
        }
    }

    //Killing a mob will turn the whole screen black and white
    void onKill(LivingDeathEvent event) {
        DamageSource source = event.getSource();
        Entity attacker = source.getEntity();
        if (attacker instanceof ServerPlayer player && player.level.getGameRules().getBoolean(RULE_CREEPY_EVENTS) && player.getRandom().nextDouble() < PS1PackTweaksConfig.SERVER.blackAndWhiteKillChance.get()) {
            ForgePacketHandler.sendToClient(new S2CShaderPacket(id("shaders/post/noir.json"),PS1PackTweaksConfig.SERVER.blackAndWhiteKillTime.get()),player);
        }

        LivingEntity entity = event.getEntityLiving();
        if (entity instanceof TamableAnimal tamableAnimal) {
            LivingEntity owner = tamableAnimal.getOwner();
            if (owner instanceof ServerPlayer serverPlayerOwner) {
                Init.PLAYER_PET_KILLED.trigger(serverPlayerOwner,tamableAnimal);
            }
        }
    }

    ////[Items appearing in chests] - Redstone torch, leaves, logs, rotten flesh. These items should randomly appear in player placed chests.
    public static final List<Item> items = List.of(Items.REDSTONE_TORCH,Items.OAK_LEAVES,Items.OAK_LOG,Items.ROTTEN_FLESH);
    public static void onRandomTick(BlockBehaviour block, BlockState pState, ServerLevel pLevel, BlockPos pPos, Random pRandom) {
        if (pLevel.getGameRules().getBoolean(RULE_CREEPY_EVENTS) && (block == Blocks.TRAPPED_CHEST || block == Blocks.CHEST)) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (pRandom.nextDouble() < PS1PackTweaksConfig.SERVER.randomItemsInChestChance.get() &&
                    blockEntity instanceof ChestBlockEntity chestBlockEntity && blockEntity.getTileData().getBoolean("ps1packtweaks:player_placed")) {
                Item item = items.get(pRandom.nextInt(items.size()));
                chestBlockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(iItemHandler -> {
                    int slots = iItemHandler.getSlots();
                    ItemStack stack = item.getDefaultInstance();
                    for (int i = 0; i < slots;i++) {
                        stack = iItemHandler.insertItem(i,stack,false);
                        if (stack.isEmpty())break;
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
        if (player instanceof ServerPlayer) {
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
                Init.ModParticleTypes.BLUE_ENDERMAN.setRegistryName("blue_enderman"));
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
                Init.ModEntityTypes.SCRIPTED_MIDNIGHT_LURKER.setRegistryName("scripted_midnight_lurker"),
                Init.ModEntityTypes.INVISIBLE_ENTITY.setRegistryName("invisible_entity")
        );
    }

    void registerSounds(RegistryEvent.Register<SoundEvent> event) {
        event.getRegistry().registerAll(Init.ModSounds.BARNACLE_AMBIENT.setRegistryName("barnacle_ambient"),
                Init.ModSounds.BARNACLE_HURT.setRegistryName("barnacle_hurt"), Init.ModSounds.BARNACLE_DEATH.setRegistryName("barnacle_death"),
                Init.ModSounds.BARNACLE_FLOP.setRegistryName("barnacle_flop"),Init.ModSounds.SCREEN.setRegistryName("screen"));
    }

    void registerFeatures(RegistryEvent.Register<Feature<?>> event) {
        event.getRegistry().registerAll(Init.ModFeatures.TUNNEL.setRegistryName("tunnel"),Init.ModFeatures.SIGN.setRegistryName("sign"),
                Init.ModFeatures.PYRAMID.setRegistryName("pyramid"),Init.ModFeatures.HUGE_ENDERSHROOM.setRegistryName("huge_endershroom"));
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
        Registry.register(Registry.LOOT_CONDITION_TYPE,id("or_loot_table_id"), OrLootTableCondition.OR_LOOT_TABLE_ID);
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
        BiomeGenerationSettingsBuilder generation = event.getGeneration();
        ResourceLocation biomeName = event.getName();
        if (GENERATE_BIOMES.contains(biomeName)) {
            generation.addFeature(GenerationStep.Decoration.UNDERGROUND_STRUCTURES, ModPlacedFeatures.PLACED_TUNNEL);
            generation.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, ModPlacedFeatures.PLACED_TUNNEL);
            generation.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, ModPlacedFeatures.COBBLE_ROCK);
        }

        Biome.BiomeCategory category = event.getCategory();

        switch (category) {
            case SAVANNA -> {
                addIfNotPresent(generation,GenerationStep.Decoration.SURFACE_STRUCTURES,ModPlacedFeatures.PLACED_OAK_SIGN);
                addIfNotPresent(generation,GenerationStep.Decoration.SURFACE_STRUCTURES,ModPlacedFeatures.PLACED_ACACIA_SIGN);
            }

            case JUNGLE -> {
                addIfNotPresent(generation,GenerationStep.Decoration.SURFACE_STRUCTURES,ModPlacedFeatures.PLACED_JUNGLE_SIGN);

            }
            case FOREST -> {
                addIfNotPresent(generation,GenerationStep.Decoration.SURFACE_STRUCTURES,ModPlacedFeatures.PLACED_OAK_SIGN);
                addIfNotPresent(generation,GenerationStep.Decoration.SURFACE_STRUCTURES,ModPlacedFeatures.PLACED_BIRCH_SIGN);
                addIfNotPresent(generation,GenerationStep.Decoration.SURFACE_STRUCTURES,ModPlacedFeatures.PLACED_DARK_OAK_SIGN);
            }
            case TAIGA -> {
                addIfNotPresent(generation,GenerationStep.Decoration.SURFACE_STRUCTURES,ModPlacedFeatures.PLACED_SPRUCE_SIGN);
            }
            case OCEAN -> {
                generation.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES,ModPlacedFeatures.PLACED_PYRAMID);
                event.getSpawns().getSpawner(MobCategory.MONSTER).add(new MobSpawnSettings.SpawnerData(Init.ModEntityTypes.BARNACLE,
                        1000, 2, 3));
            }
            case THEEND -> {

            }
        }

        ResourceLocation name = event.getName();

        if (Objects.equals(Biomes.END_BARRENS.location(),name) || Objects.equals(Biomes.END_HIGHLANDS.location(),name)){
            generation.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES,ModPlacedFeatures.HUGE_ENDERSHROOM);
        }
    }

    void entityJoinWorld(EntityJoinWorldEvent event) {
        Entity var2 = event.getEntity();
        if (var2 instanceof PathfinderMob pathfinderMob) {
            if (pathfinderMob.getNavigation() instanceof GroundPathNavigation || pathfinderMob.getNavigation() instanceof FlyingPathNavigation) {
                pathfinderMob.goalSelector.addGoal(0, new FollowPlayerGoal(pathfinderMob, 1.0, 4.0F, 30.0F));
            }
        }
    }

    static boolean hasFeature(List<Holder<PlacedFeature>> features,Holder<PlacedFeature> feature) {
        return features.stream().anyMatch(f -> f.is(feature.unwrapKey().get()));
    }

    static void addIfNotPresent(BiomeGenerationSettingsBuilder generation,GenerationStep.Decoration step, Holder<PlacedFeature> feature) {
        if (!hasFeature(generation.getFeatures(step),feature)) {
            generation.addFeature(step, feature);
        }
    }

    void onAttributeCreate(EntityAttributeCreationEvent event) {
        event.put(Init.ModEntityTypes.BARNACLE, Barnacle.setCustomAttributes().build());
        event.put(Init.ModEntityTypes.SCRIPTED_MIDNIGHT_LURKER, ScriptedMidnightLurker.createAttributes().build());
        event.put(Init.ModEntityTypes.HEROBRINE, HerobrineEntity.createAttributes().build());
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
            setCanOcclude(Blocks.ICE,true);
            ModTreeFeatures.init();
            Init.init();
            PotionBrewing.addMix(Potions.AWKWARD, StarryEndBlocks.ENDER_CLOVER.get().asItem(),Potions.LUCK);
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
            ((BlockAccess)Blocks.CHEST).setIsRandomlyTicking(true);
            ((BlockAccess)Blocks.TRAPPED_CHEST).setIsRandomlyTicking(true);
            SpawnPlacements.register(Init.ModEntityTypes.BARNACLE, SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    Barnacle::canSpawn);

            for (Enchantment enchantment : Registry.ENCHANTMENT) {
                if (enchantment.getRegistryName().getNamespace().equals(ModIntegration.alexsmobs.name())) {
                    ((EnchantmentDuck)enchantment).setDiscoverable(false);
                    ((EnchantmentDuck)enchantment).setTradeable(false);
                }
            }
        });

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

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
