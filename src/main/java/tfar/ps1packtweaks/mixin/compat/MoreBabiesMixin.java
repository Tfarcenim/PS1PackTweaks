package tfar.ps1packtweaks.mixin.compat;

import com.github.mammut53.more_babies.MoreBabies;
import com.github.mammut53.more_babies.config.MoreBabiesConfig;
import com.github.mammut53.more_babies.event.NaturalSpawning;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(MoreBabies.class)
public class MoreBabiesMixin {

    /**
     * @author tfar
     * @reason bad code
     */
    @Overwrite(remap = false)
    public static void onConstructMod(FMLConstructModEvent event) {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, MoreBabiesConfig.SPEC);
        //ConfigTracker.INSTANCE.loadConfigs(ModConfig.Type.COMMON, FMLPaths.CONFIGDIR.get());
        MinecraftForge.EVENT_BUS.addListener(NaturalSpawning::onLivingPackSize);
    }

}
