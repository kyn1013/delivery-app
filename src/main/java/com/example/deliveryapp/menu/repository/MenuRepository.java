package com.example.deliveryapp.menu.repository;

import com.example.deliveryapp.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {

    // 특정 store의 메뉴 전체 조회, 카테고리 순서로 정렬,
    @Query("SELECT m FROM Menu m WHERE m.store.id = :storeId ORDER BY m.menuCategory, m.menuName ASC")
    List<Menu> findByStoreIdOrderByCategory(@Param("storeId") Long storeId);

    // 특정 store의 상태가 AVAILABLE인 메뉴 전체 조회, 카테고리 순서로 정렬,
    @Query("SELECT m FROM Menu m WHERE m.store.id = :storeId AND" +
            " m.menuStatus = 'AVAILABLE' ORDER BY m.menuCategory, m.menuName ASC")
    List<Menu> findByStoreIdOrderByCategoryActiveOnly(@Param("storeId") Long storeId);

    boolean existsByStoreIdAndMenuName(Long storeId, String menuName);
}
