package com.example.villagerservice.party.api;

import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.party.dto.PartyDTO;
import com.example.villagerservice.party.service.PartyListService;
import com.example.villagerservice.party.service.PartyService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/parties")
public class PartyApiController {
    private final PartyService partyService;
    private final PartyListService partyListService;

    @PostMapping()
    public void createParty(@AuthenticationPrincipal Member member , @Validated @RequestBody PartyDTO.Request partyRequest) {

        partyService.createParty(member.getId() , partyRequest);

    }

    @GetMapping("/{partyId}")
    public PartyDTO.Response getParty(@PathVariable Long partyId) {

        return partyService.getParty(partyId);

    }


    @PostMapping("/{partyId}")
    public void applyParty(@AuthenticationPrincipal Member member, @PathVariable Long partyId){
        partyListService.applyParty(member.getEmail(), partyId);
    }

}
