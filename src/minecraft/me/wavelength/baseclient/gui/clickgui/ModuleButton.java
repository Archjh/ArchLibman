package me.wavelength.baseclient.gui.clickgui;

import java.awt.Color;
import me.wavelength.baseclient.module.Module;
import me.wavelength.baseclient.utils.RoundedUtil;
import net.minecraft.client.Minecraft;

public class ModuleButton {

	private int x, y, width, height;
	private Module module;
	private int categoryId;

	public ModuleButton(int x, int y, int width, int height, Module module, int categoryId) {
		this.x = x - (width / 2); // 居中显示
		this.y = y;
		this.width = width;
		this.height = height;
		this.module = module;
		this.categoryId = categoryId;
	}

	public void render() {
		// 获取可视区域边界
		int visibleTop = ClickGui.centerHeight - 90;
		int visibleBottom = ClickGui.centerHeight + 90;

		// 只渲染在可视区域内的按钮
		if (y + height >= visibleTop && y <= visibleBottom) {
			// 使用圆角矩形绘制模块按钮
			RoundedUtil.drawRoundedRect(x, y, x + width, y + height, 8, new Color(112, 112, 112, 255).getRGB());

			// 绘制开关状态指示器
			RoundedUtil.drawRoundedRect((x + width) - 28, (y + height) - 18,
					(x + width) - 5, (y + height) - 7,
					8, getToggleColor());

			// 绘制模块名称
			Minecraft.getMinecraft().fontRendererObj.drawString(module.getName(), x + 8, y + 8, new Color(255, 204, 0, 255).getRGB());
		}
	}

	private int getToggleColor() {
		return module.isToggled() ?
				new Color(131, 255, 92, 255).getRGB() : // 启用状态颜色
				new Color(255, 64, 59, 255).getRGB();  // 禁用状态颜色
	}

	public void onClick(int mouseX, int mouseY, int button) {
		if(mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height) {
			if(button == 0) { // 左键点击切换模块
				module.toggle();
			} else if(button == 1) { // 右键点击打开设置
				ModSettingManager.module = module;
			}
		}
	}

	// Getter和Setter方法...
	public int getCategoryId() { return categoryId; }
	public int getY() { return y; }
	public void setY(int y) { this.y = y; }
}