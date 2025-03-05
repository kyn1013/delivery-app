package com.example.deliveryapp.user.controller;

import com.example.deliveryapp.address.service.AddressService;
import com.example.deliveryapp.auth.entity.AuthUser;
import com.example.deliveryapp.common.annotation.Auth;
import com.example.deliveryapp.user.dto.request.ChangeAddressRequestDto;
import com.example.deliveryapp.user.dto.request.ChangePasswordRequestDto;
import com.example.deliveryapp.user.dto.request.ChangeUserNameRequestDto;
import com.example.deliveryapp.user.dto.response.AddressResponseDto;
import com.example.deliveryapp.user.dto.response.ChangeAddressResponseDto;
import com.example.deliveryapp.user.dto.response.MyPageResponseDto;
import com.example.deliveryapp.user.dto.response.UserResponseDto;
import com.example.deliveryapp.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AddressService addressService;


    //내 정보 조회
    @GetMapping("/api/v1/mypage")
    public ResponseEntity<MyPageResponseDto> myInfo(
            @Auth AuthUser authUser) {
        MyPageResponseDto myPageResponseDto = userService.myInfo(authUser.getId());
        return ResponseEntity.ok(myPageResponseDto);
    }

    //비밀번호 변경
    @PatchMapping("/api/v1/mypage/password")
    public ResponseEntity<UserResponseDto> changePassword(
            @Auth AuthUser authUser, @Valid @RequestBody ChangePasswordRequestDto dto) {
        UserResponseDto passwordChangedUser = userService.changePassword(authUser.getId(), dto);
        return ResponseEntity.ok(passwordChangedUser);
    }

    //닉네임 변경
    @PatchMapping("/api/v1/mypage/username")
    public ResponseEntity<UserResponseDto> changeUsername(
            @Auth AuthUser authUser,
            @Valid @RequestBody ChangeUserNameRequestDto dto) {
        UserResponseDto userNameChangedUser = userService.changeUsername(authUser.getId(), dto);
        return ResponseEntity.ok(userNameChangedUser);
    }

    //배송지 수정
    @PatchMapping("/api/v1/mypage/address/{addressId}")
    public ResponseEntity<ChangeAddressResponseDto> changeAddress(
            @Auth AuthUser authUser,
            @PathVariable Long addressId,
            ChangeAddressRequestDto dto) {
        ChangeAddressResponseDto changeAddressResponseDto = addressService.changeAddress(authUser.getId(), addressId, dto);
        return ResponseEntity.ok(changeAddressResponseDto);
    }

    //기본 배송지 변경
    @PatchMapping("/api/v1/mypage/address/{addressId}/default")
    public ResponseEntity<ChangeAddressResponseDto> changeDefaultAddress(
            @Auth AuthUser authUser,
            @PathVariable Long addressId) {
        ChangeAddressResponseDto changeAddressResponseDto = addressService.changeDefaultAddress(authUser.getId(), addressId);
        return ResponseEntity.ok(changeAddressResponseDto);
    }


    //배송지 추가 (최대 총합이 10개까지)
    @PostMapping("/api/v1/mypage/address")
    public ResponseEntity<String> addAddress (@Auth AuthUser authUser, String address) {
        addressService.addAddress(authUser.getId(), address);
        return ResponseEntity.ok("배송지 추가 성공");
    }

    //배송지 삭제
    @DeleteMapping("/api/v1/mypage/address/{addressId}")
    public ResponseEntity<String> deleteAddress (
            @Auth AuthUser authUser,
            @PathVariable Long addressId) {
        addressService.deleteAddress(authUser.getId(), addressId);
        return ResponseEntity.ok("배송지 삭제 성공");
    }


    //배송지 전체 조회 (10개까지)
    @GetMapping("/api/v1/mypage/address")
    public ResponseEntity<List<AddressResponseDto>> getAddresses (
            @Auth AuthUser authUser) {
        List<AddressResponseDto> addressList = addressService.getAddresses(authUser.getId());
        return ResponseEntity.ok(addressList);
    }


    //배송지 단건 조회
    @GetMapping("/api/v1/mypage/address/{addressId}")
    public ResponseEntity<AddressResponseDto> getAddress (
            @Auth AuthUser authUser,
            @PathVariable Long addressId) {
        AddressResponseDto foundAddress = addressService.getAddress(authUser.getId(), addressId);
        return ResponseEntity.ok(foundAddress);
    }


}
