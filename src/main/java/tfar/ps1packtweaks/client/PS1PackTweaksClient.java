package tfar.ps1packtweaks.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.mojang.blaze3d.Blaze3D;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.client.event.*;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.registries.IRegistryDelegate;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import tfar.ps1packtweaks.AbstractClientPlayerDuck;
import net.minecraft.Util;
import net.minecraft.client.CameraType;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraft.client.gui.screens.ProgressScreen;
import net.minecraft.client.gui.screens.ReceivingLevelScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.client.gui.IIngameOverlay;
import net.minecraftforge.client.gui.OverlayRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.commons.io.IOUtils;
import org.lwjgl.glfw.GLFW;
import tfar.ps1packtweaks.ChatSettings;
import tfar.ps1packtweaks.Init;
import tfar.ps1packtweaks.PS1PackTweaks;
import tfar.ps1packtweaks.PS1PackTweaksConfig;
import tfar.ps1packtweaks.block.CustomWoodTypes;
import tfar.ps1packtweaks.compat.BetterGuiCompassHUD;
import tfar.ps1packtweaks.compat.ModIntegration;
import tfar.ps1packtweaks.mixin.BlockColorsAccess;
import tyrannotitanlib.core.content.init.TyrannoBanners;
import vazkii.quark.base.item.QuarkMusicDiscItem;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.util.*;

public class PS1PackTweaksClient {

    // Key mapping is lazily initialized so it doesn't exist until it is registered
    public static final Lazy<KeyMapping> COPY_CLASS_NAME = Lazy.of(() -> new KeyMapping(
            "key.inventorypause.copyClassName", // Localisation
            InputConstants.Type.KEYSYM, // Default mapping is on the keyboard
            GLFW.GLFW_KEY_UNKNOWN, // No default mapping
            "key.categories.inventorypause.main" // Category localisation
    ));
    public static final PS1PackTweaksConfig.Client CLIENT;
    public static final ForgeConfigSpec CLIENT_SPEC;

    static {
        final Pair<PS1PackTweaksConfig.Client, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder()
                .configure(PS1PackTweaksConfig.Client::new);
        CLIENT_SPEC = specPair.getRight();
        CLIENT = specPair.getLeft();
    }

    static final File file = FMLPaths.GAMEDIR.get().resolve("last_seen_disc.json").toFile();

    public static final ResourceLocation HEROBRINE_SKIN = PS1PackTweaks.id("textures/entity/herobrine.png");

    public static Map<String, ResourceKey<Level>> discMap;

    private static ResourceKey<Level> CURRENT_DISC = Level.OVERWORLD;
    public static boolean showDisc;

    public static boolean isAutoScreenshot;

    public static int ticksSinceJoined;

    public static int DIRT_TIME;

    public static final IIngameOverlay banner_overlay = (gui, poseStack, partialTick, width, height) -> {
        try {
            MultiBufferSource.BufferSource multibuffersource$buffersource = Minecraft.getInstance().renderBuffers().bufferSource();
            ModelPart flag = Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.BANNER).getChild("flag");
            renderPattern(poseStack, multibuffersource$buffersource, 0, 0, flag, ModelBakery.BANNER_BASE, true, false);
        } catch (Error e) {
            Blaze3D.youJustLostTheGame();
        }
    };


    public static final ResourceLocation JUMP_SCARE = PS1PackTweaks.id("textures/screen.png");

    public static final IIngameOverlay jump_scare = (gui, poseStack, partialTick, width, height) -> {
        if (PS1PackTweaksClient.jumpscareTimer > 0) {
            gui.renderTextureOverlay(JUMP_SCARE, 1);
        }
    };

    public static int jumpscareTimer;

    public static void renderPattern(PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay, ModelPart pFlagPart, Material pFlagMaterial, boolean pBanner, boolean pGlint) {
        pFlagPart.render(pPoseStack, pFlagMaterial.buffer(pBufferSource, RenderType::entitySolid, pGlint), pPackedLight, pPackedOverlay);

        DyeColor color = DyeColor.BLACK;
        float[] afloat = color.getTextureDiffuseColors();
        BannerPattern bannerpattern = TyrannoBanners.TYRANNOTITAN;
        Material material = pBanner ? Sheets.getBannerMaterial(bannerpattern) : Sheets.getShieldMaterial(bannerpattern);
        pFlagPart.render(pPoseStack, material.buffer(pBufferSource, RenderType::entityNoOutline), pPackedLight, pPackedOverlay, afloat[0], afloat[1], afloat[2], 1.0F);
    }

    public static void onKeyInput(InputEvent.KeyInputEvent event) {
        if (PS1PackTweaksKeybinds.TURN_AROUND.matches(event.getKey(), event.getScanCode()) &&
                PS1PackTweaksKeybinds.TURN_AROUND.getKeyConflictContext().isActive() &&
                Minecraft.getInstance().player != null && event.getAction() == GLFW.GLFW_PRESS) {
            Minecraft.getInstance().player.turn(3600.0, 0.0);
        }
    }


    //lowest
    public static void onOpenGUI(ScreenEvent.DrawScreenEvent.InitScreenEvent.Pre event) {
        if (CLIENT.inventorypause_debug.get()) {
            PS1PackTweaks.LOGGER.info(event.getScreen().getClass().getName());
        }
    }

    public static float x = 4f;
    public static float y = 4f;
    public static int maxDepth = 3;

    //lowest
    public static void onGUIDrawPost(ScreenEvent.DrawScreenEvent.Post event) {
        Screen screen = event.getScreen();
        while (PS1PackTweaksClient.COPY_CLASS_NAME.get().consumeClick()) {
            var name = screen.getClass().getName();
            Minecraft.getInstance().keyboardHandler.setClipboard(name);
            Minecraft.getInstance().player.sendMessage(new TranslatableComponent("chat.inventorypause.copyClassName.action", name), Util.NIL_UUID);
        }
        if (CLIENT.inventorypause_debug.get()) {
            int line = 0;
            for (Class<?> cl = screen.getClass(); cl.getSuperclass() != null && line < maxDepth; cl = cl.getSuperclass()) {
                Minecraft.getInstance().font.drawShadow(new PoseStack(), cl.getName(), x, y + 10 * line, 0xffffffff);
                line++;
            }
        }
    }


    public static BlockState replaceBlockRender(BlockState original) {
        if (ticksSinceJoined < DIRT_TIME) {
            if (original.isAir()) return original;
            if (original.is(Blocks.LAVA)) return original;
            if (original.is(Blocks.WATER)) {
                return Blocks.LAVA.defaultBlockState().setValue(LiquidBlock.LEVEL, original.getValue(LiquidBlock.LEVEL));
            }
            return Blocks.DIRT.defaultBlockState();
        }
        return original;
    }

    public static FluidState replaceFluidRender(FluidState original) {
        if (ticksSinceJoined < DIRT_TIME) {
            if (original.getType() == Fluids.FLOWING_WATER) {
                original = Fluids.FLOWING_LAVA.defaultFluidState().setValue(FlowingFluid.LEVEL, original.getValue(FlowingFluid.LEVEL))
                        .setValue(FlowingFluid.FALLING, original.getValue(FlowingFluid.FALLING));
            }
        }
        return original;
    }

    public static void init(IEventBus bus) {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, CLIENT_SPEC);
        bus.addListener(PS1PackTweaksClient::setup);
        bus.addListener(EventPriority.LOWEST, PS1PackTweaksClient::removeBlockColors);
        bus.addListener(PS1PackTweaksClient::particleProviders);
        MinecraftForge.EVENT_BUS.addListener(PS1PackTweaksClient::joinServer);
        MinecraftForge.EVENT_BUS.addListener(MouseHider::startupScreen);
        MinecraftForge.EVENT_BUS.addListener(PS1PackTweaksClient::replaceBackground);
        MinecraftForge.EVENT_BUS.addListener(PS1PackTweaksClient::logout);
        MinecraftForge.EVENT_BUS.addListener(PS1PackTweaksClient::clientTick);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.LOW, PS1PackTweaksClient::message);

        MinecraftForge.EVENT_BUS.addListener(PS1PackTweaksClient::onOpenGUI);
        MinecraftForge.EVENT_BUS.addListener(PS1PackTweaksClient::onGUIDrawPost);
        MinecraftForge.EVENT_BUS.addListener(PS1PackTweaksClient::loadWorld);
        MinecraftForge.EVENT_BUS.addListener(PS1PackTweaksClient::onKeyInput);
    }

    //So water, foliage, grass, and leaves

    static void removeBlockColors(ColorHandlerEvent.Block event) {
        BlockColors blockColors = event.getBlockColors();
        Map<IRegistryDelegate<Block>, BlockColor> map = ((BlockColorsAccess) blockColors).getBlockColors();
        map.entrySet().removeIf(iRegistryDelegateBlockColorEntry -> {
            Block block = iRegistryDelegateBlockColorEntry.getKey().get();
            boolean b = block instanceof LeavesBlock || block instanceof LiquidBlock || block instanceof GrassBlock ||
                    block instanceof BushBlock || block instanceof VineBlock;
            if (b && PS1PackTweaks.DEV) {
                PS1PackTweaks.LOGGER.info("Removed color from: {}", block);
            }
            return b;
        });
    }

    static void particleProviders(ParticleFactoryRegisterEvent e) {
        Minecraft.getInstance().particleEngine.register(Init.ModParticleTypes.ENDERMAN, EndermanParticle.Provider::new);
    }


    //this runs in the main menu!
    static void clientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            if (MouseHider.hidden) {
                if (MouseHider.hideTimer > 0) {
                    MouseHider.hideTimer--;
                    if (MouseHider.hideTimer == 0) {
                        MouseHider.unhide();
                    }
                }
            }

            for (Iterator<MutablePair<Runnable, Integer>> iterator = scheduledTasks.iterator(); iterator.hasNext(); ) {
                MutablePair<Runnable, Integer> task = iterator.next();
                int tick = task.getRight();
                if (tick == 0) {
                    task.getLeft().run();
                    iterator.remove();
                } else {
                    tick--;
                    task.setRight(tick);
                }
            }

        } else {
            Minecraft minecraft = Minecraft.getInstance();
            if (shaderTimer > 0) {
                shaderTimer--;
                if (shaderTimer == 0) {
                    minecraft.gameRenderer.shutdownEffect();
                }
            }
            if (!minecraft.isPaused() && CLIENT.take_random_screenshots.get()) {
                Level level = minecraft.level;
                if (level != null && level.getGameTime() % CLIENT.screenshot_interval.get() == 0) {
                    isAutoScreenshot = true;
                    Screenshot.grab(minecraft.gameDirectory, minecraft.getMainRenderTarget(), component -> {
                        PS1PackTweaks.LOGGER.info("Took automatic screenshot: {}", component);
                    });
                    isAutoScreenshot = false;
                }
            }
            LocalPlayer player = minecraft.player;
            if (player != null) {
                ticksSinceJoined++;
                if (ticksSinceJoined == DIRT_TIME) {
                    minecraft.levelRenderer.allChanged();
                }
                Random random = player.getRandom();
                if (player.tickCount % 20 == 0) {
                    if (random.nextDouble() < CLIENT.pauseChance.get()) {
                        Minecraft.getInstance().pauseGame(false);
                    }
                    if (!minecraft.isPaused() && jumpscareTimer <= 0 && random.nextDouble() < CLIENT.jumpScareChance.get()) {
                        jumpscareTimer = 30;
                        minecraft.getSoundManager().play(SimpleSoundInstance.forUI(Init.ModSounds.SCREEN, 1, 1));
                    }
                }

                if (jumpscareTimer > 0) {
                    jumpscareTimer--;
                }
            }
        }
    }

    static void message(ScreenshotEvent event) {
        if (!isAutoScreenshot) {
            if (Minecraft.getInstance().player != null) {
                Minecraft.getInstance().player.displayClientMessage(new TextComponent(CLIENT.screenshot_message.get()), true);
                //event.setResultMessage(new TextComponent(PS1PackTweaksConfig.CLIENT.screenshot_message.get()));
            }
        }
    }

    static void logout(ClientPlayerNetworkEvent.LoggedOutEvent event) {
        ticksSinceJoined = 0;
    }

    public static void replaceBackground(ScreenEvent.BackgroundDrawnEvent event) {
        Screen screen = event.getScreen();
        if ((screen instanceof ProgressScreen || screen instanceof ReceivingLevelScreen) && showDisc) {
            PoseStack stack = event.getPoseStack();

            int screenHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();
            int screenWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();

            RenderSystem.disableDepthTest();
            RenderSystem.depthMask(false);
            RenderSystem.defaultBlendFunc();
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1);
            String s = CURRENT_DISC.location().getPath();
            RenderSystem.setShaderTexture(0, PS1PackTweaks.id("textures/gui/background/" + s + ".png"));
            Tesselator tesselator = Tesselator.getInstance();
            BufferBuilder bufferbuilder = tesselator.getBuilder();
            bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
            bufferbuilder.vertex(0.0D, screenHeight, -90.0D).uv(0.0F, 1.0F).endVertex();
            bufferbuilder.vertex(screenWidth, screenHeight, -90.0D).uv(1.0F, 1.0F).endVertex();
            bufferbuilder.vertex(screenWidth, 0.0D, -90.0D).uv(1.0F, 0.0F).endVertex();
            bufferbuilder.vertex(0.0D, 0.0D, -90.0D).uv(0.0F, 0.0F).endVertex();
            tesselator.end();
            RenderSystem.depthMask(true);
            RenderSystem.enableDepthTest();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);


        }
    }

    //LevelLoadingScreen -> ProgressScreen -> ReceivingLevelScreen

    static void setup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ClientRegistry.registerKeyBinding(PS1PackTweaksKeybinds.TURN_AROUND);
            CustomWoodTypes.LIST.forEach(Sheets::addWoodType);
            ItemBlockRenderTypes.setRenderLayer(Init.ModBlocks.ENDERSHROOM, RenderType.cutoutMipped());
            ItemBlockRenderTypes.setRenderLayer(Blocks.ICE,RenderType.solid());
            ItemBlockRenderTypes.setRenderLayer(Blocks.FROSTED_ICE,RenderType.solid());
            ItemBlockRenderTypes.setRenderLayer(Blocks.SLIME_BLOCK,RenderType.solid());
            ItemBlockRenderTypes.setRenderLayer(Blocks.HONEY_BLOCK,RenderType.solid());
            EntityRenderers.register(Init.ModEntityTypes.BARNACLE, BarnacleRenderer::new);
            EntityRenderers.register(Init.ModEntityTypes.SCRIPTED_MIDNIGHT_LURKER, ScriptedMidnightLurkerRenderer::new);

            EntityRenderers.register(Init.ModEntityTypes.HEROBRINE, (EntityRendererProvider.Context context) -> new SimplePlayerRenderer<>(context,
                    false, PS1PackTweaks.id("textures/entity/herobrine.png")));

            EntityRenderers.register(Init.ModEntityTypes.INVISIBLE_ENTITY, (EntityRendererProvider.Context context) -> new InvisibleEntityRenderer<>(context,
                    false, PS1PackTweaks.id("textures/entity/herobrine.png")));

            if (ModIntegration.guicompass.loaded) {
                BetterGuiCompassHUD.setup();
            }
            MinecraftForge.EVENT_BUS.addListener(PS1PackTweaksClient::playSoundEvent);
            if (PS1PackTweaks.TRIGGER_BANNER_CRASH) {
                OverlayRegistry.registerOverlayTop("banner_crash", banner_overlay);
            }

            OverlayRegistry.registerOverlayTop("jump_scare", jump_scare);

            ClientRegistry.registerKeyBinding(COPY_CLASS_NAME.get());

            BiomeColors.FOLIAGE_COLOR_RESOLVER = (biome, v, v1) -> 0xffffffff;
            BiomeColors.GRASS_COLOR_RESOLVER = (biome, v, v1) -> 0xffffffff;
            BiomeColors.WATER_COLOR_RESOLVER = (biome, v, v1) -> 0xffffffff;
        });
    }

    static void joinServer(ClientPlayerNetworkEvent.LoggedInEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.hasSingleplayerServer()) {
            ChatSettings chatSettings = CLIENT.singleplayer_chat_settings.get();
            minecraft.options.chatVisibility = chatSettings.chatVisiblity();
        } else {
            ChatSettings chatSettings = CLIENT.multiplayer_chat_settings.get();
            minecraft.options.chatVisibility = chatSettings.chatVisiblity();
        }
    }

    public static boolean shouldRemoveColor(Block[] blocks) {
        if (blocks.length == 0) return false;
        Block firstBlock = blocks[0];
        return firstBlock instanceof LeavesBlock || firstBlock instanceof LiquidBlock || firstBlock instanceof GrassBlock ||
                firstBlock instanceof BushBlock || firstBlock instanceof VineBlock || firstBlock instanceof WaterlilyBlock;
    }

    static void playSoundEvent(PlaySoundEvent event) {
        SoundInstance sound = event.getOriginalSound();
        String name = event.getName();
        if (name.equals("music.creative")) {
            Minecraft minecraft = Minecraft.getInstance();
            LevelRenderer levelRenderer = minecraft.levelRenderer;
            if (!levelRenderer.playingRecords.isEmpty()) {
                event.setSound(null);
            }
        }
    }

    static Gson gson = new GsonBuilder().setPrettyPrinting().create();


    static void loadWorld(WorldEvent.Load event) {

    }

    static String getLevelName() {
        if (Minecraft.getInstance().hasSingleplayerServer()) {
            return Minecraft.getInstance().getSingleplayerServer().getWorldData().getLevelName();
            //   ResourceKey<Level> lastSeen = player.level.dimension();
        }
        return "Server";
    }

    public static void write() {
        Gson gson = new Gson();
        JsonWriter writer = null;
        try {
            writer = gson.newJsonWriter(new FileWriter(file));
            writer.setIndent("    ");


            JsonObject jsonObject = new JsonObject();
            for (Map.Entry<String, ResourceKey<Level>> entry : discMap.entrySet()) {
                jsonObject.addProperty(entry.getKey(), entry.getValue().location().toString());
            }

            gson.toJson(jsonObject, writer);
        } catch (Exception e) {
            PS1PackTweaks.LOGGER.error("Couldn't save config");
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(writer);
        }
    }


    public static void setDisc(ResourceKey<Level> key) {
        if (CURRENT_DISC != key) {
            CURRENT_DISC = key;
            showDisc = true;
        }
    }

    static int shaderTimer;

    public static void handleShader(ResourceLocation location, int ticks) {
        Minecraft.getInstance().gameRenderer.loadEffect(location);
        shaderTimer = ticks;
    }

    public static void onPerspectiveChange(CameraType cameraType, CameraType pPointOfView) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            if (cameraType.isFirstPerson() && !pPointOfView.isFirstPerson() && player.level.getGameRules().getBoolean(PS1PackTweaks.RULE_CREEPY_EVENTS)) {
                if (CLIENT.herobrine_skin_chance.get() > player.getRandom().nextDouble()) {
                    ((AbstractClientPlayerDuck) player).setHerobrine(true);
                }
            } else if (!cameraType.isFirstPerson() && pPointOfView.isFirstPerson()) {
                ((AbstractClientPlayerDuck) player).setHerobrine(false);
            }
        }
    }

    public static void onChangeLevel(ClientLevel clientLevel) {


        //this is first load, always show custom screen and load in map
        if (discMap == null) {
            discMap = new HashMap<>();
            if (file.exists()) {

                Reader reader = null;
                try {
                    reader = new FileReader(file);
                    JsonReader jsonReader = new JsonReader(reader);
                    // Type listType = new TypeToken<ArrayList<BTSIslandConfig>>(){}.getType();

                    //   PS1PackTweaks.LOGGER.info("Loading existing config");
                    JsonObject jsonObject = gson.fromJson(jsonReader, JsonObject.class);

                    for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                        String key = entry.getKey();
                        ResourceLocation value = new ResourceLocation(entry.getValue().getAsString());
                        discMap.put(key, ResourceKey.create(Registry.DIMENSION_REGISTRY, value));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                } finally {
                    IOUtils.closeQuietly(reader);
                }
            }
        }

        String levelName = getLevelName();

        ResourceKey<Level> lastSeen = discMap.get(levelName);

        if (lastSeen == null) {
            showDisc = true;
        } else {
            setDisc(clientLevel.dimension());
        }

        PS1PackTweaksClient.discMap.put(levelName, clientLevel.dimension());
        PS1PackTweaksClient.write();
    }

    public static boolean isPauseScreen(Screen caller) {

        for (String s : CLIENT.inventorypause_screens.get()) {
            if (caller.getClass().getName().equals(s)) {
                return true;
            }
        }
        return false;
    }

    private static final List<MutablePair<Runnable, Integer>> scheduledTasks = new ArrayList<>();

    public static void schedule(Runnable runnable, int ticks) {
        scheduledTasks.add(MutablePair.of(runnable, ticks));
    }


    public static void onJukeboxLoad(JukeboxBlockEntity tile) {
        Runnable runnable = () -> {
            Minecraft mc = Minecraft.getInstance();
            LevelRenderer render = mc.levelRenderer;
            BlockPos pos = tile.getBlockPos();

            ItemStack stack = tile.getRecord();
            Item var8 = stack.getItem();
            if (var8 instanceof RecordItem recordItem && !(var8 instanceof QuarkMusicDiscItem)) {
                render.playStreamingMusic(recordItem.getSound(), pos, recordItem);
                //render.levelEvent(mc.player,LevelEvent.SOUND_PLAY_RECORDING,pos,0);
            }
        };
        schedule(runnable, 40);
    }

    //this is here so the server doesn't crash
    public static List<? extends String> defaultClasses() {
        List<String> strings = new ArrayList<>();
        strings.add(CreativeModeInventoryScreen.class.getName());
        strings.add(InventoryScreen.class.getName());
        return strings;
    }
}
