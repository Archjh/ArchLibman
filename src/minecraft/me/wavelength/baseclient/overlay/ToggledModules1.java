package me.wavelength.baseclient.overlay;

import java.awt.Color;
import java.util.List;

import me.wavelength.baseclient.BaseClient;
import me.wavelength.baseclient.event.EventListener;
import me.wavelength.baseclient.event.events.Render2DEvent;
import me.wavelength.baseclient.module.Category;
import me.wavelength.baseclient.module.Module;
import me.wavelength.baseclient.utils.RenderUtils;
import me.wavelength.baseclient.utils.Strings;

public class ToggledModules1 extends EventListener {

	// 统一颜色设置
	private static final Color BACKGROUND_COLOR = new Color(20, 20, 20, 180); // 半透明深灰背景
	private static final Color TEXT_START_COLOR = new Color(0, 255, 255);    // 蓝绿色 (渐变起始)
	private static final Color TEXT_END_COLOR = new Color(0, 100, 255);      // 蓝色 (渐变结束)
	private static final Color DOT_COLOR = new Color(200, 200, 200, 200);    // 圆点颜色
	private static final int SCREEN_MARGIN = 5;                              // 屏幕边缘间距
	private static final int CORNER_RADIUS = 5;                              // 更大的圆角半径
	private static final int MODULE_SPACING = 2;                             // 模块间间距
	private static final int DOT_SIZE = 3;                                   // 圆点大小
	private static final int DOT_MARGIN = 3;                                 // 圆点与文本间距

	public ToggledModules1() {
		BaseClient.instance.getEventManager().registerListener(this);
	}

	@Override
	public void onRender2D(Render2DEvent event) {
		List<Module> modules = BaseClient.instance.getModuleManager().getToggledModules();

		modules.sort((module1, module2) -> Strings.getStringWidthCFR(Strings.capitalizeFirstLetter(module2.getNameWithAntiCheat())) - Strings.getStringWidthCFR(Strings.capitalizeFirstLetter(module1.getNameWithAntiCheat())));

		int y = SCREEN_MARGIN; // 从屏幕边缘留出间距
		int relativeYOffset = 3;
		int offset = BaseClient.instance.getFontRenderer().getFontSize() / 2 + relativeYOffset;

		for (int i = 0; i < modules.size(); i++) {
			Module module = modules.get(i);
			if (module.getCategory().equals(Category.HIDDEN) || !(module.isShownInModuleArrayList()))
				continue;

			String s = Strings.capitalizeFirstLetter(module.getNameWithAntiCheat());
			int mWidth = Strings.getStringWidthCFR(s);

			// 计算位置 (从屏幕边缘留出间距)
			int xPos = event.getWidth() - mWidth - SCREEN_MARGIN - relativeYOffset * 2;
			int yPos = y;
			int endX = event.getWidth() - SCREEN_MARGIN; // 右侧也留出间距
			int endY = y + offset;

			/** 绘制半透明圆角背景 */
			RenderUtils.drawRoundedRect(xPos, yPos, endX, endY, CORNER_RADIUS, BACKGROUND_COLOR.getRGB());

			/** 计算渐变颜色 */
			float progress = (float) i / (modules.size() - 1);
			int red = (int) (TEXT_START_COLOR.getRed() + (TEXT_END_COLOR.getRed() - TEXT_START_COLOR.getRed()) * progress);
			int green = (int) (TEXT_START_COLOR.getGreen() + (TEXT_END_COLOR.getGreen() - TEXT_START_COLOR.getGreen()) * progress);
			int blue = (int) (TEXT_START_COLOR.getBlue() + (TEXT_END_COLOR.getBlue() - TEXT_START_COLOR.getBlue()) * progress);
			Color gradientColor = new Color(red, green, blue);

			/** 绘制模块名称 (使用渐变颜色) */
			RenderUtils.drawStringWithShadow(s, xPos + relativeYOffset, y, gradientColor.getRGB());

			/** 绘制右侧圆点 */
			int dotX = endX - DOT_MARGIN - DOT_SIZE;
			int dotY = yPos + (endY - yPos) / 2;
			RenderUtils.drawFilledCircle(dotX, dotY, DOT_SIZE, DOT_COLOR.getRGB());

			y += offset + MODULE_SPACING; // 增加模块间间距
		}
	}
}