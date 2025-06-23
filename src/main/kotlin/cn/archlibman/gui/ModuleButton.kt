package cn.archlibman.gui

import cn.archlibman.Module
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import java.awt.Color

class ModuleButton(
    var x: Int,
    var y: Int,
    var width: Int = 200,
    var height: Int = 25,
    val module: Module,
    val categoryId: Int
) {
    fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        // 只渲染在可视区域内的按钮
        val visibleArea = MinecraftClient.getInstance().window.scaledHeight / 2
        if (y + height >= ClickGui.centerY - visibleArea && y <= ClickGui.centerY + visibleArea) {
            // 绘制按钮背景
            context.fill(x, y, x + width, y + height, Color(112, 112, 112).rgb)

            // 绘制开关状态
            val toggleColor = if (module.enabled) {
                Color(131, 255, 92).rgb
            } else {
                Color(255, 64, 59).rgb
            }
            context.fill(
                x + width - 28, y + height - 18,
                x + width - 5, y + height - 7,
                toggleColor
            )

            // 绘制模块名称
            context.drawText(
                MinecraftClient.getInstance().textRenderer,
                module.name,
                x + 8, y + 8,
                Color(255, 204, 0).rgb,
                true
            )
        }
    }

    fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height) {
            when (button) {
                0 -> module.toggle() // 左键切换
                1 -> ModSettingManager.currentModule = module // 右键打开设置
            }
            return true
        }
        return false
    }

    fun mouseReleased(mouseX: Double, mouseY: Double, button: Int): Boolean {
        return false
    }
}