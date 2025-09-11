package tfar.ps1packtweaks.network.client;

import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import tfar.ps1packtweaks.client.PS1PackTweaksClient;

public record S2CShaderPacket(ResourceLocation shader,int ticks) implements S2CModPacket {

    public S2CShaderPacket(FriendlyByteBuf buf) {
        this(buf.readResourceLocation(),buf.readInt());
    }

    @Override
    public void handleClient() {
        PS1PackTweaksClient.handleShader(shader,ticks);
    }

    @Override
    public void write(FriendlyByteBuf to) {
        to.writeResourceLocation(shader);
        to.writeInt(ticks);
    }
}
