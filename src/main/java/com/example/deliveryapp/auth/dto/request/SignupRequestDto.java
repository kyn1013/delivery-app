package com.example.deliveryapp.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignupRequestDto {

    @NotBlank(message = "닉네임은 필수 입력값입니다.")
    @Pattern(regexp = "^[가-힣a-zA-Z0-9]{2,10}$" ,
            message = "닉네임은 특수문자를 포함하지 않은 2~10자리여야 합니다.")
    private String userName;

    @NotBlank(message = "권한은 필수 입력값입니다.")
    @Pattern(regexp = "ROLE_CUSTOMER|ROLE_OWNER",
            message = "ROLE_CUSTOMER 또는 ROLE_OWNER만 입력 가능합니다.")
    private String userRole;

    @NotBlank(message = "이메일은 필수 입력값입니다.")
    @Email(regexp = "^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[^a-zA-Z0-9])(?!.*\\s).{8,20}$",
            message = "비밀번호는 영문 대,소문자와 숫자, 특수기호가 적어도 1개 이상씩 포함된 8자 ~ 20자의 비밀번호여야 합니다.")
    private String password;

    @NotBlank(message = "주소는 필수 입력값입니다.")
    private String address;

    @Pattern(
            regexp = "^\\d{3}-\\d{2}-\\d{5}$",
            message = "사업자등록번호는 'XXX-XX-XXXXX' 형식이어야 합니다.")
    private String brn; //사업자 등록 번호

    @NotBlank(message = "전화번호는 필수 입력값입니다.")
    @Pattern(regexp = "^01(?:0|1|[6-9])[.-]?(\\d{3}|\\d{4})[.-]?(\\d{4})$",
            message = "10 ~ 11 자리의 숫자만 입력 가능합니다.")
    private String phoneNumber;
}
