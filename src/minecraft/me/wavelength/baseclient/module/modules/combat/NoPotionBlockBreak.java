package me.wavelength.baseclient.module.modules.combat;

import me.wavelength.baseclient.module.AntiCheat;
import me.wavelength.baseclient.module.Category;
import me.wavelength.baseclient.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class NoPotionBlockBreak extends Module {

    private Minecraft mc = Minecraft.getMinecraft();
    private boolean wasDrinking = false;

    public NoPotionBlockBreak() {
        super("NoPotionBlockBreak", "防止喝药水时破坏方块打断进程", 0, Category.COMBAT, AntiCheat.VANILLA);
    }

    @Override
    public void onUpdate() {
        if(mc.thePlayer == null) return;

        boolean isDrinking = mc.thePlayer.isUsingItem();

        if(isDrinking && !wasDrinking) {
            // 开始喝药水时取消任何方块破坏
            cancelBlockBreaking();
        }

        wasDrinking = isDrinking;
    }

    private void cancelBlockBreaking() {
        // 发送停止破坏方块的数据包
        if(mc.objectMouseOver != null && mc.objectMouseOver.getBlockPos() != null) {
            mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(
                    C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK,
                    mc.objectMouseOver.getBlockPos(),
                    EnumFacing.DOWN
            ));
        }
    }

    @Override
    public void onEnable() {
        super.onEnable();
        wasDrinking = false;
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}