package com.example.productservice.entity;

import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "ratings")
@Data
public class Rating {
    @Id
    private String id;
    private String productId;
    private Long userId;
    private Double rating = 0D;
    private String comment;
    private LocalDate createTime;
    private Boolean status;
}
