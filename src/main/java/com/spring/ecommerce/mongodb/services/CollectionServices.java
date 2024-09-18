package com.spring.ecommerce.mongodb.services;

import com.spring.ecommerce.mongodb.persistence.model.Collection;

import java.util.List;
import java.util.Optional;

public interface CollectionServices {
    public List<Collection> findAllCollections();
    public Optional<Collection> findById(String id);
    public Collection addCollection(Collection collection);
    public Collection updateCollection(Collection collection);
    public void deleteCollection(Collection collection);
}
