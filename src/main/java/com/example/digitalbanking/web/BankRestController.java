package com.example.digitalbanking.web;

import com.example.digitalbanking.Exceptions.BalanceNotSufficentException;
import com.example.digitalbanking.Exceptions.BankAccountNotFoundException;
import com.example.digitalbanking.Exceptions.CustomerNotFoundException;
import com.example.digitalbanking.entities.BankAccount;
import com.example.digitalbanking.entities.CurrentAccount;
import com.example.digitalbanking.entities.Customer;
import com.example.digitalbanking.entities.SavingAccount;
import com.example.digitalbanking.services.BankService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class BankRestController {

    private final BankService bankService;

    @PostMapping("/customers")
    public Customer saveCustomer(@RequestBody Customer customer) {
        return bankService.saveCustomer(customer);
    }

    @GetMapping("/customers")
    public List<Customer> listCustomers() {
        return bankService.listCustomers();
    }

    @PostMapping("/accounts/current")
    public CurrentAccount saveCurrentBankAccount(@RequestBody CurrentAccountRequest request)
            throws CustomerNotFoundException {
        return bankService.saveCurrentBankAccount(
                request.getInitialBalance(),
                request.getOverDraft(),
                request.getCustomerId()
        );
    }

    @PostMapping("/accounts/saving")
    public SavingAccount saveSavingBankAccount(@RequestBody SavingAccountRequest request)
            throws CustomerNotFoundException {
        return bankService.saveSavingBankAccount(
                request.getInitialBalance(),
                request.getInterestRate(),
                request.getCustomerId()
        );
    }

    @GetMapping("/accounts/{accountId}")
    public BankAccount getBankAccount(@PathVariable String accountId)
            throws BankAccountNotFoundException {
        return bankService.getBankAccount(accountId);
    }

    @PostMapping("/accounts/debit")
    public void debit(@RequestBody OperationRequest request)
            throws BalanceNotSufficentException, BankAccountNotFoundException {
        bankService.debit(
                request.getAccountId(),
                request.getAmount(),
                request.getDescription()
        );
    }

    @PostMapping("/accounts/credit")
    public void credit(@RequestBody OperationRequest request)
            throws BankAccountNotFoundException {
        bankService.credit(
                request.getAccountId(),
                request.getAmount(),
                request.getDescription()
        );
    }

    @Data
    public static class CurrentAccountRequest {
        private double initialBalance;
        private double overDraft;
        private Long customerId;
    }

    @Data
    public static class SavingAccountRequest {
        private double initialBalance;
        private double interestRate;
        private Long customerId;
    }

    @Data
    public static class OperationRequest {
        private String accountId;
        private double amount;
        private String description;
    }

}
