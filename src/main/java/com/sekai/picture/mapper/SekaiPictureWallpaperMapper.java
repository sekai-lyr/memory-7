package com.sekai.picture.mapper;

import com.sekai.picture.dataobject.SekaiPictureWallpaperDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SekaiPictureWallpaperMapper {
    int insert(SekaiPictureWallpaperDO record);

    SekaiPictureWallpaperDO selectByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    List<SekaiPictureWallpaperDO> listByUserId(@Param("userId") Long userId,
                                            @Param("categoryId") Long categoryId,
                                            @Param("keyword") String keyword,
                                            @Param("onlyFavorite") Boolean onlyFavorite,
                                            @Param("original") Boolean original,
                                            @Param("orientation") String orientation,
                                            @Param("colorTone") String colorTone,
                                            @Param("sort") String sort,
                                            @Param("offset") int offset,
                                            @Param("limit") int limit);

    int countByUserId(@Param("userId") Long userId,
                      @Param("categoryId") Long categoryId,
                      @Param("keyword") String keyword,
                      @Param("onlyFavorite") Boolean onlyFavorite,
                      @Param("original") Boolean original,
                      @Param("orientation") String orientation,
                      @Param("colorTone") String colorTone);

    List<SekaiPictureWallpaperDO> listHotByUserId(@Param("userId") Long userId, @Param("limit") int limit);

    List<String> listColorTonesByUserId(@Param("userId") Long userId);

    SekaiPictureWallpaperDO randomByUserId(@Param("userId") Long userId,
                                           @Param("categoryId") Long categoryId,
                                           @Param("keyword") String keyword,
                                           @Param("onlyFavorite") Boolean onlyFavorite,
                                           @Param("original") Boolean original,
                                           @Param("orientation") String orientation,
                                           @Param("colorTone") String colorTone);

    int updateMeta(SekaiPictureWallpaperDO record);

    int deleteByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    int batchMoveCategory(@Param("userId") Long userId,
                          @Param("ids") List<Long> ids,
                          @Param("categoryId") Long categoryId);

    int countOriginalByUserId(@Param("userId") Long userId);

    Integer sumDownloadCountByUserId(@Param("userId") Long userId);

    int increaseDownloadCount(@Param("id") Long id, @Param("userId") Long userId);
}

