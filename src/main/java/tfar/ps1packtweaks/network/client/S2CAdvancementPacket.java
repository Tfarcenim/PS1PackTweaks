package tfar.ps1packtweaks.network.client;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import tfar.ps1packtweaks.client.WorldLocker;

public record S2CAdvancementPacket(ResourceLocation advancement) implements S2CModPacket {

    public S2CAdvancementPacket(FriendlyByteBuf buf) {
        this(buf.readResourceLocation());
    }

    @Override
    public void handleClient() {
        WorldLocker.unlock(advancement);
    }

    @Override
    public void write(FriendlyByteBuf to) {
        to.writeResourceLocation(advancement);
    }
}
