package com.example.deliveryapp.order.service;

import com.example.deliveryapp.cart.entity.Cart;
import com.example.deliveryapp.cart.repository.CartRepository;
import com.example.deliveryapp.order.dto.response.OrderDetailResponseDto;
import com.example.deliveryapp.order.dto.response.OrderInfoResponseDto;
import com.example.deliveryapp.order.dto.response.OrderResponseDto;
import com.example.deliveryapp.order.entity.Order;
import com.example.deliveryapp.order.entity.OrderDetail;
import com.example.deliveryapp.order.practice_repository.MemberRepository;
import com.example.deliveryapp.order.practice_repository.StoreRepository;
import com.example.deliveryapp.order.pratice_entity.Member;
import com.example.deliveryapp.order.pratice_entity.Store;
import com.example.deliveryapp.order.repository.OrderDetailRepository;
import com.example.deliveryapp.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final StoreRepository storeRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public OrderResponseDto save(Long userId) {
        // 사용자 아이디로 장바구니에 있는 목록을 조회해서 첫번째 메뉴를 통해서 가게 아이디를 뽑아옴
        Member member = memberRepository.findById(userId).orElseThrow(() -> new RuntimeException("데이터가 없습니다"));
        List<Cart> carts = cartRepository.findByUserId(userId);
        Long storeId = carts.get(0).getPMenu().getStore().getId();
        Store store = storeRepository.findById(storeId).orElseThrow(() -> new RuntimeException("데이터가 없습니다."));
        // 뽑아온 가게 아이디를 이용해서 주문 객체 생성
        Order order = Order.builder()
                .store(store)
                .member(member)
                .orderNumber(generateMerchantUid())
                .state("주문완료")
                .build();

        Order savedOrder = orderRepository.save(order);
        // 가지고 온 목록을 이용해서 주문 상세 엔티티 생성하기
        for(Cart cart : carts){
            OrderDetail orderDetail = new OrderDetail(cart.getPMenu(), savedOrder, cart.getQuantity());
            orderDetailRepository.save(orderDetail);
        }

        // 장바구니 비우기
        cartRepository.deleteByMemberId(userId);

        // 주문 정보 조회하기
        Order readOrder = orderRepository.findByUserId(userId);
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(readOrder.getId());
        List<OrderDetailResponseDto> orderDetailResponseDtos = OrderDetailResponseDto.toResponse(orderDetails);
        OrderResponseDto orderResponseDto = OrderResponseDto.builder()
                .orderId(readOrder.getId())
                .memberName(readOrder.getMember().getName())
                .storeName(readOrder.getStore().getName())
                .state(readOrder.getState())
                .orderDetailResponseDtos(orderDetailResponseDtos)
                .build();
        return orderResponseDto;
    }

    public OrderResponseDto getOrder(Long orderId) {
        Order readOrder = orderRepository.findByOrderId(orderId);
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(readOrder.getId());
        List<OrderDetailResponseDto> orderDetailResponseDtos = OrderDetailResponseDto.toResponse(orderDetails);
        OrderResponseDto orderResponseDto = OrderResponseDto.builder()
                .orderId(readOrder.getId())
                .memberName(readOrder.getMember().getName())
                .storeName(readOrder.getStore().getName())
                .state(readOrder.getState())
                .orderDetailResponseDtos(orderDetailResponseDtos)
                .build();
        return orderResponseDto;
    }

    // 주문번호 생성 메서드
    private String generateMerchantUid() {
        // 현재 날짜와 시간을 포함한 고유한 문자열 생성
        String uniqueString = UUID.randomUUID().toString().replace("-", "");
        LocalDateTime today = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDay = today.format(formatter).replace("-", "");

        // 무작위 문자열과 현재 날짜/시간을 조합하여 주문번호 생성
        return formattedDay +'-'+ uniqueString;
    }

    public Page<OrderInfoResponseDto> getOrders(int page, int size, Long userId) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Order> orderPage = orderRepository.findByUserIdPaged(pageable, userId);
        Page<OrderInfoResponseDto> orderResponseDtoPage = OrderInfoResponseDto.toResponsePage(orderPage, pageable);
        return orderResponseDtoPage;
    }

    @Transactional
    public OrderInfoResponseDto cancelOrder(Long orderId) {
        Order order = orderRepository.findByOrderId(orderId);
        order.update("주문취소");
        OrderInfoResponseDto orderInfoResponseDto = OrderInfoResponseDto.builder()
                .orderId(order.getId())
                .memberName(order.getMember().getName())
                .storeName(order.getStore().getName())
                .state(order.getState())
                .build();
        return orderInfoResponseDto;
    }

    @Transactional
    public OrderInfoResponseDto acceptOrder(Long orderId) {
        Order order = orderRepository.findByOrderId(orderId);
        order.update("주문수락");
        OrderInfoResponseDto orderInfoResponseDto = OrderInfoResponseDto.builder()
                                            .orderId(order.getId())
                                            .memberName(order.getMember().getName())
                                            .storeName(order.getStore().getName())
                                            .state(order.getState())
                                            .build();
        return orderInfoResponseDto;
    }

    @Transactional
    public OrderInfoResponseDto rejectOrder(Long orderId) {
        Order order = orderRepository.findByOrderId(orderId);
        order.update("주문거절");
        OrderInfoResponseDto orderInfoResponseDto = OrderInfoResponseDto.builder()
                .orderId(order.getId())
                .memberName(order.getMember().getName())
                .storeName(order.getStore().getName())
                .state(order.getState())
                .build();
        return orderInfoResponseDto;
    }

    @Transactional
    public OrderInfoResponseDto deliveringOrder(Long orderId) {
        Order order = orderRepository.findByOrderId(orderId);
        order.update("배달중");
        OrderInfoResponseDto orderInfoResponseDto = OrderInfoResponseDto.builder()
                .orderId(order.getId())
                .memberName(order.getMember().getName())
                .storeName(order.getStore().getName())
                .state(order.getState())
                .build();
        return orderInfoResponseDto;
    }

    @Transactional
    public OrderInfoResponseDto completeOrder(Long orderId) {
        Order order = orderRepository.findByOrderId(orderId);
        order.update("배달완료");
        OrderInfoResponseDto orderInfoResponseDto = OrderInfoResponseDto.builder()
                .orderId(order.getId())
                .memberName(order.getMember().getName())
                .storeName(order.getStore().getName())
                .state(order.getState())
                .build();
        return orderInfoResponseDto;
    }
}
