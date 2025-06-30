package tfar.ps1packtweaks.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraft.client.gui.screens.ProgressScreen;
import net.minecraft.client.gui.screens.ReceivingLevelScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.client.event.ScreenshotEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.commons.io.IOUtils;
import tfar.ps1packtweaks.ChatSettings;
import tfar.ps1packtweaks.Init;
import tfar.ps1packtweaks.PS1PackTweaks;
import tfar.ps1packtweaks.PS1TweaksConfig;
import tfar.ps1packtweaks.compat.BetterGuiCompassHUD;
import tfar.ps1packtweaks.compat.ModIntegration;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

public class PS1PackTweaksClient {

    static final File file = FMLPaths.GAMEDIR.get().resolve("last_seen_disc.json").toFile();



    public static Map<String,ResourceKey<Level>> map;

    public static ResourceKey<Level> DISC = Level.OVERWORLD;
    public static boolean showDisc;

    public static boolean isAutoScreenshot;

    public static void init(IEventBus bus) {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT,PS1TweaksConfig.CLIENT_SPEC);
        bus.addListener(PS1PackTweaksClient::setup);
        MinecraftForge.EVENT_BUS.addListener(PS1PackTweaksClient::joinServer);
        MinecraftForge.EVENT_BUS.addListener(MouseHider::startupScreen);
        MinecraftForge.EVENT_BUS.addListener(MouseHider::clientTick);
        MinecraftForge.EVENT_BUS.addListener(PS1PackTweaksClient::replaceBackground);
        MinecraftForge.EVENT_BUS.addListener(PS1PackTweaksClient::logout);
        MinecraftForge.EVENT_BUS.addListener(PS1PackTweaksClient::clientTick);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.LOW,PS1PackTweaksClient::message);
    }

    static void clientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            Minecraft minecraft = Minecraft.getInstance();
            if (!minecraft.isPaused() && PS1TweaksConfig.CLIENT.take_random_screenshots.get()) {
                Level level = minecraft.level;
                if (level != null && level.getGameTime() % PS1TweaksConfig.CLIENT.screenshot_interval.get() == 0) {
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
            Minecraft.getInstance().gui.setTitle(new TextComponent(PS1TweaksConfig.CLIENT.screenshot_message.get()));
            //event.setResultMessage(new TextComponent(PS1TweaksConfig.CLIENT.screenshot_message.get()));
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
        if (ModIntegration.guicompass.loaded) {
            BetterGuiCompassHUD.setup();
        }
        MinecraftForge.EVENT_BUS.addListener(PS1PackTweaksClient::playSoundEvent);
    }

    static void joinServer(ClientPlayerNetworkEvent.LoggedInEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.hasSingleplayerServer()) {
            ChatSettings chatSettings = PS1TweaksConfig.CLIENT.singleplayer_chat_settings.get();
            minecraft.options.chatVisibility = chatSettings.chatVisiblity();
        } else {
            ChatSettings chatSettings = PS1TweaksConfig.CLIENT.multiplayer_chat_settings.get();
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

        ResourceKey<Level> lastSeen = map.get(levelName);

        if (lastSeen == null) {
            showDisc = true;
        } else {
            if (lastSeen != DISC) {
                DISC = lastSeen;
                showDisc = true;
            }
        }
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
}
