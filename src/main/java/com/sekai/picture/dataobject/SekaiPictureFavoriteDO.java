package com.sekai.picture.dataobject;

import com.sekai.picture.model.Favorite;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

public class SekaiPictureFavoriteDO {
    private Long id;
    private Long userId;
    private Long wallpaperId;
    private LocalDateTime createTime;

    public SekaiPictureFavoriteDO() {
    }

    public SekaiPictureFavoriteDO(Favorite favorite) {
        BeanUtils.copyProperties(favorite, this);
    }

    public Favorite convertToFavorite() {
        Favorite favorite = new Favorite();
        BeanUtils.copyProperties(this, favorite);
        return favorite;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getWallpaperId() {
        return wallpaperId;
    }

    public void setWallpaperId(Long wallpaperId) {
        this.wallpaperId = wallpaperId;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}

