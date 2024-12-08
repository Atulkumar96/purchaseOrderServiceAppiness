package com.example.purchaseorderworkflow;

import com.example.purchaseorderworkflow.entity.PurchaseOrder;
import com.example.purchaseorderworkflow.exception.UnauthorizedException;
import com.example.purchaseorderworkflow.repository.PurchaseOrderRepository;
import com.example.purchaseorderworkflow.service.PurchaseOrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PurchaseOrderServiceTest {

    @Mock
    private PurchaseOrderRepository purchaseOrderRepository;
    @InjectMocks
    private PurchaseOrderService purchaseOrderService;
    private PurchaseOrder order1;
    private PurchaseOrder order2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Sample PurchaseOrder objects for testing
        order1 = new PurchaseOrder("1", "task1", 100.0, 50.0, 150.0, PurchaseOrder.OrderStatus.CREATED, null, "user1", "user1");
        order2 = new PurchaseOrder("2", "task2", 200.0, 100.0, 300.0, PurchaseOrder.OrderStatus.REVIEWED, null, "user2", "user2");
    }

    @Test
    void testGetPurchaseOrdersByRole_Publisher() {
        // Setup mock behavior
        when(purchaseOrderRepository.findByStatusIn(anyList())).thenReturn(Arrays.asList(order1, order2));

        // Test method
        List<PurchaseOrder> orders = purchaseOrderService.getPurchaseOrdersByRole("PUBLISHER");

        // Assert the results
        assertNotNull(orders);
        assertEquals(2, orders.size());
        verify(purchaseOrderRepository).findByStatusIn(anyList());
    }

    @Test
    void testGetPurchaseOrdersByRole_Reviewer() {
        // Setup mock behavior
        when(purchaseOrderRepository.findByStatusIn(anyList())).thenReturn(Arrays.asList(order2));

        // Test method
        List<PurchaseOrder> orders = purchaseOrderService.getPurchaseOrdersByRole("REVIEWER");

        // Assert the results
        assertNotNull(orders);
        assertEquals(1, orders.size());
        assertEquals(PurchaseOrder.OrderStatus.REVIEWED, orders.get(0).getStatus());
    }


    @Test
    void testGetPurchaseOrdersByRole_Approver() {
        // Setup mock behavior
        when(purchaseOrderRepository.findByStatusIn(anyList())).thenReturn(Arrays.asList(order2));

        // Test method
        List<PurchaseOrder> orders = purchaseOrderService.getPurchaseOrdersByRole("APPROVER");

        // Assert the results
        assertNotNull(orders);
        assertEquals(1, orders.size());
        assertEquals(PurchaseOrder.OrderStatus.REVIEWED, orders.get(0).getStatus());
    }

    @Test
    void testGetPurchaseOrdersByRole_Unauthorized() {
        // Test that UnauthorizedException is thrown for an invalid role
        UnauthorizedException exception = assertThrows(UnauthorizedException.class, () ->
            purchaseOrderService.getPurchaseOrdersByRole("INVALID_ROLE"));

        assertEquals("Unauthorized role INVALID_ROLE", exception.getMessage());
    }

    @Test
    void testIsPurchaseOrderExists() {
        // Setup mock behavior
        when(purchaseOrderRepository.existsById("1")).thenReturn(true);
        when(purchaseOrderRepository.existsById("2")).thenReturn(false);

        // Test method
        assertTrue(purchaseOrderService.isPurchaseOrderExists("1"));
        assertFalse(purchaseOrderService.isPurchaseOrderExists("2"));
    }

    @Test
    void testCreatePurchaseOrder() {
        // Setup mock behavior
        when(purchaseOrderRepository.save(any(PurchaseOrder.class))).thenReturn(order1);

        // Test method
        PurchaseOrder createdOrder = purchaseOrderService.createPurchaseOrder(order1);

        // Assert the results
        assertNotNull(createdOrder);
        assertEquals("1", createdOrder.getRecordId());
        verify(purchaseOrderRepository).save(any(PurchaseOrder.class));
    }

    @Test
    void testUpdatePurchaseOrder() {
        // Setup mock behavior
        when(purchaseOrderRepository.save(any(PurchaseOrder.class))).thenReturn(order1);

        // Test method
        PurchaseOrder updatedOrder = purchaseOrderService.updatePurchaseOrder(order1);

        // Assert the results
        assertNotNull(updatedOrder);
        assertEquals("1", updatedOrder.getRecordId());
        verify(purchaseOrderRepository).save(any(PurchaseOrder.class));
    }

    @Test
    void testGetPurchaseOrderById() {
        // Setup mock behavior
        when(purchaseOrderRepository.findById("1")).thenReturn(Optional.of(order1));
        when(purchaseOrderRepository.findById("2")).thenReturn(Optional.empty());

        // Test method
        PurchaseOrder foundOrder = purchaseOrderService.getPurchaseOrderById("1");
        PurchaseOrder notFoundOrder = purchaseOrderService.getPurchaseOrderById("2");

        // Assert the results
        assertNotNull(foundOrder);
        assertNull(notFoundOrder);
        assertEquals("1", foundOrder.getRecordId());
    }


}
