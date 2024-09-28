package com.spring.ecommerce.mongodb.controller;

import com.spring.ecommerce.mongodb.persistence.model.Category;
import com.spring.ecommerce.mongodb.persistence.model.Specification.SpecificationTypes;
import com.spring.ecommerce.mongodb.persistence.model.variants.VariantTypes;
import com.spring.ecommerce.mongodb.services.CategoryServices;
import com.spring.ecommerce.mongodb.services.VariantTypeServices;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1/category")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class CategoryController {

    private final CategoryServices categoryServices;
    private final VariantTypeServices variantTypeServices;

    /**Get categories*/
    @RequestMapping(value = "",method = RequestMethod.GET)
    public ResponseEntity<List<Category>> getAllCategory() {

        return new ResponseEntity<>(categoryServices.findAll(), HttpStatus.OK);
    }
    /**add categories*/
    @RequestMapping(value = "",method = RequestMethod.POST)
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        return new ResponseEntity<>(categoryServices.save(category), HttpStatus.CREATED);
    }

    /**Get category by id*/
    @RequestMapping(value = "/{categoryId}", method = RequestMethod.GET)
    public ResponseEntity<Category> getCategoryById(@PathVariable String categoryId) {
        try {
            Optional<Category> category = categoryServices.findById(categoryId);
            if (category.isPresent()){
                return new ResponseEntity<>(category.get(), HttpStatus.OK);
            }
            else   return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/{id}/delete", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteCategory(@PathVariable String id) {
        categoryServices.delete(id);
        return new ResponseEntity<>("Deleted", HttpStatus.OK);
    }


    @RequestMapping(value = "/top", method = RequestMethod.GET)
    public ResponseEntity<List<Category>> getTopCategory(@RequestParam(value ="limit", defaultValue = "10") int limit) {
        try{
            return new ResponseEntity<>(categoryServices.getTopCategory(limit), HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }




    /** Add Sub Category */
    @RequestMapping(value = "/{parentId}/sub", method = RequestMethod.POST)
    public ResponseEntity<Category> addSubCategory(@PathVariable(value = "parentId") String parentId,@RequestParam(value = "childId") String childId ) {
        try{
            if (parentId.isEmpty() || childId.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(categoryServices.addSubCategory(parentId,childId), HttpStatus.OK);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }



    /** Get Sub Category */
    @RequestMapping(value = "/{categoryId}/sub", method = RequestMethod.GET)
    public ResponseEntity<List<Category>> getSubCategory(@PathVariable String categoryId) {
        try{
            if (categoryId.isEmpty()){
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(categoryServices.getSubCategory(categoryId), HttpStatus.OK);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }



    /** Delete Sub Category */
    @RequestMapping(value = "/{parentId}/sub", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteSubCategory(@PathVariable(value = "parentId") String parentId,@RequestParam(value = "childId") String childId ) {
        try{
            if (parentId.isEmpty() || childId.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            categoryServices.deleteSubCategory(parentId,childId);
            return new ResponseEntity<>("Deleted", HttpStatus.OK);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }



    /**VARIANT TYPE*/

    @RequestMapping(value = "/{categoryId}/variant",method = RequestMethod.POST)
    public ResponseEntity addVariantType(@PathVariable String categoryId,@RequestBody List<VariantTypes> variantTypes) {
        return new ResponseEntity<>(variantTypeServices.addVariantType(categoryId,variantTypes),HttpStatus.CREATED);
    }
    @RequestMapping(value = "/{categoryId}/variant",method = RequestMethod.GET)
    public ResponseEntity getVariantTypeByCategoryId(@PathVariable("categoryId") String categoryId){
        return new ResponseEntity<>(variantTypeServices.getVariantTypeByCategoryId(categoryId),HttpStatus.OK);
    }

    @RequestMapping(value = "/{categoryId}/variant",method = RequestMethod.DELETE)
    public ResponseEntity removeVariantByCategoryId(@PathVariable("categoryId") String categoryId, @RequestBody List<VariantTypes> variantTypes){
        return new ResponseEntity<>(variantTypeServices.removeVariantType(categoryId,variantTypes),HttpStatus.OK);
    }

    /**Add Specification Type*/
    @RequestMapping(value = "/{categoryId}/specificationType",method = RequestMethod.POST)
    public ResponseEntity addSpecificationType(@PathVariable("categoryId") String categoryId, @RequestBody List<SpecificationTypes> specificationTypes){
        return new ResponseEntity(categoryServices.addSpecificationType(categoryId,specificationTypes),HttpStatus.CREATED);
    }
    @RequestMapping(value = "/{categoryId}/specificationType", method = RequestMethod.GET)
    public ResponseEntity getSpecificationType(@PathVariable("categoryId") String categoryId){
        List<SpecificationTypes> specificationTypes = categoryServices.getSpecificationTypesByCategoryID(categoryId);
        if (specificationTypes!=null){
            return new ResponseEntity(specificationTypes,HttpStatus.OK);
        }else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }


}
