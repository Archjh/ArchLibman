package me.wavelength.baseclient.module.modules.render;

import me.wavelength.baseclient.event.events.UpdateEvent;
import me.wavelength.baseclient.module.Category;
import me.wavelength.baseclient.module.Module;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

public class FullBright extends Module {

    private float gamma = 0.0F;

    public FullBright() { //默认按键 V,分类为渲染
        super("FullBright", "It can make the gamma value up to 300.", Keyboard.KEY_V, Category.RENDER, true);
    }

    @Override
    public void onEnable() {
        this.gamma = Minecraft.getMinecraft().gameSettings.gammaSetting;
    }

    @Override
    public void onUpdate(UpdateEvent event) {
        Minecraft.getMinecraft().gameSettings.gammaSetting = 300F;
    }

    @Override
    public void onDisable() {
        Minecraft.getMinecraft().gameSettings.gammaSetting = this.gamma;
    }
}
