package com.example.deliveryapp.user.dto.response;

import com.example.deliveryapp.user.enums.UserRole;
import jakarta.annotation.security.DenyAll;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MyPageResponseDto {

    private Long id;

    private String userName;

    private UserRole userRole;

    private String email;

    private String phoneNumber;

    private String defaultAddress;
}
