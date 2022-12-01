package com.example.villagerservice.party.request;

import com.example.villagerservice.party.domain.Party;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PartyCreate {


    @Column(name = "party_name")
    @NotEmpty
    private String partyName;

    @NotEmpty
    private Integer score;

    @Column(name = "start_dt")
    private LocalDateTime startDt;

    @Column(name = "end_dt")
    private LocalDateTime endDt;

    @Size(min = 10000 , max = 30000)
    private Integer amount;


}
