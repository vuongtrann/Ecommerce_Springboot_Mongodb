package com.spring.ecommerce.mongodb.services.Impl;

import com.spring.ecommerce.mongodb.persistence.dto.ProductForm;
import com.spring.ecommerce.mongodb.persistence.model.*;
import com.spring.ecommerce.mongodb.persistence.model.variants.VariantOptions;
import com.spring.ecommerce.mongodb.repository.*;
import com.spring.ecommerce.mongodb.services.CategoryServices;
import com.spring.ecommerce.mongodb.services.CollectionServices;
import com.spring.ecommerce.mongodb.services.ProductServices;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class ProductServicesImpl implements ProductServices {

    private final ProductRepository productRepository;

    private final CategoryServices categoryServices;

    private final ProductDimensionRepository dimensionRepository;

    private final VariantOptionsRepository variantOptionsRepository;

    private final ProductVariantsRepository productVariantsRepository;


    private final CollectionServices collectionServices;


    @Override
    public List<Product> findAll(){
        return productRepository.findAll();
    }

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }


//    @Override
//    public Product update(Product product) {
//        return null;
//    }

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

        Product product = new Product(form.getName(),form.getDescription(),form.getBrandName(),form.getSellingTypes(),items,dimensions);

//
//        Product savedProduct = save(product);

        if (Boolean.TRUE.equals(form.isHasVariants()) && form.getVariants() != null) {
            List<VariantOptions> allVariantOptions = form.getVariants().stream()
                    .filter(variant -> variant.getVariantOptions() != null)
                    .flatMap(variant -> variant.getVariantOptions().stream())
                    .collect(Collectors.toList());

            if (!allVariantOptions.isEmpty()) {
                variantOptionsRepository.saveAll(allVariantOptions); // Lưu tất cả VariantOptions
            }

            productVariantsRepository.saveAll(form.getVariants()); // Lưu tất cả ProductVariants

            // Cập nhật lại sản phẩm
            product.setHasVariants(true);
            product.setVariants(form.getVariants());
        }
        else {
            product.setHasVariants(false);
            product.setVariants(null);
        }
        if(form.isHasCollection()){
            List<Collection> collections = form.getCollections().stream()
                    .map( collection-> {
                        Optional<Collection> categoryOptional = collectionServices.findById(collection);
                        if (categoryOptional.isPresent()) {
                            Collection myCollection = categoryOptional.get();
                            return myCollection;
                        }
                        return null;
                    }).toList();
            product.setHasCollection(true);
            product.setCollections(collections);
        }else {
            product.setHasCollection(false);
            product.setCollections(null);
        }
        product.setCreatedAt(LocalDateTime.now());
        return productRepository.save(product);
    }

//    @Override
//    public Product updateProduct(String productId, ProductForm form) {
//        // Tìm sản phẩm theo ID
//        Product product = productRepository.findById(productId)
//                .orElseThrow(() -> new RuntimeException("Product not found"));
//
//        // Cập nhật các trường cơ bản
//        product.setName(form.getName());
//        product.setDescription(form.getDescription());
//        product.setImageURL(product.getImageURL());
//        product.setPrimaryImageURL(product.getPrimaryImageURL());
//        product.setBrandName(form.getBrandName());
//        product.setSellingTypes(form.getSellingTypes());
//
//        // Cập nhật danh mục
//        List<Category> items = form.getCategories().stream()
//                .map( item -> {
//                    Optional<Category> categoryOptional = categoryServices.findById(item);
//                    if (categoryOptional.isPresent()) {
//                        Category category = categoryOptional.get();
//                        return category;
//                    }
//                    return null;
//                }).toList();
//        product.setCategories(items);
//
//        // Cập nhật kích thước
//        ProductDimensions dimensions = new ProductDimensions(form.getDimensions().getId(),
//                form.getDimensions().getWeight(),
//                form.getDimensions().getLength(),
//                form.getDimensions().getHeight(),
//                form.getDimensions().getWidth());
//        dimensionRepository.save(dimensions); // Lưu lại kích thước mới nếu cần
//        product.setDimensions(dimensions);
//
//        // Cập nhật biến thể
//        if (Boolean.TRUE.equals(form.isHasVariants()) && form.getVariants() != null) {
//            List<VariantOptions> allVariantOptions = form.getVariants().stream()
//                    .filter(variant -> variant.getVariantOptions() != null)
//                    .flatMap(variant -> variant.getVariantOptions().stream())
//                    .collect(Collectors.toList());
//
//            if (!allVariantOptions.isEmpty()) {
//                variantOptionsRepository.saveAll(allVariantOptions); // Lưu tất cả VariantOptions
//            }
//            product.getVariants()
//            productVariantsRepository.saveAll(form.getVariants()); // Lưu tất cả ProductVariants
//
//            // Cập nhật lại sản phẩm
//            product.setHasVariants(true);
//            product.setVariants(form.getVariants());
//        } else {
//            product.setHasVariants(false);
//            product.setVariants(null);
//        }
//
//        // Cập nhật bộ sưu tập
//        if (form.isHasCollection()) {
//            List<Collection> collections = form.getCollections().stream()
//                    .map(collection -> collectionServices.findById(collection).orElse(null))
//                    .filter(Objects::nonNull)
//                    .collect(Collectors.toList());
//            product.setHasCollection(true);
//            product.setCollections(collections);
//        } else {
//            product.setHasCollection(false);
//            product.setCollections(null);
//        }
//
//        // Cập nhật thời gian chỉnh sửa
//        product.setUpdatedAt(LocalDateTime.now());
//
//        // Lưu lại sản phẩm đã cập nhật
//        return productRepository.save(product);
//    }


    public Product updateRating(String productId, double rating) {
        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new RuntimeException("Product not found"));
        product.setNoOfReviews(product.getNoOfReviews() +1);
        product.setRating(rating);

        return productRepository.save(product);
    }












}
