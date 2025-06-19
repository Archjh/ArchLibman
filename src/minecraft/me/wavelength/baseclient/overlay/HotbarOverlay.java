package me.wavelength.baseclient.overlay;

import java.awt.Color;
import me.wavelength.baseclient.BaseClient;
import me.wavelength.baseclient.event.EventListener;
import me.wavelength.baseclient.event.events.Render2DEvent;
import me.wavelength.baseclient.gui.clickgui.ClickGui;
import me.wavelength.baseclient.gui.clickgui.GuiBind;
import me.wavelength.baseclient.utils.RenderUtils;
import me.wavelength.baseclient.utils.Time;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

/* loaded from: ArchLibman.jar:me/wavelength/baseclient/overlay/HotbarOverlay.class */
public class HotbarOverlay extends EventListener {
	public HotbarOverlay() {
		BaseClient.instance.getEventManager().registerListener(this);
	}

	@Override // me.wavelength.baseclient.event.EventListener
	public void onRender2D(Render2DEvent event) {
		if (BaseClient.instance.isDefaultHotbar()) {
			return;
		}
		RenderUtils.drawModalRectFromRight(5, 0, 5, 21, Color.RED.getRGB());
		RenderUtils.drawStringFromBottomRight(Time.getTime(System.currentTimeMillis(), "HH:mm:ss"), 9, 10, Color.YELLOW.getRGB());
		GuiScreen currentScreen = this.mc.currentScreen;
		if (currentScreen != null && !(currentScreen instanceof ClickGui) && !(currentScreen instanceof GuiBind)) {
			return;
		}
		//renderText(event, "&7[&dFPS&7]&5 " + Minecraft.getDebugFPS(), 2);
	}

	private void renderText(Render2DEvent event, String text, int x) {
		RenderUtils.drawStringFromBottomLeft(text, x, 10, -1);
	}
}