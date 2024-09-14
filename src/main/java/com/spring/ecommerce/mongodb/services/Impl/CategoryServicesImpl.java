package com.spring.ecommerce.mongodb.services.Impl;

import com.spring.ecommerce.mongodb.persistence.dto.CategoryForm;
import com.spring.ecommerce.mongodb.persistence.model.Category;
import com.spring.ecommerce.mongodb.repository.CategoryRepository;
import com.spring.ecommerce.mongodb.services.CategoryServices;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class CategoryServicesImpl implements CategoryServices {
    private final CategoryRepository categoryRepository;

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
        return categoryRepository.save(category);
    }

    @Override
    public Category addParent(CategoryForm form) {
       List<Category> items = form.getCategories().stream()
               .map(item ->{
                   Optional<Category> optional = categoryRepository.findById(String.valueOf(item));
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
}
