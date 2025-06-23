package cn.archlibman.event.events

import cn.archlibman.event.Event
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.render.VertexConsumerProvider.Immediate
import net.minecraft.client.util.math.MatrixStack

class DrawEvent(var context: DrawContext,var delta: Float): Event()