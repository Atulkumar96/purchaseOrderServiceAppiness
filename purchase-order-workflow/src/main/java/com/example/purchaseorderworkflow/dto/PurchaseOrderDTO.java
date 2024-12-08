package com.example.purchaseorderworkflow.dto;

import com.example.purchaseorderworkflow.entity.PurchaseOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/*
* This class is not being used now...but we can refactor and use DTO object to communicate with the external world.
* Can be taken as enhancement.
* */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderDTO {

    private String recordId;    // recordId as String
    private String taskId;      // taskId as String
    private double partsPrice;  // partsPrice as double
    private double labourPrice; // labourPrice as double
    private double amount;      // amount as double
    private PurchaseOrder.OrderStatus status; // status as Enum
    private List<String> reviewSummary; // collection of review comments
    private String createdBy;  // createdBy as String
    private String updatedBy;// updatedBy as String

    public static PurchaseOrder of(PurchaseOrderDTO purchaseOrderDTO) {
        PurchaseOrder po = new PurchaseOrder();
        po.setRecordId(purchaseOrderDTO.getRecordId());
        return po;
    }


}
