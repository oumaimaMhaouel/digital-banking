package com.example.digitalbanking.services;

import com.example.digitalbanking.Exceptions.BalanceNotSufficentException;
import com.example.digitalbanking.Exceptions.BankAccountNotFoundException;
import com.example.digitalbanking.Exceptions.CustomerNotFoundException;
import com.example.digitalbanking.dtos.*;
import com.example.digitalbanking.entities.BankAccount;
import com.example.digitalbanking.entities.Customer;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface BankService {
    CustomerDTO saveCustomer(CustomerDTO customerDTO);
    List<CustomerDTO> searchCustomers(String keyword);
    CustomerDTO updateCustomer(CustomerDTO customerDTO);
    void deleteCustomer(Long customerId);
    CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException;
    CurrentAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException;
    SavingAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException;
    List<CustomerDTO> listCustomers();
    List<BankAccountDTO> bankAccountList();
    List<AccountOperationDTO> accountHistory(String accountId);
    BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException;
    List<BankAccountDTO>  getAccountByCustomer(Long customerId) throws CustomerNotFoundException;
    void debit(String accountId,double amount,String description) throws BalanceNotSufficentException, BankAccountNotFoundException;
    void credit(String accountId,double amount,String description) throws BankAccountNotFoundException;
    void transfer(String accountIdSource,String accountIdDestination,double amount) throws BankAccountNotFoundException, BalanceNotSufficentException;
    AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException;

}
