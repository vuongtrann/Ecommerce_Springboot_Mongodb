package com.spring.ecommerce.mongodb.services;

import com.spring.ecommerce.mongodb.persistence.dto.CategoryForm;
import com.spring.ecommerce.mongodb.persistence.model.Category;
import com.spring.ecommerce.mongodb.persistence.model.Product;
import com.spring.ecommerce.mongodb.persistence.model.Specification.SpecificationTypes;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public interface CategoryServices {
    public List<Category> findAll();
    public Optional<Category> findById(String id);
    public Category save(Category category);
    public Category addParent(String parentId, String childId);
    public void delete(String id);
    public Category update(String categoryId,Category category);
    public List<Category> getTopCategory(int limit);

    /** Collections */
    public Category addCollection (String name);
    public List<Category> getAllCollections();

    /** SpecificationType */
    public Category addSpecificationType(String categoryId, SpecificationTypes specificationTypes);
    public List<SpecificationTypes> getSpecificationTypesByCategoryID(String categoryId);
}
