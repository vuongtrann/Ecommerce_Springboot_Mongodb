package com.spring.ecommerce.mongodb.services;

import com.opencsv.CSVParser;
import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.spring.ecommerce.mongodb.persistence.model.ImportCategoty;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;


    @Service
    public class CsvService {


        public List<ImportCategoty> readCategoriesFromCsv(String filePath) {
            try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
                CsvToBean<ImportCategoty> csvToBean = new CsvToBeanBuilder<ImportCategoty>(reader)
                        .withType(ImportCategoty.class)
                        .withIgnoreLeadingWhiteSpace(true)
                        .build();
                return csvToBean.parse();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

    }