package com.zhiyun.stock.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: ReigsterController
 * @description:
 * @author: Chonzi.xiao
 * @create: 2021/7/12 20:34
 **/
@RestController
@RequestMapping("auth")
public class AuthController {
    public ResponseEntity register() {
        return ResponseEntity.ok("ddd");
    }
}
