package me.wavelength.baseclient.gui.clickgui;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import me.wavelength.baseclient.BaseClient;
import me.wavelength.baseclient.module.Category;
import me.wavelength.baseclient.module.Module;
import me.wavelength.baseclient.utils.RoundedUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Input;
import org.newdawn.slick.opengl.renderer.SGL;

/* loaded from: ArchLibman.jar:me/wavelength/baseclient/gui/clickgui/ClickGui.class */
public class ClickGui extends GuiScreen {
	public static ArrayList<CategoryButton> categoryButtons = new ArrayList<>();
	public static ArrayList<ModuleButton> moduleButtons = new ArrayList<>();
	ScaledResolution sr;
	private ModSettingManager msManager;
	int backgroundWidth = Input.KEY_UP;
	int centerWidth;
	static int centerHeight;

	@Override // net.minecraft.client.gui.GuiScreen
	public void initGui() {
		this.mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/menu_blur.json"));
		this.sr = new ScaledResolution(this.mc);
		this.centerWidth = this.sr.getScaledWidth() / 2;
		this.centerHeight = this.sr.getScaledHeight() / 2;
		resetButtons();
		categoryButtons.add(new CategoryButton(this.centerWidth - Input.KEY_UP, this.centerHeight - 65, 100, 20, "Combat", 0));
		categoryButtons.add(new CategoryButton(this.centerWidth - Input.KEY_UP, this.centerHeight - 45, 100, 20, "Movement", 1));
		categoryButtons.add(new CategoryButton(this.centerWidth - Input.KEY_UP, this.centerHeight - 25, 100, 20, "Render", 2));
		categoryButtons.add(new CategoryButton(this.centerWidth - Input.KEY_UP, this.centerHeight - 5, 100, 20, "Player", 3));
		categoryButtons.add(new CategoryButton(this.centerWidth - Input.KEY_UP, this.centerHeight + 15, 100, 20, "World", 4));
		moduleButtons.clear();
		int yOffset = 0;
		for (Module module : BaseClient.instance.getModuleManager().getModules()) {
			int categoryId = getCategoryId(module.getCategory());
			if (categoryId != -1) {
				moduleButtons.add(new ModuleButton(this.centerWidth, this.centerHeight + yOffset, Input.KEY_UP, 25, module, categoryId));
				yOffset += 26;
			}
		}
		this.msManager = new ModSettingManager(this.centerWidth, this.centerHeight);
	}

	private int getCategoryId(Category category) {
		switch (category) {
			case COMBAT:
				return 0;
			case MOVEMENT:
				return 1;
			case RENDER:
				return 2;
			case PLAYER:
				return 3;
			case WORLD:
				return 4;
			default:
				return -1;
		}
	}

	private void resetButtons() {
		categoryButtons.clear();
		moduleButtons.clear();
	}

	@Override // net.minecraft.client.gui.GuiScreen
	public void onGuiClosed() {
		this.mc.entityRenderer.loadEntityShader(null);
		super.onGuiClosed();
	}

	@Override // net.minecraft.client.gui.GuiScreen
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.centerWidth = this.sr.getScaledWidth() / 2;
		this.centerHeight = this.sr.getScaledHeight() / 2;
		GlStateManager.pushAttrib();
		GlStateManager.pushMatrix();
		RoundedUtil.drawRoundedRect(this.centerWidth - this.backgroundWidth, this.centerHeight - 100, this.centerWidth + this.backgroundWidth, this.centerHeight + 100, 8.0f, new Color(0, 0, 0, 45).getRGB());
		RoundedUtil.drawRoundedRect((this.centerWidth - this.backgroundWidth) + 390, this.centerHeight - 100, this.centerWidth + this.backgroundWidth, this.centerHeight + 100, 8.0f, new Color(122, 255, 0, 255).getRGB());
		GlStateManager.popMatrix();
		this.msManager.render();
		Iterator<CategoryButton> it = categoryButtons.iterator();
		while (it.hasNext()) {
			it.next().render(mouseX, mouseY);
		}
		int wheel = Mouse.getDWheel();
		GL11.glEnable(SGL.GL_SCISSOR_TEST);
		glScissor(this.centerWidth - this.backgroundWidth, this.centerHeight - 90, this.centerWidth + this.backgroundWidth, 180.0d);
		List<ModuleButton> currentCategoryButtons = new ArrayList<>();
		Iterator<ModuleButton> it2 = moduleButtons.iterator();
		while (it2.hasNext()) {
			ModuleButton button = it2.next();
			if (button.getCategoryId() == CategoryManager.getInstance().getCurrentPage()) {
				currentCategoryButtons.add(button);
			}
		}
		if (wheel != 0) {
			for (ModuleButton button2 : currentCategoryButtons) {
				button2.setY(button2.getY() + (wheel > 0 ? 10 : -10));
			}
		}
		for (int i = 0; i < currentCategoryButtons.size(); i++) {
			ModuleButton button3 = currentCategoryButtons.get(i);
			button3.setY((this.centerHeight - 80) + (i * 26));
			button3.render();
		}
		GL11.glDisable(SGL.GL_SCISSOR_TEST);
		GlStateManager.popAttrib();
	}

	@Override // net.minecraft.client.gui.GuiScreen
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		if (mouseX >= this.centerWidth - this.backgroundWidth && mouseX <= this.centerWidth + this.backgroundWidth && mouseY >= this.centerHeight - 90 && mouseY <= this.centerHeight + 90) {
			Iterator<ModuleButton> it = moduleButtons.iterator();
			while (it.hasNext()) {
				ModuleButton button = it.next();
				if (button.getCategoryId() == CategoryManager.currentPage) {
					button.onClick(mouseX, mouseY, mouseButton);
				}
			}
		}
		Iterator<CategoryButton> it2 = categoryButtons.iterator();
		while (it2.hasNext()) {
			it2.next().onClick(mouseX, mouseY, mouseButton);
		}
	}

	private void glScissor(double x, double y, double width, double height) {
		ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
		Minecraft mc = Minecraft.getMinecraft();
		GL11.glScissor((int) ((x * mc.displayWidth) / scaledResolution.getScaledWidth()), (int) (((scaledResolution.getScaledHeight() - (y + height)) * mc.displayHeight) / scaledResolution.getScaledHeight()), (int) ((width * mc.displayWidth) / scaledResolution.getScaledWidth()), (int) ((height * mc.displayHeight) / scaledResolution.getScaledHeight()));
	}
}