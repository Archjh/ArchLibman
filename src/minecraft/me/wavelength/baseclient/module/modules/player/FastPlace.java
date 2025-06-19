package me.wavelength.baseclient.module.modules.player;

import me.wavelength.baseclient.event.events.UpdateEvent;
import me.wavelength.baseclient.module.AntiCheat;
import me.wavelength.baseclient.module.Category;
import me.wavelength.baseclient.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;

public class FastPlace extends Module {

    private Minecraft mc = Minecraft.getMinecraft();

    public FastPlace() {
        super("FastPlace", "Automatically places blocks faster", 0, Category.PLAYER, AntiCheat.VANILLA, AntiCheat.NCP);
    }

    @Override
    public void onUpdate(UpdateEvent event) {
        if (!this.isToggled())
            return;

        // 检查玩家是否正在看向一个方块
        if (mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            // 取消放置延迟
            mc.rightClickDelayTimer = 0;
        }
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        // 恢复默认的右键点击延迟
        mc.rightClickDelayTimer = 4;
    }
}
