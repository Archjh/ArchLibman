package cn.archlibman.modules

import cn.archlibman.Archlibman
import cn.archlibman.Category
import cn.archlibman.Module
import cn.archlibman.event.events.DrawEvent
import org.lwjgl.glfw.GLFW

object Watermark : Module("Watermark", "...", Category.CLIENT) {
    init {
        this.key = GLFW.GLFW_KEY_J
    }

    override fun onDraw(event: DrawEvent) {
        event.context.drawText(mc.textRenderer,"${Archlibman::class.java.simpleName}v0.1",100,100,-1, true)
    }
}