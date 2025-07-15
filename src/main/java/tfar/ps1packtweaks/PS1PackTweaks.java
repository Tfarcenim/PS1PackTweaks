package tfar.ps1packtweaks;

import com.google.common.collect.ImmutableList;
import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.SleepingTimeCheckEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfar.ps1packtweaks.client.PS1PackTweaksClient;
import tfar.ps1packtweaks.compat.BrewingCauldronCompat;
import tfar.ps1packtweaks.compat.EnderiteModCompat;
import tfar.ps1packtweaks.compat.ModIntegration;
import tfar.ps1packtweaks.compat.MoreHorseArmorCompat;
import tfar.ps1packtweaks.datagen.ModDataGenerator;
import tfar.ps1packtweaks.entity.Barnacle;
import tfar.ps1packtweaks.mixin.BlockAccess;
import tfar.ps1packtweaks.mixin.BlockStateAccess;
import tfar.ps1packtweaks.mixin.PoiAccess;
import tfar.ps1packtweaks.network.ForgePacketHandler;
import tfar.ps1packtweaks.network.PacketHandler;
import tfar.ps1packtweaks.network.client.S2CTargetDimensionPacket;

import java.util.Set;
import java.util.stream.Collectors;

import static tfar.ps1packtweaks.Init.ModSounds.*;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(PS1PackTweaks.MOD_ID)
public class PS1PackTweaks
{
    public static final String MOD_ID = "ps1packtweaks";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final boolean TRIGGER_BANNER_CRASH = false;

    public PS1PackTweaks()
    {
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER,PS1TweaksConfig.SERVER_SPEC);
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        // Register the setup method for modloading
        bus.addListener(this::setup);
        if (FMLEnvironment.dist.isClient()) {
            PS1PackTweaksClient.init(bus);
        }

        bus.addGenericListener(Block.class,this::registerBlocks);
        bus.addGenericListener(BlockEntityType.class,this::registerBlockEntities);
        bus.addGenericListener(Item.class,this::registerItems);
        bus.addGenericListener(EntityType.class,this::registerEntities);
        bus.addGenericListener(SoundEvent.class,this::registerSounds);

        bus.addListener(ModDataGenerator::gatherData);
        MinecraftForge.EVENT_BUS.addListener(this::sleepCheck);
        bus.addListener(this::onAttributeCreate);
        MinecraftForge.EVENT_BUS.addListener(this::rightClick);
        MinecraftForge.EVENT_BUS.addListener(this::changeDims);
        MinecraftForge.EVENT_BUS.addListener(this::playerTick);
    }

    public static void onStatAwarded(Player player, ResourceLocation pStat, int pIncrement) {
        if (player instanceof ServerPlayer) {
            if (pStat == Stats.WALK_ONE_CM || pStat == Stats.SPRINT_ONE_CM) {
                double leavesLogChance = PS1TweaksConfig.SERVER.leaves_and_logs_chance.get() * pIncrement;
                if (player.getRandom().nextDouble() < leavesLogChance) {
                    BlockPos pos = player.blockPosition();
                    int yMax = player.level.getHeight(Heightmap.Types.MOTION_BLOCKING,pos.getX(),pos.getZ());
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
                                for (int i = 9; i < 36;i++) {
                                    ItemStack stack = items.get(i);
                                    if (stack.isEmpty()) {
                                        items.set(i,itemLike.asItem().getDefaultInstance());
                                        break;
                                    } else if (itemLike.asItem() ==stack.getItem() && stack.getCount() < stack.getMaxStackSize()) {
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
        if (PS1TweaksConfig.SERVER.fallingAnimal.get() && event.phase == TickEvent.Phase.START && event.side == LogicalSide.SERVER) {
            ServerPlayer player = (ServerPlayer) event.player;
            if (player.level.dimension() == Level.OVERWORLD) {
                long time = player.level.getGameTime();
                if (time % PS1TweaksConfig.SERVER.minFallingTime.get() == 0 && player.getRandom().nextDouble() < PS1TweaksConfig.SERVER.fallingAnimalChance.get()) {
                    EntityType<?> type = Registry.ENTITY_TYPE.get(new ResourceLocation(Util.getRandom(PS1TweaksConfig.SERVER.fallingAnimalTypes.get(), player.getRandom())));
                    Vec3 vec3 = player.position().add(player.getLookAngle().scale(6));
                    BlockPos top = new BlockPos(vec3);
                    BlockPos dropLocation = player.level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,top);
                    int h = 20;
                    BlockPos spawnPos = dropLocation.above(h);

                    for (int i = h+dropLocation.getY(); i > player.level.getMinBuildHeight();i--) {
                        BlockPos pos = dropLocation.above(i);
                        BlockState state = player.level.getBlockState(pos);

                        FluidState fluidState = player.level.getFluidState(pos);
                        if (fluidState.is(FluidTags.WATER)) {
                            return;
                        } else if (!state.getCollisionShape(player.level,pos).isEmpty()) {
                            break;
                        }
                    }
                    Entity spawn = type.spawn(player.getLevel(), null, null, spawnPos, MobSpawnType.EVENT, false, false);
                    if (spawn instanceof Mob mob) {
                        mob.setHealth(Float.MIN_VALUE);
                    }
                }
            }
        }
    }

    void changeDims(PlayerEvent.PlayerChangedDimensionEvent event) {
        ResourceKey<Level> eventTo = event.getTo();
        ServerPlayer player = (ServerPlayer) event.getPlayer();
        ForgePacketHandler.sendToClient(new S2CTargetDimensionPacket(eventTo),player);
    }

    void rightClick(PlayerInteractEvent.EntityInteract event) {
    }

    void registerBlocks(RegistryEvent.Register<Block> event) {
    }

    void registerBlockEntities(RegistryEvent.Register<BlockEntityType<?>> event) {

    }

    void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(
                Init.ModItems.BARNACLE_TOOTH.setRegistryName("barnacle_tooth"),
                Init.ModItems.PRISMARINE_ROD.setRegistryName("prismarine_rod")
        );
    }
    void registerEntities(RegistryEvent.Register<EntityType<?>> event) {
        event.getRegistry().registerAll(Init.ModEntityTypes.BARNACLE.setRegistryName("barnacle"));
    }
    void registerSounds(RegistryEvent.Register<SoundEvent> event) {
        event.getRegistry().registerAll(  BARNACLE_AMBIENT.setRegistryName("barnacle_ambient"),
                BARNACLE_HURT.setRegistryName("barnacle_hurt"),BARNACLE_DEATH.setRegistryName("barnacle_death"),BARNACLE_FLOP.setRegistryName("barnacle_flop"));
    }

    void sleepCheck(SleepingTimeCheckEvent event) {
        if (event.getResult() == Event.Result.DENY) return;

        Player player = event.getPlayer();
        Level level = player.level;
        if (level.dimensionType().hasFixedTime()) return;

        int skyDarken = getSkyDarken(level);

        if (skyDarken <4) {
            event.setResult(Event.Result.DENY);
        }

    }

    void onAttributeCreate(EntityAttributeCreationEvent event) {
        event.put(Init.ModEntityTypes.BARNACLE, Barnacle.setCustomAttributes().build());;
    }

    public int getSkyDarken(Level level) {
        double d0 = 1.0D - (level.getRainLevel(1.0F) * 5.0F) / 16.0D;
        double d2 = 0.5D + 2.0D * Mth.clamp(Mth.cos(level.getTimeOfDay(1.0F) * ((float)Math.PI * 2F)), -0.25D, 0.25D);
        return (int)((1.0D - d2 * d0) * 11.0D);
    }

    private void setup(final FMLCommonSetupEvent event) {

        PacketHandler.registerPackets();
        event.enqueueWork(() -> {

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
        });
    }

    public static void skipGlassRendering(BlockAndTintGetter pLevel, BlockPos pPos, FluidState pFluidState, BlockState pBlockState, Direction pSide, FluidState pNeighborFluid, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) return;
        BlockState otherState =pLevel.getBlockState(pPos.relative(pSide));
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
            PoiAccess.getTYPE_BY_STATE().put(state,poiType);
        }
        PoiType.ALL_STATES = new ObjectOpenHashSet<>(PoiAccess.getTYPE_BY_STATE().keySet());
    }

    public static void setDestroySpeed(Block block, float v) {
        ImmutableList<BlockState> possibleStates = block.getStateDefinition().getPossibleStates();
        possibleStates.forEach(state -> ((BlockStateAccess)state).setDestroySpeed(v));
    }

    public static void setRequiresCorrectToolForDrops(Block block, boolean v) {
        ImmutableList<BlockState> possibleStates = block.getStateDefinition().getPossibleStates();
        possibleStates.forEach(state -> ((BlockStateAccess)state).setRequiresCorrectToolForDrops(v));
    }

    public static void setLootTable(Block block,ResourceLocation lootTable) {
        BlockAccess blockAccess = (BlockAccess) block;
        blockAccess.setDrops(lootTable);
        blockAccess.setLootTableSupplier(() -> lootTable);
    }

    public static void setDefaultLootTable(Block block) {
        ResourceLocation r = new ResourceLocation(block.getRegistryName().getNamespace(), "blocks/" + block.getRegistryName().getPath());
        setLootTable(block,r);
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID,path);
    }

}
