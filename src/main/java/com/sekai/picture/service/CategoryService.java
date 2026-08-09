package com.sekai.picture.service;

import com.sekai.picture.model.Category;
import com.sekai.picture.model.Result;

import java.util.List;

public interface CategoryService {
    Result<Category> create(Category category);

    List<Category> listByUserId(Long userId);

    int countByUserId(Long userId);
}

