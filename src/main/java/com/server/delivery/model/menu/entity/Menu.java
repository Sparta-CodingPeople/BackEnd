package com.server.delivery.model.menu.entity;

import com.server.delivery.model.store.entity.Store;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "p_menu")
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "menu_id")
    private UUID menuId;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(name = "food_name", nullable = false)
    private String foodName;

    @Column(name = "description")
    private String description;

    @Column(name = "price" , nullable = false)
    private int price;

    @Column(name = "availability", nullable = false)
    private boolean availability;

    @Column(name = "food_image")
    private String foodImage;
}
