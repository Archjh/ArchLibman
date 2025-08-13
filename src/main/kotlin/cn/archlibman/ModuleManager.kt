package cn.archlibman

import cn.archlibman.event.events.DrawEvent
import cn.archlibman.modules.*
import cn.archlibman.value.AbstractSetting
import java.util.concurrent.CopyOnWriteArrayList

object ModuleManager {
    var modules = CopyOnWriteArrayList<Module>()

    init {
        addModule(Sprint)
        addModule(Watermark)
        addModule(ClickGui)
        addModule(ArrayList)
        addModule(NoClickDelay)
        addModule(ArmorHUD)
        addModule(PotionHUD)
        addModule(KeyStrokes)
        addModule(TargetHUD)
        addModule(FPSDisplay)
        addModule(NoHurtCam)
        addModule(NoMiningWhileDrinking)
        addModule(ToggleNotifications)
        addModule(ChatUI.apply { defaultEnabled = true }) // Set as default enabled
    }

    fun addModule(module: Module) {
        module::class.java.declaredFields.forEach {
            it.isAccessible = true
            val obj = it.get(module)
            if (obj is AbstractSetting<*>) {
                module.settings.add(obj)
            }
        }

        // Create wrapper class that extends Module
        val wrappedModule = object : Module(module.name, module.description, module.category) {
            override fun onEnable() {
                module.onEnable()
                if (module != ToggleNotifications) {
                    ToggleNotifications.addNotification(module.name, true)
                }
            }

            override fun onDisable() {
                module.onDisable()
                if (module != ToggleNotifications) {
                    ToggleNotifications.addNotification(module.name, false)
                }
            }

            // Delegate all other properties and methods to the original module
            override var enabled: Boolean
                get() = module.enabled
                set(value) { module.enabled = value }

            override var defaultEnabled: Boolean
                get() = module.defaultEnabled
                set(value) { module.defaultEnabled = value }

            override val settings get() = module.settings
            override var key: Int
                get() = module.key
                set(value) { module.key = value }

            override fun onTick() = module.onTick()
            override fun onDraw(event: DrawEvent) = module.onDraw(event)
        }

        modules.add(wrappedModule)
        
        // Enable the module if it's marked as default enabled
        if (wrappedModule.defaultEnabled) {
            wrappedModule.enable()
        }
    }
}
