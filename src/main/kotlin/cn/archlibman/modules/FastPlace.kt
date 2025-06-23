// FastPlace.kt
package cn.archlibman.modules

import cn.archlibman.Category
import cn.archlibman.Module
import net.minecraft.client.MinecraftClient

object FastPlace : Module(
    name = "FastPlace",
    description = "移除放置方块的延迟",
    category = Category.PLAYER
) {
    // 可以添加设置项，比如速度控制
    // val speed = IntegerSetting("Speed", 0, 0, 4, 1)

    override fun onTick() {
        if (enabled) {
            // 1.20.4中需要通过混合访问(mixin)或反射来修改物品使用冷却
            // 这里提供一个反射实现的方案
            try {
                val cooldownField = MinecraftClient::class.java.getDeclaredField("itemUseCooldown")
                cooldownField.isAccessible = true
                cooldownField.setInt(mc, 0)
            } catch (e: Exception) {
                // 反射失败处理
                e.printStackTrace()
            }
        }
    }

    override fun onDisable() {
        try {
            val cooldownField = MinecraftClient::class.java.getDeclaredField("itemUseCooldown")
            cooldownField.isAccessible = true
            cooldownField.setInt(mc, 4) // 恢复默认冷却
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}