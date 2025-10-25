package tfar.ps1packtweaks.client;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.mojang.datafixers.DataFixer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.storage.LevelSummary;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import tfar.ps1packtweaks.PS1PackTweaks;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.BiFunction;

public class WorldLocker {

    private static final File FILE = new File("config/unlocked.json");

    private static final Gson GSON = new Gson();

    public static final Map<String, ResourceLocation> LOCKED_WORLDS = new HashMap<>();
    public static final ResourceLocation EAT_COOKIE =  PS1PackTweaks.id("unlock/eat_cookie");
    public static final ResourceLocation PLACE_CAKE =  PS1PackTweaks.id("unlock/place_cake");
    public static final ResourceLocation SUMMON_SNOW_GOLEM =  PS1PackTweaks.id("unlock/summon_snow_golem");
    public static final ResourceLocation PLAY_DISC_11 = PS1PackTweaks.id("unlock/play_disc_11");
    public static final ResourceLocation DRINK_POTION = PS1PackTweaks.id("unlock/drink_potion");
    public static final ResourceLocation CRAFT_JACK_O_LANTERN = PS1PackTweaks.id("unlock/craft_jack_o_lantern");
    public static final ResourceLocation CRAFT_ANYTHING = PS1PackTweaks.id("unlock/craft_anything");
    public static final ResourceLocation KILL_SPIDER = PS1PackTweaks.id("unlock/kill_spider");
    public static final ResourceLocation ENCOUNTER_MIDNIGHT_LURKER = PS1PackTweaks.id("unlock/encounter_midnight_lurker");
    public static final ResourceLocation PLAYER_PET_DIES = PS1PackTweaks.id("unlock/player_pet_dies");
    public static final ResourceLocation BEATING_THE_GAME = new ResourceLocation("end/kill_dragon");

    public static final ResourceLocation PURIFIED = PS1PackTweaks.id("purified");

    public static final ResourceLocation PLAY_ENDERMOSH = new ResourceLocation("unlock/endermosh");

    public static final File LOCKED_SCREENSHOT = new File("screenshots/1999-05-17_10.30.43.png.mcr");
    public static final File UNLOCKED_SCREENSHOT = new File("screenshots/1999-05-17_10.30.43.png");

    static {
        LOCKED_WORLDS.put("1977-09-26",EAT_COOKIE);
        LOCKED_WORLDS.put("1979-06-01", PLACE_CAKE);
        LOCKED_WORLDS.put("1983-12-26", SUMMON_SNOW_GOLEM);
        LOCKED_WORLDS.put("1986-06-03", PLAY_DISC_11);
        LOCKED_WORLDS.put("1986-08-22", DRINK_POTION);
        LOCKED_WORLDS.put("1995-10-31", CRAFT_JACK_O_LANTERN);

        LOCKED_WORLDS.put("1996-06-19", CRAFT_ANYTHING);
        LOCKED_WORLDS.put("1997-07-07", KILL_SPIDER);

        LOCKED_WORLDS.put("1998-03-12", ENCOUNTER_MIDNIGHT_LURKER);
        LOCKED_WORLDS.put("1998-10-13", PLAYER_PET_DIES);
        LOCKED_WORLDS.put("1999-05-16", BEATING_THE_GAME);

    }

    public static void deleteSpecialWorlds(Path saves) {
        for (String s : LOCKED_WORLDS.keySet()) {
            try {
                FileUtils.deleteDirectory(saves.resolve(s).toFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static final List<ResourceLocation> KEYS = new ArrayList<>();



    public static boolean isWorldLocked(String name) {
        if (LOCKED_WORLDS.containsKey(name)) {
            ResourceLocation key = LOCKED_WORLDS.get(name);
            if (!KEYS.contains(key)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isPure() {
        if (!FILE.exists())write();
        updateLocks();
        return KEYS.contains(PURIFIED);
    }

    public static void setCursed() {
        KEYS.remove(PURIFIED);
        write();
    }

    public static final BiFunction<File, DataFixer, LevelSummary> returnNothing = (file, dataFixer) -> null;

    public static void unlock(ResourceLocation location) {
        if (location.equals(PLAY_ENDERMOSH) && LOCKED_SCREENSHOT.exists()) {
            LOCKED_SCREENSHOT.renameTo(UNLOCKED_SCREENSHOT);
        }
         else if (LOCKED_WORLDS.containsValue(location)) {
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.WOODEN_DOOR_OPEN, 1.0F));

        }
        KEYS.add(location);
        write();
    }

    public static void updateLocks() {
        KEYS.clear();
        if (!FILE.exists()) return;
        load(read());
    }

    private static JsonArray read() {
        Reader reader = null;
        try {
            reader = new FileReader(FILE);
            JsonReader jsonReader = new JsonReader(reader);
            // Type listType = new TypeToken<ArrayList<BTSIslandConfig>>(){}.getType();
            // LOGGER.info("Loading existing config");
            return GSON.fromJson(jsonReader, JsonArray.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(reader);
        }
    }

    private static void load(JsonArray jsonArray) {
        for (JsonElement element : jsonArray) {
            KEYS.add(new ResourceLocation(element.getAsString()));
        }
    }

    private static void write() {

        JsonWriter writer = null;
        try {
            writer = GSON.newJsonWriter(new FileWriter(FILE));
            writer.setIndent("    ");
            GSON.toJson(keysToArray(), writer);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(writer);
        }
    }


    static JsonArray keysToArray() {
        JsonArray jsonArray = new JsonArray();
        KEYS.stream().map(ResourceLocation::toString).forEach(jsonArray::add);
        return jsonArray;
    }
}