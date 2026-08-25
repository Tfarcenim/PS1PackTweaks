package tfar.ps1packtweaks;

import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.GameRules;

public class DebugHooks {

    public static void onGameRuleChange(GameRules.Value<?> pValue, MinecraftServer server) {
        if (server != null && pValue == server.getGameRules().getRule(GameRules.RULE_WEATHER_CYCLE)) {
            new Exception().printStackTrace();
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                player.connection.send(new ClientboundSoundPacket(SoundEvents.GHAST_HURT, SoundSource.MASTER,
                        player.getX(),player.getY(),player.getZ(),1,1));
            }
        }
    }

    public static float getTestSpeed() {
        return 31;
    }
}