package com.example.deliveryapp.auth.common.util;

import com.example.deliveryapp.auth.common.exception.ServerException;
import com.example.deliveryapp.user.enums.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j(topic = "JwtUtil")
@Component
public class JwtUtil {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final long ACCESS_TOKEN_TIME = 30 * 60 * 1000L; // 30분
    private static final long REFRESH_TOKEN_TIME = 30L * 24 * 60 * 60 * 1000; // 1달

    @Value("${jwt.secret.key}")
    private String secretKey; //application.properties에 있는 jwt.secret.key 가져옴
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256; //jwt 서명을 생성할 때 사용할 해시 알고리즘

    @PostConstruct
    public void init() { //객체가 생성된 후 실행되는 초기화 메서드
        //Base64로 인코딩된 secretKey를 디코딩해서 key로 변환
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    //JWT 생성
    public String createToken(Long userId, String email, UserRole userRole) {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(String.valueOf(userId)) //JWT의 sub(주제) 필드에 userId를 문자열로 저장
                        .claim("email", email)
                        .claim("userRole", userRole) //JWT의 payload에 이메일과 역할(Role)을 추가
                        .setExpiration(new Date(date.getTime() + ACCESS_TOKEN_TIME)) //토큰의 만료 시간을 현재 시간 + 60분으로 설정
                        .setIssuedAt(date) //토큰 발급일을 현재시간으로 설정
                        .signWith(key, signatureAlgorithm) // 암호화 알고리즘으로 서명
                        .compact();
    }

    //access token(30분)과 refresh token(1달)은 만료기간에 있어서 차이가 있음.
    //JWT 생성 (access token)
    public String createAccessToken(Long userId, String email, UserRole userRole) {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(String.valueOf(userId)) //JWT의 sub(주제) 필드에 userId를 문자열로 저장
                        .claim("email", email)
                        .claim("userRole", userRole) //JWT의 payload에 이메일과 역할(Role)을 추가
                        .setExpiration(new Date(date.getTime() + ACCESS_TOKEN_TIME)) //토큰의 만료 시간을 현재 시간 + 30분으로 설정
                        .setIssuedAt(date) //토큰 발급일을 현재시간으로 설정
                        .signWith(key, signatureAlgorithm) // 암호화 알고리즘으로 서명
                        .compact();
    }

    //JWT 생성 (refresh token)
    public String createRefreshToken(Long userId, String email, UserRole userRole) {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(String.valueOf(userId)) //JWT의 sub(주제) 필드에 userId를 문자열로 저장
                        .claim("email", email)
                        .claim("userRole", userRole) //JWT의 payload에 이메일과 역할(Role)을 추가
                        .setExpiration(new Date(date.getTime() + REFRESH_TOKEN_TIME)) //토큰의 만료 시간을 현재 시간 + 1달으로 설정
                        .setIssuedAt(date) //토큰 발급일을 현재시간으로 설정
                        .signWith(key, signatureAlgorithm) // 암호화 알고리즘으로 서명
                        .compact();
    }

    //Bearer 접두사 제거
    public String substringToken(String tokenValue) {
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
            return tokenValue.substring(7); //"Bearer " 부분을 제거하고 순수한 토큰 값만 반환
        }
        throw new ServerException("Not Found Token"); //만약 tokenValue가 null이거나 "Bearer "로 시작하지 않으면 예외 발생
    }

    //JWT에서 데이터 추출
    public Claims extractClaims(String token) {
        return Jwts.parserBuilder() //JWT를 파싱
                .setSigningKey(key) //서명을 검증
                .build()
                .parseClaimsJws(token)
                .getBody(); //토큰의 Payload(사용자 정보)를 추출
    }
}
