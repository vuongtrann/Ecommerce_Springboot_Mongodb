package com.spring.ecommerce.mongodb.services.Impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.ecommerce.mongodb.persistence.dto.CategoryForm;
import com.spring.ecommerce.mongodb.persistence.model.Banners;
import com.spring.ecommerce.mongodb.persistence.model.Category;
import com.spring.ecommerce.mongodb.repository.CategoryRepository;
import com.spring.ecommerce.mongodb.services.CategoryServices;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class CategoryServicesImpl implements CategoryServices {

    private final CategoryRepository categoryRepository;
    private ObjectMapper mapper = new ObjectMapper();
    @Autowired
    private BannerServiceImpl  bannerService;

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Optional<Category> findById(String id) {
        return categoryRepository.findById(id);
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
    public List<Category> getTopCategory(int limit){
        List<Map<String, Objects>> list = categoryRepository.findTopCategorie(10);

        return null;
    };

}
