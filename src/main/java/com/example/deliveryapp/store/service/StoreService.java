package com.example.deliveryapp.store.service;

import com.example.deliveryapp.store.dto.request.StoreCreateRequestDto;
import com.example.deliveryapp.store.entity.Store;
import com.example.deliveryapp.store.repository.StoreRepository;
import com.example.deliveryapp.user.entity.User;
import com.example.deliveryapp.user.enums.UserRole;
import com.example.deliveryapp.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    // 가게 생성 메서드
    public void createStore(StoreCreateRequestDto storeCreateRequestDto, String username) {
        // 사장님 권한 확인
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 권한이 사장님(OWNER)인지 확인
        if (user.getUserRole() != UserRole.ROLE_OWNER) {
            throw new IllegalStateException("사장님 권한이 필요합니다.");
        }

        // 가게가 이미 3개 이상인 경우 예외 처리
        if (storeRepository.countByOwnerId(user.getId()) >= 3) {
            throw new IllegalStateException("가게는 최대 3개까지 운영할 수 있습니다.");
        }

        // Store 객체 생성
        Store store = new Store();
        store.setBusinessName(storeCreateRequestDto.getBusinessName());
        store.setCategory(storeCreateRequestDto.getCategory());
        store.setOpeningTime(storeCreateRequestDto.getOpeningTime());
        store.setClosingTime(storeCreateRequestDto.getClosingTime());
        store.setMinOrderPrice(storeCreateRequestDto.getMinOrderPrice());
        store.setOwner(user);

        // 저장
        storeRepository.save(store);  // 저장
    }

    //가게명으로 검색
    public List<Store> searchStores(String keyword) {
        return storeRepository.findByBusinessNameContaining(keyword);
    }

    // 가게 단건 조회
    public Store findStoreById(Long storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 가게를 찾을 수 없습니다."));
    }
}
