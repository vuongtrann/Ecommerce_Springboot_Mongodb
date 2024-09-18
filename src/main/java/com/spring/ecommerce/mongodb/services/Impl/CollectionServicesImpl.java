package com.spring.ecommerce.mongodb.services.Impl;

import com.spring.ecommerce.mongodb.persistence.model.Collection;
import com.spring.ecommerce.mongodb.repository.CollectionRepository;
import com.spring.ecommerce.mongodb.services.CollectionServices;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class CollectionServicesImpl implements CollectionServices {

    private final CollectionRepository collectionRepository;
    @Override
    public List<Collection> findAllCollections() {
        return collectionRepository.findAll();
    }

    @Override
    public Optional<Collection> findById(String id) {
       return collectionRepository.findById(id);
    }

    @Override
    public Collection addCollection(Collection collection) {
        collection.setCreatedAt(LocalDateTime.now());
        return collectionRepository.save(collection);
    }

    @Override
    public Collection updateCollection(Collection collection) {
        collection.setUpdatedAt(LocalDateTime.now());
        return collectionRepository.save(collection);
    }

    @Override
    public void deleteCollection(Collection collection) {
        collectionRepository.delete(collection);
    }
}
