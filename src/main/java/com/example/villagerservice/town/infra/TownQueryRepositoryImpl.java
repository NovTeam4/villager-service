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

    @Override
    public TownList.Response getTownListWithLocation(TownList.LocationRequest locationRequest) {
        return new TownList.Response(jdbcTemplate.query(getLocationQuery(locationRequest), mapRow()));
    }
    private String getLocationQuery(TownList.LocationRequest locationRequest) {
        return "select *" +
                ", format((6371 * acos(cos(radians(latitude)) * cos(radians(" + locationRequest.getLatitude() + ")) " +
                "* cos(radians(" + locationRequest.getLongitude() + ") - radians(longitude)) " +
                "+ sin(radians(latitude)) * sin(radians(" + locationRequest.getLatitude() + ")))), 4) as distance " +
                " from town " +
                " group by village " +
                " order by distance " +
                " limit " + locationRequest.getLimit();
    }
    private RowMapper<TownListDetail> mapRow() {
        return ((rs, rowNum) -> new TownListDetail(
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
                        town.city, town.town, town.village,
                        town.code, town.latitude, town.longitude))
                .from(town)
                .where(town.village.startsWith(nameRequest.getName()))
                .limit(nameRequest.getLimit())
                .groupBy(town.village)
                .orderBy(town.code.asc())
                .fetch();
        return new TownList.Response(result);
    }
}
