package com.sekai.picture.service;

import com.sekai.picture.model.Result;
import com.sekai.picture.model.Wallpaper;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface WallpaperService {
    Result<Wallpaper> upload(Wallpaper wallpaper, MultipartFile file);

    Wallpaper getByIdAndUserId(Long id, Long userId);

    List<Wallpaper> listByUserId(Long userId, Long categoryId, String keyword, boolean onlyFavorite,
                                  Boolean original, String orientation, String colorTone, String sort,
                                  int page, int pageSize);

    int countByUserId(Long userId, Long categoryId, String keyword, boolean onlyFavorite,
                      Boolean original, String orientation, String colorTone);

    List<Wallpaper> listHotByUserId(Long userId, int limit);

    List<String> listColorTonesByUserId(Long userId);

    Wallpaper randomByUserId(Long userId, Long categoryId, String keyword, boolean onlyFavorite,
                             Boolean original, String orientation, String colorTone);

    Result<Wallpaper> updateMeta(Long userId, Wallpaper wallpaper);

    Result<Void> delete(Long userId, Long wallpaperId);

    Result<Void> batchMoveCategory(Long userId, List<Long> wallpaperIds, Long categoryId);

    int countOriginalByUserId(Long userId);

    int sumDownloadCountByUserId(Long userId);

    void increaseDownloadCount(Long id, Long userId);
}

