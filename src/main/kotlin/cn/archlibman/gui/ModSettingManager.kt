package cn.archlibman.gui

import cn.archlibman.Module
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import java.awt.Color

object ModSettingManager {
    var currentModule: Module? = null

    fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        currentModule ?: return

        // 绘制设置面板背景
        context.fill(
            ClickGui.centerX + 205, ClickGui.centerY - 100,
            ClickGui.centerX + 405, ClickGui.centerY + 100,
            Color(245, 242, 233).rgb
        )

        // 绘制标题栏
        context.fill(
            ClickGui.centerX + 205, ClickGui.centerY - 100,
            ClickGui.centerX + 405, ClickGui.centerY - 90,
            Color(138, 66, 88).rgb
        )

        // 绘制模块名称和状态
        context.drawText(
            MinecraftClient.getInstance().textRenderer,
            "${currentModule!!.name} : ${currentModule!!.enabled}",
            ClickGui.centerX + 208, ClickGui.centerY - 87,
            Color(255, 204, 0).rgb,
            true
        )

        // 绘制模块描述
        context.drawText(
            MinecraftClient.getInstance().textRenderer,
            currentModule!!.description,
            ClickGui.centerX + 208, ClickGui.centerY - 77,
            Color(255, 204, 0).rgb,
            true
        )

        // 渲染设置组件...
    }
}