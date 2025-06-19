package me.wavelength.baseclient.event.events;

import me.wavelength.baseclient.event.Event;
import net.minecraft.block.BlockSourceImpl;
import net.minecraft.client.gui.Gui;

import static org.lwjgl.input.Mouse.getX;
import static org.lwjgl.input.Mouse.getY;

public class Render2DEvent extends Event {
	private int width;
	private int height;

	public Render2DEvent(int width, int height) {
		this.width = width;
		this.height = height;
		// 在渲染方法中尝试固定位置测试
		int testX = 10;
		int testY = 10;
		Gui.drawRect(testX + getX(), testY + getY(), testX + getX() + getWidth(), testY +  getY() + getHeight(), 225);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

    public int getAbsoluteX() {
        return 0;
    }

	public int getAbsoluteY() {
		return 0;
	}
}