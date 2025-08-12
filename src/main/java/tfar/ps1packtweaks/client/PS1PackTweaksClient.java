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
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.client.event.ScreenshotEvent;
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
import tfar.ps1packtweaks.compat.BetterGuiCompassHUD;
import tfar.ps1packtweaks.compat.ModIntegration;
import tyrannotitanlib.core.content.init.TyrannoBanners;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

public class PS1PackTweaksClient {

    // Key mapping is lazily initialized so it doesn't exist until it is registered
    public static final Lazy<KeyMapping> COPY_CLASS_NAME = Lazy.of(() -> new KeyMapping(
            "key.inventorypause.copyClassName", // Localisation
            InputConstants.Type.KEYSYM, // Default mapping is on the keyboard
            GLFW.GLFW_KEY_UNKNOWN, // No default mapping
            "key.categories.inventorypause.main" // Category localisation
    ));

    static final File file = FMLPaths.GAMEDIR.get().resolve("last_seen_disc.json").toFile();

    public static final ResourceLocation HEROBRINE_SKIN = PS1PackTweaks.id("textures/entity/herobrine.png");

    public static Map<String,ResourceKey<Level>> map;

    public static ResourceKey<Level> DISC = Level.OVERWORLD;
    public static boolean showDisc;

    public static boolean isAutoScreenshot;

    public static final IIngameOverlay banner_overlay = (gui, poseStack, partialTick, width, height) -> {
        try {
            MultiBufferSource.BufferSource multibuffersource$buffersource = Minecraft.getInstance().renderBuffers().bufferSource();
            ModelPart flag = Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.BANNER).getChild("flag");
            renderPattern(poseStack, multibuffersource$buffersource, 0, 0, flag, ModelBakery.BANNER_BASE, true, false);
        } catch (Error e) {
            Blaze3D.youJustLostTheGame();
        }
    };


    public static void renderPattern(PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay, ModelPart pFlagPart, Material pFlagMaterial, boolean pBanner, boolean pGlint) {
        pFlagPart.render(pPoseStack, pFlagMaterial.buffer(pBufferSource, RenderType::entitySolid, pGlint), pPackedLight, pPackedOverlay);

        DyeColor color = DyeColor.BLACK;
            float[] afloat = color.getTextureDiffuseColors();
            BannerPattern bannerpattern =TyrannoBanners.TYRANNOTITAN;
            Material material = pBanner ? Sheets.getBannerMaterial(bannerpattern) : Sheets.getShieldMaterial(bannerpattern);
            pFlagPart.render(pPoseStack, material.buffer(pBufferSource, RenderType::entityNoOutline), pPackedLight, pPackedOverlay, afloat[0], afloat[1], afloat[2], 1.0F);
    }

    //lowest
    public static void onOpenGUI(ScreenEvent.DrawScreenEvent.InitScreenEvent.Pre event) {
        if (PS1PackTweaksConfig.CLIENT.inventorypause_debug.get()) {
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
        if (PS1PackTweaksConfig.CLIENT.inventorypause_debug.get()) {
            int line = 0;
            for (Class<?> cl = screen.getClass(); cl.getSuperclass() != null && line < maxDepth; cl = cl.getSuperclass()) {
                Minecraft.getInstance().font.drawShadow(new PoseStack(), cl.getName(), x, y + 10 * line, 0xffffffff);
                line++;
            }
        }
    }

    public static void init(IEventBus bus) {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, PS1PackTweaksConfig.CLIENT_SPEC);
        bus.addListener(PS1PackTweaksClient::setup);
        MinecraftForge.EVENT_BUS.addListener(PS1PackTweaksClient::joinServer);
        MinecraftForge.EVENT_BUS.addListener(MouseHider::startupScreen);
        MinecraftForge.EVENT_BUS.addListener(MouseHider::clientTick);
        MinecraftForge.EVENT_BUS.addListener(PS1PackTweaksClient::replaceBackground);
        MinecraftForge.EVENT_BUS.addListener(PS1PackTweaksClient::logout);
        MinecraftForge.EVENT_BUS.addListener(PS1PackTweaksClient::clientTick);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.LOW,PS1PackTweaksClient::message);

        MinecraftForge.EVENT_BUS.addListener(PS1PackTweaksClient::onOpenGUI);
        MinecraftForge.EVENT_BUS.addListener(PS1PackTweaksClient::onGUIDrawPost);
        MinecraftForge.EVENT_BUS.addListener(PS1PackTweaksClient::loadWorld);
    }

    static void clientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            Minecraft minecraft = Minecraft.getInstance();
            if (!minecraft.isPaused() && PS1PackTweaksConfig.CLIENT.take_random_screenshots.get()) {
                Level level = minecraft.level;
                if (level != null && level.getGameTime() % PS1PackTweaksConfig.CLIENT.screenshot_interval.get() == 0) {
                    isAutoScreenshot = true;
                    Screenshot.grab(minecraft.gameDirectory, minecraft.getMainRenderTarget(), component -> {
                        PS1PackTweaks.LOGGER.info("Took automatic screenshot: {}",component);
                    });
                    isAutoScreenshot = false;
                }
            }
        }
    }

    static void message(ScreenshotEvent event) {
        if (!isAutoScreenshot) {
            Minecraft.getInstance().player.displayClientMessage(new TextComponent(PS1PackTweaksConfig.CLIENT.screenshot_message.get()),true);
            //event.setResultMessage(new TextComponent(PS1PackTweaksConfig.CLIENT.screenshot_message.get()));
        }
    }

    static void logout(ClientPlayerNetworkEvent.LoggedOutEvent event) {

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
            String s = DISC.location().getPath();
            RenderSystem.setShaderTexture(0, PS1PackTweaks.id("textures/gui/background/"+s+".png"));
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
        EntityRenderers.register(Init.ModEntityTypes.BARNACLE,BarnacleRenderer::new);
        EntityRenderers.register(Init.ModEntityTypes.SCRIPTED_MIDNIGHT_LURKER,ScriptedMidnightLurkerRenderer::new);

        EntityRenderers.register(Init.ModEntityTypes.HEROBRINE, (EntityRendererProvider.Context context) -> new SimplePlayerRenderer<>(context,
                false,PS1PackTweaks.id("textures/entity/herobrine.png")));

        if (ModIntegration.guicompass.loaded) {
            BetterGuiCompassHUD.setup();
        }
        MinecraftForge.EVENT_BUS.addListener(PS1PackTweaksClient::playSoundEvent);
        if (PS1PackTweaks.TRIGGER_BANNER_CRASH) {
            OverlayRegistry.registerOverlayTop("banner_crash",banner_overlay);
        }

        ClientRegistry.registerKeyBinding(COPY_CLASS_NAME.get());
    }

    static void joinServer(ClientPlayerNetworkEvent.LoggedInEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.hasSingleplayerServer()) {
            ChatSettings chatSettings = PS1PackTweaksConfig.CLIENT.singleplayer_chat_settings.get();
            minecraft.options.chatVisibility = chatSettings.chatVisiblity();
        } else {
            ChatSettings chatSettings = PS1PackTweaksConfig.CLIENT.multiplayer_chat_settings.get();
            minecraft.options.chatVisibility = chatSettings.chatVisiblity();
        }
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

    public static void changeDisc(String levelName) {


    }

    static void loadWorld(WorldEvent.Load event) {

        if (!(event.getWorld() instanceof ClientLevel clientLevel)) return;

        //this is first load, always show custom screen and load in map
        if (map == null) {
            map = new HashMap<>();
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
                        map.put(key, ResourceKey.create(Registry.DIMENSION_REGISTRY, value));
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

        ResourceKey<Level> lastSeen = map.get(levelName);

        if (lastSeen == null) {
            showDisc = true;
        } else {
            if (lastSeen != DISC) {
                DISC = lastSeen;
                showDisc = true;
            }
        }

        PS1PackTweaksClient.map.put(levelName,clientLevel.dimension());
        PS1PackTweaksClient.write();
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
            for (Map.Entry<String,ResourceKey<Level>> entry : map.entrySet()) {
                jsonObject.addProperty(entry.getKey(),entry.getValue().location().toString());
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


    public static void handle(ResourceKey<Level> key) {
        if (DISC != key) {
        DISC = key;
        showDisc = true;
        }
    }

    public static void onPerspectiveChange(CameraType cameraType, CameraType pPointOfView) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player!=null) {
            if (cameraType.isFirstPerson() && !pPointOfView.isFirstPerson()) {
                if (PS1PackTweaksConfig.CLIENT.herobrine_skin_chance.get() > player.getRandom().nextDouble()) {
                    ((AbstractClientPlayerDuck)player).setHerobrine(true);
                }
            }else if(!cameraType.isFirstPerson() && pPointOfView.isFirstPerson()) {
                ((AbstractClientPlayerDuck)player).setHerobrine(false);
            }
        }
    }

    public static boolean isPauseScreen(Screen caller) {

        for (String s : PS1PackTweaksConfig.CLIENT.inventorypause_screens.get()) {
            if(caller.getClass().getName().equals(s)) {
                return true;
            }
        }
        return false;
    }
}
