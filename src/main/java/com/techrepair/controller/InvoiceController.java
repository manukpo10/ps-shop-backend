package com.techrepair.controller;

import com.techrepair.model.Client;
import com.techrepair.model.Invoice;
import com.techrepair.model.RepairOrder;
import com.techrepair.repository.ClientRepository;
import com.techrepair.repository.InvoiceRepository;
import com.techrepair.repository.RepairOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceRepository invoiceRepository;
    private final ClientRepository clientRepository;
    private final RepairOrderRepository repairOrderRepository;

    @GetMapping
    public ResponseEntity<List<Invoice>> getAll() {
        return ResponseEntity.ok(invoiceRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Invoice> getById(@PathVariable Long id) {
        return invoiceRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Invoice> create(@RequestBody Invoice invoice) {
        // Buscar el cliente por ID
        Client client = null;
        if (invoice.getClient() != null && invoice.getClient().getId() != null) {
            client = clientRepository.findById(invoice.getClient().getId()).orElse(null);
        }
        
        // Buscar la orden de reparación por ID si existe
        RepairOrder repairOrder = null;
        if (invoice.getRepairOrder() != null && invoice.getRepairOrder().getId() != null) {
            repairOrder = repairOrderRepository.findById(invoice.getRepairOrder().getId()).orElse(null);
        }
        
        // Generar número de factura
        String invoiceNumber = "INV-" + LocalDateTime.now().getYear() + "-" + String.format("%04d", invoiceRepository.count() + 1);
        
        Invoice newInvoice = Invoice.builder()
                .invoiceNumber(invoiceNumber)
                .client(client)
                .repairOrder(repairOrder)
                .paymentStatus(invoice.getPaymentStatus() != null ? invoice.getPaymentStatus() : "PENDING")
                .subtotal(invoice.getSubtotal() != null ? invoice.getSubtotal() : BigDecimal.ZERO)
                .taxAmount(invoice.getTaxAmount() != null ? invoice.getTaxAmount() : BigDecimal.ZERO)
                .total(invoice.getTotal() != null ? invoice.getTotal() : BigDecimal.ZERO)
                .issuedDate(LocalDateTime.now())
                .build();
        
        Invoice saved = invoiceRepository.save(newInvoice);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Invoice> update(@PathVariable Long id, @RequestBody Invoice invoice) {
        return invoiceRepository.findById(id)
                .map(existing -> {
                    existing.setPaymentStatus(invoice.getPaymentStatus());
                    existing.setSubtotal(invoice.getSubtotal());
                    existing.setTaxAmount(invoice.getTaxAmount());
                    existing.setTotal(invoice.getTotal());
                    return ResponseEntity.ok(invoiceRepository.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (invoiceRepository.existsById(id)) {
            invoiceRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}/payment-status")
    public ResponseEntity<Invoice> updatePaymentStatus(@PathVariable Long id, @RequestBody String paymentStatus) {
        return invoiceRepository.findById(id)
                .map(existing -> {
                    existing.setPaymentStatus(paymentStatus);
                    return ResponseEntity.ok(invoiceRepository.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}