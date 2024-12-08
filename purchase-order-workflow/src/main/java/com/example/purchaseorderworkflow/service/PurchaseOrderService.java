package com.example.purchaseorderworkflow.service;

import com.example.purchaseorderworkflow.entity.PurchaseOrder;
import com.example.purchaseorderworkflow.exception.UnauthorizedException;
import com.example.purchaseorderworkflow.repository.PurchaseOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class PurchaseOrderService {
    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    public List<PurchaseOrder> getPurchaseOrdersByRole(String role) {
        // Determine the list of statuses based on the user's role
        switch (role.toUpperCase()) {
            case "PUBLISHER":
                return purchaseOrderRepository.findByStatusIn(Arrays.asList(PurchaseOrder.OrderStatus.CREATED, PurchaseOrder.OrderStatus.REWORK,PurchaseOrder.OrderStatus.REJECTED));
            case "REVIEWER":
                return purchaseOrderRepository.findByStatusIn(Arrays.asList(PurchaseOrder.OrderStatus.REVIEWED,PurchaseOrder.OrderStatus.REWORKED));
            case "APPROVER":
                return purchaseOrderRepository.findByStatusIn(Arrays.asList(PurchaseOrder.OrderStatus.APPROVED,PurchaseOrder.OrderStatus.REVIEWED));
            default:
                throw new UnauthorizedException("Unauthorized role " + role);
        }
    }

    public boolean isPurchaseOrderExists(String recordId) {
        return purchaseOrderRepository.existsById(recordId);
    }

    public PurchaseOrder createPurchaseOrder(PurchaseOrder purchaseOrder) {
        return purchaseOrderRepository.save(purchaseOrder);
    }

    public PurchaseOrder updatePurchaseOrder(PurchaseOrder purchaseOrder) {
        return purchaseOrderRepository.save(purchaseOrder);
    }

    public PurchaseOrder getPurchaseOrderById(String recordId) {
        Optional<PurchaseOrder> purchaseOrder = purchaseOrderRepository.findById(recordId);
        return purchaseOrder.orElse(null);
    }
}
