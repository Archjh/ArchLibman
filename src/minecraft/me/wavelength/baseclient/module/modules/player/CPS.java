package me.wavelength.baseclient.module.modules.player;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import me.wavelength.baseclient.event.events.Render2DEvent;
import me.wavelength.baseclient.module.AntiCheat;
import me.wavelength.baseclient.module.Category;
import me.wavelength.baseclient.module.Module;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Mouse;

public class CPS extends Module {
    private boolean wasLeftPressed;
    private boolean wasRightPressed;
    private long lastLeftClick;
    private long lastRightClick;
    private List<Long> leftClicks = new ArrayList<>();
    private List<Long> rightClicks = new ArrayList<>();

    public CPS() {
        super("CPS", "CPS", 33, Category.PLAYER, AntiCheat.VANILLA, AntiCheat.AAC);
    }

    @Override
    public int getWidth() {
        return Minecraft.getMinecraft().fontRendererObj.getStringWidth(getLeftCPS() + " : " + getRightCPS());
    }

    @Override
    public int getHeight() {
        return Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT;
    }

    @Override
    public void onRender2D(Render2DEvent pos) {
        // 处理左键点击
        boolean leftPressed = Mouse.isButtonDown(0);
        if (leftPressed != this.wasLeftPressed) {
            this.lastLeftClick = System.currentTimeMillis();
            this.wasLeftPressed = leftPressed;
            if (leftPressed) {
                leftClicks.add(this.lastLeftClick);
            }
        }

        // 处理右键点击
        boolean rightPressed = Mouse.isButtonDown(1);
        if (rightPressed != this.wasRightPressed) {
            this.lastRightClick = System.currentTimeMillis();
            this.wasRightPressed = rightPressed;
            if (rightPressed) {
                rightClicks.add(this.lastRightClick);
            }
        }

        // 渲染CPS
        String cpsText = getLeftCPS() + " : " + getRightCPS();
        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(
                cpsText,
                pos.getAbsoluteX(),
                pos.getAbsoluteY(),
                Color.WHITE.getRGB()
        );
    }

    public int getLeftCPS() {
        long currentTime = System.currentTimeMillis();
        leftClicks.removeIf(clickTime -> clickTime + 1000 < currentTime);
        return leftClicks.size();
    }

    public int getRightCPS() {
        long currentTime = System.currentTimeMillis();
        rightClicks.removeIf(clickTime -> clickTime + 1000 < currentTime);
        return rightClicks.size();
    }

    @Override
    public void render(Render2DEvent pos) {
        onRender2D(pos);
    }
}
