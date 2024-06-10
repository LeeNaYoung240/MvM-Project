package com.sparta.mvm.controller;

import com.sparta.mvm.dto.ProfileRequestDto;
import com.sparta.mvm.dto.ProfileResponseDto;
import com.sparta.mvm.exception.CommonResponse;
import com.sparta.mvm.exception.CustomException;
import com.sparta.mvm.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.sparta.mvm.exception.ErrorEnum.USER_NOT_FOUND;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/profiles")
public class ProfileController {
    private final ProfileService profileService;

    @GetMapping("/{userId}")
    public ResponseEntity<CommonResponse> getProfile(@PathVariable Long userId) {

        ProfileResponseDto profile = profileService.getProfile(userId);

        return getResponseEntity(HttpStatus.OK.value(), "프로필 조회 성공\uD83C\uDF89", profile);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<CommonResponse> updateProfile(@PathVariable Long userId, @Valid @RequestBody ProfileRequestDto requestDto) {
        // userId가 전달되지 않은 경우
        if (userId == null) {
            throw new CustomException(USER_NOT_FOUND);
        }

        ProfileResponseDto profile = profileService.updateProfile(userId, requestDto);

        return getResponseEntity(HttpStatus.OK.value(), "프로필 수정 성공\uD83C\uDF89", profile);

    }

    private static ResponseEntity<CommonResponse> getResponseEntity(int statusCode, String msg, ProfileResponseDto profile) {
        return ResponseEntity.ok().body(CommonResponse.builder()
                .statusCode(statusCode)
                .msg(msg)
                .data(profile)
                .build());
    }

}
