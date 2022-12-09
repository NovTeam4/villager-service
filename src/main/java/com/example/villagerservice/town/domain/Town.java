package com.example.villagerservice.town.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@Table(uniqueConstraints = @UniqueConstraint(
        columnNames = {
                "code"
        }))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Builder
    private Town(String code, String city, String town, String village, Double latitude, Double longitude) {
        this.code = code;
        this.city = city;
        this.town = town;
        this.village = village;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
