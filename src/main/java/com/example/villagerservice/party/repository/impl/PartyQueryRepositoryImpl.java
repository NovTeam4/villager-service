package com.example.villagerservice.party.repository.impl;

import com.example.villagerservice.party.domain.Party;
import com.example.villagerservice.party.dto.PartyListDTO;
import com.example.villagerservice.party.repository.PartyLikeRepository;
import com.example.villagerservice.party.repository.PartyQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PartyQueryRepositoryImpl implements PartyQueryRepository {

    private final JdbcTemplate jdbcTemplate;

    private final PartyLikeRepository partyLikeRepository;

    @Override
    public List<PartyListDTO> getPartyList(String email ,Double LAT, Double LNT) {

        List<PartyListDTO> partyList = jdbcTemplate.query(getQuery(), mapRow(), LAT, LNT, LAT);
        for (PartyListDTO partyListDTO : partyList) {
            List<String> tagNameList = getTagNameList(partyListDTO.getPartyId());
            for (String tagName : tagNameList) {
                partyListDTO.getTagNameList().add(tagName);
            }

            boolean partyLike = partyLikeRepository.existsByParty_IdAndMember_Email(partyListDTO.getPartyId(), email);
            partyListDTO.setPartyLike(partyLike);
        }

        return partyList;
    }

    private List<String> getTagNameList(Long partyId) {
        return jdbcTemplate.query(" select tag_name from party_tag where party_id = ? ", new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getString("tag_name");
            }
        }, partyId);
    }

    private String getQuery(){

        return " SELECT p.party_id ,p.party_name ,p.start_dt , p.end_dt ,m.nickname , p.content, p.location , m.member_id ,(6371*acos(cos(radians(?))*cos(radians(p.latitude))*cos(radians(p.longitude)-radians(?))+sin(radians(?))*sin(radians(p.latitude)))) AS distance " +
                " FROM party as p join member_detail as m on m.member_id = p.member_id " +
                " ORDER BY distance " +
                " limit 5 ";
    }

    private RowMapper<PartyListDTO> mapRow() {

        return ((rs, rowNum) -> new PartyListDTO(
                rs.getLong("party_id"),
                rs.getString("party_name"),
                rs.getDate("start_dt").toLocalDate(),
                rs.getDate("end_dt").toLocalDate(),
                rs.getString("nickname"),
                rs.getString("content"),
                rs.getString("location"),
                new ArrayList<>(),
                false,
                rs.getLong("member_id")
        ));
    }
}
