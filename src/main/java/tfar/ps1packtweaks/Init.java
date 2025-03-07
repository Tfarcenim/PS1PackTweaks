package tfar.ps1packtweaks;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import tfar.ps1packtweaks.entity.Barnacle;

import java.util.Map;

public class Init {

    public static class ModItems{
        public static final Item BARNACLE_TOOTH = new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS));
        public static final Item PRISMARINE_ROD = new Item(new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS));
    }

    public static class ModEntityTypes {
        public static final EntityType<Barnacle> BARNACLE = EntityType.Builder
                .of(Barnacle::new, MobCategory.MONSTER)
                .sized(1.2F, 1.2F)
                .build(new ResourceLocation(PS1PackTweaks.MOD_ID, "barnacle").toString());
    }

    public static class ModSounds {
        public static final SoundEvent BARNACLE_AMBIENT = new SoundEvent(PS1PackTweaks.id( "barnacle_ambient"));
        public static final SoundEvent BARNACLE_HURT = new SoundEvent(PS1PackTweaks.id("barnacle_hurt"));
        public static final SoundEvent BARNACLE_DEATH = new SoundEvent(PS1PackTweaks.id( "barnacle_death"));
        public static final SoundEvent BARNACLE_FLOP = new SoundEvent(PS1PackTweaks.id( "barnacle_flop"));
    }

    public static class ModBlocks {
    }

}
