package com.techrepair.controller;

import com.techrepair.model.RepairOrder;
import com.techrepair.model.enums.RepairStatus;
import com.techrepair.repository.RepairOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/repair-orders")
@RequiredArgsConstructor
public class RepairOrderController {

    private final RepairOrderRepository repairOrderRepository;

    @GetMapping
    public ResponseEntity<List<RepairOrder>> getAll(@RequestParam(required = false) RepairStatus status) {
        if (status != null) {
            return ResponseEntity.ok(repairOrderRepository.findByStatus(status));
        }
        return ResponseEntity.ok(repairOrderRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RepairOrder> getById(@PathVariable Long id) {
        return repairOrderRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<RepairOrder> create(@RequestBody RepairOrder order) {
        RepairOrder saved = repairOrderRepository.save(order);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RepairOrder> update(@PathVariable Long id, @RequestBody RepairOrder order) {
        return repairOrderRepository.findById(id)
                .map(existing -> {
                    existing.setDeviceType(order.getDeviceType());
                    existing.setDeviceBrand(order.getDeviceBrand());
                    existing.setDeviceModel(order.getDeviceModel());
                    existing.setSerialNumber(order.getSerialNumber());
                    existing.setStatus(order.getStatus());
                    existing.setProblemDescription(order.getProblemDescription());
                    existing.setDiagnosis(order.getDiagnosis());
                    existing.setSolution(order.getSolution());
                    existing.setEstimatedCost(order.getEstimatedCost());
                    existing.setFinalCost(order.getFinalCost());
                    existing.setCompletedAt(order.getCompletedAt());
                    return ResponseEntity.ok(repairOrderRepository.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<RepairOrder> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> request) {
        System.out.println("PATCH /repair-orders/" + id + "/status - body: " + request);
        String statusStr = request.get("status");
        RepairStatus status = RepairStatus.valueOf(statusStr);
        return repairOrderRepository.findById(id)
                .map(existing -> {
                    existing.setStatus(status);
                    if (status == RepairStatus.COMPLETED) {
                        existing.setCompletedAt(java.time.LocalDateTime.now());
                    }
                    return ResponseEntity.ok(repairOrderRepository.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (repairOrderRepository.existsById(id)) {
            repairOrderRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}