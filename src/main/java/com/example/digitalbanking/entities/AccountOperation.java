package com.example.digitalbanking.entities;

import com.example.digitalbanking.enums.OperationType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountOperation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date operationDate;
    @Enumerated(EnumType.STRING)
    private OperationType operationType;
    private double amount;
    @ManyToOne
    @JsonIgnore
    private BankAccount bankAccount;
}
