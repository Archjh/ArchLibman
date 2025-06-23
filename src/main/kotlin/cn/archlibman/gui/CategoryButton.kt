package cn.archlibman.gui

import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import java.awt.Color

class CategoryButton(
    var x: Int,
    var y: Int,
    var width: Int,
    var height: Int,
    var text: String,
    var id: Int
) {
    var hovered = false
    var animationProgress = 0f

    fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        hovered = isMouseOver(mouseX, mouseY)
        val active = CategoryManager.currentPage == id

        // 更新动画
        animationProgress = if (active) {
            (animationProgress + 0.1f).coerceAtMost(1f)
        } else {
            (animationProgress - 0.1f).coerceAtLeast(0f)
        }

        // 绘制背景
        if (animationProgress > 0) {
            val bgColor = Color(191, 226, 246, (255 * animationProgress).toInt()).rgb
            context.fill(x, y, x + (width * animationProgress).toInt(), y + height, bgColor)
        }

        // 绘制文本
        val textColor = Color(255, 204, 0).rgb
        context.drawCenteredTextWithShadow(
            MinecraftClient.getInstance().textRenderer,
            text,
            x + width / 2,
            y + height / 2 - 4,
            textColor
        )
    }

    fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if (isMouseOver(mouseX.toInt(), mouseY.toInt())) {
            CategoryManager.currentPage = id
            return true
        }
        return false
    }

    fun mouseReleased(mouseX: Double, mouseY: Double, button: Int): Boolean {
        return false
    }

    private fun isMouseOver(mouseX: Int, mouseY: Int): Boolean {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height
    }
}