package com.example.deliveryapp.store.controller;

import com.example.deliveryapp.auth.entity.AuthUser;
import com.example.deliveryapp.common.annotation.Auth;
import com.example.deliveryapp.store.dto.request.StoreCreateRequestDto;
import com.example.deliveryapp.store.service.StoreService;
import com.example.deliveryapp.user.entity.User;
import com.example.deliveryapp.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/stores")
public class StoreController {


    private final StoreService storeService;
    private final UserRepository userRepository;
    // 가게 생성
    @PostMapping
    public ResponseEntity<String> createStore(
            @Valid
            @RequestBody StoreCreateRequestDto storeCreateRequestDto,
            @Auth AuthUser user) {


        try {
            User storeOwner = userRepository.findById(user.getId()).orElseThrow();

            String username = storeOwner.getUserName();
            storeService.createStore(storeCreateRequestDto, username);
            return new ResponseEntity<>("가게가 성공적으로 생성되었습니다.", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("가게 생성에 실패했습니다.", HttpStatus.BAD_REQUEST);
        }
    }
}
