package tfar.ps1packtweaks.network.client;

import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import tfar.ps1packtweaks.client.PS1PackTweaksClient;

public record S2CTargetDimensionPacket(ResourceKey<Level> key) implements S2CModPacket {

    public S2CTargetDimensionPacket(FriendlyByteBuf buf) {
        this(ResourceKey.create(Registry.DIMENSION_REGISTRY, buf.readResourceLocation()));
    }

    @Override
    public void handleClient() {
        PS1PackTweaksClient.handle(key);
    }

    @Override
    public void write(FriendlyByteBuf to) {
        to.writeResourceLocation(key.location());
    }
}
