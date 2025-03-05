package com.example.deliveryapp.order.service;

import com.example.deliveryapp.cart.entity.Cart;
import com.example.deliveryapp.cart.repository.CartRepository;
import com.example.deliveryapp.order.dto.response.OrderDetailResponseDto;
import com.example.deliveryapp.order.dto.response.OrderInfoResponseDto;
import com.example.deliveryapp.order.dto.response.OrderResponseDto;
import com.example.deliveryapp.order.entity.Order;
import com.example.deliveryapp.order.entity.OrderDetail;
import com.example.deliveryapp.order.enums.OrderStatus;
import com.example.deliveryapp.order.repository.OrderDetailRepository;
import com.example.deliveryapp.order.repository.OrderRepository;
import com.example.deliveryapp.store.entity.Store;
import com.example.deliveryapp.store.repository.StoreRepository;
import com.example.deliveryapp.user.entity.User;
import com.example.deliveryapp.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
    private final UserRepository userRepository;

    @Transactional
    public OrderResponseDto save(Long userId) {
        // 사용자 아이디로 장바구니에 있는 목록을 조회해서 첫번째 메뉴를 통해서 가게 아이디를 뽑아옴
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("데이터가 없습니다"));
        List<Cart> carts = cartRepository.findByUserId(userId);
        Long storeId = carts.get(0).getMenu().getStore().getId();
        Store store = storeRepository.findById(storeId).orElseThrow(() -> new RuntimeException("데이터가 없습니다."));
        // 뽑아온 가게 아이디를 이용해서 주문 객체 생성
        Order order = Order.builder()
                .store(store)
                .user(user)
                .orderNumber(generateMerchantUid())
                .state(OrderStatus.of(0))
                .build();

        Order savedOrder = orderRepository.save(order);
        // 가지고 온 목록을 이용해서 주문 상세 엔티티 생성하기
        for(Cart cart : carts){
            OrderDetail orderDetail = new OrderDetail(cart.getMenu(), savedOrder, cart.getQuantity(), cart.getMenu().getPrice());
            orderDetailRepository.save(orderDetail);
        }

        // 장바구니 비우기
        cartRepository.deleteByUserId(userId);

        // 주문 정보 조회하기
        Order readOrder = orderRepository.findByUserId(userId);
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(readOrder.getId());

        // 총 주문 금액 구하기
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (OrderDetail orderDetail : orderDetails){
            BigDecimal itemTotal = orderDetail.getPrice().multiply(BigDecimal.valueOf(orderDetail.getQuantity()));
            totalPrice = totalPrice.add(itemTotal);
        }

        // 주문 엔티티에 총 주문 금액 입력
        order.updateTotalPrice(totalPrice);

        List<OrderDetailResponseDto> orderDetailResponseDtos = OrderDetailResponseDto.toResponse(orderDetails);
        OrderResponseDto orderResponseDto = OrderResponseDto.builder()
                .orderId(readOrder.getId())
                .orderNumber(readOrder.getOrderNumber())
                .memberName(readOrder.getUser().getUserName())
                .storeName(readOrder.getStore().getBusinessName())
                .state(readOrder.getState().getDescription())
                .totalPrice(totalPrice)
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
                .orderNumber(readOrder.getOrderNumber())
                .memberName(readOrder.getUser().getUserName())
                .storeName(readOrder.getStore().getBusinessName())
                .state(readOrder.getState().getDescription())
                .totalPrice(readOrder.getTotalPrice())
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
        order.update(OrderStatus.of(3));
        OrderInfoResponseDto orderInfoResponseDto = OrderInfoResponseDto.builder()
                .orderId(order.getId())
                .orderNumber(order.getOrderNumber())
                .memberName(order.getUser().getUserName())
                .storeName(order.getStore().getBusinessName())
                .state(order.getState().getDescription())
                .totalPrice(order.getTotalPrice())
                .build();
        return orderInfoResponseDto;
    }

    @Transactional
    public OrderInfoResponseDto acceptOrder(Long orderId) {
        Order order = orderRepository.findByOrderId(orderId);
        order.update(OrderStatus.of(1));
        OrderInfoResponseDto orderInfoResponseDto = OrderInfoResponseDto.builder()
                                            .orderId(order.getId())
                                            .orderNumber(order.getOrderNumber())
                                            .memberName(order.getUser().getUserName())
                                            .storeName(order.getStore().getBusinessName())
                                            .state(order.getState().getDescription())
                                            .totalPrice(order.getTotalPrice())
                                            .build();
        return orderInfoResponseDto;
    }

    @Transactional
    public OrderInfoResponseDto rejectOrder(Long orderId) {
        Order order = orderRepository.findByOrderId(orderId);
        order.update(OrderStatus.of(2));
        OrderInfoResponseDto orderInfoResponseDto = OrderInfoResponseDto.builder()
                .orderId(order.getId())
                .orderNumber(order.getOrderNumber())
                .memberName(order.getUser().getUserName())
                .storeName(order.getStore().getBusinessName())
                .state(order.getState().getDescription())
                .totalPrice(order.getTotalPrice())
                .build();
        return orderInfoResponseDto;
    }

    @Transactional
    public OrderInfoResponseDto deliveringOrder(Long orderId) {
        Order order = orderRepository.findByOrderId(orderId);
        order.update(OrderStatus.of(4));
        OrderInfoResponseDto orderInfoResponseDto = OrderInfoResponseDto.builder()
                .orderId(order.getId())
                .orderNumber(order.getOrderNumber())
                .memberName(order.getUser().getUserName())
                .storeName(order.getStore().getBusinessName())
                .state(order.getState().getDescription())
                .totalPrice(order.getTotalPrice())
                .build();
        return orderInfoResponseDto;
    }

    @Transactional
    public OrderInfoResponseDto completeOrder(Long orderId) {
        Order order = orderRepository.findByOrderId(orderId);
        order.update(OrderStatus.of(5));
        OrderInfoResponseDto orderInfoResponseDto = OrderInfoResponseDto.builder()
                .orderId(order.getId())
                .orderNumber(order.getOrderNumber())
                .memberName(order.getUser().getUserName())
                .storeName(order.getStore().getBusinessName())
                .state(order.getState().getDescription())
                .totalPrice(order.getTotalPrice())
                .build();
        return orderInfoResponseDto;
    }
}
