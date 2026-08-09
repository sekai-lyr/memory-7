package com.sekai.picture.service;

import com.sekai.picture.model.Result;
import com.sekai.picture.model.User;

public interface UserService {
    Result<User> register(User user);

    Result<User> login(String userName, String password);
}

