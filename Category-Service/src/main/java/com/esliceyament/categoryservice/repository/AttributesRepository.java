package com.esliceyament.categoryservice.repository;

import com.esliceyament.categoryservice.entity.Attributes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttributesRepository extends JpaRepository<Attributes, Long> {
    List<Attributes> findAllByCategoryName(String name);
    Attributes findByName(String name);

}
