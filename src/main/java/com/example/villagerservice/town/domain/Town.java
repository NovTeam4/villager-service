package com.example.villagerservice.town.domain;

import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
@Table(uniqueConstraints = @UniqueConstraint(
        columnNames = {
                "code"
        }))
public class Town {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "town_id")
    private Long id;

    @Column(nullable = false, length = 10)
    private String code;
    @Column(nullable = false, length = 20)
    private String city;
    @Column(nullable = false, length = 20)
    private String town;
    @Column(nullable = false, length = 20)
    private String village;
    @Column(nullable = false)
    private Double latitude;
    @Column(nullable = false)
    private Double longitude;
}
