package cn.archlibman.modules

import cn.archlibman.Category
import cn.archlibman.Module
import cn.archlibman.event.events.DrawEvent
import net.minecraft.client.MinecraftClient
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.text.Text
import org.lwjgl.glfw.GLFW

object TargetHUD : Module("TargetHUD", "Displays targeted entity info", Category.RENDER) {
    init {
        key = GLFW.GLFW_KEY_UNKNOWN
        enabled = true
    }

    override fun onDraw(event: DrawEvent) {
        if (!enabled) return

        val mc = MinecraftClient.getInstance()
        val target = mc.targetedEntity as? LivingEntity ?: return
        val player = mc.player ?: return

        var x = mc.window.scaledWidth / 2 + 10
        var y = mc.window.scaledHeight / 2 - 20

        val name = target.displayName?.string
        val health = target.health
        val maxHealth = target.maxHealth
        val distance = String.format("%.1f", player.distanceTo(target))

        // Draw name
        event.context.drawTextWithShadow(
            mc.textRenderer,
            name,
            x, y,
            0xFFFFFF
        )
        y += mc.textRenderer.fontHeight + 2

        // Draw health bar
        val healthPercentage = (health / maxHealth).coerceIn(0f, 1f)
        val healthColor = when {
            healthPercentage > 0.6 -> 0xFF00FF00.toInt() // Green
            healthPercentage > 0.3 -> 0xFFFFFF00.toInt() // Yellow
            else -> 0xFFFF0000.toInt() // Red
        }

        event.context.fill(x, y, x + 100, y + 2, 0x80000000.toInt())
        event.context.fill(x, y, x + (100 * healthPercentage).toInt(), y + 2, healthColor)
        y += 4

        // Draw health text
        event.context.drawTextWithShadow(
            mc.textRenderer,
            "${String.format("%.1f", health)}/${String.format("%.1f", maxHealth)}",
            x, y,
            0xFFFFFF
        )
        y += mc.textRenderer.fontHeight + 2

        // Draw distance
        event.context.drawTextWithShadow(
            mc.textRenderer,
            "$distance blocks",
            x, y,
            0xFFFFFF
        )

        // For players, show armor and items
        if (target is PlayerEntity) {
            y += mc.textRenderer.fontHeight + 2
            val armor = target.inventory.armor
            armor.forEachIndexed { index, item ->
                if (!item.isEmpty) {
                    event.context.matrices.push()
                    event.context.matrices.scale(0.5f, 0.5f, 0.5f)
                    event.context.drawItem(item, (x * 2), (y * 2))
                    event.context.matrices.pop()
                    x += 10
                }
            }
        }
    }
}