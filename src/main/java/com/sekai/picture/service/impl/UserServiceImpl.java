package com.sekai.picture.service.impl;

import com.sekai.picture.dataobject.SekaiPictureUserDO;
import com.sekai.picture.mapper.SekaiPictureUserMapper;
import com.sekai.picture.model.Result;
import com.sekai.picture.model.User;
import com.sekai.picture.service.UserService;
import com.sekai.picture.util.PasswordHashUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private SekaiPictureUserMapper userMapper;

    @Override
    @Transactional
    public Result<User> register(User user) {
        if (user == null) {
            return Result.fail("用户信息不能为空");
        }
        user.setUserName(trimToNull(user.getUserName()));
        user.setPassword(trimToNull(user.getPassword()));
        user.setNickName(trimToNull(user.getNickName()));
        if (user.getUserName() == null || user.getPassword() == null) {
            return Result.fail("用户名和密码不能为空");
        }
        if (user.getNickName() == null) {
            user.setNickName(user.getUserName());
        }
        if (userMapper.selectByUserName(user.getUserName()) != null) {
            return Result.fail("用户名已存在");
        }
        user.setPassword(PasswordHashUtil.hash(user.getPassword()));
        SekaiPictureUserDO userDO = new SekaiPictureUserDO(user);
        userMapper.insert(userDO);
        return Result.ok(userMapper.selectByPrimaryKey(userDO.getId()).convertToUser(), "注册成功");
    }

    @Override
    @Transactional
    public Result<User> login(String userName, String password) {
        userName = trimToNull(userName);
        password = trimToNull(password);
        if (userName == null || password == null) {
            return Result.fail("用户名和密码不能为空");
        }
        SekaiPictureUserDO dbUser = userMapper.selectByLoginName(userName);
        if (dbUser == null) {
            return Result.fail("用户不存在或登录名错误");
        }
        if (!PasswordHashUtil.matches(password, dbUser.getPassword())) {
            return Result.fail("密码错误");
        }
        if (!PasswordHashUtil.isHashed(dbUser.getPassword())) {
            dbUser.setPassword(PasswordHashUtil.hash(password));
            userMapper.updateByPrimaryKeySelective(dbUser);
        }
        return Result.ok(dbUser.convertToUser(), "登录成功");
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
