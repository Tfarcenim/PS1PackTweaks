package tfar.ps1packtweaks;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.SleepingTimeCheckEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.slf4j.Logger;
import tfar.ps1packtweaks.client.PS1PackTweaksClient;
import tfar.ps1packtweaks.compat.EnderiteModCompat;
import tfar.ps1packtweaks.compat.ModIntegration;
import tfar.ps1packtweaks.compat.MoreHorseArmorCompat;
import tfar.ps1packtweaks.datagen.ModDataGenerator;
import tfar.ps1packtweaks.entity.Barnacle;
import tfar.ps1packtweaks.mixin.BlockAccess;
import tfar.ps1packtweaks.mixin.BlockStateAccess;

import java.util.Set;

import static tfar.ps1packtweaks.Init.ModSounds.*;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(PS1PackTweaks.MOD_ID)
public class PS1PackTweaks
{
    public static final String MOD_ID = "ps1packtweaks";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final Set<BlockState> ANVILS = ImmutableList.of(Blocks.ANVIL,Blocks.CHIPPED_ANVIL,Blocks.DAMAGED_ANVIL).stream()
            .flatMap((block) -> block.getStateDefinition().getPossibleStates().stream()).collect(ImmutableSet.toImmutableSet());

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
                Init.ModItems.PRISMARINE_ROD.setRegistryName("prismarine_rods")
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
        if (ModIntegration.morehorsearmor.loaded) {
            MoreHorseArmorCompat.setup();
        }
        if (ModIntegration.enderitemod.loaded) {
            EnderiteModCompat.setup();
        }
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
