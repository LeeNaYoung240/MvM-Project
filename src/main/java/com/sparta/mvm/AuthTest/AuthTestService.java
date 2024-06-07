package com.sparta.mvm.AuthTest;

import com.sparta.mvm.config.PasswordConfig;
import com.sparta.mvm.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthTestService {

    private final JwtUtil jwtUtil;
    private final TestUserRepository userRepository;
    private final PasswordConfig passwordConfig;
    private final Map<String, RefreshTokenDto> refreshTokenDtoMap = new HashMap<>();
    
    @Transactional
    public void saveRefreshToken(String username, String refreshToken) {
        TestUser user = userRepository.findByUsername(username);
        user.setRefreshToken(refreshToken);
    }
    
    // 액세스 토큰 재발급
    public void tokenReissuance(String refreshToken, HttpServletResponse res) throws IOException {
        String username = jwtUtil.getUserInfoFromToken(refreshToken).getSubject();
        // TODO : db에 존재하는지 체크
        TestUser user = userRepository.findByUsername(username);

        // 클라이언트(현재로그인중, 리프레쉬토큰 만료x, 토큰만료 상태) 에서 보내온 refresh토큰과 db에 저장된,
        // 현재 로그인중인 username에 해당하는 refresh토큰 비교후 같으면 새로운 토큰발급
        String userTokenValue = jwtUtil.substringToken(user.getRefreshToken());
        if (userTokenValue.equals(refreshToken)) {
            String newToken = jwtUtil.createAccessToken(username);
            jwtUtil.addAccessJwtToCookie(newToken, res);
        }
        res.setStatus(200);
        res.setCharacterEncoding("UTF-8");
        res.getWriter().write("토큰 재발급 성공");
    }

    public void initTable() {
        String password = "testPassword";
        password = passwordConfig.passwordEncoder().encode(password);
        TestUser user = new TestUser("testUserId", password);
        userRepository.save(user);
    }
    // reFreshDto 값 저장하는 용도
    public void setRefreshToken(String username, String refreshTokenValue) {
        RefreshTokenDto tokenDto = new RefreshTokenDto(refreshTokenValue);
        tokenDto.setTokenValid(true);
        refreshTokenDtoMap.put(username, tokenDto);
    }
    // 요구사항중 reFresh 유효성 여부 검사하는 메서드
    public boolean isRefreshTokenValid(String username) {
        // TODO: 존재하는 유저명인지 체크
        RefreshTokenDto tokenDto = refreshTokenDtoMap.get(username);
        return tokenDto.isTokenValid();
    }

    public void setRefreshTokenValid(String username, boolean valid) {
        // TODO: 존재하는 유저명인지 체크
        refreshTokenDtoMap.get(username).setTokenValid(valid);
    }
}
