package com.example.deliveryapp.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignupRequestDto {
    private String userName;

    private String userRole;

    private String email;

    private String password;

    private String address;

    private String brn; //사업자 등록 번호

    private String phoneNumber;
}
