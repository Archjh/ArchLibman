package cn.archlibman.modules

import cn.archlibman.Category
import cn.archlibman.Module
import cn.archlibman.event.events.DrawEvent
import net.minecraft.client.MinecraftClient
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.text.Text
import org.lwjgl.glfw.GLFW
import java.awt.Color

object PotionHUD : Module("PotionHUD", "Displays active potion effects", Category.RENDER) {
    init {
        key = GLFW.GLFW_KEY_UNKNOWN
        enabled = true
    }

    override fun onDraw(event: DrawEvent) {
        if (!enabled) return

        val mc = MinecraftClient.getInstance()
        val player = mc.player ?: return
        val effects = player.activeStatusEffects.values

        if (effects.isEmpty()) return

        var yOffset = 16
        var spacing = 33

        if (effects.size > 5) {
            spacing = 132 / (effects.size - 1)
        }

        effects.forEach { effect ->
            val effectType = effect.effectType
            val name = Text.translatable(effectType.translationKey).string
            var displayName = name

            // Handle amplifier levels
            when (effect.amplifier) {
                0 -> displayName = "$name"
                1 -> displayName = "$name ${Text.translatable("enchantment.level.2").string}"
                2 -> displayName = "$name ${Text.translatable("enchantment.level.3").string}"
                3 -> displayName = "$name ${Text.translatable("enchantment.level.4").string}"
                else -> displayName = "$name ${effect.amplifier + 1}"
            }

            // Draw effect name
            event.context.drawTextWithShadow(
                mc.textRenderer,
                displayName,
                10,
                250 + yOffset - 30,
                Color.WHITE.rgb
            )

            // Draw duration
            val durationText = getDurationString(effect)
            event.context.drawTextWithShadow(
                mc.textRenderer,
                durationText,
                10,
                250 + yOffset + 10 - 30,
                0x7F9F9F  // Same as 8355711 in Java version
            )

            yOffset += spacing
        }
    }

    private fun getDurationString(effect: StatusEffectInstance): String {
        // Check for "infinite" duration (typically -1)
        if (effect.duration <= 0) {
            return Text.translatable("effect.duration.infinite").string
        }

        val ticks = effect.duration
        val seconds = ticks / 20
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60

        return if (minutes > 0) {
            String.format("%d:%02d", minutes, remainingSeconds)
        } else {
            seconds.toString()
        }
    }
}