package com.example.deliveryapp.user.controller;

import com.example.deliveryapp.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    //비밀번호 변경
}
