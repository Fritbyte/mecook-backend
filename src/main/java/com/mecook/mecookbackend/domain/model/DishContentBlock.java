package com.mecook.mecookbackend.domain.model;

import jakarta.persistence.*;

@Entity
public class DishContentBlock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Dish dish;

    @Column(name = "type")
    private String type;

    @Column(name = "block_value", length = 255)
    private String value;

    private int orderIndex;

    public DishContentBlock() {
    }

    public DishContentBlock(Dish dish, String type, String value, int orderIndex) {
        this.dish = dish;
        this.type = type;
        this.value = value;
        this.orderIndex = orderIndex;
    }

    public Long getId() {
        return id;
    }

    public Dish getDish() {
        return dish;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public int getOrderIndex() {
        return orderIndex;
    }
}
