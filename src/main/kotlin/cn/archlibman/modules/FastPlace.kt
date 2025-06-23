package cn.archlibman.modules

import cn.archlibman.Category
import cn.archlibman.Module
import net.minecraft.client.MinecraftClient
import java.lang.reflect.Field

object FastPlace : Module(
    name = "FastPlace",
    description = "移除放置方块的延迟",
    category = Category.PLAYER
) {
    private var cooldownField: Field? = null

    init {
        try {
            // 尝试所有可能的字段名
            val possibleFieldNames = listOf("itemUseCooldown", "field_1755", "useCooldown")
            for (fieldName in possibleFieldNames) {
                try {
                    cooldownField = MinecraftClient::class.java.getDeclaredField(fieldName)
                    cooldownField?.isAccessible = true
                    break
                } catch (e: NoSuchFieldException) {
                    continue
                }
            }

            if (cooldownField == null) {
                System.err.println("[FastPlace] 无法找到物品冷却字段！")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onTick() {
        if (enabled && cooldownField != null) {
            try {
                // 确保在主线程执行
                if (mc.isOnThread) {
                    cooldownField?.setInt(mc, 0)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onDisable() {
        if (cooldownField != null) {
            try {
                if (mc.isOnThread) {
                    cooldownField?.setInt(mc, 4) // 恢复默认冷却
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}