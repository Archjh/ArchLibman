package cn.archlibman.modules

import cn.archlibman.Category
import cn.archlibman.Module
import cn.archlibman.ModuleManager
import cn.archlibman.event.events.DrawEvent
import net.minecraft.client.MinecraftClient
import net.minecraft.client.util.Window
import org.lwjgl.glfw.GLFW

object ArrayList: Module("ArrayList", "Displays enabled modules", Category.RENDER) {
    init {
        key = GLFW.GLFW_KEY_X
        // 确保模块默认启用
        enabled = true
    }

    override fun onDraw(event: DrawEvent) {
        if (!enabled) return

        val mc = MinecraftClient.getInstance()
        val window: Window = mc.window
        val screenWidth = window.scaledWidth
        var y = 20 // 从屏幕顶部开始，留出一些空间
        val rightMargin = 5 // 右侧留出5像素的边距
        var x = screenWidth - rightMargin // 初始x坐标设为屏幕宽度减去右边距

        // 按字母顺序排序模块
        val sortedModules = ModuleManager.modules
            .filter { it.enabled && it != this } // 过滤出启用的模块，排除自己
            .sortedBy { it.name }

        // 先计算最大宽度以确定背景和文本位置
        val maxWidth = if (sortedModules.isNotEmpty()) {
            sortedModules.maxOf { mc.textRenderer.getWidth(it.name) }
        } else {
            0
        }

        // 调整x坐标减去最大文本宽度
        x -= maxWidth

        // 绘制背景（可选）
        if (sortedModules.isNotEmpty()) {
            val textHeight = mc.textRenderer.fontHeight + 2
            val totalHeight = sortedModules.size * textHeight
            event.context.fill(
                x - 2, y - 2,
                x + maxWidth + 2,
                y + totalHeight,
                0x80000000.toInt() // 半透明黑色背景
            )
        }

        // 绘制模块列表
        sortedModules.forEach { module ->
            event.context.drawTextWithShadow(
                mc.textRenderer,
                module.name,
                x, y,
                -1 // 白色文本
            )
            y += mc.textRenderer.fontHeight + 2
        }
    }
}