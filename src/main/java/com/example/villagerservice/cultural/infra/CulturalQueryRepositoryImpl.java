package com.example.villagerservice.cultural.infra;

import com.example.villagerservice.cultural.domain.CulturalQueryRepository;
import com.example.villagerservice.cultural.dto.CulturalBannerDto;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.example.villagerservice.cultural.domain.QCultural.cultural;

@Repository
@RequiredArgsConstructor
public class CulturalQueryRepositoryImpl implements CulturalQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public CulturalBannerDto.Response getBannerList(int size) {
        List<CulturalBannerDto.CulturalBannerInfo> result = queryFactory
                .select(Projections.constructor(CulturalBannerDto.CulturalBannerInfo.class,
                        cultural.id,
                        cultural.codeName,
                        cultural.title,
                        cultural.guName,
                        cultural.place,
                        cultural.orgLink,
                        cultural.mainImg,
                        cultural.startDate.substring(0, 10),
                        cultural.endDate.substring(0, 10)))
                .from(cultural)
                .where(isActiveCultural(),
                        goeStartDateToday(getStartDateStringTemplate()))
                .orderBy(cultural.startDate.asc())
                .limit(size)
                .fetch();

        return CulturalBannerDto.Response.builder()
                .banners(result)
                .totalCount(result.size())
                .build();
    }

    private BooleanExpression isActiveCultural() {
        return cultural.orgLink.isNotNull()
                .and(cultural.mainImg.isNotNull()
                        .and(cultural.place.isNotNull()
                                .and(cultural.guName.isNotNull()
                                        .and(cultural.startDate.isNotNull()
                                                .and(cultural.endDate.isNotNull())))));
    }

    private BooleanExpression goeStartDateToday(StringTemplate formattedDate) {
        return formattedDate.goe(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
    }

    private StringTemplate getStartDateStringTemplate() {
        return Expressions.stringTemplate(
                "DATE_FORMAT({0}, {1})"
                , cultural.startDate
                , ConstantImpl.create("%Y%m%d")
        );
    }
}
