package com.ll.grabit.boundedcontext.restaurant.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class RestaurantImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId;

    private String uploadFileName;
    private String storedFileName;

    private String imagePath;

    public RestaurantImage(String uploadFileName, String storedFileName, String imagePath) {
        this.uploadFileName = uploadFileName;
        this.storedFileName = storedFileName;
        this.imagePath = imagePath;
    }
}
