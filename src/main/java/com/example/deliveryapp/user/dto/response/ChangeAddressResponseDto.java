package com.example.deliveryapp.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChangeAddressResponseDto {

    private Long id;

    private String address;

    private Boolean isDefault;
}
