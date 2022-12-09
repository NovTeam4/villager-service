package com.example.villagerservice.post.domain;

import lombok.Data;
import lombok.Getter;

import javax.persistence.*;
@Data
@Getter
@Entity
public class Category {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private CategoryList name;
}
