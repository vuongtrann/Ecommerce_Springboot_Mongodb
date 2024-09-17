package com.spring.ecommerce.mongodb.services.Impl;

import com.spring.ecommerce.mongodb.persistence.dto.ProductForm;
import com.spring.ecommerce.mongodb.persistence.model.*;
import com.spring.ecommerce.mongodb.persistence.model.variants.OptionValues;
import com.spring.ecommerce.mongodb.persistence.model.variants.ProductVariants;
import com.spring.ecommerce.mongodb.persistence.model.variants.VariantOptions;
import com.spring.ecommerce.mongodb.repository.*;
import com.spring.ecommerce.mongodb.services.CategoryServices;
import com.spring.ecommerce.mongodb.services.ProductServices;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class ProductServicesImpl implements ProductServices {


    private final ProductRepository productRepository;

    private final CategoryServices categoryServices;

    private final ProductDimensionRepository dimensionRepository;

    private final VariantOptionsRepository variantOptionsRepository;

    private final ProductVariantsRepository productVariantsRepository;

    private final OptionValuesRepository optionValuesRepository;


    @Override
    public List<Product> findAll(){
        return productRepository.findAll();
    }

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }


    @Override
    public Product update(Product product) {
        return null;
    }

    @Override
    public Optional<Product> findById(String id){
        return productRepository.findById(id);
    }

    @Override
    public Product addProduct(ProductForm form) {
        List<Category> items = form.getCategories().stream()
                .map( item -> {
                Optional<Category> categoryOptional = categoryServices.findById(item);
                if (categoryOptional.isPresent()) {
                    Category category = categoryOptional.get();
                    return category;
                }
                return null;
                }).toList();
        ProductDimensions dimensions = new ProductDimensions(form.getDimensions().getId(),form.getDimensions().getWeight(),form.getDimensions().getLength(),form.getDimensions().getHeight(),form.getDimensions().getWidth());
        dimensions = dimensionRepository.save(dimensions);


        Product product = new Product(form.getName(),form.getDescription(),form.getMsrp(),form.getSalePrice(),
                form.getPrice(),form.getQuantity(),form.getSKU(),form.getSellingTypes(),items,dimensions);

        product.setCreatedAt(LocalDateTime.now());
        Product savedProduct = save(product);
        if(form.isHasVariants()) {
            if (form.getVariants() != null) {
                for (ProductVariants variant : form.getVariants()) {
                    variant.setProductId(savedProduct.getId());
                    if (variant.getVariantOptions() != null) {
                        for (VariantOptions option : variant.getVariantOptions()) {
                            variantOptionsRepository.save(option);
                            if (option.getOptionValues()!=null){
                                for (OptionValues optionValues : option.getOptionValues()){
                                    optionValuesRepository.save(optionValues);
                                }
                            }
                        }
                    }
                    productVariantsRepository.save(variant);
                }
            }
            savedProduct.setVariants(form.getVariants());
            savedProduct.setHasVariants(form.isHasVariants());
        }else {
            savedProduct.setHasVariants(false);
        }
        return productRepository.save(savedProduct);
    }
}
