package me.wavelength.baseclient.module.modules.movement;

import me.wavelength.baseclient.event.events.UpdateEvent;
import me.wavelength.baseclient.module.Category;
import me.wavelength.baseclient.module.Module;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

public class Sneak extends Module {

    private boolean sneak = false;

    public Sneak() {//默认按键Z,分类为移动
        super("Sneak", "It can make you always sneak.",Keyboard.KEY_Z, Category.MOVEMENT, true);
    }

    @Override
    public void onEnable() {
        this.sneak = Minecraft.getMinecraft().gameSettings.keyBindSneak.isPressed();
    }

    @Override
    public void onUpdate(UpdateEvent event) {
        Minecraft.getMinecraft().gameSettings.keyBindSneak.setPressed(true);
    }

    @Override
    public void onDisable() {
        Minecraft.getMinecraft().gameSettings.keyBindSneak.setPressed(this.sneak);
    }
}