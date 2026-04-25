package com.techrepair.controller;

import com.techrepair.model.Invoice;
import com.techrepair.model.RepairOrder;
import com.techrepair.model.InventoryItem;
import com.techrepair.repository.InvoiceRepository;
import com.techrepair.repository.RepairOrderRepository;
import com.techrepair.repository.InventoryRepository;
import com.techrepair.model.enums.RepairStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final RepairOrderRepository repairOrderRepository;
    private final InvoiceRepository invoiceRepository;
    private final InventoryRepository inventoryRepository;

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        List<RepairOrder> allOrders = repairOrderRepository.findAll();
        List<Invoice> allInvoices = invoiceRepository.findAll();
        List<InventoryItem> allInventory = inventoryRepository.findAll();

        LocalDateTime now = LocalDateTime.now();
        YearMonth currentMonth = YearMonth.now();
        LocalDateTime startOfMonth = currentMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = currentMonth.atEndOfMonth().atTime(23, 59, 59);

        // Active repairs (PENDING + IN_PROGRESS)
        long activeRepairs = allOrders.stream()
                .filter(o -> o.getStatus() == RepairStatus.PENDING || o.getStatus() == RepairStatus.IN_PROGRESS)
                .count();

        // Completed this month
        long completedThisMonth = allOrders.stream()
                .filter(o -> o.getStatus() == RepairStatus.COMPLETED)
                .filter(o -> o.getCompletedAt() != null)
                .filter(o -> !o.getCompletedAt().isBefore(startOfMonth) && !o.getCompletedAt().isAfter(endOfMonth))
                .count();

        // Revenue this month
        BigDecimal revenueThisMonth = allInvoices.stream()
                .filter(i -> i.getPaymentStatus().equals("PAID"))
                .filter(i -> !i.getIssuedDate().isBefore(startOfMonth) && !i.getIssuedDate().isAfter(endOfMonth))
                .map(Invoice::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Low stock items
        long lowStockItems = allInventory.stream()
                .filter(item -> item.getQuantity() < item.getMinStockLevel())
                .count();

        // Recent repairs (last 5)
        List<RepairOrder> recentRepairs = allOrders.stream()
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .limit(5)
                .toList();

        // Repairs by status
        Map<String, Long> repairsByStatus = new HashMap<>();
        for (RepairStatus status : RepairStatus.values()) {
            long count = allOrders.stream()
                    .filter(o -> o.getStatus() == status)
                    .count();
            repairsByStatus.put(status.name(), count);
        }

        Map<String, Object> stats = new HashMap<>();
        stats.put("activeRepairs", activeRepairs);
        stats.put("completedThisMonth", completedThisMonth);
        stats.put("revenueThisMonth", revenueThisMonth.longValue());
        stats.put("lowStockItems", lowStockItems);
        stats.put("recentRepairs", recentRepairs);
        stats.put("repairsByStatus", repairsByStatus);

        return ResponseEntity.ok(stats);
    }

    @GetMapping("/monthly-stats")
    public ResponseEntity<Map<String, Object>> getMonthlyStats() {
        List<RepairOrder> allOrders = repairOrderRepository.findAll();
        List<Invoice> allInvoices = invoiceRepository.findAll();

        LocalDateTime now = LocalDateTime.now();
        int currentYear = now.getYear();

        List<Map<String, Object>> repairsByMonth = new java.util.ArrayList<>();
        List<Map<String, Object>> revenueByMonth = new java.util.ArrayList<>();

        String[] monthNames = {"Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic"};

        for (int i = 0; i < 12; i++) {
            YearMonth ym = YearMonth.of(currentYear, i + 1);
            LocalDateTime start = ym.atDay(1).atStartOfDay();
            LocalDateTime end = ym.atEndOfMonth().atTime(23, 59, 59);

            long completed = allOrders.stream()
                    .filter(o -> o.getStatus() == RepairStatus.COMPLETED)
                    .filter(o -> o.getCompletedAt() != null)
                    .filter(o -> !o.getCompletedAt().isBefore(start) && !o.getCompletedAt().isAfter(end))
                    .count();

            BigDecimal revenue = allInvoices.stream()
                    .filter(inv -> inv.getPaymentStatus().equals("PAID"))
                    .filter(inv -> !inv.getIssuedDate().isBefore(start) && !inv.getIssuedDate().isAfter(end))
                    .map(Invoice::getTotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            Map<String, Object> repairData = new HashMap<>();
            repairData.put("month", monthNames[i]);
            repairData.put("value", completed);
            repairsByMonth.add(repairData);

            Map<String, Object> revenueData = new HashMap<>();
            revenueData.put("month", monthNames[i]);
            revenueData.put("value", revenue.longValue());
            revenueByMonth.add(revenueData);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("repairsByMonth", repairsByMonth);
        result.put("revenueByMonth", revenueByMonth);

        return ResponseEntity.ok(result);
    }
}