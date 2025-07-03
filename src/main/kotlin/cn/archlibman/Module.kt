package cn.archlibman

import cn.archlibman.event.events.DrawEvent
import cn.archlibman.value.AbstractSetting
import net.minecraft.client.MinecraftClient
import java.util.concurrent.CopyOnWriteArrayList

open class Module(var name: String,var description: String,var category: Category) {

    open var enabled = false
    open val settings = CopyOnWriteArrayList<AbstractSetting<*>>()
    open var key = -1
    open val mc = MinecraftClient.getInstance()

    open fun onTick() {

    }

    open fun onEnable() {}
    open fun onDisable() {}
    open fun onDraw(event: DrawEvent) {}
    fun enable() {
        this.enabled = true
        onEnable()
    }

    fun disable() {
        this.enabled = false
        onDisable()
    }
    fun toggle() {
        if (enabled) {
            disable()
        }else {
            enable()
        }
    }
}