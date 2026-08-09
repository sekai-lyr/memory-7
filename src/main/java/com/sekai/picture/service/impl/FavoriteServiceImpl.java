package com.sekai.picture.service.impl;

import com.sekai.picture.dataobject.SekaiPictureFavoriteDO;
import com.sekai.picture.mapper.SekaiPictureFavoriteMapper;
import com.sekai.picture.model.Favorite;
import com.sekai.picture.model.Result;
import com.sekai.picture.service.FavoriteService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    @Resource
    private SekaiPictureFavoriteMapper favoriteMapper;

    @Override
    @Transactional
    public Result<Void> toggle(Long userId, Long wallpaperId) {
        if (favoriteMapper.selectByUserIdAndWallpaperId(userId, wallpaperId) != null) {
            favoriteMapper.deleteByUserIdAndWallpaperId(userId, wallpaperId);
            return Result.ok(null, "已取消收藏");
        }
        Favorite favorite = new Favorite();
        favorite.setUserId(userId);
        favorite.setWallpaperId(wallpaperId);
        favoriteMapper.insertIgnore(new SekaiPictureFavoriteDO(favorite));
        return Result.ok(null, "收藏成功");
    }

    @Override
    @Transactional
    public Result<Void> batchSet(Long userId, List<Long> wallpaperIds, boolean favorite) {
        if (wallpaperIds == null || wallpaperIds.isEmpty()) {
            return Result.fail("请先选择壁纸");
        }
        int changed = 0;
        for (Long wallpaperId : wallpaperIds) {
            if (wallpaperId == null) {
                continue;
            }
            if (favorite) {
                Favorite item = new Favorite();
                item.setUserId(userId);
                item.setWallpaperId(wallpaperId);
                changed += favoriteMapper.insertIgnore(new SekaiPictureFavoriteDO(item));
            } else {
                changed += favoriteMapper.deleteByUserIdAndWallpaperId(userId, wallpaperId);
            }
        }
        String action = favorite ? "批量收藏" : "批量取消收藏";
        return Result.ok(null, action + "完成，处理 " + changed + " 张壁纸");
    }

    @Override
    public int countByUserId(Long userId) {
        return favoriteMapper.countByUserId(userId);
    }
}
