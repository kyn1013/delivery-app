package com.example.deliveryapp.store.controller;

import com.example.deliveryapp.auth.entity.AuthUser;
import com.example.deliveryapp.common.annotation.Auth;
import com.example.deliveryapp.store.dto.request.StoreCreateRequestDto;
import com.example.deliveryapp.store.dto.request.StoreUpdateRequestDto;
import com.example.deliveryapp.store.dto.response.StoreResponseDto;
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
import java.util.stream.Collectors;

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
    // 특정 키워드로 가게 검색
    @GetMapping("/search")
    public ResponseEntity<?> searchStores(@RequestParam String keyword) {
        List<StoreResponseDto> responseDtos = storeService.searchStores(keyword);

        if (responseDtos.isEmpty()) {
            return new ResponseEntity<>("검색된 가게가 없습니다.", HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(responseDtos);  // DTO 리스트를 반환
    }

    // 가게 단건 조회
    @GetMapping("/{storeId}")
    public ResponseEntity<StoreResponseDto> getStoreById(@PathVariable Long storeId) {
        try {
            // StoreResponseDto를 직접 반환
            StoreResponseDto responseDto = storeService.findStoreById(storeId);
            return ResponseEntity.ok(responseDto);  // DTO를 반환
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);  // 404 응답
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);  // 500 응답
        }
    }

    // 가게 정보 전체 수정
    @PutMapping("/{storeId}")
    public ResponseEntity<String> updateStore(
            @PathVariable Long storeId,
            @RequestBody StoreUpdateRequestDto requestDto,
            @Auth AuthUser user) {

        try {
            // 현재 로그인한 사용자 확인
            User storeOwner = userRepository.findById(user.getId()).orElseThrow();

            // 가게 정보 수정
            storeService.updateStore(storeId, requestDto, storeOwner);

            return ResponseEntity.ok("가게 정보가 성공적으로 수정되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("가게 정보 수정에 실패했습니다.");
        }
    }

    // 가게 삭제
    @DeleteMapping("/{storeId}")
    public ResponseEntity<String> deleteStore(
            @PathVariable Long storeId,
            @Auth AuthUser authUser) {
        // 인증된 사용자 정보가 없다면 401 Unauthorized 반환
        if (authUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 후 삭제 가능합니다.");
        }

        // 인증된 사용자의 이메일을 기반으로 User 객체 찾기
        User storeOwner = userRepository.findByEmail(authUser.getEmail())
                .orElseThrow();

        try {
            // 해당 사용자가 소유한 가게만 삭제 가능
            storeService.deleteStore(storeId, storeOwner); // 삭제 서비스 호출
            return ResponseEntity.ok("가게가 성공적으로 삭제되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 가게를 찾을 수 없습니다.");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("본인 가게만 삭제할 수 있습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("가게 삭제 중 오류가 발생했습니다.");
        }
    }
}
