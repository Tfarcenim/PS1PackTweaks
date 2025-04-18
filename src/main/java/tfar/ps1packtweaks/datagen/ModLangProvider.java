package tfar.ps1packtweaks.datagen;

import net.minecraft.client.KeyMapping;
import net.minecraft.core.Holder;
import net.minecraft.data.DataGenerator;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.LanguageProvider;
import org.codehaus.plexus.util.StringUtils;
import tfar.ps1packtweaks.Init;
import tfar.ps1packtweaks.PS1PackTweaks;

import java.util.function.Supplier;

public class ModLangProvider extends LanguageProvider {
    public ModLangProvider(DataGenerator gen) {
        super(gen, PS1PackTweaks.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        addDefaultItem(() -> Init.ModItems.PRISMARINE_ROD);
        addDefaultItem(() -> Init.ModItems.BARNACLE_TOOTH);
    }

    void addKey(KeyMapping mapping, String translation) {
        add(mapping.getName(),translation);
    }
    void addAttribute(Attribute attribute, String translation) {
        add(attribute.getDescriptionId(),translation);
    }

    protected void addDefaultMobEffect(Holder<MobEffect> holder) {
        addEffect(holder::value,getNameFromEffect(holder.value()));
    }

    protected void addDefaultMobEffect(MobEffect effect) {
        addEffect(() -> effect,getNameFromEffect(effect));
    }

    protected void addDefaultItem(Supplier<? extends Item> supplier) {
        addItem(supplier,getNameFromItem(supplier.get()));
    }

    protected void addDefaultBlock(Supplier<? extends Block> supplier) {
        addBlock(supplier,getNameFromBlock(supplier.get()));
    }

    protected void addDefaultEntityType(Supplier<EntityType<?>> supplier) {
        addEntityType(supplier,getNameFromEntity(supplier.get()));
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

    protected void addTextComponent(MutableComponent component, String text) {
    //    ComponentContents contents = component.getContents();
     //   if (contents instanceof TranslatableContents translatableContents) {
    //        add(translatableContents.getKey(),text);
    //    } else {
            throw new UnsupportedOperationException(component +" is not translatable");
  //      }
    }
}
