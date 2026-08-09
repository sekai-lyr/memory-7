package com.sekai.picture.mapper;

import com.sekai.picture.dataobject.SekaiPictureUserDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SekaiPictureUserMapper {
    int insert(SekaiPictureUserDO record);

    SekaiPictureUserDO selectByPrimaryKey(Long id);

    SekaiPictureUserDO selectByUserName(@Param("userName") String userName);

    SekaiPictureUserDO selectByLoginName(@Param("loginName") String loginName);

    int updateByPrimaryKeySelective(SekaiPictureUserDO record);
}

