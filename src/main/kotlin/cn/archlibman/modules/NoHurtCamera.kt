package cn.archlibman.modules

import cn.archlibman.Category
import cn.archlibman.Module
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.GameRenderer
import java.lang.reflect.Field

object NoHurtCamera : Module(
    name = "NoHurtCamera",
    description = "移除受伤时的镜头抖动效果",
    category = Category.MOVEMENT
) {
    private var shakeField: Field? = null
    private var originalShake: Float = 0f

    init {
        try {
            // 尝试获取 shakeIntensity 字段（不同版本可能不同）
            shakeField = GameRenderer::class.java.getDeclaredField("shakeIntensity") // 1.19+
            shakeField?.isAccessible = true
        } catch (e: NoSuchFieldException) {
            // 如果 shakeIntensity 不存在，尝试其他可能的字段名
            try {
                shakeField = GameRenderer::class.java.getDeclaredField("cameraShake") // 1.20.4+?
                shakeField?.isAccessible = true
            } catch (e2: NoSuchFieldException) {
                System.err.println("[NoHurtCamera] 无法找到相机抖动字段！")
            }
        }
    }

    override fun onEnable() {
        if (shakeField != null) {
            originalShake = shakeField!!.get(mc.gameRenderer) as Float
        }
    }

    override fun onTick() {
        if (enabled && shakeField != null) {
            shakeField!!.setFloat(mc.gameRenderer, 0f) // 强制设为 0
        }
    }

    override fun onDisable() {
        if (shakeField != null) {
            shakeField!!.setFloat(mc.gameRenderer, originalShake) // 恢复原值
        }
    }
}