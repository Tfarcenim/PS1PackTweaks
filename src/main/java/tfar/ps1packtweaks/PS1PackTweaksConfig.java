package tfar.ps1packtweaks;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.apache.commons.lang3.tuple.Pair;
import tfar.ps1packtweaks.client.PS1PackTweaksClient;

import java.util.List;

public class PS1PackTweaksConfig {

    public static final Server SERVER;
    public static final ForgeConfigSpec SERVER_SPEC;

    static {
        final Pair<Server, ForgeConfigSpec> specPair2 = new ForgeConfigSpec.Builder().configure(Server::new);
        SERVER_SPEC = specPair2.getRight();
        SERVER = specPair2.getLeft();
    }

    public static class Server {
        public final ForgeConfigSpec.DoubleValue barnacleHealth;
        public final ForgeConfigSpec.DoubleValue fallingAnimalChance;
        public final ForgeConfigSpec.LongValue minFallingAnimalDelay;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>>  fallingAnimalTypes;

        public final ForgeConfigSpec.DoubleValue leaves_and_logs_chance;

        //public final ForgeConfigSpec.DoubleValue duplicateWorldChance;

        public final ForgeConfigSpec.DoubleValue herobrineChance;
        public final ForgeConfigSpec.LongValue minHerobrineDelay;
        public final ForgeConfigSpec.DoubleValue herobrineSpawnDistance;

        public final ForgeConfigSpec.IntValue herobrineEvent0Weight;
        public final ForgeConfigSpec.IntValue herobrineEvent1Weight;
        public final ForgeConfigSpec.IntValue herobrineEvent2Weight;
        public final ForgeConfigSpec.IntValue herobrineEvent3Weight;

        public final ForgeConfigSpec.DoubleValue disappearingLeavesChance;
        public final ForgeConfigSpec.IntValue disappearingLeavesDelay;
        public final ForgeConfigSpec.IntValue disappearingLeavesRadius;

        public final ForgeConfigSpec.DoubleValue disappearingLogsChance;
        public final ForgeConfigSpec.IntValue disappearingLogsDelay;

        public final ForgeConfigSpec.DoubleValue tunnelChance;
        public final ForgeConfigSpec.IntValue tunnelSizeMin;
        public final ForgeConfigSpec.IntValue tunnelDelay;
        public final ForgeConfigSpec.IntValue tunnelSizeMax;
        public final ForgeConfigSpec.DoubleValue tunnelTorchChance;
        public final ForgeConfigSpec.IntValue tunnelYMax;

        public final ForgeConfigSpec.IntValue randomSoundDelay;
        public final ForgeConfigSpec.DoubleValue randomItemsInChestChance;

        public final ForgeConfigSpec.DoubleValue look_up_burn_chance;
        public final ForgeConfigSpec.DoubleValue break_glass_looked_at_chance;
        public final ForgeConfigSpec.DoubleValue invisible_entity_chance;

        public final ForgeConfigSpec.DoubleValue mobsFollowPlayerChance;
        public final ForgeConfigSpec.IntValue mobsFollowPlayerDuration;

        public final ForgeConfigSpec.DoubleValue blackAndWhiteKillChance;
        public final ForgeConfigSpec.IntValue blackAndWhiteKillTime;

        public final ForgeConfigSpec.DoubleValue wakeupSurpriseChance;
        public final ForgeConfigSpec.DoubleValue courbetReplaceChance;
        public final ForgeConfigSpec.DoubleValue randomTotemUseChance;
        public final ForgeConfigSpec.DoubleValue randomArrowChance;

        public final ForgeConfigSpec.BooleanValue allowHerobrineShrine;

        //debug
        public final ForgeConfigSpec.BooleanValue listVillagerTrades;

        Server(ForgeConfigSpec.Builder builder) {
            builder.push("tweaks");
            builder.push("barnacle");
            barnacleHealth = builder.defineInRange("health",40, 1, 1023.);
            builder.pop();
            builder.push("events");

            fallingAnimalChance = builder.defineInRange("falling_animal_chance",.5,0,1);
            minFallingAnimalDelay = builder.defineInRange("min_falling_animal_delay",1000,1,100000000000000L);


            fallingAnimalTypes = builder.defineList("falling_animal_types",List.of("minecraft:cow"),
                    o -> o instanceof String s && Registry.ENTITY_TYPE.containsKey(new ResourceLocation(s)));
            leaves_and_logs_chance = builder.defineInRange("leaves_and_logs_chance",1/32768d,0,1);

            disappearingLeavesChance = builder.defineInRange("disappearing_leaves_chance",.5,0,1);
            disappearingLeavesDelay = builder.defineInRange("disappearing_leaves_delay",10000,1,1000000000);
            disappearingLeavesRadius = builder.defineInRange("disappearing_leaves_radius",64,1,512);

            disappearingLogsChance = builder.defineInRange("disappearing_logs_chance",.5,0,1);
            disappearingLogsDelay = builder.defineInRange("disappearing_logs_delay",10000,1,1000000000);

            tunnelChance = builder.defineInRange("tunnel_chance",1/4d,0,1);
            tunnelDelay = builder.defineInRange("tunnel_delay",50000,0,1000000000);
            tunnelSizeMin = builder.defineInRange("tunnel_size_min",16,1,200);
            tunnelSizeMax = builder.defineInRange("tunnel_size_max",32,1,200);
            tunnelTorchChance = builder.defineInRange("tunnel_torch_chance",.25,0,1);
            tunnelYMax = builder.comment("Max Y at which to rarely place extra tunnels").defineInRange("tunnel_y_max",16,-2048,2048);
            randomSoundDelay = builder.defineInRange("random_sound_delay",50000,1,1000000000);

            look_up_burn_chance = builder.comment("Chance per tick to set player on fire when looking up")
                    .defineInRange("look_up_burn_chance",1/1024d,0,1);

            break_glass_looked_at_chance = builder.comment("Chance per tick to break glass when looking at it")
                    .defineInRange("break_glass_looked_at_chance",1/65536d,0,1);

            invisible_entity_chance = builder.comment("Chance to spawn invisible entity near player")
                    .defineInRange("invisible_entity_chance",1/1024d,0,1);

            mobsFollowPlayerChance = builder.comment("Chance every tick of mobs following player")
                    .defineInRange("mobs_follow_player_chance",1/65536d,0,1);

            mobsFollowPlayerDuration = builder.comment("Length of time in ticks that mobs follow player")
                    .defineInRange("mobs_follow_player_duration",400,0,1000000000);

            blackAndWhiteKillChance = builder.comment("Chance to turn world black and white after killing a mob")
                    .defineInRange("black_and_white_kill_chance",1/256d,0,1);

            blackAndWhiteKillTime = builder.comment("Time before reverting black and white after kill")
                    .defineInRange("black_and_white_kill_time",20,1,1000000000);

            randomTotemUseChance = builder.comment("Chance to randomly use a totem every tick when held")
                    .defineInRange("random_totem_use_chance",1/65536d,0,1);

            randomArrowChance = builder.comment("Chance to randomly shoot an arrow at player every tick")
                    .defineInRange("random_arrow_chance",1/65536d,0,1);

            builder.push("herobrine");
            herobrineChance = builder.defineInRange("chance",.5,0,1);
            minHerobrineDelay = builder.defineInRange("min_delay",10000,1,100000000000000L);
            herobrineSpawnDistance = builder.defineInRange("spawn_distance",32,1,128d);


            herobrineEvent0Weight = builder.defineInRange("vanish_on_seen_weight",8,0,500000000);
            herobrineEvent1Weight = builder.defineInRange("run_on_seen_weight",4,0,500000000);
            herobrineEvent2Weight = builder.defineInRange("teleport_on_seen_weight",2,0,500000000);
            herobrineEvent3Weight = builder.defineInRange("lurker_weight",1,0,500000000);

            randomItemsInChestChance = builder.comment("Chance to put random items in chest when randomly ticked")
                    .defineInRange("random_items_in_chest",1/64d,0,1);

            wakeupSurpriseChance = builder.comment("Chance to put randomly spawn a skeleton or zombie after player wakes up")
                    .defineInRange("wakeup_surprise_chance",1/256d,0,1);

            courbetReplaceChance = builder.comment("Chance to replace courbet painting when looked at")
                    .defineInRange("courbet_replace_chance",1/256d,0,1);

            allowHerobrineShrine = builder.comment("Whether to allow the herobrine shrine to be activated, only has effect on dedicated servers")
                    .define("allow_herobrine_shrine",false);

            builder.pop();

            //duplicateWorldChance = builder.defineInRange("duplicate_world_chance",.5,0,1);



            builder.pop();
            builder.pop();
            builder.push("debug");
            listVillagerTrades = builder.comment("Log all active villager trades on server start").define("list_villager_trades",false);
            builder.pop();
        }
    }

    public static void configUpdate(ModConfigEvent event) {
        if (event.getConfig().getSpec() == SERVER_SPEC) {

        }
    }

    //Upon joining the world, all blocks are visually rendered as dirt and all water is rendered as lava for 2 seconds before returning to normal

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

        //public final ForgeConfigSpec.BooleanValue disableColorMaps;

        public final ForgeConfigSpec.DoubleValue minMusicPitch;
        public final ForgeConfigSpec.DoubleValue maxMusicPitch;
        public final ForgeConfigSpec.IntValue ticksMusicPitch;
        public final ForgeConfigSpec.DoubleValue randomPitchChance;

        public final ForgeConfigSpec.DoubleValue pauseChance;

        public final ForgeConfigSpec.DoubleValue replaceBlocksChance;
        public final ForgeConfigSpec.IntValue replaceBlocksTime;

        public final ForgeConfigSpec.DoubleValue jumpScareChance;
        public final ForgeConfigSpec.IntValue delaySongTime;

        public final ForgeConfigSpec.IntValue chatOffset;


        public Client(ForgeConfigSpec.Builder builder) {
            builder.push("tweaks");
            singleplayer_chat_settings = ConfigHelper.defineObject(builder.comment("FULL,SYSTEM,HIDDEN"),"singleplayer_chat_settings",
                    ChatSettings.CODEC,ChatSettings.DEFAULT_SINGLEPLAYER);
            multiplayer_chat_settings = ConfigHelper.defineObject(builder.comment("FULL,SYSTEM,HIDDEN"),"multiplayer_chat_settings",
                    ChatSettings.CODEC,ChatSettings.DEFAULT_MULTIPLAYER);
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

            inventorypause_screens = builder.defineList("screens", PS1PackTweaksClient.defaultClasses(), o -> o instanceof String);
            herobrine_skin_chance = builder.defineInRange("herobrine_skin_chance",1/64d,0,1);

           // disableColorMaps = builder.comment("Disables colormaps for leaves, grass and water").define("disable_color_maps",true);

            builder.pop();

            builder.push("random_music_pitch");

            minMusicPitch = builder.defineInRange("min",.5,0,64);
            maxMusicPitch = builder.defineInRange("max",1.5,0,64);
            ticksMusicPitch = builder.comment("How long the modified pitch will last").defineInRange("ticks",80,1,100000000);
            randomPitchChance = builder.comment("Chance of pitch changing every time music manager ticks").defineInRange("chance",.125d,0,1);

            builder.pop();

            pauseChance = builder.comment("Chance every tick of game pausing").defineInRange("pause_chance",1/1024d,0,1);
            replaceBlocksChance = builder.comment("Chance to replace blocks with dirt and lava when joining the world")
                    .defineInRange("replace_blocks_chance",1/128d,0,1);
            replaceBlocksTime = builder.comment("Length of time before reloading the world").defineInRange("replace_blocks_time",150,0,100000000);

            jumpScareChance = builder.comment("Chance every second for jumpscare")
                    .defineInRange("jump_scare_chance",1/4096d,0,1);

            delaySongTime = builder.comment("Time to delay first song at start").defineInRange("delay_song_time",300,1,1000000000);
            chatOffset = builder.defineInRange("chat_offset",-52,-1000000000,1000000000);

            builder.pop();
        }
    }
}
