package com.example.villagerservice.town.infra;

import com.example.villagerservice.town.domain.QTown;
import com.example.villagerservice.town.domain.TownQueryRepository;
import com.example.villagerservice.town.dto.TownList;
import com.example.villagerservice.town.dto.TownListDetail;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TownQueryRepositoryImpl implements TownQueryRepository {

    private final JPAQueryFactory queryFactory;
    private final JdbcTemplate jdbcTemplate;
    private final static int DEFAULT_LIMIT = 40;

    @Override
    public TownList.Response getTownListWithLocation(TownList.LocationRequest locationRequest) {
        List<TownListDetail> result = jdbcTemplate.query(getLocationQuery(locationRequest), mapRow());
        return TownList.Response.builder().towns(result).build();
    }
    private String getLocationQuery(TownList.LocationRequest locationRequest) {
        return "select *, (6371*acos(cos(radians(" + locationRequest.getLatitude() + "))*cos(radians(latitude))*cos(radians(longitude) " +
                "-radians("+locationRequest.getLongitude() + "))+sin(radians(" + locationRequest.getLatitude() + "))*sin(radians(latitude)))) " +
                "as distance\n" +
                "from town\n" +
                "group by village " +
                "order by distance " +
                "limit " + DEFAULT_LIMIT;
    }
    private RowMapper<TownListDetail> mapRow() {
        return ((rs, rowNum) -> new TownListDetail(
                rs.getLong("town_id"),
                rs.getString("city"),
                rs.getString("town"),
                rs.getString("village"),
                rs.getString("code"),
                rs.getDouble("latitude"),
                rs.getDouble("longitude")));
    }

    @Override
    public TownList.Response getTownListWithName(TownList.NameRequest nameRequest) {
        QTown town = new QTown("town");

        List<TownListDetail> result = queryFactory
                .select(Projections.constructor(TownListDetail.class,
                        town.id, town.city, town.town, town.village,
                        town.code, town.latitude, town.longitude))
                .from(town)
                .where(town.village.startsWith(nameRequest.getName()))
                .limit(DEFAULT_LIMIT)
                .groupBy(town.village)
                .orderBy(town.code.asc())
                .fetch();
        return TownList.Response.builder().towns(result).build();
    }
}
