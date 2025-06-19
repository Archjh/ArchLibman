package me.wavelength.baseclient.module.modules.player;

import me.wavelength.baseclient.event.events.UpdateEvent;
import me.wavelength.baseclient.module.AntiCheat;
import me.wavelength.baseclient.module.Category;
import me.wavelength.baseclient.module.Module;
import org.lwjgl.input.Keyboard;

public class Sprint extends Module {

    public Sprint() {
        super("Sprint", "the Sprinting", Keyboard.KEY_F, Category.PLAYER, AntiCheat.VANILLA, AntiCheat.AAC);

    }

    @Override
    public void onUpdate(UpdateEvent event) {
        mc.thePlayer.setSprinting(true);
    }
}
