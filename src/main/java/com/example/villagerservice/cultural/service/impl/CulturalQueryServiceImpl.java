package com.example.villagerservice.cultural.service.impl;

import com.example.villagerservice.cultural.domain.CulturalQueryRepository;
import com.example.villagerservice.cultural.dto.CulturalBannerDto;
import com.example.villagerservice.cultural.service.CulturalQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CulturalQueryServiceImpl implements CulturalQueryService {

    private final CulturalQueryRepository culturalQueryRepository;
    @Override
    public CulturalBannerDto.Response getBannerList(int size) {
        return culturalQueryRepository.getBannerList(size);
    }
}
