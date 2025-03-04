package com.example.deliveryapp.auth.dto.response;

import com.example.deliveryapp.user.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignupResponseDto {
    private Long id;

    private String userName;

    private UserRole userRole;

    private String email;

    private String password;

    private String address;

    private String brn; //사업자 등록 번호

    private String phoneNumber;

}
