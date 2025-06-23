package cn.archlibman.modules

import cn.archlibman.Category
import cn.archlibman.Module
import cn.archlibman.event.events.DrawEvent
import net.minecraft.client.MinecraftClient
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
import org.lwjgl.glfw.GLFW
import java.awt.Color
import java.util.*

object KeyStrokes : Module("KeyStrokes", "Displays pressed keys and CPS", Category.RENDER) {
    private val cpsMap = mutableMapOf<Int, Pair<Queue<Long>, Int>>()
    private var showCps = true
    private lateinit var mode: KeystrokesMode

    // 延迟初始化的键位列表
    private val keysCache = mutableMapOf<KeystrokesMode, List<Key>>()

    init {
        key = GLFW.GLFW_KEY_UNKNOWN
        enabled = true

        // Initialize CPS tracking for mouse buttons
        cpsMap[GLFW.GLFW_MOUSE_BUTTON_1] = Pair(LinkedList(), 0) // Left click
        cpsMap[GLFW.GLFW_MOUSE_BUTTON_2] = Pair(LinkedList(), 0) // Right click
    }

    enum class KeystrokesMode {
        WASD,
        WASD_MOUSE
    }

    class Key(
        val name: String,
        private val keyCode: Int,
        val x: Int,
        val y: Int,
        val width: Int,
        val height: Int,
        val id: String
    ) {
        fun isDown(mc: MinecraftClient): Boolean {
            return when {
                id == "LMB" -> GLFW.glfwGetMouseButton(mc.window.handle, GLFW.GLFW_MOUSE_BUTTON_1) == GLFW.GLFW_PRESS
                id == "RMB" -> GLFW.glfwGetMouseButton(mc.window.handle, GLFW.GLFW_MOUSE_BUTTON_2) == GLFW.GLFW_PRESS
                else -> GLFW.glfwGetKey(mc.window.handle, keyCode) == GLFW.GLFW_PRESS
            }
        }
    }

    private fun initializeKeys(mc: MinecraftClient): List<Key> {
        val options = mc.options ?: return emptyList()

        return when (mode) {
            KeystrokesMode.WASD -> listOf(
                createKey("W", options.forwardKey, 21, 1, 18, 18, "W"),
                createKey("A", options.leftKey, 1, 21, 18, 18, "A"),
                createKey("S", options.backKey, 21, 21, 18, 18, "S"),
                createKey("D", options.rightKey, 41, 21, 18, 18, "D")
            )
            KeystrokesMode.WASD_MOUSE -> listOf(
                createKey("W", options.forwardKey, 21, 1, 18, 18, "W"),
                createKey("A", options.leftKey, 1, 21, 18, 18, "A"),
                createKey("S", options.backKey, 21, 21, 18, 18, "S"),
                createKey("D", options.rightKey, 41, 21, 18, 18, "D"),
                Key("LMB", GLFW.GLFW_MOUSE_BUTTON_1, 1, 41, 28, 18, "LMB"),
                Key("RMB", GLFW.GLFW_MOUSE_BUTTON_2, 31, 41, 28, 18, "RMB"),
                createKey("JUMP", options.jumpKey, 1, 62, 58, 18, "SB")
            )
        }
    }

    private fun createKey(name: String, binding: KeyBinding, x: Int, y: Int, width: Int, height: Int, id: String): Key {
        val keyCode = InputUtil.fromTranslationKey(binding.boundKeyTranslationKey).code
        return Key(name, keyCode, x, y, width, height, id)
    }

    override fun onDraw(event: DrawEvent) {
        if (!enabled) return

        val mc = MinecraftClient.getInstance()
        if (mc.options == null) return

        // 初始化模式和键位
        if (!this::mode.isInitialized) {
            mode = KeystrokesMode.WASD_MOUSE
        }

        // 获取或初始化键位
        val keys = keysCache.getOrPut(mode) { initializeKeys(mc) }

        val matrices = event.context.matrices
        matrices.push()
        matrices.translate(0.0, 0.0, 0.0)

        for (key in keys) {
            val isDown = key.isDown(mc)
            val textWidth = mc.textRenderer.getWidth(key.name)

            // 绘制键位背景
            event.context.fill(
                key.x, key.y,
                key.x + key.width, key.y + key.height,
                if (isDown) Color(255, 255, 255, 150).rgb else Color(0, 0, 0, 150).rgb
            )

            // 绘制键位文本
            val displayText = if (showCps && key.id.contains("MB")) {
                if (key.id == "RMB") getCPS(GLFW.GLFW_MOUSE_BUTTON_2).toString()
                else getCPS(GLFW.GLFW_MOUSE_BUTTON_1).toString()
            } else {
                key.name
            }

            event.context.drawCenteredTextWithShadow(
                mc.textRenderer,
                displayText,
                key.x + key.width / 2,
                key.y + key.height / 2 - 4,
                if (isDown) Color.BLACK.rgb else Color.WHITE.rgb
            )
        }

        matrices.pop()
    }

    fun onMouseClick(button: Int) {
        if (!enabled) return

        val now = System.currentTimeMillis()
        val (queue, _) = cpsMap[button] ?: return

        queue.add(now)
        while (queue.isNotEmpty() && now - queue.peek() > 1000) {
            queue.poll()
        }

        cpsMap[button] = Pair(queue, queue.size)
    }

    private fun getCPS(button: Int): Int {
        return cpsMap[button]?.second ?: 0
    }

    fun setMode(newMode: KeystrokesMode) {
        mode = newMode
        keysCache.clear() // 清除缓存以便重新初始化
    }
}