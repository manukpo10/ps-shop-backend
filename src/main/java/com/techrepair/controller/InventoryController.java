package com.techrepair.controller;

import com.techrepair.model.InventoryItem;
import com.techrepair.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryRepository inventoryRepository;

    @GetMapping
    public ResponseEntity<List<InventoryItem>> getAll() {
        return ResponseEntity.ok(inventoryRepository.findAll());
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<InventoryItem>> getLowStock() {
        return ResponseEntity.ok(inventoryRepository.findAll().stream()
                .filter(item -> item.getQuantity() < item.getMinStockLevel())
                .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventoryItem> getById(@PathVariable Long id) {
        return inventoryRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/sku/{sku}")
    public ResponseEntity<InventoryItem> getBySku(@PathVariable String sku) {
        return inventoryRepository.findBySku(sku)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<InventoryItem> create(@RequestBody InventoryItem item) {
        InventoryItem saved = inventoryRepository.save(item);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InventoryItem> update(@PathVariable Long id, @RequestBody InventoryItem item) {
        return inventoryRepository.findById(id)
                .map(existing -> {
                    existing.setSku(item.getSku());
                    existing.setName(item.getName());
                    existing.setCategory(item.getCategory());
                    existing.setBrand(item.getBrand());
                    existing.setModel(item.getModel());
                    existing.setQuantity(item.getQuantity());
                    existing.setMinStockLevel(item.getMinStockLevel());
                    existing.setUnitPrice(item.getUnitPrice());
                    return ResponseEntity.ok(inventoryRepository.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/quantity")
    public ResponseEntity<InventoryItem> updateQuantity(@PathVariable Long id, @RequestBody Integer quantity) {
        return inventoryRepository.findById(id)
                .map(existing -> {
                    existing.setQuantity(quantity);
                    return ResponseEntity.ok(inventoryRepository.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (inventoryRepository.existsById(id)) {
            inventoryRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}