package com.spring.ecommerce.mongodb.services.Impl;

import com.spring.ecommerce.mongodb.persistence.dto.ProductForm;
import com.spring.ecommerce.mongodb.persistence.model.*;
import com.spring.ecommerce.mongodb.persistence.model.variants.ProductVariants;
import com.spring.ecommerce.mongodb.persistence.model.variants.VariantOptions;
import com.spring.ecommerce.mongodb.persistence.model.variants.VariantType;
import com.spring.ecommerce.mongodb.persistence.projection.ProductProjection;
import com.spring.ecommerce.mongodb.repository.*;
import com.spring.ecommerce.mongodb.services.CategoryServices;
import com.spring.ecommerce.mongodb.services.CollectionServices;
import com.spring.ecommerce.mongodb.services.ProductServices;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
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
    public Page<ProductProjection> findAll(Pageable pageable){
        return productRepository.findAllProjectedBy(pageable);
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
        // Lấy danh sách categories
        List<Category> items = form.getCategories().stream()
                .map(item -> categoryServices.findById(item).orElse(null))
                .filter(Objects::nonNull) // Loại bỏ các category null
                .collect(Collectors.toList());

        // Tạo ProductDimensions
        ProductDimensions dimensions = new ProductDimensions(
                form.getDimensions().getId(),
                form.getDimensions().getWeight(),
                form.getDimensions().getLength(),
                form.getDimensions().getHeight(),
                form.getDimensions().getWidth()
        );
        dimensions = dimensionRepository.save(dimensions);

        // Tạo Product với options
        Product product = new Product(
                form.getName(),
                form.getDescription(),
                form.getBrandName(),
                form.getSellingTypes(),
                items,
                dimensions
        );
        // xu ly variant
        if (Boolean.TRUE.equals(form.isHasVariants()) && form.getVariants() != null) {
            product.setOptions(form.getOptions());
            List<ProductVariants> newVariants = new ArrayList<>();

            // Tạo các biến thể mới từ form
            for (ProductVariants variantForm : form.getVariants()) {
                // Tạo danh sách VariantOptions dựa trên options từ form
                List<VariantOptions> variantOptionsList = Arrays.asList(
                        new VariantOptions(VariantType.COLOR, variantForm.getVariantOptions().get(0).getValue()),
                        new VariantOptions(VariantType.RAM, variantForm.getVariantOptions().get(1).getValue()),
                        new VariantOptions(VariantType.STORAGE, variantForm.getVariantOptions().get(2).getValue())
                );
                variantOptionsRepository.saveAll(variantOptionsList);
                // Tạo mới biến thể
                ProductVariants newVariant = new ProductVariants();
                newVariant.setVariantOptions(variantOptionsList);
                newVariant.setRating(variantForm.getRating());
                newVariant.setSKU(variantForm.getSKU());
                newVariant.setQuantityAvailable(variantForm.getQuantityAvailable());
                newVariant.setSoldQuantity(variantForm.getSoldQuantity());
                newVariant.setPrice(variantForm.getPrice());
                newVariant.setSalePrice(variantForm.getSalePrice());
                newVariant.setMRSP(variantForm.getMRSP());
                newVariant.setImageURLs(variantForm.getImageURLs());

                newVariants.add(newVariant);
            }

            // Lưu tất cả ProductVariants mới
            productVariantsRepository.saveAll(newVariants);

            // Cập nhật lại sản phẩm với thông tin biến thể mới
            product.setHasVariants(true);
            product.setVariants(newVariants);
        } else {
            product.setHasVariants(false);
            product.setVariants(null);
        }


        //Xu ly spetification
        product.setSpecifications(form.getSpecifications());
        // Cập nhật thời gian tạo và lưu sản phẩm
        product.setCreatedAt(LocalDateTime.now());
        return productRepository.save(product);
    }



    @Override
    public Product updateProduct(String productId, ProductForm form) {
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
                    Optional<Category> categoryOptional = categoryServices.findById(item);
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
            product.setOptions(form.getOptions());
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
                    List<VariantOptions> variantOptionsList = Arrays.asList(
                            new VariantOptions(VariantType.COLOR, variantForm.getVariantOptions().get(0).getValue()),
                            new VariantOptions(VariantType.RAM, variantForm.getVariantOptions().get(1).getValue()),
                            new VariantOptions(VariantType.STORAGE, variantForm.getVariantOptions().get(2).getValue())
                    );
                    ProductVariants newVariant = new ProductVariants();
                    newVariant.setVariantOptions(variantOptionsList);
                    newVariant.setRating(variantForm.getRating());
                    newVariant.setSKU(variantForm.getSKU());
                    newVariant.setQuantityAvailable(variantForm.getQuantityAvailable());
                    newVariant.setSoldQuantity(variantForm.getSoldQuantity());
                    newVariant.setPrice(variantForm.getPrice());
                    newVariant.setSalePrice(variantForm.getSalePrice());
                    newVariant.setMRSP(variantForm.getMRSP());
                    newVariant.setImageURLs(variantForm.getImageURLs());
                    updatedVariants.add(newVariant);
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
