package cn.archlibman.command

import cn.archlibman.Archlibman
import cn.archlibman.ModuleManager
import com.mojang.brigadier.arguments.StringArgumentType
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.text.Text

object CommandModuleManager {
    fun register() {
        ClientCommandRegistrationCallback.EVENT.register { dispatcher, _ ->
            dispatcher.register(
                ClientCommandManager.literal("amod")
                    .then(ClientCommandManager.literal("-l")
                        .executes { context ->
                            listModules(context.source)
                            1
                        }
                        .then(ClientCommandManager.argument("module", StringArgumentType.string())
                            .suggests { _, builder ->
                                ModuleManager.modules.forEach {
                                    builder.suggest(it.name)
                                }
                                builder.buildFuture()
                            }
                            .executes { context ->
                                val moduleName = StringArgumentType.getString(context, "module")
                                listModuleStatus(context.source, moduleName)
                                1
                            }
                        )
                    )
                    .then(ClientCommandManager.literal("-s")
                        .then(ClientCommandManager.argument("module", StringArgumentType.string())
                            .suggests { _, builder ->
                                ModuleManager.modules.forEach {
                                    builder.suggest(it.name)
                                }
                                builder.buildFuture()
                            }
                            .executes { context ->
                                val moduleName = StringArgumentType.getString(context, "module")
                                toggleModule(context.source, moduleName, true)
                                1
                            }
                        )
                    )
                    .then(ClientCommandManager.literal("-q")
                        .then(ClientCommandManager.argument("module", StringArgumentType.string())
                            .suggests { _, builder ->
                                ModuleManager.modules.forEach {
                                    builder.suggest(it.name)
                                }
                                builder.buildFuture()
                            }
                            .executes { context ->
                                val moduleName = StringArgumentType.getString(context, "module")
                                toggleModule(context.source, moduleName, false)
                                1
                            }
                        )
                    )
                    .executes { context ->
                        showUsage(context.source)
                        1
                    }
            )
        }
    }

    private fun listModules(source: FabricClientCommandSource) {
        source.sendFeedback(Text.literal("§6=== 模块列表 ==="))
        
        ModuleManager.modules.groupBy { it.category }.forEach { (category, modules) ->
            source.sendFeedback(Text.literal("§b${category.name}:"))
            
            modules.forEach { module ->
                val status = if (module.enabled) "§a启用" else "§c禁用"
                source.sendFeedback(Text.literal(" §7- ${module.name}: $status §7(默认: ${if (module.defaultEnabled) "§a启用" else "§c禁用"})"))
            }
        }
    }

    private fun listModuleStatus(source: FabricClientCommandSource, moduleName: String) {
        val module = ModuleManager.modules.find { it.name.equals(moduleName, ignoreCase = true) }
        
        if (module != null) {
            val status = if (module.enabled) "§a启用" else "§c禁用"
            source.sendFeedback(Text.literal("§6模块 ${module.name} 状态: $status"))
            source.sendFeedback(Text.literal("§7描述: ${module.description}"))
            source.sendFeedback(Text.literal("§7分类: ${module.category.name}"))
            source.sendFeedback(Text.literal("§7默认状态: ${if (module.defaultEnabled) "§a启用" else "§c禁用"}"))
            source.sendFeedback(Text.literal("§7绑定按键: ${if (module.key != -1) "Key ${module.key}" else "未绑定"}"))
        } else {
            source.sendError(Text.literal("§c未找到名为 $moduleName 的模块"))
        }
    }

    private fun toggleModule(source: FabricClientCommandSource, moduleName: String, enable: Boolean) {
        val module = ModuleManager.modules.find { it.name.equals(moduleName, ignoreCase = true) }
        
        if (module != null) {
            if (enable) {
                if (!module.enabled) {
                    module.enable()
                    source.sendFeedback(Text.literal("§a已启用模块 ${module.name}"))
                } else {
                    source.sendFeedback(Text.literal("§e模块 ${module.name} 已经是启用状态"))
                }
            } else {
                if (module.enabled) {
                    module.disable()
                    source.sendFeedback(Text.literal("§a已禁用模块 ${module.name}"))
                } else {
                    source.sendFeedback(Text.literal("§e模块 ${module.name} 已经是禁用状态"))
                }
            }
        } else {
            source.sendError(Text.literal("§c未找到名为 $moduleName 的模块"))
        }
    }

    private fun showUsage(source: FabricClientCommandSource) {
        source.sendFeedback(Text.literal("§6=== ArchLibman 模块管理器 ==="))
        source.sendFeedback(Text.literal("§b/amod -l §7- 列出所有模块"))
        source.sendFeedback(Text.literal("§b/amod -l [模块名] §7- 查看指定模块状态"))
        source.sendFeedback(Text.literal("§b/amod -s [模块名] §7- 启用指定模块"))
        source.sendFeedback(Text.literal("§b/amod -q [模块名] §7- 禁用指定模块"))
        source.sendFeedback(Text.literal("§7提示: 按Tab键可以自动补全模块名"))
    }
}
