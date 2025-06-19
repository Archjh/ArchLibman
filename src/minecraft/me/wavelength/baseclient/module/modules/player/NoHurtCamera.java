package me.wavelength.baseclient.module.modules.player;

import me.wavelength.baseclient.module.Category;
import me.wavelength.baseclient.module.Module;
import org.lwjgl.input.Keyboard;

public class NoHurtCamera extends Module {
    public NoHurtCamera() {
        super("NoHurtCam", "It can none the hurt camera.", Keyboard.KEY_NONE, Category.RENDER, true);
    }
}
