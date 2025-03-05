package com.example.deliveryapp.user.controller;

import com.example.deliveryapp.auth.entity.AuthUser;
import com.example.deliveryapp.common.annotation.Auth;
import com.example.deliveryapp.user.dto.request.ChangePasswordRequestDto;
import com.example.deliveryapp.user.dto.response.UserResponseDto;
import com.example.deliveryapp.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    //비밀번호 변경
    @PatchMapping("/api/v1/users")
    public ResponseEntity<UserResponseDto> changePassword(
            @Auth AuthUser authUser, @Valid @RequestBody ChangePasswordRequestDto dto) {
        UserResponseDto passwordChangedUser = userService.changePassword(authUser.getId(), dto);
        return ResponseEntity.ok(passwordChangedUser);
    }
}
