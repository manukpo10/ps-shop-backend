package com.techrepair.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class InvoiceRequest {
    private Long clientId;
    private Long repairOrderId;
    private String paymentStatus;
    private BigDecimal subtotal;
    private BigDecimal taxAmount;
    private BigDecimal total;
}