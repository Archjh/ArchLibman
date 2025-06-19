package me.wavelength.baseclient.module.modules.render;

import me.wavelength.baseclient.module.Category;
import me.wavelength.baseclient.module.Module;
import org.lwjgl.input.Keyboard;

public class NoUnderWaterOverlay extends Module {

    public NoUnderWaterOverlay() {//无默认按键,分类为渲染类
        super("NoUnderOverlay", "It can make none the under water overlay.", Keyboard.KEY_NONE, Category.RENDER, true);
    }
}
