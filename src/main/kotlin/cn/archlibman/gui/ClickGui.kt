package cn.archlibman.gui

import cn.archlibman.Category
import cn.archlibman.ModuleManager
import cn.archlibman.ModuleManager.addModule
import cn.archlibman.modules.ArmorHUD
import cn.archlibman.modules.FPSDisplay
import cn.archlibman.modules.KeyStrokes
import cn.archlibman.modules.NoClickDelay
import cn.archlibman.modules.PotionHUD
import cn.archlibman.modules.Sprint
import cn.archlibman.modules.TargetHUD
import cn.archlibman.modules.Watermark
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text
import java.awt.Color
import java.util.concurrent.CopyOnWriteArrayList

class ClickGui : Screen(Text.of("ClickGui")) {
    private val categoryButtons = CopyOnWriteArrayList<CategoryButton>()
    private val moduleButtons = CopyOnWriteArrayList<ModuleButton>()
    private val modSettingManager = ModSettingManager

    private var centerX = 0
    private var centerY = 0
    private val backgroundWidth = 200
    companion object {
        var centerX = 0
        var centerY = 0
    }

    init {
        // 初始化分类按钮
        Category.entries.forEachIndexed { index, category ->
            categoryButtons.add(CategoryButton(
                x = 0,
                y = 0,
                width = 100,
                height = 20,
                text = category.name,
                id = index
            ))
        }

        val modules = listOf(
            Watermark,
            Sprint,
            NoClickDelay,  // 保留一个
            ArmorHUD,
            PotionHUD,
            KeyStrokes,
            TargetHUD,
            FPSDisplay,
            // 其他模块...
        )

        // 确保所有模块都添加了
        ModuleManager.modules.forEach { module ->
            moduleButtons.add(ModuleButton(
                x = 0,
                y = 0,
                width = 200,
                height = 25,
                module = module,
                categoryId = module.category.ordinal
            ))
        }
    }

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        super.render(context, mouseX, mouseY, delta)

        val client = MinecraftClient.getInstance()
        val window = client.window
        centerX = window.scaledWidth / 2
        centerY = window.scaledHeight / 2
        ClickGui.centerX = centerX
        ClickGui.centerY = centerY

        // 绘制背景（使用更透明的颜色以便调试）
        context.fill(
            centerX - backgroundWidth, centerY - 100,
            centerX + backgroundWidth, centerY + 100,
            0x40000000.toInt() // 更透明的黑色
        )

        // 更新并渲染分类按钮位置 - 修改这部分
        val categoryStartX = centerX - backgroundWidth + 5 // 向左偏移一点
        val categoryStartY = centerY - 80 // 从更高位置开始

        categoryButtons.forEachIndexed { index, button ->
            button.x = categoryStartX
            button.y = categoryStartY + index * 25 // 增加间距
            button.width = 90 // 稍微缩小宽度
            button.render(context, mouseX, mouseY, delta)
        }

        // 渲染模块按钮
        val currentCategory = CategoryManager.currentPage
        val visibleModules = moduleButtons.filter { it.categoryId == currentCategory }

        // 在ClickGui.kt的render方法中
        visibleModules.forEachIndexed { index, button ->
            button.x = centerX - backgroundWidth + 110
            button.y = centerY - 80 + index * 30
            button.width = 180
            // 确保y位置不会超出可见范围
            button.render(context, mouseX, mouseY, delta)
        }

        context.drawText(
            MinecraftClient.getInstance().textRenderer,
            "Visible modules: ${visibleModules.size}, Current category: ${Category.entries[CategoryManager.currentPage]}",
            centerX - backgroundWidth + 110,
            centerY - 100,
            Color.WHITE.rgb,
            true
        )

        // 渲染模块设置
        modSettingManager.render(context, mouseX, mouseY, delta)
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        categoryButtons.forEach { it.mouseClicked(mouseX, mouseY, button) }

        if (mouseX >= centerX - backgroundWidth && mouseX <= centerX + backgroundWidth &&
            mouseY >= centerY - 90 && mouseY <= centerY + 90) {
            moduleButtons
                .filter { it.categoryId == CategoryManager.currentPage }
                .forEach { it.mouseClicked(mouseX, mouseY, button) }
        }

        return super.mouseClicked(mouseX, mouseY, button)
    }

    override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int): Boolean {
        categoryButtons.forEach { it.mouseReleased(mouseX, mouseY, button) }
        moduleButtons.forEach { it.mouseReleased(mouseX, mouseY, button) }
        return super.mouseReleased(mouseX, mouseY, button)
    }

    override fun mouseScrolled(
        mouseX: Double,
        mouseY: Double,
        horizontalAmount: Double,
        verticalAmount: Double
    ): Boolean {
        // 使用 verticalAmount 代替 amount（垂直滚动）
        moduleButtons
            .filter { it.categoryId == CategoryManager.currentPage }
            .forEach { it.y += (verticalAmount * 10).toInt() }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount)
    }

    override fun close() {
        super.close()
        // 清理资源
    }
}

