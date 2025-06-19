package me.wavelength.baseclient.module.modules.render;

import me.wavelength.baseclient.event.events.Render2DEvent;
import me.wavelength.baseclient.module.AntiCheat;
import me.wavelength.baseclient.module.Category;
import me.wavelength.baseclient.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

public class ModArmorHUD extends Module {
    public ModArmorHUD() {
        super("ModArmorHUD", "ModArmorHUD", 33, Category.PLAYER, AntiCheat.VANILLA, AntiCheat.AAC);
    }

    @Override // me.wavelength.baseclient.event.EventListener
    public void onRender2D(Render2DEvent pos) {
        int itemY = this.mc.displayHeight / 2;
        for (int i = 0; i < 4; i++) {
            ItemStack itemStack = this.mc.thePlayer.inventory.armorInventory[i];
            if (itemStack != null) {
                itemY -= 20;
                renderItem(pos, 10, itemY - 100, itemStack);
            }
        }
    }

    private void renderItem(Render2DEvent pos, int xPos, int yPos, ItemStack itemStack) {
        if (itemStack == null) {
            return;
        }
        GL11.glPushMatrix();
        if (itemStack.getItem().isDamageable()) {
            double damage = ((itemStack.getMaxDamage() - itemStack.getItemDamage()) / itemStack.getMaxDamage()) * 100.0d;
            Minecraft.getMinecraft().fontRendererObj.drawString(String.format("%.2f%%", Double.valueOf(damage)), xPos + 15, yPos + 5, -1);
        }
        RenderHelper.enableGUIStandardItemLighting();
        this.mc.getRenderItem().renderItemAndEffectIntoGUI(itemStack, xPos, yPos);
        GL11.glPopMatrix();
    }
}


