package com.example.deliveryapp.address.service;

import com.example.deliveryapp.address.entity.Address;
import com.example.deliveryapp.address.repository.AddressRepository;
import com.example.deliveryapp.common.exception.custom_exception.InvalidRequestException;
import com.example.deliveryapp.common.exception.errorcode.ErrorCode;
import com.example.deliveryapp.user.dto.request.AddAddressRequestDto;
import com.example.deliveryapp.user.dto.request.ChangeAddressRequestDto;
import com.example.deliveryapp.user.dto.response.AddressResponseDto;
import com.example.deliveryapp.user.dto.response.ChangeAddressResponseDto;
import com.example.deliveryapp.user.entity.User;
import com.example.deliveryapp.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    //배송지 추가
    @Transactional
    public AddressResponseDto addAddress(Long id, AddAddressRequestDto dto) {

        //유효한 유저 검사
        User foundUser = userRepository.findById(id).orElseThrow(
                () -> new InvalidRequestException(ErrorCode.USER_NOT_FOUND)
        );

        //해당 유저 아이디로 저장된 주소 개수가 10개 초과인지 확인
        Long addressCount = addressRepository.countAddressesByUserId(id);
        if (addressCount > 9) {
            throw new InvalidRequestException(ErrorCode.MAX_ADDRESS_COUNT);
        }

        //배송지 생성
        Address newAddress = new Address(foundUser, dto.getAddress());

        //저장
        Address savedAddress = addressRepository.save(newAddress);

        return new AddressResponseDto(
                savedAddress.getId(),
                savedAddress.getAddress(),
                savedAddress.getIsDefault()
        );
    }

    //배송지 삭제
    @Transactional
    public void deleteAddress(Long userId, Long addressId) {
        //유효한 유저 검사
        User foundUser = userRepository.findById(userId).orElseThrow(
                () -> new InvalidRequestException(ErrorCode.USER_NOT_FOUND)
        );

        //유효한 주소 검사
        Address foundAddress = addressRepository.findById(addressId).orElseThrow(
                () -> new InvalidRequestException(ErrorCode.ADDRESS_NOT_FOUND)
        );

        //이 주소가 이 유저의 주소가 맞는지
        if (foundAddress.getUser().getId() != foundUser.getId()) {
            throw new InvalidRequestException(ErrorCode.USER_ADDRESS_MISMATCH);
        }

        //해당 주소가 기본배송지인지 검사 -> 기본배송지라면 삭제 불가능
        if(foundAddress.getIsDefault() == true) {
            throw new InvalidRequestException(ErrorCode.FORBIDDEN_DEFAULT_ADDRESS_DELETION);
        }

        //해당 유저 아이디로 조회 시 총 주소개수가 1개라면 삭제 불가능
        Long addressCount = addressRepository.countAddressesByUserId(foundUser.getId());

        if (addressCount <= 1) {
            throw new InvalidRequestException(ErrorCode.FORBIDDEN_LAST_ADDRESS_DELETION);
        }

        //삭제
        addressRepository.deleteById(addressId);
    }

    //배송지 수정 (주소 수정)
    @Transactional
    public ChangeAddressResponseDto changeAddress (Long userId, Long addressId, ChangeAddressRequestDto dto) {
        //유효한 유저 검사
        User foundUser = userRepository.findById(userId).orElseThrow(
                () -> new InvalidRequestException(ErrorCode.USER_NOT_FOUND)
        );

        //유효한 주소 검사
        Address foundAddress = addressRepository.findById(addressId).orElseThrow(
                () -> new InvalidRequestException(ErrorCode.ADDRESS_NOT_FOUND)
        );

        //이 주소가 이 유저의 주소가 맞는지
        if (foundAddress.getUser().getId() != foundUser.getId()) {
            throw new InvalidRequestException(ErrorCode.USER_ADDRESS_MISMATCH);
        }

        //수정
        foundAddress.updateAddress(dto.getNewAddress());

        return new ChangeAddressResponseDto(
                foundAddress.getId(),
                foundAddress.getAddress(),
                foundAddress.getIsDefault()
        );
    }

    //기본 배송지 변경
    @Transactional
    public ChangeAddressResponseDto changeDefaultAddress(Long userId, Long addressId) {
        //유효한 유저 검사
        User foundUser = userRepository.findById(userId).orElseThrow(
                () -> new InvalidRequestException(ErrorCode.USER_NOT_FOUND)
        );

        //유효한 주소 검사
        Address foundAddress = addressRepository.findById(addressId).orElseThrow(
                () -> new InvalidRequestException(ErrorCode.ADDRESS_NOT_FOUND)
        );

        //이 주소가 이 유저의 주소가 맞는지
        if (foundAddress.getUser().getId() != foundUser.getId()) {
            throw new InvalidRequestException(ErrorCode.USER_ADDRESS_MISMATCH);
        }

        //해당 사용자의 모든 주소들을 false로
        addressRepository.resetDefaultAddress(userId);

        //요청값으로 들어온 주소만 true로
        foundAddress.setDefault(true);

        return new ChangeAddressResponseDto(
                foundAddress.getId(),
                foundAddress.getAddress(),
                foundAddress.getIsDefault()
        );
    }

    //배송지 전체 조회
    @Transactional
    public List<AddressResponseDto> getAddresses (Long userId) {
        //유효한 유저 검사
        User foundUser = userRepository.findById(userId).orElseThrow(
                () -> new InvalidRequestException(ErrorCode.USER_NOT_FOUND)
        );

        //해당 유저가 가지고있는 모든 주소들을 반환
        return addressRepository.findAllByUserId(foundUser.getId()).stream().map(Address::toDto).toList();
    }

    //배송지 단건 조회
    @Transactional
    public AddressResponseDto getAddress (Long userId, Long addressId) {
        //유효한 유저 검사
        User foundUser = userRepository.findById(userId).orElseThrow(
                () -> new InvalidRequestException(ErrorCode.USER_NOT_FOUND)
        );

        //유효한 주소 검사
        Address foundAddress = addressRepository.findById(addressId).orElseThrow(
                () -> new InvalidRequestException(ErrorCode.ADDRESS_NOT_FOUND)
        );

        //이 주소가 이 유저의 주소가 맞는지
        if (foundAddress.getUser().getId() != foundUser.getId()) {
            throw new InvalidRequestException(ErrorCode.USER_ADDRESS_MISMATCH);
        }

        return new AddressResponseDto(
                foundAddress.getId(),
                foundAddress.getAddress(),
                foundAddress.getIsDefault()
        );
    }


}
