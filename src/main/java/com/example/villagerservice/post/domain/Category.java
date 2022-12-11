package com.example.villagerservice.post.domain;

import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    private String name;
    private boolean isVisible;
}
