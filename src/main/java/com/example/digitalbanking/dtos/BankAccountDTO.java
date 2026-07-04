package com.example.digitalbanking.dtos;

import com.example.digitalbanking.enums.AccountStatus;
import lombok.Data;

import java.util.Date;

@Data
public class BankAccountDTO {
    private String id;
    private String accountNumber;
    private double balance;
    private Date createdAt;
    private AccountStatus status;
    private String currency;
    private Long customerId;
    private String customerName;
    private String type;
}
