package cn.archlibman.modules

import cn.archlibman.Module
import cn.archlibman.Category
import cn.archlibman.event.events.DrawEvent
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.Text
import java.util.concurrent.ConcurrentLinkedQueue
import java.awt.Color

object ToggleNotifications : Module("ToggleNotifications", "Shows module toggle notifications", Category.CLIENT) {
    private data class Notification(val text: String, val color: Int, var time: Long, val enabled: Boolean)

    private val notifications = ConcurrentLinkedQueue<Notification>()
    private const val DISPLAY_TIME = 2000L // 2 seconds
    private const val ANIMATION_TIME = 500L // 0.5 seconds for slide in/out
    private const val WIDTH = 150
    private const val HEIGHT = 25
    private const val PADDING = 10
    private const val ICON_SIZE = 12

    override fun onDraw(event: DrawEvent) {
        if (notifications.isEmpty() || !this.enabled) return

        val currentTime = System.currentTimeMillis()
        val context = event.context
        val textRenderer = mc.textRenderer

        // Remove expired notifications
        notifications.removeIf { currentTime - it.time > DISPLAY_TIME + ANIMATION_TIME }

        // Draw notifications (we'll only show the latest one with animation)
        notifications.firstOrNull()?.let { notification ->
            val elapsed = currentTime - notification.time
            val progress = when {
                elapsed < ANIMATION_TIME -> elapsed.toFloat() / ANIMATION_TIME // Slide in
                elapsed > DISPLAY_TIME -> 1 - (elapsed - DISPLAY_TIME).toFloat() / ANIMATION_TIME // Slide out
                else -> 1f // Fully visible
            }

            val screenWidth = mc.window.scaledWidth
            val screenHeight = mc.window.scaledHeight

            val targetX = screenWidth - WIDTH - PADDING.toFloat()
            val startX = screenWidth.toFloat()
            val x = startX + (targetX - startX) * progress
            val y = screenHeight - HEIGHT - PADDING.toFloat()

            // Draw background (green for enabled, red for disabled)
            val bgColor = if (notification.enabled) 0xAA00FF00.toInt() else 0xAAFF0000.toInt()
            context.fill(x.toInt(), y.toInt(), x.toInt() + WIDTH, y.toInt() + HEIGHT, bgColor)

            // Draw icon (checkmark or cross)
            val iconX = x + PADDING
            val iconY = y + (HEIGHT - ICON_SIZE) / 2
            if (notification.enabled) {
                // Draw checkmark (✓)
                context.drawText(textRenderer, "✓", iconX.toInt(), iconY.toInt(), Color.WHITE.rgb, false)
            } else {
                // Draw cross (✗)
                context.drawText(textRenderer, "✗", iconX.toInt(), iconY.toInt(), Color.WHITE.rgb, false)
            }

            // Draw text (shifted right to account for icon)
            val text = Text.literal(notification.text)
            val textX = x + PADDING + ICON_SIZE + 5
            val textY = y + (HEIGHT - 9) / 2
            context.drawText(textRenderer, text, textX.toInt(), textY.toInt(), -1, false)
        }
    }

    fun addNotification(moduleName: String, enabled: Boolean) {
        if (!this.enabled) return

        val color = if (enabled) Color.GREEN.rgb else Color.RED.rgb
        val text = if (enabled) "$moduleName ON" else "$moduleName OFF"

        // Clear previous notifications to avoid overlap
        notifications.clear()
        notifications.add(Notification(text, color, System.currentTimeMillis(), enabled))
    }
}