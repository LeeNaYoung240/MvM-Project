package com.sparta.mvm.service;

import com.sparta.mvm.dto.ProfileResponseDto;
import com.sparta.mvm.entity.User;
import com.sparta.mvm.exception.CustomException;
import com.sparta.mvm.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.sparta.mvm.exception.ErrorEnum.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;

    public ProfileResponseDto getProfile(Long userId) {
        User user = profileRepository.findById(userId).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        ProfileResponseDto responseDto = new ProfileResponseDto(user);
        return  responseDto;
    }
}
