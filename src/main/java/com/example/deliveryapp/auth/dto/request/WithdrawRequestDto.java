package com.example.deliveryapp.auth.dto.request;

import jakarta.annotation.security.DenyAll;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WithdrawRequestDto {
    private String userName;

    private String userRole;

    private String email;

    private String password;
}
