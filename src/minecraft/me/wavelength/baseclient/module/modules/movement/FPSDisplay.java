package me.wavelength.baseclient.module.modules.movement;

import me.wavelength.baseclient.event.events.Render2DEvent;
import me.wavelength.baseclient.module.AntiCheat;
import me.wavelength.baseclient.module.Category;
import me.wavelength.baseclient.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.EnumChatFormatting;

public class FPSDisplay extends Module {

    public FPSDisplay() {
        super("FPS Display", "Shows current FPS at the top center of the screen", 0, Category.MOVEMENT, AntiCheat.VANILLA);
    }

    @Override
    public void onRender2D(Render2DEvent event) {
        if (!this.isToggled())
            return;

        // 获取Minecraft实例
        Minecraft mc = Minecraft.getMinecraft();

        // 创建ScaledResolution
        ScaledResolution scaledResolution = new ScaledResolution(mc);

        // 获取FPS
        int fps = mc.getDebugFPS();

        // 获取字体渲染器
        FontRenderer fontRenderer = mc.fontRendererObj;

        // 准备显示的文本
        String displayText = EnumChatFormatting.WHITE + "FPS: " + EnumChatFormatting.GREEN + fps;

        // 计算文本居中位置并绘制
        int textWidth = fontRenderer.getStringWidth(displayText);
        int x = (scaledResolution.getScaledWidth() - textWidth) / 2;
        int y = 2; // 顶部位置

        fontRenderer.drawStringWithShadow(displayText, x, y, 0xFFFFFF);
    }
}