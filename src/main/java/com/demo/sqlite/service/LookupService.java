package com.demo.sqlite.service;

import com.demo.sqlite.model.entity.Category;

public interface LookupService {

    Iterable<Category> allCategories();

}

