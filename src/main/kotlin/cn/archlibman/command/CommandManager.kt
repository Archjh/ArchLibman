package cn.archlibman.command

import cn.archlibman.command.commands.SayCommand
import net.minecraft.client.MinecraftClient
import java.util.concurrent.CopyOnWriteArrayList

object CommandManager {
    val commandPrefix = "."
    val mc = MinecraftClient.getInstance()
    val commands = CopyOnWriteArrayList<Command>()
init {
    commands.add(SayCommand)
}
    fun addMessage(message: String) {
        if(mc.player == null || mc.world == null) {
            return
        }
        mc.inGameHud.chatHud.addMessage(net.minecraft.text.Text.of(message))
    }
}