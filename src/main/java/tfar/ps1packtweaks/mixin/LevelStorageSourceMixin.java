package tfar.ps1packtweaks.mixin;

import com.mojang.datafixers.DataFixer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.LevelSummary;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import tfar.ps1packtweaks.client.WorldLocker;

import java.io.File;

@Mixin(LevelStorageSource.class)
@Debug(export = true)
public class LevelStorageSourceMixin {
    //  private synthetic lambda$levelSummaryReader$2(Ljava/io/File;ZLjava/io/File;Lcom/mojang/datafixers/DataFixer;)Lnet/minecraft/world/level/storage/LevelSummary;
    @Inject(method = "*(Ljava/io/File;ZLjava/io/File;Lcom/mojang/datafixers/DataFixer;)Lnet/minecraft/world/level/storage/LevelSummary",at = @At("RETURN")
            ,remap = false, cancellable = true,locals = LocalCapture.CAPTURE_FAILHARD)
    private void preventOpen(File p_193013_, boolean p_193014_, File p_193015_, DataFixer p_193016_, CallbackInfoReturnable<LevelSummary> cir, Tag tag) {
        WorldLocker.handle((LevelStorageSource)(Object)this,cir,tag);
    }
}
