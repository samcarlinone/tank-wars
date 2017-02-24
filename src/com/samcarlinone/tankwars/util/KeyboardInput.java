package com.samcarlinone.tankwars.util;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;

/**
 * Created by CARLINSE1 on 2/8/2017.
 */
public class KeyboardInput extends GLFWKeyCallback {
    public static final int UP = 265;
    public static final int LEFT = 263;
    public static final int DOWN = 264;
    public static final int RIGHT = 262;

    public static boolean[] keys = new boolean[65536];

    public void invoke(long window, int key, int scancode, int action, int mods) {
        if(key > keys.length)
            return;

        keys[key] = action != GLFW.GLFW_RELEASE;
    }

    public static boolean isKeyDown(int keycode) {
        return keys[keycode];
    }

}
