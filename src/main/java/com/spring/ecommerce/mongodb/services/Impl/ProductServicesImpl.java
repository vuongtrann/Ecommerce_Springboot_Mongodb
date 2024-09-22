package com.spring.ecommerce.mongodb.services.Impl;

import com.spring.ecommerce.mongodb.persistence.dto.ProductForm;
import com.spring.ecommerce.mongodb.persistence.model.*;
import com.spring.ecommerce.mongodb.persistence.model.variants.ProductVariants;
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
        product.setCreatedAt(LocalDateTime.now());
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(String productId, Product form) {
        // Tìm sản phẩm theo ID
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Cập nhật các trường cơ bản
        product.setName(form.getName());
        product.setDescription(form.getDescription());
        product.setImageURL(product.getImageURL());
        product.setPrimaryImageURL(product.getPrimaryImageURL());
        product.setBrandName(form.getBrandName());
        product.setSellingTypes(form.getSellingTypes());

        // Cập nhật danh mục
        List<Category> items = form.getCategories().stream()
                .map( item -> {
                    Optional<Category> categoryOptional = categoryServices.findById(item.getId());
                    if (categoryOptional.isPresent()) {
                        Category category = categoryOptional.get();
                        return category;
                    }
                    return null;
                }).toList();
        product.setCategories(items);

        // Cập nhật kích thước
        ProductDimensions dimensions = new ProductDimensions(form.getDimensions().getId(),
                form.getDimensions().getWeight(),
                form.getDimensions().getLength(),
                form.getDimensions().getHeight(),
                form.getDimensions().getWidth());
        dimensionRepository.save(dimensions); // Lưu lại kích thước mới nếu cần
        product.setDimensions(dimensions);

        // Cập nhật biến thể sản phẩm và tùy chọn
        if (Boolean.TRUE.equals(form.isHasVariants()) && form.getVariants() != null) {
            List<ProductVariants> updatedVariants = new ArrayList<>();

            for (ProductVariants variantForm : form.getVariants()) {
                ProductVariants variant = null;

                if (variantForm.getId() != null) {
                    // Kiểm tra xem biến thể có tồn tại không
                    Optional<ProductVariants> existingVariantOptional = productVariantsRepository.findById(variantForm.getId());
                    if (existingVariantOptional.isPresent()) {
                        variant = existingVariantOptional.get();
                        // Cập nhật các trường của biến thể
                        variant.setRating(variantForm.getRating());
                        variant.setSKU(variantForm.getSKU());
                        variant.setQuantityAvailable(variantForm.getQuantityAvailable());
                        variant.setSoldQuantity(variantForm.getSoldQuantity());
                        variant.setPrice(variantForm.getPrice());
                        variant.setSalePrice(variantForm.getSalePrice());
                        variant.setMRSP(variantForm.getMRSP());
                        variant.setImageURLs(variantForm.getImageURLs());
                        // Xử lý các tùy chọn biến thể (variantOptions)
                        if (variantForm.getVariantOptions() != null) {
                            List<VariantOptions> updatedVariantOptions = new ArrayList<>();
                            for (VariantOptions optionForm : variantForm.getVariantOptions()) {
                                VariantOptions option = null;

                                if (optionForm.getId() != null) {
                                    // Kiểm tra xem tùy chọn có tồn tại không
                                    Optional<VariantOptions> existingOptionOptional = variantOptionsRepository.findById(optionForm.getId());
                                    if (existingOptionOptional.isPresent()) {
                                        option = existingOptionOptional.get();
                                        // Cập nhật các trường của tùy chọn
                                        option.setVariantTypes(optionForm.getVariantTypes());
                                        option.setValue(optionForm.getValue());
                                        // Thêm vào danh sách đã cập nhật
                                        updatedVariantOptions.add(option);
                                    }
                                } else {
                                    // Tạo mới nếu không có ID
                                    updatedVariantOptions.add(optionForm);
                                }
                            }
                            // Lưu tất cả các tùy chọn biến thể đã cập nhật
                            variantOptionsRepository.saveAll(updatedVariantOptions);
                            variant.setVariantOptions(updatedVariantOptions); // Đặt lại danh sách variantOptions
                        }
                        updatedVariants.add(variant); // Thêm vào danh sách biến thể đã cập nhật
                    }
                } else {
                    // Nếu biến thể không có ID, coi như tạo mới và thêm vào danh sách
                    updatedVariants.add(variantForm);
                }
            }

            // Lưu tất cả các biến thể sản phẩm đã cập nhật
            productVariantsRepository.saveAll(updatedVariants);

            // Cập nhật lại sản phẩm với thông tin biến thể
            product.setHasVariants(true);
            product.setVariants(updatedVariants);
        } else {
            product.setHasVariants(false);
            product.setVariants(null);
        }


        // Cập nhật thời gian chỉnh sửa
        product.setUpdatedAt(LocalDateTime.now());

        // Lưu lại sản phẩm đã cập nhật
        return productRepository.save(product);
    }


    public Product updateRating(String productId, double rating) {
        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new RuntimeException("Product not found"));
        product.setNoOfReviews(product.getNoOfReviews() +1);
        product.setRating(rating);

        return productRepository.save(product);
    }












}
