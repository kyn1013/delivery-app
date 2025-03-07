package com.example.deliveryapp.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReissueTokenResponseDto {

    private final String accessToken;

    private final String refreshToken;
}
