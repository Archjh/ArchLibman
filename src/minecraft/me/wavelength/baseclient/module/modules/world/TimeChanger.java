package me.wavelength.baseclient.module.modules.world;

import me.wavelength.baseclient.module.Category;
import me.wavelength.baseclient.module.Module;
import org.lwjgl.input.Keyboard;

public class TimeChanger extends Module {

    public TimeChanger() {//无默认按键,分类为世界,默认配置 time 1000
        super("TimeChanger", "It can custom the time of the world.", Keyboard.KEY_NONE, Category.WORLD, true);
        this.moduleSettings.addDefault("time",1000);
    }
}
