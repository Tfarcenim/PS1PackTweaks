// SPDX-License-Identifier: EUPL-1.2

package de.macbrayne.forge.inventorypause.common;

import net.minecraft.client.gui.screens.Screen;
import tfar.ps1packtweaks.PS1TweaksConfig;

public class ScreenHelper {


    public static boolean isPauseScreen(Screen caller) {

        for (String s : PS1TweaksConfig.CLIENT.inventorypause_screens.get()) {
            if(caller.getClass().getName().equals(s)) {
                return true;
            }
        }
        return false;
    }
}
