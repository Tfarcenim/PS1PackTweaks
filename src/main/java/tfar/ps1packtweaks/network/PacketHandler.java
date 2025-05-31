package tfar.ps1packtweaks.network;

import net.minecraft.resources.ResourceLocation;
import tfar.ps1packtweaks.PS1PackTweaks;
import tfar.ps1packtweaks.network.client.S2CTargetDimensionPacket;

import java.util.Locale;

public class PacketHandler {

    public static void registerPackets() {
        ForgePacketHandler.registerClientPacket(S2CTargetDimensionPacket.class, S2CTargetDimensionPacket::new);
    }

    public static ResourceLocation packet(Class<?> clazz) {
        return PS1PackTweaks.id(clazz.getName().toLowerCase(Locale.ROOT));
    }

}
