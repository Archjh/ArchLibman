package me.wavelength.baseclient.module.modules.render;

import me.wavelength.baseclient.module.Category;
import me.wavelength.baseclient.module.Module;
import org.lwjgl.input.Keyboard;

public class NoFireOverlay extends Module {

    public NoFireOverlay() {//无默认按键,分类为渲染类
        super("NoFireOverlay", "It can make the game not to render the fire overlay.", Keyboard.KEY_NONE, Category.RENDER, true);
    }
}