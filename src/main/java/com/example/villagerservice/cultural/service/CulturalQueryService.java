package com.example.villagerservice.cultural.service;

import com.example.villagerservice.cultural.dto.CulturalBannerDto;

public interface CulturalQueryService {
    CulturalBannerDto.Response getBannerList(int size);
}
