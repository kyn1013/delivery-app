package com.example.deliveryapp.order.aop.annotaion;


import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OrderRequest {
}
