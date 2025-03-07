package com.example.deliveryapp.order.aop;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class OrderLoggingAspect {

    private final HttpServletRequest request;
    private final ObjectMapper objectMapper;

    @Around("@annotation(com.example.deliveryapp.order.aop.annotaion.OrderRequest)")
    public Object logAdminApiAccess(ProceedingJoinPoint joinPoint) throws Throwable {
        Long userId = (Long) request.getAttribute("userId");
        String url = request.getRequestURI();
        LocalDateTime requestTimestamp = LocalDateTime.now();

        log.info("AOP - 주문 상태 변경 요청 : userId={}, Timestamp={}, URL={}",
                userId, requestTimestamp, url);

        Object result = joinPoint.proceed();

        String responseBody = objectMapper.writeValueAsString(result);
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        Map<String, Object> params = new HashMap<>();


        for (JsonNode node : jsonNode) {
            if (node.has("storeName")) {
                String storeName = String.valueOf(node.get("storeName"));
                params.put("storeName", storeName);
            }
            if (node.has("orderNumber")) {
                String orderNumber = String.valueOf(node.get("orderNumber"));
                params.put("orderNumber", orderNumber);
            }
            if (node.has("state")) {
                String orderState = String.valueOf(node.get("state"));
                params.put("orderState", orderState);
                }
        }


        log.info("AOP - 주문 상태 변경 응답 : userId={}, Timestamp={}, storeName={}, orderNumber={}, state={}",
                userId, LocalDateTime.now(), params.get("storeName"), params.get("orderNumber"), params.get("orderState"));

        return result;

    }
}
