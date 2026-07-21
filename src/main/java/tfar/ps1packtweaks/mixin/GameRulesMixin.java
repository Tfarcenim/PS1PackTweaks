package tfar.ps1packtweaks.mixin;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.ps1packtweaks.DebugHooks;

@Mixin(GameRules.Value.class)
public class GameRulesMixin {


    /*@Inject(method = "onChanged",at = @At("HEAD"))
    private void detectGameRuleChange(MinecraftServer pServer, CallbackInfo ci) {
        DebugHooks.onGameRuleChange((GameRules.Value<?>) (Object)this,pServer);
    }*/
}
