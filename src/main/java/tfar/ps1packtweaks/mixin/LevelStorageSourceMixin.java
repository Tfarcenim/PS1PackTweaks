package tfar.ps1packtweaks.mixin;

import com.mojang.datafixers.DataFixer;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.LevelSummary;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfar.ps1packtweaks.client.WorldLocker;

import java.io.File;
import java.util.function.BiFunction;

@Mixin(LevelStorageSource.class)
@Debug(export = true)
public class LevelStorageSourceMixin {//need to manually supply name because lambda
    //  private synthetic lambda$levelSummaryReader$2(Ljava/io/File;ZLjava/io/File;Lcom/mojang/datafixers/DataFixer;)Lnet/minecraft/world/level/storage/LevelSummary;
    //m_193012_
    @Inject(method = "levelSummaryReader",at = @At(value = "HEAD")
            , cancellable = true)//LocalCapture.CAPTURE_FAILHARD)
    private void preventOpen(File pSaveDir, boolean pLocked, CallbackInfoReturnable<BiFunction<File, DataFixer, LevelSummary>> cir){//,Tag tag) {
        if (WorldLocker.isWorldLocked(pSaveDir.getName())) {
            cir.setReturnValue(WorldLocker.returnNothing);
        }
        //WorldLocker.handle((LevelStorageSource)(Object)this,cir,null);
    }
}
