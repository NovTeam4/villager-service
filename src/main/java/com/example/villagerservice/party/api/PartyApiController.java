package com.example.villagerservice.party.api;

import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.party.dto.PartyApplyDto;
import com.example.villagerservice.party.dto.PartyDTO;
import com.example.villagerservice.party.dto.PartyLikeDto;
import com.example.villagerservice.party.dto.PartyListDTO;
import com.example.villagerservice.party.dto.UpdatePartyDTO;
import com.example.villagerservice.party.service.PartyApplyService;
import com.example.villagerservice.party.service.PartyCommentService;
import com.example.villagerservice.party.service.PartyLikeService;
import com.example.villagerservice.party.service.PartyMemberQueryService;
import com.example.villagerservice.party.service.PartyQueryService;
import com.example.villagerservice.party.service.PartyService;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/parties")
public class PartyApiController {
    private final PartyService partyService;
    private final PartyQueryService partyQueryService;
    private final PartyApplyService partyApplyService;
    private final PartyLikeService partyLikeService;
    private final PartyCommentService partyCommentService;
    private final PartyMemberQueryService partyMemberQueryService;


    @PostMapping()
    public void createParty(@AuthenticationPrincipal Member member , @Validated @RequestBody PartyDTO.Request partyRequest) {

        partyService.createParty(member.getId() , partyRequest);

    }

    @GetMapping("/{LAT}/{LNT}")
    public List<PartyListDTO> getAllParty(@AuthenticationPrincipal Member member ,@PathVariable("LAT") Double LAT , @PathVariable("LNT") Double LNT) {

        return partyQueryService.getPartyList(member.getEmail() , LAT, LNT);

    }

    @GetMapping()
    public List<PartyListDTO> getAllPartyWithMember(@AuthenticationPrincipal Member member) {

        return partyQueryService.getAllPartyWithMember(member);
    }

    @GetMapping("/{partyId}")
    public PartyDTO.Response getParty(@PathVariable Long partyId , @AuthenticationPrincipal Member member) {

        return partyService.getParty(partyId , member.getEmail());

    }

    @PatchMapping("/{partyId}")
    public PartyDTO.Response updateParty(@PathVariable Long partyId , @RequestBody UpdatePartyDTO.Request updatePartyRequest , @AuthenticationPrincipal Member member) {
        return partyService.updateParty(partyId , updatePartyRequest , member.getEmail());
    }

    @DeleteMapping("/{partyId}")
    public void deleteParty(@PathVariable Long partyId) {
        partyService.deleteParty(partyId);
    }


    @PostMapping("/{partyId}/apply")
    public PartyApplyDto.Response applyParty(@AuthenticationPrincipal Member member, @PathVariable Long partyId){
        return partyApplyService.applyParty(member.getId(), partyId);
    }

    @GetMapping("/{partyId}/apply")
    public Page<PartyApplyDto.Response> getApplyPartyList(@PathVariable Long partyId, final Pageable pageable){
        return partyApplyService.getApplyPartyList(partyId, pageable);
    }

    @PatchMapping("/{partyId}/permission/{targetMemberId}")
    public PartyApplyDto.Response partyPermission(@PathVariable Long partyId,
                                                        @PathVariable Long targetMemberId,
                                                        @AuthenticationPrincipal Member member){

        return partyApplyService.partyPermission(partyId, targetMemberId, member.getEmail());
    }

    @PostMapping("/{partyId}/like")
    public PartyLikeDto.Response partyLike(@PathVariable Long partyId, @AuthenticationPrincipal Member member){
        return partyLikeService.partyLike(partyId, member);
    }

    @GetMapping("/{partyId}/like")
    public boolean isPartyLike(@PathVariable Long partyId, @AuthenticationPrincipal Member member){
        return partyLikeService.isPartyLike(partyId, member.getEmail());
    }

    @PostMapping("/{partyId}/comment")
    public void createComment(@PathVariable Long partyId , @RequestBody String contents , @AuthenticationPrincipal Member member){

        partyCommentService.createComment(partyId , contents , member);

    }

    @PatchMapping("/{partyCommentId}/comment")
    public String updateComment(@PathVariable Long partyCommentId , @RequestBody String contents) {
        return partyCommentService.updateComment(partyCommentId , contents);
    }

    @PostMapping("{partyId}/start")
    public void partyStart(@PathVariable Long partyId, @AuthenticationPrincipal Member member){
        partyService.startParty(partyId, member);
    }

    @PostMapping("{partyId}/extension/{endDt}")
    public void partyExtension(@PathVariable Long partyId,
        @DateTimeFormat(pattern = "yyyy-MM-dd") @PathVariable LocalDate endDt,
        @AuthenticationPrincipal Member member){
        partyService.extensionParty(partyId, member, endDt);
    }

    @PostMapping("{partyId}/end")
    public void partyExtension(@PathVariable Long partyId,
        @AuthenticationPrincipal Member member){
        partyService.endParty(partyId, member);
    }

    @GetMapping("{partyId}/member")
    public List<Long> getPartyMemberList(@PathVariable Long partyId){
        return partyMemberQueryService.getPartyMemberId(partyId);
    }
}
