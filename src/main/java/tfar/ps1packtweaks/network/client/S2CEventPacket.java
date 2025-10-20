package tfar.ps1packtweaks.network.client;

import net.minecraft.network.FriendlyByteBuf;
import tfar.ps1packtweaks.client.PS1PackTweaksClient;

public enum S2CEventPacket implements S2CModPacket {
    OPEN_ONLINE_OPTIONS_SCREEN,
    STOP_MUSIC;

    static final S2CEventPacket[] VALUES = values();

    public static S2CEventPacket read(FriendlyByteBuf buf) {
        int ordinal = buf.readInt();
        return VALUES[ordinal];
    }

    @Override
    public void handleClient() {
        PS1PackTweaksClient.handleEvent(this);
    }

    @Override
    public void write(FriendlyByteBuf to) {
        to.writeInt(ordinal());
    }
}
