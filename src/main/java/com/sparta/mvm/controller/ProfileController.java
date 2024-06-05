package com.sparta.mvm.controller;

import com.sparta.mvm.dto.ProfileResponseDto;
import com.sparta.mvm.exception.CommonResponse;
import com.sparta.mvm.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profile")
public class ProfileController {
    private final ProfileService profileService;

    @GetMapping("/{userId}")
    public ResponseEntity<CommonResponse> getProfile(@PathVariable Long userId) {
        ProfileResponseDto profile = profileService.getProfile(userId);
        return ResponseEntity.ok().body(CommonResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .msg("프로필 조회 성공!")
                .data(profile)
                .build());
    }
}
