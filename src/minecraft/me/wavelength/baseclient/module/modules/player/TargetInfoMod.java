package me.wavelength.baseclient.module.modules.player;

import me.wavelength.baseclient.event.events.Render2DEvent;
import me.wavelength.baseclient.module.AntiCheat;
import me.wavelength.baseclient.module.Category;
import me.wavelength.baseclient.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class TargetInfoMod extends Module {
    public TargetInfoMod() {
        super("TargetInfoMod", "Displays targeted entity's head and info", Keyboard.KEY_F, Category.PLAYER, AntiCheat.VANILLA, AntiCheat.AAC);
    }

    @Override
    public void onRender2D(Render2DEvent event) {
        Entity entityHit = Minecraft.getMinecraft().objectMouseOver.entityHit;
        if (entityHit instanceof EntityLivingBase) {
            EntityLivingBase entity = (EntityLivingBase) entityHit;

            // 绘制背景
            drawBackground(100, 80, 120, 50);

            // 绘制实体头部 (缩小尺寸)
            GlStateManager.pushMatrix();
            GlStateManager.translate(105, 90, 0);
            GlStateManager.scale(0.8F, 0.8F, 0.8F); // 缩小头部尺寸
            GuiInventory.drawEntityOnScreen(0, 0, 20, 0, 0, entity);
            GlStateManager.popMatrix();

            // 绘制信息 (头部右侧)
            int textX = 125;
            int textY = 85;
            int textColor = 0xFFFFFFFF;

            // 名称
            Minecraft.getMinecraft().fontRendererObj.drawString(entity.getName(), textX, textY, textColor);

            // 生命值 (带颜色变化)
            float healthPercent = entity.getHealth() / entity.getMaxHealth();
            int healthColor = getHealthColor(healthPercent);
            Minecraft.getMinecraft().fontRendererObj.drawString(
                    String.format("HP: %.1f/%.1f", entity.getHealth(), entity.getMaxHealth()),
                    textX, textY + 10, healthColor);

            // 距离
            double distance = Minecraft.getMinecraft().thePlayer.getDistanceToEntity(entity);
            Minecraft.getMinecraft().fontRendererObj.drawString(
                    String.format("距离: %.1f", distance),
                    textX, textY + 20, textColor);
        }
    }

    // 绘制半透明背景
    private void drawBackground(int x, int y, int width, int height) {
        Gui.drawRect(x, y, x + width, y + height, 0x80000000); // 暗灰色半透明背景
        Gui.drawRect(x, y, x + width, y + 1, 0xFF555555); // 顶部边框
        Gui.drawRect(x, y + height - 1, x + width, y + height, 0xFF555555); // 底部边框
        Gui.drawRect(x, y, x + 1, y + height, 0xFF555555); // 左侧边框
        Gui.drawRect(x + width - 1, y, x + width, y + height, 0xFF555555); // 右侧边框
    }

    // 根据生命值百分比获取颜色 (从绿到红)
    private int getHealthColor(float percent) {
        if (percent > 0.6F) return 0xFF00FF00; // 绿色
        if (percent > 0.3F) return 0xFFFFFF00; // 黄色
        return 0xFFFF0000; // 红色
    }
}