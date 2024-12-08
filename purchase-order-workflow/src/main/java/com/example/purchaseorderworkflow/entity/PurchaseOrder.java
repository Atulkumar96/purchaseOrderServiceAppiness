package com.example.purchaseorderworkflow.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrder {
    @Id
    private String recordId;    // recordId as String
    private String taskId;      // taskId as String
    private double partsPrice;  // partsPrice as double
    private double labourPrice; // labourPrice as double
    private double amount;      // amount as double

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // status as Enum

    @JsonManagedReference
    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ReviewSummary> reviewSummary; // collection of review comments

    private String createdBy;  // createdBy as String
    private String updatedBy;  // updatedBy as String

    // Enum for status
    public enum OrderStatus {
        CREATED,
        REWORK,
        REWORKED,
        APPROVED,
        REVIEWED,
        REJECTED,
    }
}
