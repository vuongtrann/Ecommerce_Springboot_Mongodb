package com.spring.ecommerce.mongodb.services.Impl;

import com.spring.ecommerce.mongodb.persistence.dto.ProductForm;
import com.spring.ecommerce.mongodb.persistence.model.*;
import com.spring.ecommerce.mongodb.persistence.model.variants.ProductVariants;
import com.spring.ecommerce.mongodb.persistence.model.variants.VariantOptions;
import com.spring.ecommerce.mongodb.persistence.model.variants.VariantTypes;
import com.spring.ecommerce.mongodb.persistence.projection.ProductProjection;
import com.spring.ecommerce.mongodb.repository.*;
import com.spring.ecommerce.mongodb.services.CategoryServices;
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

    private final VariantTypeRepository variantTypeRepository;




    @Override
    public Page<ProductProjection> findAll(Pageable pageable){
        return productRepository.findAllProjectedBy(pageable);
    }

    @Override
    public List<Product> findAllPopularProducts(int limit) {
        return productRepository.findAllPopularProduct(limit);
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

        List<Category> collections = form.getCollections().stream()
                .map(collection -> categoryServices.findById(collection).orElse(null))
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


        product.setCollections(collections);
        if(!form.isHasVariants()){
            product.setSKU(form.getSKU());
            product.setQuantityAvailable(form.getQuantityAvailable());
            product.setSoldQuantity(form.getSoldQuantity());
            product.setPrice(form.getPrice());
            product.setSalePrice(form.getSalePrice());
            product.setMRSP(form.getMRSP());
        }


        if (form.isHasVariants() && form.getVariants() != null) {
            product.setOptions(form.getOptions());
            List<ProductVariants> newVariants = new ArrayList<>();

            List<VariantTypes> availableVariantTypes = new ArrayList<>();
            for (Category category : items) {
                // Giả sử mỗi category có một danh sách các VariantTypes
                availableVariantTypes.addAll(category.getVariantTypes());
            }
            // Tạo biến thể từ form
            for (ProductVariants variantForm : form.getVariants()) {
                List<VariantOptions> variantOptionsList = new ArrayList<>();

                // Tạo danh sách VariantOptions dựa trên các tùy chọn
                for (VariantOptions option : variantForm.getVariantOptions()) {
                    // Tìm VariantTypes tương ứng với loại từ danh sách VariantTypes của các Category
                    VariantTypes variantType = availableVariantTypes.stream()
                            .filter(vt -> vt.getType().equalsIgnoreCase(option.getVariantTypes()))
                            .findFirst()
                            .orElse(null);

                    // Nếu tìm thấy loại biến thể, tạo VariantOptions
                    if (variantType != null) {
                        VariantOptions variantOption = new VariantOptions(variantType.getType(), option.getValue());

                        // Lưu VariantOptions và lấy lại đối tượng với id đã được gán
                        variantOption = variantOptionsRepository.save(variantOption);

                        // Thêm VariantOptions đã được lưu vào danh sách
                        variantOptionsList.add(variantOption);
                    }
                }

                // Sau khi tất cả các VariantOptions được tạo xong, thêm vào biến thể
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

            // Lưu tất cả ProductVariants mới chỉ một lần
            productVariantsRepository.saveAll(newVariants);
            product.setHasVariants(true);
            product.setVariants(newVariants);
        } else {
            product.setHasVariants(false);
            product.setVariants(null);
        }

        product.setSpecifications(form.getSpecifications());
        product.setCreatedAt(LocalDateTime.now());

        return productRepository.save(product);
    }



    @Override
    public Product updateProduct(String productId, ProductForm form) {
        // Tìm sản phẩm theo ID
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Cập nhật danh mục
        List<Category> items = form.getCategories().stream()
                .map(item -> categoryServices.findById(item).orElse(null))
                .filter(Objects::nonNull) // Loại bỏ các category null
                .collect(Collectors.toList());

        List<Category> collections = form.getCollections().stream()
                .map(collection -> categoryServices.findById(collection).orElse(null))
                .filter(Objects::nonNull) // Loại bỏ các category null
                .collect(Collectors.toList());

        // Cập nhật các trường cơ bản, chỉ cập nhật nếu giá trị mới không phải null
         product.setName(form.getName());
         product.setDescription(form.getDescription());
         product.setImageURL(form.getImageURL());
         product.setPrimaryImageURL(form.getPrimaryImageURL());
         product.setBrandName(form.getBrandName());
         product.setSellingTypes(form.getSellingTypes());
         product.setCategories(items);
         product.setCollections(collections);



        // Cập nhật kích thước sản phẩm nếu có
        if (form.getDimensions() != null) {
            ProductDimensions dimensions = new ProductDimensions(
                    form.getDimensions().getId(),
                    form.getDimensions().getWeight(),
                    form.getDimensions().getLength(),
                    form.getDimensions().getHeight(),
                    form.getDimensions().getWidth()
            );
            dimensions = dimensionRepository.save(dimensions);
            product.setDimensions(dimensions);
        }

        if(!form.isHasVariants()){
            product.setSKU(form.getSKU());
            product.setQuantityAvailable(form.getQuantityAvailable());
            product.setSoldQuantity(form.getSoldQuantity());
            product.setPrice(form.getPrice());
            product.setSalePrice(form.getSalePrice());
            product.setMRSP(form.getMRSP());
        }



        if (Boolean.TRUE.equals(form.isHasVariants()) && form.getVariants() != null) {
            // Khi chuyển từ false sang true, đảm bảo rằng danh sách biến thể được khởi tạo đúng cách
            if (!product.isHasVariants()) {
                product.setVariants(new ArrayList<>());  // Khởi tạo danh sách biến thể mới nếu hasVariants chuyển từ false sang true
            }

            product.setOptions(form.getOptions());
            List<ProductVariants> updatedVariants = new ArrayList<>();
            List<VariantTypes> availableVariantTypes = new ArrayList<>();

            // Lấy danh sách các VariantTypes từ các category
            for (Category category : product.getCategories()) {
                availableVariantTypes.addAll(category.getVariantTypes());
            }

            // Cập nhật hoặc tạo mới biến thể
            for (ProductVariants variantForm : form.getVariants()) {
                ProductVariants variant;

                // Nếu biến thể đã tồn tại, cập nhật lại
                if (variantForm.getId() != null) {
                    variant = productVariantsRepository.findById(variantForm.getId())
                            .orElseThrow(() -> new RuntimeException("Variant not found"));

                    // Cập nhật các trường nếu có giá trị, tránh cập nhật giá trị null
                    variant.setRating(variantForm.getRating());
                    variant.setSKU(variantForm.getSKU());
                    variant.setQuantityAvailable(variantForm.getQuantityAvailable());
                    variant.setSoldQuantity(variantForm.getSoldQuantity());
                    variant.setPrice(variantForm.getPrice());
                    variant.setSalePrice(variantForm.getSalePrice());
                     variant.setMRSP(variantForm.getMRSP());
                    variant.setImageURLs(variantForm.getImageURLs());

                    // Xử lý các VariantOptions
                    if (variantForm.getVariantOptions() != null) {
                        List<VariantOptions> updatedVariantOptions = new ArrayList<>();

                        for (VariantOptions optionForm : variantForm.getVariantOptions()) {
                            VariantOptions option;

                            // Nếu Option đã có ID, cập nhật lại
                            if (optionForm.getId() != null) {
                                option = variantOptionsRepository.findById(optionForm.getId())
                                        .orElseThrow(() -> new RuntimeException("Variant Option not found"));
                                option.setValue(optionForm.getValue());  // Cập nhật giá trị nếu đã tồn tại
                            } else {
                                // Nếu Option chưa có ID, tạo mới
                                VariantTypes variantType = availableVariantTypes.stream()
                                        .filter(vt -> vt.getType().equalsIgnoreCase(optionForm.getVariantTypes()))
                                        .findFirst()
                                        .orElse(null);

                                if (variantType != null) {
                                    option = new VariantOptions(variantType.getType(), optionForm.getValue());
                                    variantOptionsRepository.save(option);  // Lưu mới Option
                                } else {
                                    throw new RuntimeException("Variant type not found");
                                }
                            }

                            updatedVariantOptions.add(option);
                        }
                        variant.setVariantOptions(updatedVariantOptions);  // Cập nhật danh sách VariantOptions
                    }
                } else {
                    // Nếu biến thể chưa tồn tại, tạo mới
                    variant = new ProductVariants();
                     variant.setRating(variantForm.getRating());
                    variant.setSKU(variantForm.getSKU());
                     variant.setQuantityAvailable(variantForm.getQuantityAvailable());
                   variant.setSoldQuantity(variantForm.getSoldQuantity());
                     variant.setPrice(variantForm.getPrice());
                    variant.setSalePrice(variantForm.getSalePrice());
                     variant.setMRSP(variantForm.getMRSP());
                     variant.setImageURLs(variantForm.getImageURLs());

                    // Tạo mới VariantOptions
                    List<VariantOptions> newVariantOptions = new ArrayList<>();
                    for (VariantOptions optionForm : variantForm.getVariantOptions()) {
                        VariantTypes variantType = availableVariantTypes.stream()
                                .filter(vt -> vt.getType().equalsIgnoreCase(optionForm.getVariantTypes()))
                                .findFirst()
                                .orElse(null);

                        if (variantType != null) {
                            VariantOptions option = new VariantOptions(variantType.getType(), optionForm.getValue());
                            newVariantOptions.add(variantOptionsRepository.save(option));
                        }
                    }
                    variant.setVariantOptions(newVariantOptions);
                }
                updatedVariants.add(variant);
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

        // Cập nhật specifications
        if (form.getSpecifications() != null) {
            product.setSpecifications(form.getSpecifications());
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
