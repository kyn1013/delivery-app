package com.example.deliveryapp.menu.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/{storeId}/menus")
@RequiredArgsConstructor
public class MenuController {
}
