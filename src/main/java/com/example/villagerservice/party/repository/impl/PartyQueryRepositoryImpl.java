package com.example.villagerservice.party.repository.impl;

import com.example.villagerservice.party.domain.Party;
import com.example.villagerservice.party.dto.PartyDTO;
import com.example.villagerservice.party.repository.PartyQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PartyQueryRepositoryImpl implements PartyQueryRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Party> findById(Long partyId) {
        return Optional.ofNullable(jdbcTemplate.queryForObject("select * from party p where p.party_id = ?" , mapRowParty() , partyId));
    }


    @Override
    public Optional<PartyDTO.Response> getParty(Long partyId) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(getPartyQuery(), mapRow(), partyId));
        }catch (Exception e) {

        }
        return Optional.empty();
    }

    private String getPartyQuery(){
        return " select p.party_name , p.score , p.start_dt , p.end_dt , p.amount , m.nickname , m.point " +
                " from party p join member_detail m on m.member_id = p.member_id " +
                " where p.party_id = ? ";
    }

    private RowMapper<PartyDTO.Response> mapRow() {

        return ((rs, rowNum) -> new PartyDTO.Response(
                rs.getString("party_name"),
                rs.getInt("score"),
                rs.getDate("start_dt").toLocalDate(),
                rs.getDate("end_dt").toLocalDate(),
                rs.getInt("amount"),
                rs.getString("nickname"),
                rs.getInt("point")
        ));
    }

    private RowMapper<Party> mapRowParty() {

        return ((rs, rowNum) -> new Party(
                rs.getLong("party_id"),
                rs.getString("party_name"),
                rs.getInt("score"),
                rs.getDate("start_dt").toLocalDate(),
                rs.getDate("end_dt").toLocalDate(),
                rs.getInt("amount"),
                null
        ));
    }

}
