package com.spring.ecommerce.mongodb.services.Impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.DocumentToDBRefTransformer;
import com.spring.ecommerce.mongodb.persistence.dto.CategoryForm;
import com.spring.ecommerce.mongodb.persistence.model.Banners;
import com.spring.ecommerce.mongodb.persistence.model.Category;
import com.spring.ecommerce.mongodb.repository.CategoryRepository;
import com.spring.ecommerce.mongodb.services.CategoryServices;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class CategoryServicesImpl implements CategoryServices {

    private final CategoryRepository categoryRepository;
    private ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    @Autowired
    private BannerServiceImpl  bannerService;

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Optional<Category> findById(String id) {
        return categoryRepository.findByIdCategory(id);
    }

    @Override
    public Category save(Category category) {
        category.setCreatedAt(LocalDateTime.now());
        if (category.getBanner()!=null ) {
           Banners banners=  bannerService.findById(category.getBanner().getId())
                   .orElseThrow(() -> new RuntimeException("Banner not found"));
           category.setBanner(banners);
        }

        if (category.getParentIds()!=null && !category.getParentIds().isEmpty()) {
            category.getParentIds().forEach(id -> {
                Category parent = categoryRepository.findById(id).
                        orElseThrow(() -> new RuntimeException("Parent not found with id : " + id));
                category.getParents().add(parent);
            });
        }

        return categoryRepository.save(category);
    }

    @Override
    public Category addParent(CategoryForm form) {
       List<Category> items = form.getSubCategories().stream()
               .map(item ->{
                   Optional<Category> optional = findById(String.valueOf(item));
                   if(optional.isPresent()) {
                       Category category = optional.get();
                       return category;
                   }
                   return null;
               }).toList();
       Category category = new Category(form.getName(),form.getLevel(),items);
       category.setCreatedAt(LocalDateTime.now());
       category = save(category);
       return category;
    }

    @Override
    public void delete(String id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public Category update(String categoryId, Category category) {
        return null;
    }


    @Override
    public List<Category> getTopCategory(int limit) {
        try {
            List<Category>  list =categoryRepository.findTopCategorie(limit);
            return list;
        }catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }


}
