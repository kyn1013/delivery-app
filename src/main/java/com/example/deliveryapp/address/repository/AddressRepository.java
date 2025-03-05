package com.example.deliveryapp.address.repository;

import com.example.deliveryapp.address.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    @Query("SELECT a FROM Address a WHERE a.user.id = :userId AND a.isDefault = true")
    Address findDefaultAddressByUserId(@Param("userId") Long userId);

    //새로운 기본 배송지를 설정할 때, 기존 기본 배송지를 false로 같이 업데이트하는 JPQL 메서드
    @Modifying
    @Query("""
    UPDATE Address a 
    SET a.isDefault = CASE 
        WHEN a.id = :addressId THEN true  
        WHEN a.user.id = :userId THEN false  
        ELSE a.isDefault 
    END
    WHERE a.user.id = :userId""")
    void updateDefaultAddress(@Param("userId") Long userId, @Param("addressId") Long addressId);

    //해당 유저가 등록한 주소 개수를 반환
    @Query("SELECT COUNT(a) FROM Address a WHERE a.user.id = :userId")
    Long countAddressesByUserId(@Param("userId") Long userId);

    //해당 유저의 주소들을 모두 반환
    List<Address> findAllByUserId(Long userId);
}


