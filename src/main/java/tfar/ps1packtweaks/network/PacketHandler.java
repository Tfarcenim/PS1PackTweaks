package tfar.ps1packtweaks.network;

import net.minecraft.resources.ResourceLocation;
import tfar.ps1packtweaks.PS1PackTweaks;
import tfar.ps1packtweaks.network.client.S2CAdvancementPacket;
import tfar.ps1packtweaks.network.client.S2CShaderPacket;

import java.util.Locale;

public class PacketHandler {

    public static void registerPackets() {
        ForgePacketHandler.registerClientPacket(S2CShaderPacket.class, S2CShaderPacket::new);
        ForgePacketHandler.registerClientPacket(S2CAdvancementPacket.class, S2CAdvancementPacket::new);
    }

    public static ResourceLocation packet(Class<?> clazz) {
        return PS1PackTweaks.id(clazz.getName().toLowerCase(Locale.ROOT));
    }

}
