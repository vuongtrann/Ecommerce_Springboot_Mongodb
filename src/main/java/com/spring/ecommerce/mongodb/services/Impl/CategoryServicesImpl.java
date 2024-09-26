package com.spring.ecommerce.mongodb.services.Impl;

import com.spring.ecommerce.mongodb.persistence.dto.CategoryForm;
import com.spring.ecommerce.mongodb.persistence.model.Banners;
import com.spring.ecommerce.mongodb.persistence.model.Category;
import com.spring.ecommerce.mongodb.persistence.model.Specification.SpecificationTypes;
import com.spring.ecommerce.mongodb.repository.CategoryRepository;
import com.spring.ecommerce.mongodb.repository.SpecificationTypeRepository;
import com.spring.ecommerce.mongodb.services.CategoryServices;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class CategoryServicesImpl implements CategoryServices {

    private final CategoryRepository categoryRepository;
    private final SpecificationTypeRepository specificationTypeRepository;
//    private ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);


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
        category = categoryRepository.save(category);

        if (category.getParentId()!=null && !category.getParentId().isEmpty()) {
            Category finalCategory = category;
            category.getParentId().forEach(id -> {
                Category parent = categoryRepository.findById(id).
                        orElseThrow(() -> new RuntimeException("Parent not found with id : " + id));
                parent.getSubCategories().add(finalCategory);
                categoryRepository.save(parent);
            });
        }


        return category;
    }



    @Override
    public Category addParent(String parentId, String childId) {
        Category parent = categoryRepository.findByIdCategory(parentId).orElseThrow(() -> new RuntimeException("Parent not found with id : " + parentId));
        Category child = categoryRepository.findByIdCategory(childId).orElseThrow(() -> new RuntimeException("Child not found with id : " + childId));
        parent.getSubCategories().add(child);
       return categoryRepository.save(parent);
    }



    @Override
    public void delete(String id) {
        categoryRepository.deleteById(id);
        categoryRepository.updateParents(id);
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

    @Override
    public Category addCollection(String name) {
        Category category = new Category();
        category.setName(name);
        category.setCreatedAt(LocalDateTime.now());
        category.setCollection(true);
        category.setActive(true);
        try {
            return categoryRepository.save(category);
        }catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    @Override
    public List<Category> getAllCollections() {
        try {
            return categoryRepository.findCollections();
        }catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Category addSpecificationType(String categoryId, SpecificationTypes specificationTypes) {
        Category category = categoryRepository.findById(categoryId).orElse(null);
        if (category!=null){
            specificationTypes.setCategoryId(categoryId);
            SpecificationTypes savedSpecificationTypes = specificationTypeRepository.save(specificationTypes);
            category.getSpecificationTypes().add(savedSpecificationTypes);
            return categoryRepository.save(category);
        }
        return null;
    }

    @Override
    public List<SpecificationTypes> getSpecificationTypesByCategoryID(String categoryId) {
        Category category = categoryRepository.findById(categoryId).orElse(null);
        if (category!=null){
            return category.getSpecificationTypes();
        }
        return null;
    }


//    @Override
//    public Category addParent(CategoryForm form) {
//       List<Category> items = form.getSubCategories().stream()
//               .map(item ->{
//                   Optional<Category> optional = findById(String.valueOf(item));
//                   if(optional.isPresent()) {
//                       Category category = optional.get();
//                       return category;
//                   }
//                   return null;
//               }).toList();
//       Category category = new Category();
////       Category category = new Category(form.getName(),form.getLevel(),items);
//       category.setCreatedAt(LocalDateTime.now());
//       category = save(category);
//       return category;
//    }
}
