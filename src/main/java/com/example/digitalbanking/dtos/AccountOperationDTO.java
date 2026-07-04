package com.example.digitalbanking.dtos;

import com.example.digitalbanking.enums.OperationType;
import lombok.Data;

import java.util.Date;

@Data
public class AccountOperationDTO {
    private Long id;
    private Date operationDate;
    private OperationType operationType;
    private double amount;
    private String bankAccountId;
}
