package cn.archlibman.modules

import cn.archlibman.Module
import cn.archlibman.Category
import cn.archlibman.event.events.DrawEvent
import cn.archlibman.api.ChatInputAccessor
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.ChatScreen
import net.minecraft.client.gui.widget.TextFieldWidget
import net.minecraft.text.Text
import java.awt.Color

object ChatUI : Module("ChatUI", "Beautiful chat interface", Category.CLIENT) {
    private val backgroundColor = Color(30, 30, 30, 200)
    private val borderColor = Color(70, 130, 180, 255) // Changed to blue
    private val commandPrefixColor = Color(100, 149, 237)
    private val tagColor = Color(30, 144, 255)
    private val tagBorderColor = Color(70, 130, 180)
    private val promptColor = Color(100, 149, 237)
    
    private const val MARGIN = 10
    private const val CHAT_HEIGHT = 20
    private const val TAG_HEIGHT = 16
    private const val CORNER_RADIUS = 5
    private const val PROMPT_WIDTH = 10
    
    private var currentInput = ""
    
    override fun onDraw(event: DrawEvent) {
        val mc = MinecraftClient.getInstance()
        if (mc.currentScreen is ChatScreen) {
            val screen = mc.currentScreen as ChatScreen
            updateCurrentInput(screen)
            drawChatBackground(event.context, screen)
            drawCommandPrefix(event.context, screen)
            drawUserPrompt(event.context, screen)
        }
    }
    
    private fun updateCurrentInput(screen: ChatScreen) {
        try {
            val accessor = screen as ChatInputAccessor
            currentInput = accessor.chatField.text
        } catch (e: Exception) {
            // 回退方案
            currentInput = ""
        }
    }

    private fun getChatField(screen: ChatScreen): TextFieldWidget? {
        return try {
            (screen as ChatInputAccessor).chatField
        } catch (e: Exception) {
            null
        }
    }
    
    private fun drawChatBackground(context: DrawContext, screen: ChatScreen) {
        val width = screen.width
        val height = screen.height
        val chatWidth = (width - 2 * MARGIN).coerceAtMost(400)
        val x = MARGIN
        val baseY = height - CHAT_HEIGHT - MARGIN
        
        // Determine height based on whether it's command input
        val isCommand = currentInput.startsWith("/")
        val chatBackgroundHeight = if (isCommand) height / 2 else height / 4
        
        // Draw main chat background
        drawRoundedRectWithBorder(context, x, baseY - chatBackgroundHeight + CHAT_HEIGHT, 
                                 chatWidth, chatBackgroundHeight, CORNER_RADIUS, 
                                 backgroundColor, borderColor)
        
        // Draw small chat input field
        drawRoundedRectWithBorder(context, x, baseY, chatWidth, CHAT_HEIGHT, 
                                 CORNER_RADIUS, backgroundColor, borderColor)
    }
    
    private fun drawUserPrompt(context: DrawContext, screen: ChatScreen) {
        val mc = MinecraftClient.getInstance()
        val playerName = mc.player?.name?.string ?: "Player"
        val width = screen.width
        val height = screen.height
    
        // Calculate tag position (above chat input)
        val tagY = height - CHAT_HEIGHT - TAG_HEIGHT - MARGIN + 2
        val tagWidth = mc.textRenderer.getWidth(playerName) + 10
        val tagX = MARGIN.coerceAtLeast(0)
    
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
    
        // Draw prompt and input
        val promptX = tagX + tagWidth + 5
        val promptText = "╭─"
        val inputText = if (currentInput.isNotEmpty()) currentInput else "·······································································"
    
        context.drawText(mc.textRenderer, Text.literal(promptText), 
                        promptX, tagY + 4, promptColor.rgb, false)
    
        // Draw input text with ellipsis if too long
        val maxInputWidth = (width - MARGIN) - (promptX + mc.textRenderer.getWidth(promptText)) - 10
        val renderedInput = if (mc.textRenderer.getWidth(inputText) > maxInputWidth) {
            mc.textRenderer.trimToWidth(inputText, maxInputWidth - mc.textRenderer.getWidth("...")) + "..."
        } else {
            inputText
        }
    
        context.drawText(mc.textRenderer, Text.literal(renderedInput), 
                        promptX + mc.textRenderer.getWidth(promptText), tagY + 4, Color.WHITE.rgb, false)
    }
    
    private fun drawCommandPrefix(context: DrawContext, screen: ChatScreen) {
        val chatField = try {
            (screen as ChatInputAccessor).chatField
        } catch (e: Exception) {
            return
        }
    
        if (chatField.text.startsWith("/")) {
            val height = screen.height
            // Highlight command prefix
            context.drawText(mc.textRenderer, Text.literal("/"), 
                            MARGIN + 2, height - CHAT_HEIGHT - MARGIN + 6, 
                            commandPrefixColor.rgb, false)
        }
    }
    
    private fun drawRoundedRectWithBorder(context: DrawContext, x: Int, y: Int, width: Int, height: Int, radius: Int, fillColor: Color, borderColor: Color) {
        // Draw fill
        drawRoundedRect(context, x, y, width, height, radius, fillColor)
        
        // Draw border
        // Top border
        context.fill(x + radius, y, x + width - radius, y + 1, borderColor.rgb)
        // Bottom border
        context.fill(x + radius, y + height - 1, x + width - radius, y + height, borderColor.rgb)
        // Left border
        context.fill(x, y + radius, x + 1, y + height - radius, borderColor.rgb)
        // Right border
        context.fill(x + width - 1, y + radius, x + width, y + height - radius, borderColor.rgb)
        
        // Draw rounded corners
        drawQuarterCircleBorder(context, x + radius, y + radius, radius, 0, borderColor) // Top-left
        drawQuarterCircleBorder(context, x + width - radius, y + radius, radius, 1, borderColor) // Top-right
        drawQuarterCircleBorder(context, x + width - radius, y + height - radius, radius, 2, borderColor) // Bottom-right
        drawQuarterCircleBorder(context, x + radius, y + height - radius, radius, 3, borderColor) // Bottom-left
    }
    
    private fun drawQuarterCircleBorder(context: DrawContext, centerX: Int, centerY: Int, radius: Int, quadrant: Int, color: Color) {
        for (i in 0 until radius) {
            for (j in 0 until radius) {
                if (i * i + j * j <= radius * radius && (i * i + j * j >= (radius - 1) * (radius - 1))) {
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
