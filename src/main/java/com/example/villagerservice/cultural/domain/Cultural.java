package com.example.villagerservice.cultural.domain;

import com.example.villagerservice.common.domain.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cultural extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cultural_id")
    private Long id;

    private String codeName;
    private String guName;
    private String title;
    private String date;
    private String place;
    private String orgName;
    private String useTarget;
    private String useFee;
    @Lob
    private String player;
    @Lob
    private String program;
    @Lob
    @Column(length = 512)
    private String etcDesc;
    @Lob
    @Column(length = 512)
    private String orgLink;
    private String mainImg;
    private String registerDate;
    private String ticket;
    private String startDate;
    private String endDate;
    private String themeCode;
}
