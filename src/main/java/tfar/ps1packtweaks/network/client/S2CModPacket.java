package tfar.ps1packtweaks.network.client;


import tfar.ps1packtweaks.network.ModPacket;

public interface S2CModPacket extends ModPacket {
    void handleClient();
}
