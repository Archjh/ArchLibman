package cn.archlibman

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
    }

    fun addModule(module: Module) {
        module::class.java.declaredFields.forEach {
            it.isAccessible = true
            val obj = it.get(module)
            if(obj is AbstractSetting<*>) {
                module.settings.add(obj)
            }
        }
        modules.add(module)
    }
}