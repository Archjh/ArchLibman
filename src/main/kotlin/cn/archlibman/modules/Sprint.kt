package cn.archlibman.modules

import cn.archlibman.Category
import cn.archlibman.Module
import cn.archlibman.value.BooleanSetting
import org.lwjgl.glfw.GLFW

object Sprint : Module("Sprint", "...", Category.MOVEMENT) {
    val test = BooleanSetting("test","sb.",false)
    init {
        this.enabled = true
        this.key = GLFW.GLFW_KEY_I
    }


    override fun onTick() {
        if ((mc.player != null && mc.world != null )) {
            mc.player!!.isSprinting = true
        }

    }

}