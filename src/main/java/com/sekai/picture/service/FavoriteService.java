package com.sekai.picture.service;

import com.sekai.picture.model.Result;

import java.util.List;

public interface FavoriteService {
    Result<Void> toggle(Long userId, Long wallpaperId);

    Result<Void> batchSet(Long userId, List<Long> wallpaperIds, boolean favorite);

    int countByUserId(Long userId);
}

