package com.esliceyament.categoryservice.controller;

import com.esliceyament.categoryservice.dto.AttributesDto;
import com.esliceyament.categoryservice.payload.AttributesPayload;
import com.esliceyament.categoryservice.service.AttributesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/attributes")
@RequiredArgsConstructor
public class AttributesController {

    private final AttributesService attributesService;

    @PostMapping("/add")
    public ResponseEntity<?> createAttribute(@RequestBody AttributesPayload payload) {
        return ResponseEntity.ok(attributesService.addAttribute(payload));
    }

    @PostMapping("/adds")
    public ResponseEntity<?> createAttributes(@RequestBody List<AttributesPayload> payloads) {
        attributesService.addAttributes(payloads);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAttribute(@PathVariable Long id, @RequestBody AttributesDto dto) {
        return ResponseEntity.ok(attributesService.updateAttribute(id, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAttributes(@PathVariable Long id) {
        return ResponseEntity.ok(attributesService.getAttribute(id));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<AttributesDto> getAttributesByName(@PathVariable String name) {
        return ResponseEntity.ok(attributesService.getAttributesByName(name));
    }

    @GetMapping("/all/{name}")
    public ResponseEntity<?> getAllAttributes(@PathVariable String name) {
        return ResponseEntity.ok(attributesService.getAllAttributes(name));
    }

}
