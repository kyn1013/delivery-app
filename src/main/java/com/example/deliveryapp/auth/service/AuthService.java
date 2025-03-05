package com.example.deliveryapp.auth.service;

import com.example.deliveryapp.address.repository.AddressRepository;
import com.example.deliveryapp.auth.common.encoder.PasswordEncoder;
import com.example.deliveryapp.auth.common.exception.InvalidRequestException;
import com.example.deliveryapp.auth.common.util.JwtUtil;
import com.example.deliveryapp.auth.dto.request.LoginRequestDto;
import com.example.deliveryapp.auth.dto.request.SignupRequestDto;
import com.example.deliveryapp.auth.dto.response.LoginResponseDto;
import com.example.deliveryapp.auth.dto.response.ReissueTokenResponseDto;
import com.example.deliveryapp.auth.dto.response.SignupResponseDto;
import com.example.deliveryapp.auth.entity.RefreshToken;
import com.example.deliveryapp.auth.entity.RefreshTokenBlackList;
import com.example.deliveryapp.auth.repository.BlackListRepository;
import com.example.deliveryapp.auth.repository.RefreshTokenRepository;
import com.example.deliveryapp.address.entity.Address;
import com.example.deliveryapp.user.entity.User;
import com.example.deliveryapp.user.enums.IsDeleted;
import com.example.deliveryapp.user.enums.UserRole;
import com.example.deliveryapp.user.repository.UserRepository;
import com.sun.jdi.request.InvalidRequestStateException;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final BlackListRepository blackListRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AddressRepository addressRepository;

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
        Address newAddress;

        //고객
        if(UserRole.ROLE_CUSTOMER.equals(userRole)) {
            newUser = new User(
                    dto.getUserName(),
                    userRole,
                    dto.getEmail(),
                    encodedPassword,
                    dto.getPhoneNumber()
            );
            newAddress = new Address(newUser, dto.getAddress());
            newAddress.setDefault(true);
        }else if(UserRole.ROLE_OWNER.equals(userRole)) {
            newUser = new User(
                    dto.getUserName(),
                    userRole,
                    dto.getEmail(),
                    encodedPassword,
                    dto.getBrn(),
                    dto.getPhoneNumber()
            );
            newAddress = new Address(newUser, dto.getAddress());
            newAddress.setDefault(true);
        } else {
            throw new InvalidRequestStateException("유효하지 않은 사용자 역할입니다.");
        }

        //저장
        Address savedAddress = addressRepository.save(newAddress);
        User savedUser = userRepository.save(newUser);

        //처음 받아온 1개의 배송지를 기본 배송지로 설정
        savedAddress.setDefault(true);

        return new SignupResponseDto(
                savedUser.getId(),
                savedUser.getUserName(),
                savedUser.getUserRole(),
                savedUser.getEmail(),
                savedUser.getPassword(),
                savedAddress.getAddress(),
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

        //access token 발급 (30분)
        String accessToken = jwtUtil.createAccessToken(foundUser.getId(), foundUser.getEmail(), foundUser.getUserRole());

        //refresh token 발급 (1달)
        String refreshToken = jwtUtil.createRefreshToken(foundUser.getId(), foundUser.getEmail(), foundUser.getUserRole());

        //DB에 refresh token 저장
        String substringRefreshToken = jwtUtil.substringToken(refreshToken);
        RefreshToken storedRefreshToken = new RefreshToken(substringRefreshToken);
        refreshTokenRepository.save(storedRefreshToken);

        return new LoginResponseDto(accessToken, refreshToken);
    }

    public void logout(Long id, String refreshToken) {
        //유저 검색
        if(!userRepository.existsById(id)) {
            throw new InvalidRequestException("존재하지 않는 유저입니다.");
        }

        //해당 사용자의 refresh token을 blacklist에 추가
        RefreshTokenBlackList logoutUser = new RefreshTokenBlackList(refreshToken);
        blackListRepository.save(logoutUser);
    }

    public ReissueTokenResponseDto reissueToken(String refreshToken) {
        // 블랙 리스트 검증 (로그아웃된 사용자의 refresh token은 아닌가?_
        if(blackListRepository.existsByRefreshToken(refreshToken)) {
            throw new InvalidRequestException("로그아웃된 사용자의 Refresh Token입니다.");
        }

        // DB 검증
        if(!refreshTokenRepository.existsByRefreshToken(refreshToken)) {
            throw new InvalidRequestException("유효하지 않은 Refresh Token입니다.");
        }

        // Access Token 재발급
        Claims claims = jwtUtil.extractClaims(refreshToken);
        Long userId = Long.parseLong(claims.getSubject());
        User foundUser = userRepository.findById(userId).orElseThrow(
                () -> new InvalidRequestException("존재하지 않는 사용자입니다.")
        );

        String reissuedAccessToken = jwtUtil.createAccessToken(foundUser.getId(), foundUser.getEmail(), foundUser.getUserRole());

        return new ReissueTokenResponseDto(reissuedAccessToken, refreshToken);

    }

}
