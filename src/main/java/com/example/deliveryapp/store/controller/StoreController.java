package com.example.deliveryapp.store.controller;

import com.example.deliveryapp.auth.entity.AuthUser;
import com.example.deliveryapp.common.annotation.Auth;
import com.example.deliveryapp.store.dto.request.StoreCreateRequestDto;
import com.example.deliveryapp.store.entity.Store;
import com.example.deliveryapp.store.service.StoreService;
import com.example.deliveryapp.user.entity.User;
import com.example.deliveryapp.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/stores")
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

            String username = storeOwner.getEmail();
            storeService.createStore(storeCreateRequestDto, username);
            return new ResponseEntity<>("가게가 성공적으로 생성되었습니다.", HttpStatus.CREATED);
        } catch (Exception e) {

            return new ResponseEntity<>("가게 생성에 실패했습니다.", HttpStatus.BAD_REQUEST);
        }
    }
    //특정 키워드로 가게 검색
    @GetMapping("/search")
    public ResponseEntity<?> searchStores(@RequestParam String keyword) {
        List<Store> stores = storeService.searchStores(keyword);
        if (stores.isEmpty()) {
            return new ResponseEntity<>("검색된 가게가 없습니다.", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(stores);
    }

    // 가게 단건 조회
    @GetMapping("/{storeId}")
    public ResponseEntity<Store> getStoreById(@PathVariable Long storeId) {
        Store store = storeService.findStoreById(storeId); // 가게를 조회

        if (store == null) {
            return ResponseEntity.notFound().build(); // 가게가 없으면 404 반환
        }

        return ResponseEntity.ok(store);
    }
}
