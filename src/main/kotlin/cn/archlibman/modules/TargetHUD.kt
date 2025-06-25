package cn.archlibman.modules

import cn.archlibman.Category
import cn.archlibman.Module
import cn.archlibman.event.events.DrawEvent
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.render.*
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.math.RotationAxis
import org.lwjgl.glfw.GLFW
import kotlin.math.max

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

        // Draw entity preview (50x50 pixels)
        val previewSize = 50
        val previewX = x
        val previewY = y

        drawEntityPreview(event.context, target, previewX, previewY, previewSize)

        x += previewSize + 10 // Move text to the right of the preview

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

    private fun drawEntityPreview(context: DrawContext, entity: LivingEntity, x: Int, y: Int, size: Int) {
        val mc = MinecraftClient.getInstance()
        val matrices = context.matrices

        // Setup rendering
        matrices.push()
        matrices.translate(x.toFloat(), y.toFloat(), 1000.0f)
        matrices.scale(size.toFloat(), size.toFloat(), -size.toFloat())

        // Calculate rotation based on entity type and tick count
        val rotation = (mc.player?.age ?: 0) % 360
        val scale = max(entity.width, entity.height) * 1.5f

        matrices.translate(0.5f, 0.5f, 0.5f)
        matrices.scale(1.0f / scale, 1.0f / scale, 1.0f / scale)
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180f))
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rotation.toFloat()))

        // Setup lighting
        val light = 0xF000F0

        // Get the vertex consumer provider
        val vertexConsumers = context.vertexConsumers

        // Render entity
        mc.entityRenderDispatcher.render(
            entity,
            0.0, 0.0, 0.0,
            0f,
            1f,
            matrices,
            vertexConsumers,
            light
        )

        matrices.pop()

        // Draw background/border
        context.fill(x - 1, y - 1, x + size + 1, y + size + 1, 0x80000000.toInt())
    }
}