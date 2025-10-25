package tfar.ps1packtweaks.datagen;

import net.minecraft.client.KeyMapping;
import net.minecraft.core.Holder;
import net.minecraft.data.DataGenerator;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.LanguageProvider;
import org.codehaus.plexus.util.StringUtils;
import tfar.ps1packtweaks.Init;
import tfar.ps1packtweaks.PS1PackTweaks;
import tfar.ps1packtweaks.client.PS1PackTweaksKeybinds;
import tfar.ps1packtweaks.util.TextComponents;

import java.util.function.Supplier;

public class ModLangProvider extends LanguageProvider {
    public ModLangProvider(DataGenerator gen) {
        super(gen, PS1PackTweaks.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        addDefaultItem(() -> Init.ModItems.PRISMARINE_ROD);
        addDefaultItem(() -> Init.ModItems.BARNACLE_TOOTH);

        addDefaultBlock(() -> Init.ModBlocks.ENDERSHROOM);
        addDefaultBlock(() -> Init.ModBlocks.ENDERSHROOM_BLOCK);

        addDefaultBlock(() -> Init.ModBlocks.EBONY_SIGN);
        addDefaultBlock(() -> Init.ModBlocks.ENDERVIOLET_SIGN);

        addDefaultItem(() -> Init.ModItems.EBONY_BOAT);
        addDefaultItem(() -> Init.ModItems.ENDERVIOLET_BOAT);

        addDefaultEntityType(() -> Init.ModEntityTypes.BARNACLE);
        addDefaultEntityType(() -> Init.ModEntityTypes.HEROBRINE);

        addDefaultEntityType(() -> Init.ModEntityTypes.INVISIBLE_ENTITY);
        addDefaultEntityType(() -> Init.ModEntityTypes.SCRIPTED_MIDNIGHT_LURKER);
        addDefaultEntityType(() -> Init.ModEntityTypes.FINAL_HEROBRINE);

        add("text.autoconfig.inventorypause.title", "Inventory Pause Config");


        add("text.autoconfig.inventorypause.category.default", "General");

        add("text.autoconfig.inventorypause.option.enabled", "Enable Mod");
        add("text.autoconfig.inventorypause.option.disableSaving", "Disable Save on Pause");
        add("text.autoconfig.inventorypause.option.pauseSounds", "Pause Sounds");
        add("text.autoconfig.inventorypause.option.debug", "Enable Debug Mode");
        add("text.autoconfig.inventorypause.option.debugText", "Debug Overlay Options");
        add("text.autoconfig.inventorypause.option.debugText.x", "X-Coordinate");
        add("text.autoconfig.inventorypause.option.debugText.y", "Y-Coordinate");
        add("text.autoconfig.inventorypause.option.debugText.maxDepth", "Maximum Crawl Depth");


        add("text.autoconfig.inventorypause.category.abilities", "Abilities");

        add("text.autoconfig.inventorypause.option.abilities.pauseInventory", "Pause on Inventory");
        add("text.autoconfig.inventorypause.option.abilities.pauseCreativeInventory", "Pause on Creative Inventory");
        add("text.autoconfig.inventorypause.option.abilities.pauseDeath", "Pause on Death Screen");
        add("text.autoconfig.inventorypause.option.abilities.pauseGameModeSwitcher", "Pause on Gamemode Switcher");
        add("text.autoconfig.inventorypause.option.abilities.pauseFurnace", "Pause on Furnace");
        add("text.autoconfig.inventorypause.option.abilities.pauseCraftingTable", "Pause on Crafting Table");
        add("text.autoconfig.inventorypause.option.abilities.pauseShulkerBox", "Pause on Shulker Box");
        add("text.autoconfig.inventorypause.option.abilities.pauseChest", "Pause on Chests");

        add("text.autoconfig.inventorypause.option.abilities.additionalGUIs", "Additional GUIs");
        add("text.autoconfig.inventorypause.option.abilities.additionalGUIs.pauseAnvil", "Pause on Anvil");
        add("text.autoconfig.inventorypause.option.abilities.additionalGUIs.pauseBeacon", "Pause on Beacon");
        add("text.autoconfig.inventorypause.option.abilities.additionalGUIs.pauseDispenser", "Pause on Dispenser / Dropper");
        add("text.autoconfig.inventorypause.option.abilities.additionalGUIs.pauseBrewingStand", "Pause on Brewing Stand");
        add("text.autoconfig.inventorypause.option.abilities.additionalGUIs.pauseHopper", "Pause on Hopper");
        add("text.autoconfig.inventorypause.option.abilities.additionalGUIs.pauseCartographyTable", "Pause on Cartography Table");
        add("text.autoconfig.inventorypause.option.abilities.additionalGUIs.pauseStonecutter", "Pause on Stonecutter");

        add("text.autoconfig.inventorypause.option.abilities.worldGUIs", "World GUIs");
        add("text.autoconfig.inventorypause.option.abilities.worldGUIs.pauseHorse", "Pause on Horse Inventory");
        add("text.autoconfig.inventorypause.option.abilities.worldGUIs.pauseMerchant", "Pause on Villager");

        add("text.autoconfig.inventorypause.category.modCompat", "Mod Compat");
        add("text.autoconfig.inventorypause.option.modCompat.@PrefixText", "All mod compats have been removed for the initial 1.17/1.18 release, however you can still add classes manually");
        add("text.autoconfig.inventorypause.option.modCompat.waystonesCompat", "Waystones Compat");
        add("text.autoconfig.inventorypause.option.modCompat.ironchestCompat", "Iron Chest Compat");
        add("text.autoconfig.inventorypause.option.modCompat.appliedEnergistics2Compat", "Applied Energistics 2 Compat ⚠");
        add("text.autoconfig.inventorypause.option.modCompat.botaniaCompat", "Botania Compat");
        add("text.autoconfig.inventorypause.option.modCompat.curiosCompat", "Curios Compat");
        add("text.autoconfig.inventorypause.option.modCompat.theTwilightForestCompat", "The Twilight Forest Compat");
        add("text.autoconfig.inventorypause.option.modCompat.mekanismCompat", "Mekanism Compat ⚠");
        add("text.autoconfig.inventorypause.option.modCompat.mekanismGeneratorsCompat", "Mekanism Generators Compat ⚠");
        add("text.autoconfig.inventorypause.option.modCompat.refinedStorageCompat", "Refined Storage Compat ⚠");
        add("text.autoconfig.inventorypause.option.modCompat.titaniumCompat", "Titanium Compat ⚠");
        add("text.autoconfig.inventorypause.option.modCompat.industrialForegoingCompat", "Industrial Foregoing Compat ⚠");
        add("text.autoconfig.inventorypause.option.modCompat.projectECompat", "ProjectE Compat ⚠");
        add("text.autoconfig.inventorypause.option.modCompat.aquaculture2Compat", "Aquaculture 2 Compat");
        add("text.autoconfig.inventorypause.option.modCompat.pneumaticcraftCompat", "PneumaticCraft Compat ⚠");
        add("text.autoconfig.inventorypause.option.modCompat.extendedCraftingCompat", "Extended Crafting Compat");
        add("text.autoconfig.inventorypause.option.modCompat.customScreens", "Custom mod class names");
        add("text.autoconfig.inventorypause.option.modCompat.timeBetweenCompatTicks.@PrefixText", "Some mod GUIs may not work correctly without any ticking being done. Any classes listed in compat classes will still be ticked - no real pause - but far slower than normal. The default 'Time between compat ticks' value of 20 means that every 20 ticks (one second) screens listed will be ticked when displayed.");
        add("text.autoconfig.inventorypause.option.modCompat.timeBetweenCompatTicks", "Time between compat ticks");
        add("text.autoconfig.inventorypause.option.modCompat.compatScreens", "Compat mod class names");

        add("chat.inventorypause.copyClassName.action", "Copied internal name of the current screen to clipboard,%s");

        add("key.categories.inventorypause.main", "Inventory Pause Helpers");
        add("key.inventorypause.openSettings", "Open Settings");
        add("key.inventorypause.copyClassName", "Copy Class Name");

        addTextComponent(TextComponents.ADVANCEMENT_EAT_COOKIE_TITLE,"Eat Cookie");
        addTextComponent(TextComponents.ADVANCEMENT_EAT_COOKIE_DESC,"Eat Cookie");

        addTextComponent(TextComponents.ADVANCEMENT_PLACE_CAKE_TITLE,"Place Cake");
        addTextComponent(TextComponents.ADVANCEMENT_PLACE_CAKE_DESC,"Place Cake");

        addTextComponent(TextComponents.ADVANCEMENT_SUMMON_SNOW_GOLEM_TITLE,"Summon Snow Golem");
        addTextComponent(TextComponents.ADVANCEMENT_SUMMON_SNOW_GOLEM_DESC,"Summon Snow Golem");

        addTextComponent(TextComponents.ADVANCEMENT_PLAY_RECORD_TITLE,"Play Record Title");
        addTextComponent(TextComponents.ADVANCEMENT_PLAY_RECORD_DESC,"Play Record Description");

        addTextComponent(TextComponents.ADVANCEMENT_DRINK_POTION_TITLE,"Drink Potion");
        addTextComponent(TextComponents.ADVANCEMENT_DRINK_POTION_DESC,"Drink Potion");

        addTextComponent(TextComponents.ADVANCEMENT_CRAFT_JACK_O_LANTERN_TITLE,"Craft Jack o Lantern");
        addTextComponent(TextComponents.ADVANCEMENT_CRAFT_JACK_O_LANTERN_DESC,"Craft Jack o Lantern");

        addTextComponent(TextComponents.ADVANCEMENT_CRAFT_ANYTHING_TITLE,"Crafting");
        addTextComponent(TextComponents.ADVANCEMENT_CRAFT_ANYTHING_DESC,"Crafting");

        addTextComponent(TextComponents.ADVANCEMENT_KILL_SPIDER_TITLE,"Kill Spider");
        addTextComponent(TextComponents.ADVANCEMENT_KILL_SPIDER_DESC,"Kill Spider");

        addTextComponent(TextComponents.ADVANCEMENT_FIND_LURKER_TITLE,"Find Midnight Lurker");
        addTextComponent(TextComponents.ADVANCEMENT_FIND_LURKER_DESC,"Find Midnight Lurker");

        addTextComponent(TextComponents.ADVANCEMENT_PLAYER_PET_DIED_TITLE,"Player Pet Died");
        addTextComponent(TextComponents.ADVANCEMENT_PLAYER_PET_DIED_DESC,"Player Pet Died");

        addTextComponent(TextComponents.ADVANCEMENT_PLAY_ENDERMOSH_TITLE,"Play Endermosh");
        addTextComponent(TextComponents.ADVANCEMENT_PLAY_ENDERMOSH_DESC,"Play Endermosh");

        add(PS1PackTweaksKeybinds.CATEGORY,"PS1 Pack Tweaks");
        addKey(PS1PackTweaksKeybinds.TURN_AROUND,"Turn Around");

        add("container.fletching","Fletching Table");
    }

    void addKey(KeyMapping mapping, String translation) {
        add(mapping.getName(), translation);
    }

    void addAttribute(Attribute attribute, String translation) {
        add(attribute.getDescriptionId(), translation);
    }

    protected void addDefaultMobEffect(Holder<MobEffect> holder) {
        addEffect(holder::value, getNameFromEffect(holder.value()));
    }

    protected void addDefaultMobEffect(MobEffect effect) {
        addEffect(() -> effect, getNameFromEffect(effect));
    }

    protected void addDefaultItem(Supplier<? extends Item> supplier) {
        addItem(supplier, getNameFromItem(supplier.get()));
    }

    protected void addDefaultBlock(Supplier<? extends Block> supplier) {
        addBlock(supplier, getNameFromBlock(supplier.get()));
    }

    protected void addDefaultEntityType(Supplier<EntityType<?>> supplier) {
        addEntityType(supplier, getNameFromEntity(supplier.get()));
    }

    public static String getNameFromItem(Item item) {
        return StringUtils.capitaliseAllWords(item.getDescriptionId().split("\\.")[2].replace("_", " "));
    }

    public static String getNameFromBlock(Block block) {
        return StringUtils.capitaliseAllWords(block.getDescriptionId().split("\\.")[2].replace("_", " "));
    }

    public static String getNameFromEffect(MobEffect effect) {
        return StringUtils.capitaliseAllWords(effect.getDescriptionId().split("\\.")[2].replace("_", " "));
    }

    public static String getNameFromEntity(EntityType<?> entity) {
        return StringUtils.capitaliseAllWords(entity.getDescriptionId().split("\\.")[2].replace("_", " "));
    }

    protected void addTextComponent(TranslatableComponent component, String text) {
        add(component.getKey(),text);
    }
}
