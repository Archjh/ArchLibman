package cn.archlibman.modules

import cn.archlibman.Module
import cn.archlibman.Category
import cn.archlibman.event.events.DrawEvent
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.ChatScreen
import net.minecraft.client.gui.widget.TextFieldWidget
import net.minecraft.text.Text
import java.awt.Color

object ChatUI : Module("ChatUI", "Beautiful chat interface", Category.CLIENT) {
    private val backgroundColor = Color(30, 30, 30, 200)
    private val borderColor = Color(60, 60, 60, 255)
    private val commandPrefixColor = Color(100, 149, 237)
    private val tagColor = Color(30, 144, 255)
    private val tagBorderColor = Color(70, 130, 180)
    
    private const val MARGIN = 10
    private const val CHAT_HEIGHT = 20
    private const val TAG_HEIGHT = 16
    private const val CORNER_RADIUS = 5
    
    override fun onDraw(event: DrawEvent) {
        val mc = MinecraftClient.getInstance()
        if (mc.currentScreen is ChatScreen) {
            val screen = mc.currentScreen as ChatScreen
            drawChatBackground(event.context, screen)
            drawCommandPrefix(event.context, screen)
        }
    }
    
    private fun drawChatBackground(context: DrawContext, screen: ChatScreen) {
        val width = screen.width
        val height = screen.height
        val chatWidth = (width - 2 * MARGIN).coerceAtMost(400)
        val x = MARGIN
        val y = height - CHAT_HEIGHT - MARGIN
        
        // Draw rounded chat background
        drawRoundedRect(context, x, y, chatWidth, CHAT_HEIGHT, CORNER_RADIUS, backgroundColor)
    }
    
    private fun drawCommandPrefix(context: DrawContext, screen: ChatScreen) {
        val chatField = try {
            val field = ChatScreen::class.java.getDeclaredField("chatField")
            field.isAccessible = true
            field.get(screen) as TextFieldWidget
        } catch (e: Exception) {
            return
        }
        
        if (chatField.text.startsWith("/")) {
            val mc = MinecraftClient.getInstance()
            val playerName = mc.player?.name?.string ?: "Player"
            val width = screen.width
            val height = screen.height
            
            // Calculate tag position (above chat input)
            val tagY = height - CHAT_HEIGHT - TAG_HEIGHT - MARGIN + 2
            val tagWidth = mc.textRenderer.getWidth(playerName) + 10
            val tagX = MARGIN.coerceAtLeast(0) // Ensure it stays on screen
            
            // Draw tag with rounded corners
            drawRoundedRect(context, tagX, tagY, tagWidth, TAG_HEIGHT, CORNER_RADIUS, tagColor)
            
            // Draw connecting corner
            context.fill(tagX + CORNER_RADIUS, tagY + TAG_HEIGHT, 
                         tagX + tagWidth - CORNER_RADIUS, tagY + TAG_HEIGHT + 2, 
                         tagColor.rgb)
            context.fill(tagX + tagWidth - CORNER_RADIUS, tagY + TAG_HEIGHT, 
                         tagX + tagWidth, tagY + TAG_HEIGHT + CORNER_RADIUS, 
                         tagColor.rgb)
            
            // Draw player name
            context.drawText(mc.textRenderer, Text.literal(playerName), 
                            tagX + 5, tagY + 4, Color.WHITE.rgb, false)
            
            // Highlight command prefix
            context.drawText(mc.textRenderer, Text.literal("/"), 
                            MARGIN + 2, height - CHAT_HEIGHT - MARGIN + 6, 
                            commandPrefixColor.rgb, false)
        }
    }
    
    private fun drawRoundedRect(context: DrawContext, x: Int, y: Int, width: Int, height: Int, radius: Int, color: Color) {
        // Main rectangle
        context.fill(x + radius, y, x + width - radius, y + height, color.rgb)
        context.fill(x, y + radius, x + width, y + height - radius, color.rgb)
        
        // Rounded corners
        drawQuarterCircle(context, x + radius, y + radius, radius, 0, color) // Top-left
        drawQuarterCircle(context, x + width - radius, y + radius, radius, 1, color) // Top-right
        drawQuarterCircle(context, x + width - radius, y + height - radius, radius, 2, color) // Bottom-right
        drawQuarterCircle(context, x + radius, y + height - radius, radius, 3, color) // Bottom-left
    }
    
    private fun drawQuarterCircle(context: DrawContext, centerX: Int, centerY: Int, radius: Int, quadrant: Int, color: Color) {
        for (i in 0 until radius) {
            for (j in 0 until radius) {
                if (i * i + j * j <= radius * radius) {
                    when (quadrant) {
                        0 -> context.fill(centerX - i, centerY - j, centerX - i + 1, centerY - j + 1, color.rgb)
                        1 -> context.fill(centerX + j, centerY - i, centerX + j + 1, centerY - i + 1, color.rgb)
                        2 -> context.fill(centerX + i, centerY + j, centerX + i + 1, centerY + j + 1, color.rgb)
                        3 -> context.fill(centerX - j, centerY + i, centerX - j + 1, centerY + i + 1, color.rgb)
                    }
                }
            }
        }
    }
}