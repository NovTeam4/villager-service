package com.example.villagerservice.party.domain;

import javax.persistence.*;

@Entity
public class PartyImage {

    @Id @GeneratedValue
    @Column(name = "party_image_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_id")
    private Party party;

    private Boolean isThumbnail;

    @Column(name = "image_size")
    private Long imageSize;

    @Column(name = "image_path")
    private String imagePath;
}
