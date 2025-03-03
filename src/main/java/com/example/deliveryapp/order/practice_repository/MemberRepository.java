package com.example.deliveryapp.order.practice_repository;

import com.example.deliveryapp.order.pratice_entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
