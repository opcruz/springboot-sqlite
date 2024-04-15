package com.demo.sqlite.service.impl;

import com.demo.sqlite.model.entity.Category;
import com.demo.sqlite.repository.CategoryRepository;
import com.demo.sqlite.service.LookupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LookupServiceImpl implements LookupService {

    private final CategoryRepository categoryRepository;

    public LookupServiceImpl(@Autowired CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Iterable<Category> allCategories() {
        return categoryRepository.findAll();
    }

}
