package me.wavelength.baseclient.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C16PacketClientStatus;

public class InventoryUtils {

    private static final Minecraft mc = Minecraft.getMinecraft();

    /**
     * 将物品从一个槽位移到另一个槽位
     * @param fromSlot 源槽位 (0-35)
     * @param toSlot 目标槽位 (0-35)
     */
    public static void moveSlotToSlot(int fromSlot, int toSlot) {
        if (fromSlot == toSlot) return;

        // 确保槽位有效
        if (fromSlot < 0 || fromSlot > 35 || toSlot < 0 || toSlot > 35)
            return;

        // 交换物品
        mc.playerController.windowClick(
                mc.thePlayer.inventoryContainer.windowId,
                fromSlot,
                0,
                1, // 左键点击
                mc.thePlayer
        );

        mc.playerController.windowClick(
                mc.thePlayer.inventoryContainer.windowId,
                toSlot,
                0,
                1, // 左键点击
                mc.thePlayer
        );

        // 如果是从背包移到快捷栏，需要额外点击一次
        if (fromSlot >= 9 && toSlot < 9) {
            mc.playerController.windowClick(
                    mc.thePlayer.inventoryContainer.windowId,
                    fromSlot,
                    0,
                    1, // 左键点击
                    mc.thePlayer
            );
        }
    }

    /**
     * 获取指定槽位的物品堆栈
     * @param slot 槽位ID (0-35)
     */
    public static ItemStack getStackInSlot(int slot) {
        if (slot < 0 || slot > 35) return null;
        return mc.thePlayer.inventory.getStackInSlot(slot);
    }

    /**
     * 切换到指定的快捷栏槽位
     * @param slot 快捷栏槽位 (0-8)
     */
    public static void switchToSlot(int slot) {
        if (slot < 0 || slot > 8) return;
        mc.thePlayer.inventory.currentItem = slot;
        mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(slot));
    }

    /**
     * 获取当前手持物品的槽位
     */
    public static int getCurrentSlot() {
        return mc.thePlayer.inventory.currentItem;
    }

    /**
     * 丢弃指定槽位的物品
     * @param slot 槽位ID (0-35)
     */
    public static void dropSlot(int slot) {
        if (slot < 0 || slot > 35) return;
        mc.playerController.windowClick(
                mc.thePlayer.inventoryContainer.windowId,
                slot,
                1, // 丢弃模式
                4, // 丢弃操作
                mc.thePlayer
        );
    }

    /**
     * 检查物品栏是否已满
     */
    public static boolean isInventoryFull() {
        for (int i = 9; i < 36; i++) { // 只检查背包部分
            if (getStackInSlot(i) == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * 使用当前手持物品
     */
    public static void useCurrentItem() {
        mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(
                mc.thePlayer.inventory.getCurrentItem()
        ));
    }

    /**
     * 打开物品栏 (默认按E键的效果)
     */
    public static void openInventory() {
        mc.getNetHandler().addToSendQueue(new C16PacketClientStatus(
                C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT
        ));
    }

    /**
     * 关闭当前打开的GUI窗口
     */
    public static void closeWindow() {
        mc.getNetHandler().addToSendQueue(new C0DPacketCloseWindow(
                mc.thePlayer.inventoryContainer.windowId
        ));
    }
}
