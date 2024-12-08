package com.example.purchaseorderworkflow.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "purchase_order_review_summary")
public class ReviewSummary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Auto-incremented ID for the review summary
    private Long id;  // Auto-generated ID for the review entry

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)  // Many reviews can belong to one PurchaseOrder
    @JoinColumn(name = "record_id")  // Foreign key column in the review summary table
    private PurchaseOrder purchaseOrder;

    @Column(name = "review_comment")
    private String reviewComment;  // Review comment text

}
