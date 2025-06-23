package cn.archlibman.event

import cn.archlibman.ModuleManager.modules
import cn.archlibman.command.CommandManager
import cn.archlibman.event.events.ChatEvent
import cn.archlibman.event.events.DrawEvent
import cn.archlibman.event.events.KeyboardEvent
import cn.archlibman.event.events.TickEvent

object EventManager {
    fun init() {
        EventBus.register(this)
    }

    @Listener
    fun onTick(event: TickEvent) {
        modules
            .filter { it.enabled}
            .forEach { it.onTick() }
    }

    @Listener
    fun onKey(event: KeyboardEvent) {
        modules.forEach {
            if(it.key == event.key) {
                it.toggle()
            }
        }
    }

    @Listener
    fun onDraw(event: DrawEvent) {
        modules
            .filter { it.enabled }
            .forEach {
                it.onDraw(event)
            }
    }

    @Listener
    fun onChat(event: ChatEvent) {
        if(event.content.startsWith(CommandManager.commandPrefix)) {
            event.cancelled = true
            if(event.content.length > 1) {
                val args = event.content.removePrefix(CommandManager.commandPrefix).split(" ")
                CommandManager.commands.forEach {
                    if(it.command == args[0]) {
                        it.run(args.drop(1).toTypedArray())
                    }
                }

            }
        }
    }

}