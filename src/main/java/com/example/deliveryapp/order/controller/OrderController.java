package com.example.deliveryapp.order.controller;

import com.example.deliveryapp.order.dto.response.OrderInfoResponseDto;
import com.example.deliveryapp.order.dto.response.OrderResponseDto;
import com.example.deliveryapp.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    /*
     * 주문 등록하기
     */
    @PostMapping("/{userId}")
    public ResponseEntity<OrderResponseDto> save(@PathVariable Long userId){
        OrderResponseDto responseDto = orderService.save(userId);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    /*
     * 주문 단건 조회
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> getOrder(@PathVariable Long orderId){
        OrderResponseDto responseDto = orderService.getOrder(orderId);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    /*
     * 주문 다건 조회 (자신이 한 주문)
     */
    @GetMapping("/all/{userId}")
    public ResponseEntity<Page<OrderInfoResponseDto>> getOrders(@RequestParam(defaultValue = "1") int page,
                                                                @RequestParam(defaultValue = "10") int size,
                                                                @PathVariable Long userId
    ) {
        Page<OrderInfoResponseDto> responseDto = orderService.getOrders(page, size, userId);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    /*
     * 주문 상태 변경 - 손님 : 주문 취소
     */
    @PatchMapping("/cancel/{orderId}")
    public ResponseEntity<OrderInfoResponseDto> cancelOrder(@PathVariable Long orderId){
        OrderInfoResponseDto responseDto = orderService.cancelOrder(orderId);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    /*
     * 주문 상태 변경 - 사장님 : 주문 수락
     */
    @PatchMapping("/accept/{orderId}")
    public ResponseEntity<OrderInfoResponseDto> acceptOrder(@PathVariable Long orderId){
        OrderInfoResponseDto responseDto = orderService.acceptOrder(orderId);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    /*
     * 주문 상태 변경 - 사장님 : 주문 거절
     */
    @PatchMapping("/reject/{orderId}")
    public ResponseEntity<OrderInfoResponseDto> rejectOrder(@PathVariable Long orderId){
        OrderInfoResponseDto responseDto = orderService.rejectOrder(orderId);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    /*
     * 주문 상태 변경 - 사장님 : 배달중
     */
    @PatchMapping("/delivering/{orderId}")
    public ResponseEntity<OrderInfoResponseDto> deliveringOrder(@PathVariable Long orderId){
        OrderInfoResponseDto responseDto = orderService.deliveringOrder(orderId);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    /*
     * 주문 상태 변경 - 사장님 : 배달완료
     */
    @PatchMapping("/complete/{orderId}")
    public ResponseEntity<OrderInfoResponseDto> completeOrder(@PathVariable Long orderId){
        OrderInfoResponseDto responseDto = orderService.completeOrder(orderId);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

}
