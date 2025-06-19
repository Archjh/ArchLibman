package me.wavelength.baseclient.gui.clickgui;

import me.wavelength.baseclient.utils.RoundedUtil;
import net.minecraft.client.Minecraft;

import java.awt.*;

public class CategoryButton {
    private int x, y, width, height;
    private String text;
    private int id;
    private boolean hovered;
    private boolean active;
    private float animationProgress;

    public CategoryButton(int x, int y, int width, int height, String text, int id) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;
        this.id = id;
        this.animationProgress = 0.0f;
    }

    // 保留这个完整的方法
    public void render(int mouseX, int mouseY) {
        hovered = isMouseOver(mouseX, mouseY);
        active = CategoryManager.getInstance().getCurrentPage() == id;

        // 更新动画进度
        if (active) {
            animationProgress = Math.min(1.0f, animationProgress + 0.1f);
        } else {
            animationProgress = Math.max(0.0f, animationProgress - 0.1f);
        }

        // 绘制背景
        if (animationProgress > 0) {
            int bgColor = new Color(191, 226, 246, (int) (255 * animationProgress)).getRGB();
            RoundedUtil.drawRoundedRect(x, y, x + (int) (width * animationProgress), y + height, 5, bgColor);
        }

        // 绘制文本
        int textColor = new Color(255, 204, 0, 255).getRGB();
        Minecraft.getMinecraft().fontRendererObj.drawString(
                text,
                x + width / 2 - Minecraft.getMinecraft().fontRendererObj.getStringWidth(text) / 2,
                y + height / 2 - Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT / 2,
                textColor
        );
    }


    public void onClick(int mouseX, int mouseY, int mouseButton) {
        if (isMouseOver(mouseX, mouseY)) {
            CategoryManager.getInstance().setCurrentPage(id);
        }
    }

    public boolean isMouseOver(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public String getText() { return text; }
    public int getId() { return id; }
}
