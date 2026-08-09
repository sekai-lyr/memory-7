package com.sekai.picture.model;

public class GalleryStats {
    private int wallpaperCount;
    private int categoryCount;
    private int favoriteCount;
    private int originalCount;

    public int getWallpaperCount() {
        return wallpaperCount;
    }

    public void setWallpaperCount(int wallpaperCount) {
        this.wallpaperCount = wallpaperCount;
    }

    public int getCategoryCount() {
        return categoryCount;
    }

    public void setCategoryCount(int categoryCount) {
        this.categoryCount = categoryCount;
    }

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(int favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public int getOriginalCount() {
        return originalCount;
    }

    public void setOriginalCount(int originalCount) {
        this.originalCount = originalCount;
    }
}

