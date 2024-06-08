package com.sparta.mvm.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.mvm.dto.LoginRequestDto;
import com.sparta.mvm.service.AuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthService authService;
    private LoginRequestDto requestDto;
    // 로그인 요청 url
    public JwtAuthenticationFilter(AuthService authService) {
        this.authService = authService;
        setFilterProcessesUrl("/users/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try {
            requestDto = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getUsername(),
                            requestDto.getPassword(),
                            null
                    )
            );
        } catch (IOException e) {

            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        // 부모 클래스의 successfulAuthentication 메서드를 호출하여 인증을 완료
        super.successfulAuthentication(request, response, chain, authResult);

        // AuthService를 사용하여 사용자를 로그인 처리
        authService.login(requestDto, response);

        // 로그인이 성공한 경우에는 성공 메시지를 반환
        successLogin(response);

        // 로그아웃 요청 URL을 확인
        String logoutUrl = "/users/logout";

        // 요청 URL이 로그아웃 URL이고, HTTP 메소드가 POST인 경우에만 실행
        if (request.getRequestURI().equals(logoutUrl) && request.getMethod().equals("POST")) {
            // AuthService를 사용하여 사용자의 토큰을 초기화하여 로그아웃 처리
            authService.invalidateTokens(requestDto.getUsername());

            // 로그아웃이 성공한 경우에는 성공 메시지를 반환
            successLogout(response);
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(401);
    }

    private void successLogin(HttpServletResponse res) {
        try {
            res.setCharacterEncoding("UTF-8");
            res.getWriter().println("로그인이 성공하였습니다! (토큰/리프레시토큰 생성)");
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    // 로그아웃이 성공했을 때 클라이언트에게 전송항 응답을 생성
    private void successLogout(HttpServletResponse response) {
        // 클라이언트에게 토큰을 무효화하는 응답을 전달
        try {
            response.setCharacterEncoding("UTF-8");
            response.getWriter().println("로그아웃이 성공하였습니다! (토큰 무효화)");
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}