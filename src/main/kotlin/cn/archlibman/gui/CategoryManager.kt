package cn.archlibman.gui

object CategoryManager {
    var currentPage = 0
        set  // 如果需要可以限制外部修改

    var openDragScreen = false
        private set

    init {
        openDragScreen = currentPage == 4
    }
}