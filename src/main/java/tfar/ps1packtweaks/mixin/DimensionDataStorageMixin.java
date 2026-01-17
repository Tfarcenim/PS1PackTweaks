package tfar.ps1packtweaks.mixin;

import net.minecraft.Util;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import tfar.ps1packtweaks.PS1PackTweaks;

import java.io.File;
import java.util.Map;

@Mixin(DimensionDataStorage.class)
public abstract class DimensionDataStorageMixin {
    @Shadow @Final private Map<String, SavedData> cache;

    @Shadow protected abstract File getDataFile(String pName);

    /**
     * @author tfar
     * @reason profiling

    @Overwrite
    public void save() {
        long allStart = Util.getNanos();
        this.cache.forEach((p_164866_, p_164867_) -> {
            if (p_164867_ != null) {
                long start = Util.getNanos();
                p_164867_.save(this.getDataFile(p_164866_));
                long end = Util.getNanos();
                PS1PackTweaks.LOGGER.info("took {} ms to save {}", (end - start)/1000000f, p_164866_);
            }
        });
        long allEnd = Util.getNanos();
        PS1PackTweaks.LOGGER.info("took {} ms to save all saveddatas", (allEnd - allStart)/1000000f);
    }*/
}
