    package com.example.digitalbanking.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@DiscriminatorValue("CA")
@Data
public class CurrentAccount extends BankAccount{
    private double overDraft;
}
