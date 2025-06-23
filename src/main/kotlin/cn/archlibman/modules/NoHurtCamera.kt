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
            // 尝试所有可能的字段名
            val possibleFieldNames = listOf("shakeIntensity", "cameraShake", "field_18468", "shakingIntensity")
            for (fieldName in possibleFieldNames) {
                try {
                    shakeField = GameRenderer::class.java.getDeclaredField(fieldName)
                    shakeField?.isAccessible = true
                    break
                } catch (e: NoSuchFieldException) {
                    continue
                }
            }

            if (shakeField == null) {
                System.err.println("[NoHurtCamera] 无法找到相机抖动字段！")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onEnable() {
        if (shakeField != null) {
            try {
                originalShake = shakeField!!.get(mc.gameRenderer) as Float
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onTick() {
        if (enabled && shakeField != null) {
            try {
                shakeField!!.setFloat(mc.gameRenderer, 0f)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onDisable() {
        if (shakeField != null) {
            try {
                shakeField!!.setFloat(mc.gameRenderer, originalShake)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}