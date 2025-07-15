package tfar.ps1packtweaks;

import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class PS1TweaksConfig {

    public static final Server SERVER;
    public static final ForgeConfigSpec SERVER_SPEC;

    public static final Client CLIENT;
    public static final ForgeConfigSpec CLIENT_SPEC;

    static {
        final Pair<Server, ForgeConfigSpec> specPair2 = new ForgeConfigSpec.Builder().configure(Server::new);
        SERVER_SPEC = specPair2.getRight();
        SERVER = specPair2.getLeft();

        final Pair<Client, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Client::new);
        CLIENT_SPEC = specPair.getRight();
        CLIENT = specPair.getLeft();
    }

    public static class Server {
        public final ForgeConfigSpec.DoubleValue barnacleHealth;
        public final ForgeConfigSpec.BooleanValue fallingAnimal;
        public final ForgeConfigSpec.DoubleValue fallingAnimalChance;
        public final ForgeConfigSpec.LongValue minFallingTime;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>>  fallingAnimalTypes;

        public final ForgeConfigSpec.DoubleValue leaves_and_logs_chance;

        public final ForgeConfigSpec.DoubleValue duplicateWorldChance;

        Server(ForgeConfigSpec.Builder builder) {
            builder.push("tweaks");
            builder.push("barnacle");
            barnacleHealth = builder.defineInRange("health",40, 1, 1023.);
            builder.pop();
            builder.push("events");
            fallingAnimal = builder.define("falling_animal",true);
            fallingAnimalChance = builder.defineInRange("falling_animal_chance",.5,0,1);
            minFallingTime = builder.defineInRange("falling_delay",100,1,100000000000000L);
            fallingAnimalTypes = builder.defineList("falling_animal_types",List.of("minecraft:cow"),
                    o -> o instanceof String s && Registry.ENTITY_TYPE.containsKey(new ResourceLocation(s)));
            leaves_and_logs_chance = builder.defineInRange("leaves_and_logs_chance",1/32768d,0,1);

            duplicateWorldChance = builder.defineInRange("duplicate_world_chance",.5,0,1);

            builder.pop();
            builder.pop();
        }
    }

    public static class Client {

        public final ConfigHelper.ConfigObject<ChatSettings> singleplayer_chat_settings;

        public final ConfigHelper.ConfigObject<ChatSettings> multiplayer_chat_settings;
        public final ForgeConfigSpec.IntValue hideTitleMouseTimer;

        public final ForgeConfigSpec.BooleanValue take_random_screenshots;

        public final ForgeConfigSpec.LongValue screenshot_interval;
        public final ForgeConfigSpec.BooleanValue stop_music_when_record_plays;

        public final ForgeConfigSpec.ConfigValue<? extends String> screenshot_message;

        public final ForgeConfigSpec.BooleanValue inventorypause_enabled;
        public final ForgeConfigSpec.BooleanValue inventorypause_disable_saving;
        public final ForgeConfigSpec.BooleanValue inventorypause_pause_sounds;
        public final ForgeConfigSpec.BooleanValue inventorypause_debug;

        public final ForgeConfigSpec.ConfigValue<List<? extends String>> inventorypause_screens;
        public final ForgeConfigSpec.DoubleValue herobrine_skin_chance;

        //

        Client(ForgeConfigSpec.Builder builder) {
            builder.push("tweaks");
            singleplayer_chat_settings = ConfigHelper.defineObject(builder.comment("FULL,SYSTEM,HIDDEN"),"singleplayer_chat_settings",ChatSettings.CODEC,ChatSettings.DEFAULT_SINGLEPLAYER);
            multiplayer_chat_settings = ConfigHelper.defineObject(builder.comment("FULL,SYSTEM,HIDDEN"),"multiplayer_chat_settings",ChatSettings.CODEC,ChatSettings.DEFAULT_MULTIPLAYER);
            hideTitleMouseTimer = builder.defineInRange("hide_title_mouse_timer",1000,1,1000000);

            take_random_screenshots = builder.define("take_random_screenshots",true);
            screenshot_interval = builder.defineInRange("screenshot_interval",1200 * 25,1,Long.MAX_VALUE);
            stop_music_when_record_plays = builder.define("stop_music_when_record_plays",true);

            screenshot_message = builder.define("screenshot_message","Screenshot Saved");

            builder.push("inventorypause");

            inventorypause_enabled = builder.define("enabled",true);
            inventorypause_disable_saving = builder.define("disable_saving",false);
            inventorypause_pause_sounds = builder.define("pause_sounds",false);
            inventorypause_debug = builder.define("debug",false);

            inventorypause_screens = builder.defineList("screens",defaultClasses(), o -> o instanceof String);
            herobrine_skin_chance = builder.defineInRange("herobrine_skin_chance",1/64d,0,1);

            builder.pop();

            builder.pop();
        }

        static List<? extends String> defaultClasses() {
            List<String> strings = new ArrayList<>();
            strings.add(CreativeModeInventoryScreen.class.getName());
            strings.add(InventoryScreen.class.getName());
            return strings;
        }
    }

}
