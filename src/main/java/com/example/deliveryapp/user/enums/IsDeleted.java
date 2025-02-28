package com.example.deliveryapp.user.enums;

import com.sun.jdi.request.InvalidRequestStateException;

import java.util.Arrays;

public enum IsDeleted {
    ACTIVE, WITHDRAWN;

    public static IsDeleted of(String role) {
        return Arrays.stream(IsDeleted.values())
                .filter(r -> r.name().equalsIgnoreCase(role))
                .findFirst()
                .orElseThrow(() -> new InvalidRequestStateException("유효하지 않은 deleted 여부"));
    }
}
