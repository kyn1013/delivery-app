package com.example.deliveryapp.auth.controller;

import com.example.deliveryapp.auth.dto.request.LoginRequestDto;
import com.example.deliveryapp.auth.dto.request.SignupRequestDto;
import com.example.deliveryapp.auth.dto.request.WithdrawRequestDto;
import com.example.deliveryapp.auth.dto.response.LoginResponseDto;
import com.example.deliveryapp.auth.dto.response.SignupResponseDto;
import com.example.deliveryapp.auth.entity.AuthUser;
import com.example.deliveryapp.auth.service.AuthService;
import com.example.deliveryapp.common.annotation.Auth;
import com.example.deliveryapp.common.annotation.OwnerUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

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
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto dto) {
        LoginResponseDto loginUser = authService.login(dto);
        return ResponseEntity.ok(loginUser);
    }

    //로그아웃




}
