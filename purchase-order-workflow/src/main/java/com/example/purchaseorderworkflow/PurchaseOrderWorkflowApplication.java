package com.example.purchaseorderworkflow;

import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableProcessApplication
@SpringBootApplication
public class PurchaseOrderWorkflowApplication {

	public static void main(String[] args) {
		SpringApplication.run(PurchaseOrderWorkflowApplication.class, args);
	}

}
