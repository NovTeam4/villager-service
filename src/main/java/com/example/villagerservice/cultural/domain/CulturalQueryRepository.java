package com.example.villagerservice.cultural.domain;

import com.example.villagerservice.cultural.dto.CulturalBannerDto;

public interface CulturalQueryRepository {
    CulturalBannerDto.Response getBannerList(int size);
}
