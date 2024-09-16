package com.spring.ecommerce.mongodb.repository;

import com.spring.ecommerce.mongodb.persistence.model.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {

    @Query("db.getCollection('category').find({})")
    List<Category> findAll();


    @Query(value="db.category.aggregate([" +
            "  {" +
            "    $lookup: {" +
            "      from: \"product\"," +
            "      localField: \"_id\", " +
            "      foreignField: \"categories\"," +
            "      as: \"products\" " +
            "}" +
            "  }," +
            "  {" +
            "    $unwind: \"$products\" " +
            "  }," +
            "  {" +
            "    $group: {" +
            "      _id: \"$_id\"," +
            "      noOfViews: { $sum: \"$products.viewCount\" }," +
            "      noOfSold : {$sum:  \" $product.quantitySold\"}"+
            "      name: { $first: \"$name\" }," +
            "      icon: { $first: \"$icon\" }  " +
            "    }" +
            "  }," +
            "  {" +
            "    $sort: { noOfSold:  -1 ,noOfViews: -1 }" +
            "  }," +
            "  {" +
            "    $limit: ?0 " +
            "  }" +
            "])")
    List<Map<String, Objects>> findTopCategorie(int limit);








}
