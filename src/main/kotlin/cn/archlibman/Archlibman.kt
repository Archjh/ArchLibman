package cn.archlibman

import cn.archlibman.command.CommandModuleManager
import cn.archlibman.event.EventManager
import cn.archlibman.gui.ClickGui
import cn.archlibman.modules.ChatUI
import net.fabricmc.api.ModInitializer
import org.slf4j.LoggerFactory

object Archlibman : ModInitializer {
    private val logger = LoggerFactory.getLogger("archlibman")
    lateinit var clickGui: ClickGui

    override fun onInitialize() {
        logger.info("ArchLibman Initializing...")
        EventManager.init()
        ModuleManager
        
        // Ensure these modules are enabled
        ModuleManager.modules.find { it.name == "ChatUI" }?.enable()
        ModuleManager.modules.find { it.name == "ToggleNotifications" }?.enable()
        
        clickGui = ClickGui()
        CommandModuleManager.register()
    }
}
