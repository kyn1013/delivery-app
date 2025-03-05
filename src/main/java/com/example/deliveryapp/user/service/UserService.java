package com.example.deliveryapp.user.service;

import com.example.deliveryapp.address.entity.Address;
import com.example.deliveryapp.address.repository.AddressRepository;
import com.example.deliveryapp.auth.common.encoder.PasswordEncoder;
import com.example.deliveryapp.auth.common.exception.InvalidRequestException;
import com.example.deliveryapp.user.dto.request.ChangePasswordRequestDto;
import com.example.deliveryapp.user.dto.request.ChangeUserNameRequestDto;
import com.example.deliveryapp.user.dto.response.MyPageResponseDto;
import com.example.deliveryapp.user.dto.response.UserResponseDto;
import com.example.deliveryapp.user.entity.User;
import com.example.deliveryapp.user.enums.UserRole;
import com.example.deliveryapp.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AddressRepository addressRepository;

    public MyPageResponseDto myInfo(Long id) {
        //유효한 유저 검사
        User foundUser = userRepository.findById(id).orElseThrow(
                () -> new InvalidRequestException("존재하지 않는 유저입니다.")
        );

        // 주소 테이블에서 해당 유저의 주소들을 검색 -> 그 주소들중 isDefault가 true인 주소 검색
        Address defaultAddressByUserId = addressRepository.findDefaultAddressByUserId(id);

        return new MyPageResponseDto(
                foundUser.getId(),
                foundUser.getUserName(),
                foundUser.getUserRole(),
                foundUser.getEmail(),
                foundUser.getPhoneNumber(),
                defaultAddressByUserId.getAddress()
        );
    }


    @Transactional
    public UserResponseDto changePassword(Long id, ChangePasswordRequestDto dto) {
        //유효한 유저 검사
        User foundUser = userRepository.findById(id).orElseThrow(
                () -> new InvalidRequestException("존재하지 않는 유저입니다.")
        );

        //비밀번호 일치 검사
        if(!passwordEncoder.matches(dto.getOldPassword(), foundUser.getPassword())) {
            throw new InvalidRequestException("비밀번호가 일치하지 않습니다.");
        }

        //객체 업데이트
        foundUser.changePassword(passwordEncoder.encode(dto.getNewPassword()));

        return new UserResponseDto(
                foundUser.getId(),
                foundUser.getUserName(),
                foundUser.getUserRole(),
                foundUser.getEmail(),
                foundUser.getPassword()
        );
    }

    @Transactional
    public UserResponseDto changeUsername(Long id, ChangeUserNameRequestDto dto) {
        //유효한 유저 검사
        User foundUser = userRepository.findById(id).orElseThrow(
                () -> new InvalidRequestException("존재하지 않는 유저입니다.")
        );

        //객체 업데이트
        foundUser.changeUsername(dto.getUserName());

        return new UserResponseDto(
                foundUser.getId(),
                foundUser.getUserName(),
                foundUser.getUserRole(),
                foundUser.getEmail(),
                foundUser.getPassword()
        );
    }
}
