package com.example.deliveryapp.user.dto.response;

import com.example.deliveryapp.user.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponseDto {
    private long id;

    private String userName;

    private UserRole userRole;

    private String email;

    private String password;

}
