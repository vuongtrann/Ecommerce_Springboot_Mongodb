package com.spring.ecommerce.mongodb.services;

import com.spring.ecommerce.mongodb.persistence.model.Category;
import com.spring.ecommerce.mongodb.persistence.model.variants.VariantTypes;

import java.util.List;

public interface VariantTypeServices {
    public Category addVariantType(String categoryId, List<VariantTypes> variantTypes);
    public List<VariantTypes> getVariantTypeByCategoryId (String categoryId);
    public Category removeVariantType(String categoryId, List<VariantTypes> variantTypes);

}
