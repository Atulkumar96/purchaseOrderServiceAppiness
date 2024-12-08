package com.example.purchaseorderworkflow.repository;

import com.example.purchaseorderworkflow.entity.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder,String> {
    List<PurchaseOrder> findByStatusIn(List<PurchaseOrder.OrderStatus> statuses);
}
