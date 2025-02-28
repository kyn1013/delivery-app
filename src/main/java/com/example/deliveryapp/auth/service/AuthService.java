package com.example.deliveryapp.auth.service;

import com.example.deliveryapp.auth.common.encoder.PasswordEncoder;
import com.example.deliveryapp.auth.common.exception.InvalidRequestException;
import com.example.deliveryapp.auth.common.util.JwtUtil;
import com.example.deliveryapp.auth.dto.request.LoginRequestDto;
import com.example.deliveryapp.auth.dto.request.SignupRequestDto;
import com.example.deliveryapp.auth.dto.request.WithdrawRequestDto;
import com.example.deliveryapp.auth.dto.response.LoginResponseDto;
import com.example.deliveryapp.auth.dto.response.SignupResponseDto;
import com.example.deliveryapp.user.entity.User;
import com.example.deliveryapp.user.enums.IsDeleted;
import com.example.deliveryapp.user.enums.UserRole;
import com.example.deliveryapp.user.repository.UserRepository;
import com.sun.jdi.request.InvalidRequestStateException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public SignupResponseDto signup(SignupRequestDto dto) {
        //중복 이메일 검증
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new InvalidRequestStateException("이미 존재하는 이메일입니다.");
        }

        //비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(dto.getPassword());

        //객체 생성
        UserRole userRole = UserRole.of(dto.getUserRole());
        User newUser;

        //고객
        if(UserRole.ROLE_CUSTOMER.equals(userRole)) {
            newUser = new User(
                    dto.getUserName(),
                    userRole,
                    dto.getEmail(),
                    encodedPassword,
                    dto.getAddress(),
                    dto.getPhoneNumber()
            );
        }else if(UserRole.ROLE_OWNER.equals(userRole)) {
            newUser = new User(
                    dto.getUserName(),
                    userRole,
                    dto.getEmail(),
                    encodedPassword,
                    dto.getAddress(),
                    dto.getBrn(),
                    dto.getPhoneNumber()
            );
        } else {
            throw new InvalidRequestStateException("유효하지 않은 사용자 역할입니다.");
        }

        //저장
        User savedUser = userRepository.save(newUser);

        return new SignupResponseDto(
                savedUser.getId(),
                savedUser.getUserName(),
                savedUser.getUserRole(),
                savedUser.getEmail(),
                savedUser.getPassword(),
                savedUser.getAddress(),
                savedUser.getBrn(), // `ROLE_CUSTOMER`일 경우 `null`로 설정됨
                savedUser.getPhoneNumber()
        );
    }

    @Transactional
    public void withdraw(Long id) {
        //유저 검색
        User foundUser = userRepository.findById(id).orElseThrow(
                () -> new InvalidRequestException("존재하지 않는 유저입니다.")
        );

        //idDeleted 업데이트
        foundUser.updateDelete(IsDeleted.WITHDRAWN);
    }

    @Transactional
    public LoginResponseDto login(LoginRequestDto dto) {
        //유효 이메일 검증
        User foundUser = userRepository.findByEmail(dto.getEmail()).orElseThrow(
                () -> new InvalidRequestException("존재하지 않는 유저입니다."));

        //비밀번호 일치 검증
        if (!passwordEncoder.matches(dto.getPassword(), foundUser.getPassword())) {
            throw new InvalidRequestException("비밀번호가 일치하지 않습니다.");
        }

        //토큰 발급
        String token = jwtUtil.createToken(foundUser.getId(), foundUser.getEmail(), foundUser.getUserRole());

        return new LoginResponseDto(token);
    }
}
