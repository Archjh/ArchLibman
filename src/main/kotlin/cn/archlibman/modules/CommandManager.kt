package cn.archlibman.modules

import cn.archlibman.Module
import cn.archlibman.Category
import cn.archlibman.ModuleManager
import net.minecraft.client.MinecraftClient
import net.minecraft.text.Text

object CommandManager : Module("CommandManager", "Manage modules via commands", Category.CLIENT) {
    // 添加 commandPrefix 常量
    private const val commandPrefix = "amod"
    
    // 单例实例
    val INSTANCE = this
    
    fun processCommand(command: String): Boolean {
        if (!command.startsWith("/$commandPrefix")) return false
        
        val args = command.split(" ").drop(1) // 去掉命令前缀
        
        if (args.isEmpty()) {
            sendHelp()
            return true
        }
        
        when (args[0]) {
            "-s", "--enable" -> {
                if (args.size < 2) {
                    sendMessage("Usage: /$commandPrefix -s <module>")
                    return true
                }
                toggleModule(args[1], true)
            }
            "-q", "--disable" -> {
                if (args.size < 2) {
                    sendMessage("Usage: /$commandPrefix -q <module>")
                    return true
                }
                toggleModule(args[1], false)
            }
            "-l", "--list" -> {
                listModules()
            }
            else -> {
                sendHelp()
            }
        }
        
        return true
    }
    
    private fun toggleModule(moduleName: String, enable: Boolean) {
        val module = ModuleManager.modules.find { it.name.equals(moduleName, ignoreCase = true) }
        
        if (module == null) {
            sendMessage("Module '$moduleName' not found!")
            return
        }
        
        if (enable) {
            if (module.enabled) {
                sendMessage("${module.name} is already enabled")
            } else {
                module.enable()
                sendMessage("Enabled ${module.name}")
            }
        } else {
            if (!module.enabled) {
                sendMessage("${module.name} is already disabled")
            } else {
                module.disable()
                sendMessage("Disabled ${module.name}")
            }
        }
    }
    
    private fun listModules() {
        sendMessage("Available modules:")
        ModuleManager.modules.groupBy { it.category }.forEach { (category, modules) ->
            sendMessage("§b${category.name}:")
            modules.forEach { module ->
                val status = if (module.enabled) "§aEnabled" else "§cDisabled"
                sendMessage("  ${module.name} - $status§r")
            }
        }
    }
    
    private fun sendHelp() {
        sendMessage("§6ArchLibman Module Manager§r")
        sendMessage("Usage:")
        sendMessage("/$commandPrefix -s <module> - Enable a module")
        sendMessage("/$commandPrefix -q <module> - Disable a module")
        sendMessage("/$commandPrefix -l - List all modules")
    }
    
    private fun sendMessage(message: String) {
        MinecraftClient.getInstance().player?.sendMessage(Text.literal(message))
    }
}