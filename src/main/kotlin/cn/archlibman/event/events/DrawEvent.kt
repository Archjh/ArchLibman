// DrawEvent.kt
package cn.archlibman.event.events

import net.minecraft.client.gui.DrawContext
import net.minecraft.client.util.math.MatrixStack

class DrawEvent(val context: DrawContext, val tickDelta: Float) {
    val matrices: MatrixStack
        get() = context.matrices
}