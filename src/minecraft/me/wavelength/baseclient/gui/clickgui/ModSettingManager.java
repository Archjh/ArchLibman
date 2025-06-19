package me.wavelength.baseclient.gui.clickgui;

import me.wavelength.baseclient.module.Module;
import me.wavelength.baseclient.utils.RoundedUtil;
import net.minecraft.client.Minecraft;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ModSettingManager {
    public static Module module;
    private static Module currentModule = null;
    private int x, y;
    private List<SettingComponent> settingComponents = new ArrayList<>();

    public ModSettingManager(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static void setCurrentModule(Module module) {
        currentModule = module;
    }

    public static Module getCurrentModule() {
        return currentModule;
    }

    public void render() {
        if (currentModule == null) return;

        // 绘制设置面板背景
        RoundedUtil.drawRoundedRect(x + 205, y - 100, x + 405, y + 100, 8, new Color(245, 242, 233, 255).getRGB());

        // 绘制标题栏
        RoundedUtil.drawRoundedRect(x + 205, y - 100, x + 405, y - 90, 8, new Color(138, 66, 88, 255).getRGB());

        // 绘制模块名称和状态
        Minecraft.getMinecraft().fontRendererObj.drawString(
                currentModule.getName() + " : " + currentModule.isToggled(),
                x + 208, y - 87, new Color(255, 204, 0, 255).getRGB()
        );

        // 绘制模块描述
        Minecraft.getMinecraft().fontRendererObj.drawString(
                currentModule.getDescription(),
                x + 208, y - 77, new Color(255, 204, 0, 255).getRGB()
        );

        // 渲染所有设置组件
        for (SettingComponent component : settingComponents) {
            component.render(x + 210, y - 70);
        }
    }

    public void addSettingComponent(SettingComponent component) {
        settingComponents.add(component);
    }

    public void clearSettingComponents() {
        settingComponents.clear();
    }

    public abstract static class SettingComponent {
        protected int offsetY;

        public abstract void render(int x, int y);

        public int getHeight() {
            return 20; // 默认高度
        }
    }

    public static class BooleanSetting extends SettingComponent {
        private String name;
        private boolean value;

        public BooleanSetting(String name, boolean defaultValue) {
            this.name = name;
            this.value = defaultValue;
        }

        @Override
        public void render(int x, int y) {
            y += offsetY;

            // 绘制设置名称
            Minecraft.getMinecraft().fontRendererObj.drawString(name, x, y + 4, new Color(255, 204, 0, 255).getRGB());

            // 绘制开关
            int toggleX = x + 150;
            int toggleColor = value ? new Color(131, 255, 92, 255).getRGB() : new Color(255, 64, 59, 255).getRGB();
            RoundedUtil.drawRoundedRect(toggleX, y, toggleX + 40, y + 15, 5, toggleColor);
        }

        @Override
        public int getHeight() {
            return 20;
        }
    }
}
