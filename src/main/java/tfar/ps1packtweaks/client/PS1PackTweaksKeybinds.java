package tfar.ps1packtweaks.client;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class PS1PackTweaksKeybinds {
    public static final String CATEGORY = "key.categories.ps1packtweaks";
    public static final KeyMapping TURN_AROUND = new KeyMapping("key.ps1packtweaks.turnaround", GLFW.GLFW_KEY_Y,
            CATEGORY);
    static {
        TURN_AROUND.setKeyConflictContext(KeyConflictContext.IN_GAME);
    }

}
