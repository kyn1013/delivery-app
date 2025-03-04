package com.example.deliveryapp.menu.repository;

import com.example.deliveryapp.menu.entity.Menu;
import com.example.deliveryapp.menu.enums.MenuStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {

    // 특정 store의 메뉴 전체 조회, 카테고리 순서로 정렬,
    @Query("SELECT m FROM Menu m WHERE m.store.id = :storeId ORDER BY m.menuCategory, m.menuName ASC")
    List<Menu> findByStoreIdOrderByCategory(Long storeId);

    // 특정 store의 상태가 AVAILABLE인 메뉴 전체 조회, 카테고리 순서로 분류, 판매량에 따라 내림차순 정렬
    @Query("SELECT m FROM Menu m WHERE m.store.id = :storeId AND" +
            " m.menuStatus = 'AVAILABLE' ORDER BY m.menuCategory, m.salesCount DESC ")
    List<Menu> findByStoreIdOrderByCategoryActiveOnly(Long storeId);

    List<Menu> findAllByStoreIdAndMenuStatusNot(Long storeId, MenuStatus status);

    boolean existsByStoreIdAndMenuName(Long storeId, String menuName);
}
