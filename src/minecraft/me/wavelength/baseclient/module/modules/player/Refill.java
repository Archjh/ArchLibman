package me.wavelength.baseclient.module.modules.player;

import me.wavelength.baseclient.BaseClient;
import me.wavelength.baseclient.event.EventListener;
import me.wavelength.baseclient.event.events.UpdateEvent;
import me.wavelength.baseclient.module.AntiCheat;
import me.wavelength.baseclient.module.Category;
import me.wavelength.baseclient.module.Module;
import me.wavelength.baseclient.utils.InventoryUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

import java.util.List;
import java.util.Random;

public class Refill extends Module {

    private final Minecraft mc = Minecraft.getMinecraft();

    // 可配置的设置
    private int threshold = 3; // 当药水少于这个数量时触发补充
    private int potionSlot = 1; // 快捷栏中放置药水的槽位 (0-8)
    private int delay = 0;
    private int currentDelay = 0;
    private Random random = new Random();

    public Refill() {
        super("Refill", "Automatically refills potions in NoDebuff mode", 0, Category.PLAYER,
                AntiCheat.VANILLA, AntiCheat.NCP, AntiCheat.AAC, AntiCheat.VANILLA);
    }

    @Override
    public void onUpdate(UpdateEvent event) {
        if (mc.thePlayer == null || mc.theWorld == null || !this.isToggled())
            return;

        // 添加随机延迟以避免模式检测
        if (currentDelay > 0) {
            currentDelay--;
            return;
        }

        // 检查当前快捷栏中的药水数量
        ItemStack heldStack = mc.thePlayer.inventory.getStackInSlot(potionSlot);
        int potionCount = (heldStack != null && isSplashPotion(heldStack)) ? heldStack.stackSize : 0;

        // 如果药水数量低于阈值，尝试补充
        if (potionCount < threshold) {
            refillPotions(potionSlot);
            // 设置随机延迟 (10-30 ticks)
            currentDelay = 10 + random.nextInt(20);
        }
    }

    /**
     * 检查物品是否是喷溅型药水
     */
    private boolean isSplashPotion(ItemStack stack) {
        if (stack == null || !(stack.getItem() instanceof ItemPotion))
            return false;

        ItemPotion potion = (ItemPotion)stack.getItem();
        return ItemPotion.isSplash(stack.getItemDamage()) &&
                isGoodPotion(potion.getEffects(stack)); // 只补充有用的药水
    }

    /**
     * 检查药水效果是否是有益的（用于NoDebuff）
     */
    private boolean isGoodPotion(List<PotionEffect> effects) {
        for (PotionEffect effect : effects) {
            int id = effect.getPotionID();
            // 只补充治疗、再生、力量等有益药水
            if (id == Potion.heal.id ||
                    id == Potion.regeneration.id ||
                    id == Potion.moveSpeed.id ||
                    id == Potion.damageBoost.id) {
                return true;
            }
        }
        return false;
    }

    /**
     * 从背包中寻找并补充药水到指定快捷栏
     */
    private void refillPotions(int hotbarSlot) {
        // 在背包中寻找喷溅型药水
        for (int i = 9; i < 36; i++) { // 9-35是背包槽位
            ItemStack stack = mc.thePlayer.inventory.getStackInSlot(i);
            if (stack != null && isSplashPotion(stack)) {
                // 添加随机延迟和人类化操作
                try {
                    // 模拟人类点击延迟
                    Thread.sleep(50 + random.nextInt(100));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // 将药水移动到快捷栏
                InventoryUtils.moveSlotToSlot(i, hotbarSlot);

                // 随机决定是否立即停止或继续寻找更多药水
                if (random.nextBoolean()) {
                    return;
                }
            }
        }
    }
}
