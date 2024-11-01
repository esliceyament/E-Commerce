package com.example.productservice.repository;

import com.example.productservice.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {

    Optional<Product> findByName(String name);

    Page<Product> findAllByStatusTrue(Pageable pageable);
    Optional<Product> findFirstByStatusTrueOrderByOrderNumberDesc();
    Optional<Product> findByOrderNumber(Long orderNumber);

    Optional<Product> findFirstByOrderByProductCodeDesc();

    Optional<Product> findByProductCode(Long productCode);

}
