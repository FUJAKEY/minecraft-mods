package com.pipimod.pipimod.client;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class KeyBindingHolder {
    private final KeyBinding peeKey = new KeyBinding(
            "key.pipimod.pee",
            KeyConflictContext.IN_GAME,
            InputMappings.Type.KEYSYM,
            GLFW.GLFW_KEY_P,
            "key.categories.pipimod"
    );

    public KeyBinding peeKey() {
        return peeKey;
    }
}
