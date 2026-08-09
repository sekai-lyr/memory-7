package com.sekai.picture.dataobject;

import com.sekai.picture.model.User;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

public class SekaiPictureUserDO {
    private Long id;
    private String userName;
    private String password;
    private String nickName;
    private String avatar;
    private LocalDateTime createTime;

    public SekaiPictureUserDO() {
    }

    public SekaiPictureUserDO(User user) {
        BeanUtils.copyProperties(user, this);
    }

    public User convertToUser() {
        User user = new User();
        BeanUtils.copyProperties(this, user);
        return user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}

