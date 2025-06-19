package me.wavelength.baseclient.gui.clickgui;

import java.util.ArrayList;
import java.util.List;

public class CategoryManager {
    private static CategoryManager instance;
    static int currentPage = 0;
    private boolean openDragScreen = false;
    private List<Category> categories = new ArrayList<>();

    private CategoryManager() {
        // 初始化分类
        categories.add(new Category("Combat", 0));
        categories.add(new Category("Movement", 1));
        categories.add(new Category("Render", 2));
        categories.add(new Category("Player", 3));
        categories.add(new Category("World", 4));
    }

    public static CategoryManager getInstance() {
        if (instance == null) {
            instance = new CategoryManager();
        }
        return instance;
    }

    public void setCurrentPage(int page) {
        this.currentPage = page;

        // 更新所有分类的状态
        for (Category category : categories) {
            category.setActive(category.getId() == page);
        }

        if (currentPage == 4) {
            openDragScreen = true;
        } else {
            openDragScreen = false;
        }
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public boolean isOpenDragScreen() {
        return openDragScreen;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public static class Category {
        private String name;
        private int id;
        private boolean active;

        public Category(String name, int id) {
            this.name = name;
            this.id = id;
            this.active = false;
        }

        public String getName() {
            return name;
        }

        public int getId() {
            return id;
        }

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }
    }
}
