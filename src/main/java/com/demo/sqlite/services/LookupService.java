package com.demo.sqlite.services;

import com.demo.sqlite.models.Category;
import com.demo.sqlite.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LookupService {

    private final CategoryRepository categoryRepository;

    public LookupService(@Autowired CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Iterable<Category> allCategories() {
        return categoryRepository.findAll();
    }

}
