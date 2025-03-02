package com.example.deliveryapp.user.service;

import com.example.deliveryapp.auth.common.encoder.PasswordEncoder;
import com.example.deliveryapp.auth.common.exception.InvalidRequestException;
import com.example.deliveryapp.user.dto.request.ChangePasswordRequestDto;
import com.example.deliveryapp.user.dto.response.UserResponseDto;
import com.example.deliveryapp.user.entity.User;
import com.example.deliveryapp.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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
}
