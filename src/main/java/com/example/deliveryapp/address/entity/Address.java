package com.example.deliveryapp.address.entity;

import com.example.deliveryapp.user.dto.response.AddressResponseDto;
import com.example.deliveryapp.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Address {

    // 처음 회원가입 시 주소 1개는 필수로 받아옴
    // 처음 회원가입 시 받아온 주소를 기본 배송지(isDefault = true)로 설정
    // 배송지는 최대 총합 10개까지 가능
    // 기본배송지는 유저 당 only 1개

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private Boolean isDefault = false;

    public Address(User user, String address) {
        this.user = user;
        this.address = address;
    }

    public void setDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public void updateAddress(String newAddress) {
        this.address = newAddress;
    }

    public static AddressResponseDto toDto(Address address) {
        return new AddressResponseDto(
                address.getId(),
                address.getAddress(),
                address.getIsDefault()
        );
    }
}
