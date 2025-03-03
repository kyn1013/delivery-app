package com.example.deliveryapp.menu.service;

import com.example.deliveryapp.menu.dto.request.MenuCreateRequestDto;
import com.example.deliveryapp.menu.dto.request.MenuSimpleUpdateRequestDto;
import com.example.deliveryapp.menu.dto.request.MenuUpdateRequestDto;
import com.example.deliveryapp.menu.dto.response.MenuDetailResponseDto;
import com.example.deliveryapp.menu.dto.response.MenuSimpleResponseDto;
import com.example.deliveryapp.menu.entity.Menu;
import com.example.deliveryapp.menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;

    @Transactional
    public MenuSimpleResponseDto saveMenu(MenuCreateRequestDto dto, Long storeId) {

        // 해당 스토어에 중복된 메뉴 이름 검증.
        if (menuRepository.existsByStoreIdAndMenuName(storeId,dto.getMenuName())) {
            throw new IllegalArgumentException("Menu already exists");
        }

        Menu menu = new Menu(
                dto.getMenuName(),
                dto.getPrice(),
                dto.getMenuCategory(),
                dto.getMenuStatus(),
                dto.getStockQuantity(),
                dto.getDescription()
        );

        Menu savedMenu = menuRepository.save(menu);
        return MenuSimpleResponseDto.toDto(savedMenu);
    }

    // 가게 메뉴 페이지 to 클라이언트
    @Transactional(readOnly = true)
    public List<MenuSimpleResponseDto> findAvailableMenusByStoreId(Long storeId) {
        List<Menu> menus = menuRepository.findByStoreIdOrderByCategoryActiveOnly(storeId);
        return menus.stream().map(MenuSimpleResponseDto::toDto)
                .collect(Collectors.toList());
    }

    // 가게 메뉴 페이지 to 사장님
    @Transactional(readOnly = true)
    public List<MenuDetailResponseDto> findAllMenusByStoreId(Long storeId) {
        List<Menu> menus = menuRepository.findByStoreIdOrderByCategory(storeId);
        return menus.stream().map(MenuDetailResponseDto::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public MenuSimpleResponseDto updateMenuSimple(MenuSimpleUpdateRequestDto dto, Long menuId) {
        Menu menu = menuRepository.findById(menuId).orElseThrow(
                () -> new IllegalArgumentException("Menu not found")
        );

        menu.simpleUpdate(
                dto.getMenuStatus(),
                dto.getStockQuantity()
        );

        return MenuSimpleResponseDto.toDto(menuRepository.save(menu));
    }

    @Transactional
    public MenuDetailResponseDto updateMenuDetail(MenuUpdateRequestDto dto) {
        return null;
    }

}
