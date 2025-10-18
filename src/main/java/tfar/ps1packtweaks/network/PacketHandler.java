package tfar.ps1packtweaks.network;

import tfar.ps1packtweaks.network.client.S2CAdvancementPacket;
import tfar.ps1packtweaks.network.client.S2CEventPacket;
import tfar.ps1packtweaks.network.client.S2CShaderPacket;

public class PacketHandler {

    public static void registerPackets() {
        ForgePacketHandler.registerClientPacket(S2CShaderPacket.class, S2CShaderPacket::new);
        ForgePacketHandler.registerClientPacket(S2CAdvancementPacket.class, S2CAdvancementPacket::new);
        ForgePacketHandler.registerClientPacket(S2CEventPacket.class, S2CEventPacket::read);
    }

}
