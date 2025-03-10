package com.example.deliveryapp.menu.repository;

import com.example.deliveryapp.menu.entity.Menu;
import com.example.deliveryapp.menu.enums.MenuStatus;
import com.example.deliveryapp.store.entity.Store;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {

    List<Menu> findByStoreId(Long storeId);

    // 특정 store의 메뉴 전체 조회, 카테고리 순서로 정렬,
    @Query("SELECT m FROM Menu m WHERE m.store.id = :storeId ORDER BY m.menuCategory, m.menuName ASC")
    List<Menu> findByStoreIdOrderByCategory(Long storeId);

    // 특정 store의 상태가 AVAILABLE인 메뉴 전체 조회, 카테고리 순서로 분류, 판매량에 따라 내림차순 정렬
    @Query("SELECT m FROM Menu m WHERE m.store.id = :storeId AND" +
            " m.menuStatus = 'AVAILABLE' ORDER BY m.menuCategory, m.salesCount DESC ")
    List<Menu> findByStoreIdOrderByCategoryActiveOnly(Long storeId);

    List<Menu> findAllByStoreIdAndMenuStatusNot(Long storeId, MenuStatus status);

    boolean existsByStoreIdAndMenuName(Long storeId, String menuName);

    // 메뉴와 가게를 함께 조회
    @Query("SELECT m FROM Menu m " +
            "LEFT JOIN FETCH m.store s " +
            "WHERE m.id = :menuId")
    Menu findByIdWithStore(@Param("menuId") Long menuId);

    boolean existsByStoreIdAndMenuNameAndMenuStatusNot(Long storeId, String menuName, MenuStatus status);

    void deleteByStore(Store store);

    // 비관적인 락을 적용하여 메뉴 조회
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = "jakarta.persistence.lock.timeout", value = "5000")}) // 5초 타임아웃 설정, 5초 동안 락 대기,
    Optional<Menu> findById(Long id);
}


