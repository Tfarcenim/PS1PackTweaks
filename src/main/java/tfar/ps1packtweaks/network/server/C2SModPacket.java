package tfar.ps1packtweaks.network.server;

import net.minecraft.server.level.ServerPlayer;
import tfar.ps1packtweaks.network.ModPacket;

public interface C2SModPacket extends ModPacket {

    void handleServer(ServerPlayer player);

}
