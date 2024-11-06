package com.esliceyament.orderservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String buyerName;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<CartItem> cartItems = new HashSet<>();

    private Double totalPrice;
    private Double discountPrice;

    @ElementCollection
    private Set<String> discountCodes = new HashSet<>();
    private String discountCode;

    private LocalDateTime createdAt;

    private Boolean isActive;


    @Override
    public int hashCode() {
        return Objects.hash(id); // Only use fields that uniquely identify Cart
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Cart other = (Cart) obj;
        return Objects.equals(id, other.id);
    }

}
