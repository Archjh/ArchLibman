package cn.archlibman.modules

import cn.archlibman.Category
import cn.archlibman.Module
import cn.archlibman.event.events.DrawEvent
import net.minecraft.client.MinecraftClient
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.ItemStack
import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.GL11

object ArmorHUD : Module("ArmorHUD", "Displays armor status", Category.RENDER) {
    init {
        key = GLFW.GLFW_KEY_UNKNOWN
        enabled = true
    }

    override fun onDraw(event: DrawEvent) {
        if (!enabled) return

        val mc = MinecraftClient.getInstance()
        val player = mc.player ?: return
        val armorItems = player.inventory.armor
        var itemY = mc.window.scaledHeight / 2

        for (i in 0 until 4) {
            val itemStack = armorItems[i]
            if (!itemStack.isEmpty) {
                itemY -= 20
                renderItem(event, mc, 10, itemY - 100, itemStack)
            }
        }
    }

    private fun renderItem(event: DrawEvent, mc: MinecraftClient, xPos: Int, yPos: Int, itemStack: ItemStack) {
        val matrices = event.context.matrices
        val textRenderer = mc.textRenderer

        matrices.push()
        GL11.glEnable(GL11.GL_BLEND)
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)

        // 渲染耐久度百分比
        if (itemStack.isDamageable) {
            val damage = (itemStack.maxDamage - itemStack.damage).toDouble() / itemStack.maxDamage.toDouble() * 100.0
            event.context.drawTextWithShadow(
                textRenderer,
                "%.2f%%".format(damage),
                xPos + 15,
                yPos + 5,
                -1
            )
        }

        // 正确的1.20.4物品渲染方法
        event.context.drawItem(itemStack, xPos, yPos)
        event.context.drawItemInSlot(
            textRenderer,
            itemStack,
            xPos,
            yPos
        )

        GL11.glDisable(GL11.GL_BLEND)
        matrices.pop()
    }
}