package com.sekai.picture.mapper;

import com.sekai.picture.dataobject.SekaiPictureFavoriteDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SekaiPictureFavoriteMapper {
    int insertIgnore(SekaiPictureFavoriteDO record);

    int deleteByUserIdAndWallpaperId(@Param("userId") Long userId, @Param("wallpaperId") Long wallpaperId);

    int countByUserId(@Param("userId") Long userId);

    SekaiPictureFavoriteDO selectByUserIdAndWallpaperId(@Param("userId") Long userId, @Param("wallpaperId") Long wallpaperId);
}

