package com.sekai.picture.mapper;

import com.sekai.picture.dataobject.SekaiPictureCategoryDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SekaiPictureCategoryMapper {
    int insert(SekaiPictureCategoryDO record);

    SekaiPictureCategoryDO selectByPrimaryKey(Long id);

    List<SekaiPictureCategoryDO> listByUserId(@Param("userId") Long userId);

    int countByUserId(@Param("userId") Long userId);
}

