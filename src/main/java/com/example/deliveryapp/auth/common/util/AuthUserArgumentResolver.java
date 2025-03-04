package com.example.deliveryapp.auth.common.util;

import com.example.deliveryapp.auth.common.exception.AuthException;
import com.example.deliveryapp.auth.entity.AuthUser;
import com.example.deliveryapp.common.annotation.Auth;
import com.example.deliveryapp.user.enums.UserRole;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

//Argument Resolver를 사용하면 컨트롤러 메서드의 파라미터 중 특정 조건에 맞는 파라미터가 있다면,
//요청에 들어온 값을 이용해 원하는 객체를 만들어 바인딩해줄 수 있다.
//매번 JWT의 페이로드에서 사용자 정보를 가져오는 행동을 컨트롤러에서 반복한다면 수많은 반복이 일어날 것이다.
//따라서 매번 컨트롤러에서 작업반복 보다 resolver와 어노테이션으로 해결하자.

//컨트롤러의 @Auth AuthUser authUser 파라미터를
//자동으로 현재 로그인한 사용자 정보 (AuthUser 객체) 로 변환해준다.

public class AuthUserArgumentResolver implements HandlerMethodArgumentResolver {
    //파라미터 지원여부 확인
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        //파라미터에 @Auth 어노테이션이 붙어 있는지 확인, 붙어있으면 true
        boolean hasAuthAnnotation = parameter.getParameterAnnotation(Auth.class) != null;
        //파라미터의 타입이 AuthUser 인지 확인
        boolean isAuthUserType = parameter.getParameterType().equals(AuthUser.class);

        // @Auth 어노테이션과 AuthUser 타입이 함께 사용되지 않은 경우 예외 발생
        if (hasAuthAnnotation != isAuthUserType) {
            throw new AuthException("@Auth와 AuthUser 타입은 함께 사용되어야 합니다.");
        }

        return hasAuthAnnotation;
    }

    //사용자 정보 변환 및 주입
    @Override
    public Object resolveArgument(
            @Nullable MethodParameter parameter,
            @Nullable ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            @Nullable WebDataBinderFactory binderFactory
    ) {
        //현재 요청 객체 (HttpServletRequest) 를 가져옴
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        // JwtFilter 에서 set 한 userId, email, userRole 값을 가져옴
        Long userId = (Long) request.getAttribute("userId");
        String email = (String) request.getAttribute("email");
        UserRole userRole = UserRole.of((String) request.getAttribute("userRole"));

        System.out.println("JwtFilter 에서 set 한 userId, email, userRole 값을 가져옴");
        //가져온 정보를 AuthUser 객체로 변환하여 반환
        return new AuthUser(userId, email, userRole);
    }
}
//동작 흐름 정리
// ✅ 1. 클라이언트가 요청 보냄 (Authorization: Bearer JWT)
// ✅ 2. JwtFilter 에서 JWT 검증 후 request.setAttribute() 로 사용자 정보 저장
// ✅ 3. 컨트롤러에서 @Auth AuthUser authUser 파라미터가 있으면 AuthUserArgumentResolver 실행
// ✅ 4. supportsParameter()에서 @Auth 여부 확인 후 resolveArgument() 실행
// ✅ 5. request 객체에서 사용자 정보 추출하여 AuthUser 객체 생성 후 컨트롤러에 주입
// ✅ 6. 컨트롤러에서 authUser.getId(), authUser.getEmail() 등 사용 가능
