// SPDX-License-Identifier: EUPL-1.2

package tfar.ps1packtweaks.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.server.IntegratedServer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import tfar.ps1packtweaks.PS1PackTweaksConfig;

@Mixin(IntegratedServer.class)
public class IntegratedServerMixin {
    @Shadow @Final private Minecraft minecraft;

    @WrapOperation(method = "tickServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/server/IntegratedServer;saveEverything(ZZZ)Z"))
    public boolean disableSaving(IntegratedServer instance, boolean suppressLogs, boolean flush, boolean force, Operation<Boolean> original) {
        if (PS1PackTweaksConfig.CLIENT.inventorypause_enabled.get() && PS1PackTweaksConfig.CLIENT.inventorypause_disable_saving.get()
                && !(this.minecraft.screen instanceof PauseScreen)) {
            return false;
        }
        return original.call(instance, suppressLogs, flush, force);
    }
}