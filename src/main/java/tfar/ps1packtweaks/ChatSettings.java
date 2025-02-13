package tfar.ps1packtweaks;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.entity.player.ChatVisiblity;

public record ChatSettings(ChatVisiblity chatVisiblity) {

    public static final Codec<ChatVisiblity> CHAT_VISIBLITY_CODEC = Codec.STRING.xmap(ChatVisiblity::valueOf, Enum::name);

    public static final Codec<ChatSettings> CODEC = RecordCodecBuilder.create(chatSettingsInstance ->
            chatSettingsInstance.group(CHAT_VISIBLITY_CODEC.fieldOf("chatVisibility").forGetter(ChatSettings::chatVisiblity))
                    .apply(chatSettingsInstance,ChatSettings::new));

    public static final ChatSettings DEFAULT_SINGLEPLAYER = new ChatSettings(ChatVisiblity.HIDDEN);

    public static final ChatSettings DEFAULT_MULTIPLAYER = new ChatSettings(ChatVisiblity.FULL);
}
