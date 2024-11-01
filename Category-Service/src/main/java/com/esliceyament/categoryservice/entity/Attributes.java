package com.esliceyament.categoryservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "attributes")
@Data
public class Attributes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;


    @ElementCollection
    @CollectionTable(name = "attribute_values", joinColumns = @JoinColumn(name = "attribute_id"))
    @Column(name = "value")
    private Set<AttributeValues> values = new HashSet<>();
    private String unit;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")                       //////////////////// FETCH TYPE
    private Category category;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attributes that = (Attributes) o;
        return Objects.equals(id, that.id);  // Compare only by id
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);  // Use only the id field
    }
}
