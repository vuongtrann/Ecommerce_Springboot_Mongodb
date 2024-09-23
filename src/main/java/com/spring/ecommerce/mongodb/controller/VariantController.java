package com.spring.ecommerce.mongodb.controller;

import com.spring.ecommerce.mongodb.persistence.model.variants.ProductVariants;
import com.spring.ecommerce.mongodb.repository.ProductVariantsRepository;
import com.spring.ecommerce.mongodb.services.S3Services;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1/variant")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class VariantController {
    private final ProductVariantsRepository productVariantsRepository;
    private final S3Services s3Services;

    @RequestMapping(value = "/{variantId}/upload/image",method = RequestMethod.POST)
    public ResponseEntity uploadImage(@PathVariable("variantId") String variantId, @RequestParam("files") MultipartFile[] files) throws IOException {
        List<File> fileList = new ArrayList<>();
        String keyUrl = "/variants/" + variantId + "/";
        ProductVariants productVariant = productVariantsRepository.findById(variantId).orElseThrow(() -> new RuntimeException("Variant not found"));
       try{
            for (MultipartFile file : files) {
                File localFile = File.createTempFile("image_", file.getOriginalFilename());
                file.transferTo(localFile);
                fileList.add(localFile);
            }

            // Upload các file lên S3 và lấy danh sách các URL đã upload
            List<String> fileURLs = s3Services.upload(variantId, fileList, keyUrl);
            List<String> oldFileURLs = productVariant.getImageURLs();

            // Kiểm tra xem variant đã có ảnh trước đó chưa, nếu có thì nối thêm vào danh sách cũ
            if (oldFileURLs != null) {
                fileURLs.addAll(oldFileURLs);
            }

            productVariant.setImageURLs(fileURLs);
            productVariantsRepository.save(productVariant);

            for (File file : fileList) {
                file.delete();
            }
            // Trả về kết quả với biến thể đã được cập nhật
            return new ResponseEntity<>( productVariant, HttpStatus.CREATED);
        }catch (Exception e){
           return new ResponseEntity<>( e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
       }
    }
}
