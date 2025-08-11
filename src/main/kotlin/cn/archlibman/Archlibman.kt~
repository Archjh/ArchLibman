package cn.archlibman

import cn.archlibman.event.EventManager
import cn.archlibman.gui.ClickGui // 确保导入的是你的 GUI 类
import net.fabricmc.api.ModInitializer
import org.slf4j.LoggerFactory

object Archlibman : ModInitializer {
	private val logger = LoggerFactory.getLogger("archlibman")
	lateinit var clickGui: ClickGui // 确保类型正确

	override fun onInitialize() {
		logger.info("ArchLibman Initializing...")
		EventManager.init()
		ModuleManager
		clickGui = ClickGui() // 初始化 GUI
	}
}