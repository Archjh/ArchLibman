package cn.archlibman.modules

import cn.archlibman.Archlibman
import cn.archlibman.Archlibman.clickGui
import cn.archlibman.Category
import cn.archlibman.Module
import org.lwjgl.glfw.GLFW

object ClickGui : Module("ClickGui", "Open GUI", Category.CLIENT) {
    init {
        // 绑定到右 Shift (GLFW.GLFW_KEY_RIGHT_SHIFT)
        this.key = GLFW.GLFW_KEY_RIGHT_SHIFT
    }

    override fun onEnable() {
        mc.setScreen(Archlibman.clickGui) // 确保调用的是 Archlibman.clickGui
    }

    override fun onDisable() {
        // 关闭 GUI 时自动禁用模块（可选）
        mc.setScreen(null)
    }
}