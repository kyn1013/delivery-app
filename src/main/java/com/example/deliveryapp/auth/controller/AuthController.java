package com.example.deliveryapp.auth.controller;

import com.example.deliveryapp.auth.common.util.JwtUtil;
import com.example.deliveryapp.auth.dto.request.LoginRequestDto;
import com.example.deliveryapp.auth.dto.request.SignupRequestDto;
import com.example.deliveryapp.auth.dto.request.WithdrawRequestDto;
import com.example.deliveryapp.auth.dto.response.LoginResponseDto;
import com.example.deliveryapp.auth.dto.response.ReissueTokenResponseDto;
import com.example.deliveryapp.auth.dto.response.SignupResponseDto;
import com.example.deliveryapp.auth.entity.AuthUser;
import com.example.deliveryapp.auth.service.AuthService;
import com.example.deliveryapp.common.annotation.Auth;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    //회원가입
    @PostMapping("/api/v1/auth/users/signup")
    public ResponseEntity<SignupResponseDto> Signup(@Valid @RequestBody SignupRequestDto dto) {
        SignupResponseDto signupUser = authService.signup(dto);
        return ResponseEntity.ok(signupUser);
    }

    //탈퇴
    @PostMapping("/api/v1/users/withdraw")
    public ResponseEntity<String> withdraw(@Auth AuthUser authUser) {
        authService.withdraw(authUser.getId());
        return ResponseEntity.ok("탈퇴 성공");
    }

    //로그인
    @PostMapping("/api/v1/auth/users/login")
    public ResponseEntity<LoginResponseDto> login(
            @Valid @RequestBody LoginRequestDto dto,
            HttpServletResponse response) {
        LoginResponseDto loginUser = authService.login(dto);

        String substringRefreshToken = jwtUtil.substringToken(loginUser.getRefreshToken());

        // Refresh Token을 ResponseCookie로 설정
        // "REFRESH_TOKEN"이라는 이름으로 Refresh Token 값을 담은 쿠키 객체를 생성
        ResponseCookie refreshTokenCookie = ResponseCookie.from("REFRESH_TOKEN", substringRefreshToken)
                .httpOnly(true)   // JavaScript에서 접근 불가능
                .secure(true)     // HTTPS 환경에서만 전송
                .path("/")        // 모든 경로에서 사용 가능
                .sameSite("None") // 크로스사이트 요청에서도 쿠키 전송
                .build();

        // 생성한 쿠키를 응답헤더에 추가
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

        return ResponseEntity.ok(loginUser);
    }

    //로그아웃
    @PostMapping("/api/v1/users/logout")
    public ResponseEntity<String> logout(
            @Auth AuthUser authUser,
            @CookieValue(name = "REFRESH_TOKEN") String refreshToken) {
        authService.logout(authUser.getId(), refreshToken);
        return ResponseEntity.ok("로그아웃 성공");
    }

    //Access Token 재발급
    @GetMapping("/api/v1/auth/users/reissue-token")
    public ResponseEntity<ReissueTokenResponseDto> reissueToken(
            @CookieValue(name = "REFRESH_TOKEN") String refreshToken) {

        ReissueTokenResponseDto reissuedToken = authService.reissueToken(refreshToken);

        return ResponseEntity.ok(reissuedToken);
    }


    //탈퇴한 사용자의 refresh Token 관리
    //Refresh Token이 만료된 경우에는..?


}
