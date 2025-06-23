package cn.archlibman.modules

import cn.archlibman.Category
import cn.archlibman.Module

/**
 * NoHurtCam module
 *
 * Disables the hurt camera effect when player gets hurt.
 */
object NoHurtCam : Module(
    name = "NoHurtCam",
    description = "Disables the hurt camera effect",
    category = Category.COMBAT
) {
    @JvmStatic
    fun isActive() = enabled // 提供静态访问方法
}

