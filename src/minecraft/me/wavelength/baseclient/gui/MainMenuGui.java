package me.wavelength.baseclient.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

import static java.awt.SystemColor.text;

public class MainMenuGui extends GuiScreen {

    @Override
    public void initGui() {
        int j = this.height / 4 + 48;
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, j + 72 + 12, 98, 20, I18n.format("menu.options", new Object[0])));
        this.buttonList.add(new GuiButton(4, this.width / 2 + 2, j + 72 + 12, 98, 20, I18n.format("menu.quit", new Object[0])));
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, j, I18n.format("menu.singleplayer", new Object[0])));
        this.buttonList.add(new GuiButton(2, this.width / 2 - 100, j + 24, I18n.format("menu.multiplayer", new Object[0])));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawBackground(0);
        mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/2qer2354.png"));
        Gui.drawModalRectWithCustomSizedTexture(0, 0, 0, 0, width, height, width, height);
        FontRenderer font = mc.fontRendererObj;
        font.drawString(String.valueOf(text), width / 2 - font.getStringWidth(String.valueOf(text)) / 2, height / 2 - font.FONT_HEIGHT / 2 - 50, 0xFFFFFFFF);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 0) {
            this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
        }

        if (button.id == 4) {
            this.mc.shutdown();
        }

        if (button.id == 1) {
            this.mc.displayGuiScreen(new GuiSelectWorld(this));
        }

        if (button.id == 2) {
            this.mc.displayGuiScreen(new GuiMultiplayer(this));
        }
    }
}
