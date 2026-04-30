package tfar.ps1packtweaks.network.client;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import tfar.ps1packtweaks.client.PS1PackTweaksClient;
import tfar.ps1packtweaks.network.ForgePacketHandler;

public enum S2CEventPacket implements S2CModPacket {
    PLAY_LURKER_JUMPSCARE,
    START_FINAL_HEROBRINE,
    PLAY_VIDEO,
    END_FINAL_HEROBRINE;

    public static S2CEventPacket read(FriendlyByteBuf buf) {
        return buf.readEnum(S2CEventPacket.class);
    }

    public void send(ServerPlayer player) {
        ForgePacketHandler.sendToClient(this,player);
    }

    @Override
    public void handleClient() {
        PS1PackTweaksClient.handleEvent(this);
    }

    @Override
    public void write(FriendlyByteBuf to) {
        to.writeEnum(this);
    }
}
