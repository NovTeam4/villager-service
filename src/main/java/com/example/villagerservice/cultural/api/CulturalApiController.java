package com.example.villagerservice.cultural.api;

import com.example.villagerservice.cultural.dto.CulturalBannerDto;
import com.example.villagerservice.cultural.service.CulturalQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/banners")
public class CulturalApiController {

    private final CulturalQueryService culturalQueryService;

    @GetMapping({"", "/{size}"})
    public CulturalBannerDto.Response getBanners(@PathVariable(required = false) Optional<Integer> size) {
        return culturalQueryService.getBannerList(size.orElse(10));
    }
}
