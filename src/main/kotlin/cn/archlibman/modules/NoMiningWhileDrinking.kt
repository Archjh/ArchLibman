package cn.archlibman.modules

import cn.archlibman.Category
import cn.archlibman.Module
import cn.archlibman.event.events.TickEvent
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.item.Items

object NoMiningWhileDrinking : Module(
    name = "NoMiningWhileDrinking",
    description = "Prevents mining blocks while drinking potions",
    category = Category.PLAYER
) {
    override fun onTick() {
        val player = mc.player ?: return

        // 检查玩家是否正在使用物品（喝药水）
        if (isDrinking(player)) {
            // 停止挖掘
            mc.options.attackKey.isPressed = false
        }
    }

    private fun isDrinking(player: ClientPlayerEntity): Boolean {
        // 检查玩家是否正在使用药水
        return player.isUsingItem &&
                (player.activeItem.item == Items.POTION ||
                        player.activeItem.item == Items.SPLASH_POTION ||
                        player.activeItem.item == Items.LINGERING_POTION)
    }
}