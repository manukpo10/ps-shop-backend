package com.techrepair.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/publico")
    public ResponseEntity<Map<String, String>> publico() {
        return ResponseEntity.ok(Map.of("mensaje", "Este es público"));
    }

    @GetMapping("/protegido")
    public ResponseEntity<Map<String, String>> protegido() {
        return ResponseEntity.ok(Map.of("mensaje", "Este es protegido, necesitas token"));
    }
}