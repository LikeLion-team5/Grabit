package com.ll.grabit.boundedcontext.restaurant.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter @Getter
@NoArgsConstructor
public class RestaurantImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId;

    private String uploadFileName;
    private String storedFileName;

    private String imagePath;

    @OneToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
        if(restaurant != null)
            this.restaurant.setRestaurantImage(this);
    }

    public RestaurantImage(String uploadFileName, String storedFileName, String imagePath) {
        this.uploadFileName = uploadFileName;
        this.storedFileName = storedFileName;
        this.imagePath = imagePath;
    }
}
