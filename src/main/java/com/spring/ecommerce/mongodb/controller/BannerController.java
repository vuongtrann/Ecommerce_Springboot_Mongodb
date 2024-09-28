
package com.spring.ecommerce.mongodb.controller;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.spring.ecommerce.mongodb.persistence.model.Banners;
import com.spring.ecommerce.mongodb.services.BannerService;
import com.spring.ecommerce.mongodb.services.Impl.BannerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/banner")
public class BannerController {
    @Autowired
    private BannerServiceImpl bannerService;


    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<List<Banners>> getAll() throws NullPointerException, AmazonS3Exception  {
            try {
                return new ResponseEntity<>(bannerService.findAll(), HttpStatus.OK);
            }catch (Exception e){
                e.printStackTrace();
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
    }


    @GetMapping(value = "/{id}")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Banners> getById(@PathVariable("id") String id) throws NullPointerException, AmazonS3Exception {
        try{
            Optional<Banners> banners = bannerService.findById(id);
            if (banners.isPresent()) {
                return new ResponseEntity<>(banners.get(), HttpStatus.OK);
            }
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

    }


    @RequestMapping(value = "/add", method = RequestMethod.POST, consumes = "multipart/form-data")
    public ResponseEntity<Banners> add(@RequestPart("file") MultipartFile file,
                                       @RequestPart(value = "categoryId", required = false) String categoryId) throws NullPointerException, AmazonS3Exception {
        try{
            if (categoryId == null) {
                categoryId = "";
            }

            return new ResponseEntity<>( bannerService.save(file, categoryId), HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }




//    @PostMapping(value = "/add", consumes = "multipart/form-data")
    @RequestMapping(value = "/multi", method = RequestMethod.POST, consumes = "multipart/form-data")
    public ResponseEntity<List<Banners>> add (
            @RequestPart("file") List<MultipartFile> file,
            @RequestPart(value = "categoryId", required = false) String categoryId) throws IOException, AmazonS3Exception {
        try{
            List<Banners> bannerURLs= bannerService.saveMultil(file, categoryId);
            return new ResponseEntity<>(bannerURLs, HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }




//    @PutMapping(value = "/{id}/update", consumes = "multipart/form-data")
    @RequestMapping(value = "/{id}/update", method = RequestMethod.PUT,  consumes = "multipart/form-data" )
    public ResponseEntity<Banners> update (@RequestPart("file") MultipartFile file, @PathVariable("id") String id) throws IOException,AmazonS3Exception, NullPointerException {
        try {
            return new ResponseEntity<>(bannerService.update(file, id), HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @RequestMapping(value = "/{id}/delete", method = RequestMethod.DELETE)
    public ResponseEntity<String> delete (@PathVariable("id") String id) throws  NullPointerException,SdkClientException {
        try {
            bannerService.delete(id);
            return new ResponseEntity<>("Deleted", HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @RequestMapping(value = "/status", method = RequestMethod.PUT)
    public ResponseEntity<Banners> updateStatus(@PathVariable("id") String id) throws NullPointerException {
       try {
           return new ResponseEntity<>(bannerService.updateStatus(id), HttpStatus.OK);
       }catch (Exception e){
           e.printStackTrace();
           return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
       }
    }








}
