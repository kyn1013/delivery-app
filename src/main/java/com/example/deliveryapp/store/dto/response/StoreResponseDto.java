package com.example.deliveryapp.store.dto.response;

import com.example.deliveryapp.store.entity.Store;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StoreResponseDto {

    private Long id;
    private String businessName;
    private String category;
    private LocalDateTime openingTime;
    private LocalDateTime closingTime;
    private Double minOrderPrice;
    private String ownerUsername;  // 소유자의 username
    private String address;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    //store 객체를 StoreResponseDto로변환
    public StoreResponseDto(Store store) {
        this.id = store.getId();
        this.businessName = store.getBusinessName();
        this.category = store.getCategory();
        this.openingTime = store.getOpeningTime();
        this.closingTime = store.getClosingTime();
        this.minOrderPrice = store.getMinOrderPrice();
        this.ownerUsername = store.getOwner().getUserName(); // 소유자의 username
        this.address = store.getAddress();
        this.createdAt = store.getCreatedAt();
        this.updatedAt = store.getUpdatedAt();
    }
}
