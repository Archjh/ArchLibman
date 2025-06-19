package me.wavelength.baseclient.module.modules.combat;

import me.wavelength.baseclient.event.events.UpdateEvent;
import me.wavelength.baseclient.module.Category;
import me.wavelength.baseclient.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C16PacketClientStatus;
import org.lwjgl.input.Keyboard;

public class AutoRespawn extends Module {

    public AutoRespawn() {
        super("AutoRespawn", "It make you can auto respawn.", Keyboard.KEY_V, Category.COMBAT, true);
    }

    @Override
    public void onUpdate(UpdateEvent event) {
        if(Minecraft.getMinecraft().thePlayer!=null && !Minecraft.getMinecraft().thePlayer.isEntityAlive()){
            Minecraft.getMinecraft().getNetHandler().getNetworkManager().sendPacket(new C16PacketClientStatus(C16PacketClientStatus.EnumState.PERFORM_RESPAWN));
        }
    }
}
