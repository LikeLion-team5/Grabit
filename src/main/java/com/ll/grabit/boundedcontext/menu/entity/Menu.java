package com.ll.grabit.boundedcontext.menu.entity;

import com.ll.grabit.boundedcontext.restaurant.entity.Restaurant;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long menuId;

    private String menuName;

    private Integer price;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @Builder
    public Menu(String menuName, Integer price) {
        this.menuName = menuName;
        this.price = price;
    }

    //연관관계 편의 메서드
    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
        this.restaurant.getMenuList().add(this);
    }
}
