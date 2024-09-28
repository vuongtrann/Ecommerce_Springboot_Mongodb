package com.spring.ecommerce.mongodb.services.Impl;

import com.spring.ecommerce.mongodb.persistence.model.Category;
import com.spring.ecommerce.mongodb.persistence.model.variants.VariantTypes;
import com.spring.ecommerce.mongodb.repository.CategoryRepository;
import com.spring.ecommerce.mongodb.repository.VariantTypeRepository;
import com.spring.ecommerce.mongodb.services.VariantTypeServices;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class VariantTypeServicesImpl implements VariantTypeServices {
    private final VariantTypeRepository variantTypeRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public Category addVariantType(String categoryId, List<VariantTypes> variantTypes) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(()-> new RuntimeException("Category not found!"));
        for (VariantTypes variantType : variantTypes) {
            Optional<VariantTypes> existingVariantType = category.getVariantTypes().stream()
                    .filter(vt -> vt.getType().equalsIgnoreCase(variantType.getType()))
                    .findFirst();

            if (existingVariantType.isPresent()) {

                System.out.println("Variant type " + variantType.getType() + " already exists in category");
                continue;
            }
            variantType.setCategoryId(categoryId);
            VariantTypes savedVariantType = variantTypeRepository.save(variantType);

            category.getVariantTypes().add(savedVariantType);
        }

        return categoryRepository.save(category);
    }

    @Override
    public List<VariantTypes> getVariantTypeByCategoryId(String categoryId) {
        return variantTypeRepository.findByCategoryId(categoryId);
    }

    @Override
    public Category removeVariantType(String categoryId, List<VariantTypes> variantTypes) {
        // Tìm category theo categoryId, nếu không có thì ném exception
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + categoryId));

        // Lọc các variantTypes trong category và giữ lại những cái không có trong danh sách cần xóa
        List<VariantTypes> updatedVariantTypes = category.getVariantTypes().stream()
                .filter(vt -> !variantTypes.contains(vt.getType()))
                .collect(Collectors.toList());

        // Cập nhật danh sách variantTypes của category
        category.setVariantTypes(updatedVariantTypes);

        // Lưu category đã cập nhật vào cơ sở dữ liệu
        return categoryRepository.save(category);
    }
}
