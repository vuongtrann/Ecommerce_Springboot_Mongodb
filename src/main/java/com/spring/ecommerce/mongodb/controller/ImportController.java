package com.spring.ecommerce.mongodb.controller;

import com.spring.ecommerce.mongodb.persistence.model.ImportCategoty;
import com.spring.ecommerce.mongodb.services.CsvService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(value = "")
public class ImportController {
    private final CsvService csvService;

    public ImportController(CsvService csvService) {
        this.csvService = csvService;
    }

    @GetMapping("/import")
    public List<ImportCategoty> getCategories(@RequestPart MultipartFile file) {
        String filePath = "/Users/mr.hung/MyDocuments/Ecommerce_Springboot_Mongodb/src/main/java/com/spring/ecommerce/mongodb/ecommerce_categories.csv"; // Đường dẫn đến tệp CSV
        return  csvService.readCategoriesFromCsv(file);
    }
}
