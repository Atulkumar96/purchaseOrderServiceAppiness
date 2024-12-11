package com.example.purchaseorderworkflow.controller;

import com.example.purchaseorderworkflow.entity.PurchaseOrder;
import com.example.purchaseorderworkflow.exception.PurchaseOrderAlreadyExistsException;
import com.example.purchaseorderworkflow.exception.PurchaseOrderNotFoundException;
import com.example.purchaseorderworkflow.exception.UnauthorizedException;
import com.example.purchaseorderworkflow.service.PurchaseOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PurchaseOrderController {

    private static final Logger logger = LoggerFactory.getLogger(PurchaseOrderController.class);

    @Autowired
    private PurchaseOrderService purchaseOrderService;

    @GetMapping("/list")
    public List<PurchaseOrder> listPurchaseOrders(
        @RequestHeader("X-Username") String username,
        @RequestHeader("X-Role") String role) {

        //TODO : precondition
        logger.info("Received request to list purchase orders. Username: {}, Role: {}", username, role);


        return purchaseOrderService.getPurchaseOrdersByRole(role);
    }

    @PostMapping("/txn")
    public ResponseEntity<?> createPurchaseOrder(
        @RequestHeader("X-Username") String username,
        @RequestHeader("X-Role") String role,
        @RequestBody PurchaseOrder request) {


        logger.info("Received request to update purchase orders. Username: {}, Role: {}", username, role);

        if (role.equalsIgnoreCase("PUBLISHER")) {
            // PUBLISHER: Create the purchase order
            return createPurchaseOrder(username, request);
        } else {
            throw new UnauthorizedException("Unauthorized role");
        }
    }

    // Endpoint for updating a purchase order (PATCH)
    @PatchMapping("/txn")
    public ResponseEntity<?> updatePurchaseOrder(
        @RequestHeader("X-Username") String username,
        @RequestHeader("X-Role") String role,
        @RequestBody PurchaseOrder request) {

        logger.info("Received request to review/approve purchase orders. Username: {}, Role: {}", username, role);

        if (role.equalsIgnoreCase("PUBLISHER")) {
            // PUBLISHER: Update the purchase order
            return updatePurchaseOrder(username, request);
        } else if (role.equalsIgnoreCase("REVIEWER") || role.equalsIgnoreCase("APPROVER")) {
            // REVIEWER/APPROVER: Update purchase order status (Accept or Reject)
            return reviewOrApprovePurchaseOrder(username, request);
        } else {
            throw new UnauthorizedException("Unauthorized role");
        }
    }

    // Create a new Purchase Order (POST)
    private ResponseEntity<?> createPurchaseOrder(String username, PurchaseOrder request) {
        if (purchaseOrderService.isPurchaseOrderExists(request.getRecordId())) {
            throw new PurchaseOrderAlreadyExistsException("Purchase Order already exists");
        }

        // Set createdBy to the username
        request.setStatus(PurchaseOrder.OrderStatus.CREATED);
        request.setCreatedBy(username);
        PurchaseOrder createdOrder = purchaseOrderService.createPurchaseOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    // Update an existing Purchase Order (PATCH)
    private ResponseEntity<?> updatePurchaseOrder(String username, PurchaseOrder request) {
        PurchaseOrder existingOrder = purchaseOrderService.getPurchaseOrderById(request.getRecordId());

        if (existingOrder == null) {
            throw new PurchaseOrderNotFoundException("Purchase Order not found");
        }

        if (!existingOrder.getCreatedBy().equals(username)) {
            throw new UnauthorizedException("Unauthorized owner");
        }

        // Update the purchase order fields
        existingOrder.setTaskId(request.getTaskId());
        existingOrder.setPartsPrice(request.getPartsPrice());
        existingOrder.setLabourPrice(request.getLabourPrice());
        existingOrder.setAmount(request.getAmount());
        existingOrder.setStatus(request.getStatus());

        PurchaseOrder updatedOrder = purchaseOrderService.updatePurchaseOrder(existingOrder);
        return ResponseEntity.ok(updatedOrder);
    }

    // Handle REVIEWER/APPROVER action (accept/reject)
    private ResponseEntity<?> reviewOrApprovePurchaseOrder(String username, PurchaseOrder request) {
        PurchaseOrder existingOrder = purchaseOrderService.getPurchaseOrderById(request.getRecordId());

        if (existingOrder == null) {
            throw new PurchaseOrderNotFoundException("Purchase Order not found");
        }

        // Update the order's status based on the action
         if (PurchaseOrder.OrderStatus.REWORK.equals(request.getStatus())) {
            existingOrder.setStatus(PurchaseOrder.OrderStatus.REWORK);
        } else if (PurchaseOrder.OrderStatus.REVIEWED.equals(request.getStatus())) {
            existingOrder.setStatus(PurchaseOrder.OrderStatus.REVIEWED);
        } else if (PurchaseOrder.OrderStatus.APPROVED.equals(request.getStatus())) {
             existingOrder.setStatus(PurchaseOrder.OrderStatus.APPROVED);
         } else if (PurchaseOrder.OrderStatus.REJECTED.equals(request.getStatus())) {
             existingOrder.setStatus(PurchaseOrder.OrderStatus.REJECTED);
         } else {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body("Invalid action");
        }

        // Add review comments if any
        if (request.getReviewSummary() != null && !request.getReviewSummary().isEmpty()) {
            existingOrder.setReviewSummary(request.getReviewSummary());
            existingOrder.getReviewSummary().forEach(reviewSummary -> reviewSummary.setPurchaseOrder(existingOrder));
        }

        existingOrder.setUpdatedBy(username);

        PurchaseOrder updatedOrder = purchaseOrderService.updatePurchaseOrder(existingOrder);
        return ResponseEntity.ok(updatedOrder);
    }

}
