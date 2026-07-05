package com.example.digitalbanking.services.impl;

import com.example.digitalbanking.Exceptions.BalanceNotSufficentException;
import com.example.digitalbanking.Exceptions.BankAccountNotFoundException;
import com.example.digitalbanking.Exceptions.CustomerNotFoundException;
import com.example.digitalbanking.dtos.*;
import com.example.digitalbanking.entities.AccountOperation;
import com.example.digitalbanking.entities.*;
import com.example.digitalbanking.enums.AccountStatus;
import com.example.digitalbanking.enums.OperationType;
import com.example.digitalbanking.mappers.bankAccountMapper;
import com.example.digitalbanking.repositories.AccountOperationRepository;
import com.example.digitalbanking.repositories.BankAccountRepository;
import com.example.digitalbanking.repositories.CustomerRepository;
import com.example.digitalbanking.services.BankService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class BankServiceImpl implements BankService {
    private final CustomerRepository customerRepository;
    private final BankAccountRepository bankAccountRepository;
    private final AccountOperationRepository accountOperationRepository;
    private final bankAccountMapper bankAccountMapper;

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        log.info("Saving new Customer");
        Customer customer = bankAccountMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);
        return bankAccountMapper.fromCustomer(savedCustomer);
    }

    @Override
    public List<CustomerDTO> searchCustomers(String keyword) {
        List<Customer> customers=customerRepository.searchCustomer(keyword);
        return customers.stream().map(cust -> bankAccountMapper.fromCustomer(cust)).collect(Collectors.toList());
    }
    @Override
    public CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException  {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer Not found"));
        return bankAccountMapper.fromCustomer(customer);
    }
    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        log.info("Saving new Customer");
        Customer customer=bankAccountMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);
        return bankAccountMapper.fromCustomer(savedCustomer);
    }
    @Override
    public void deleteCustomer(Long customerId){
        customerRepository.deleteById(customerId);
    }
    @Override
    public CurrentAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException {

        CurrentAccount bankAccount = new CurrentAccount();
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null) {
            throw new CustomerNotFoundException("Customer not found");
        }
        bankAccount.setId(UUID.randomUUID().toString());
        bankAccount.setBalance(initialBalance);
        bankAccount.setStatus(AccountStatus.CREATED);
        bankAccount.setCreatedAt(new Date());
        bankAccount.setOverDraft(overDraft);
        bankAccount.setCustomer(customer);
        CurrentAccount currentAccountSaved = bankAccountRepository.save(bankAccount);
        return bankAccountMapper.fromCurrentAccount(currentAccountSaved);
    }


    @Override
    public SavingAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException {
        SavingAccount bankAccount = new SavingAccount();
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null) {
            throw new CustomerNotFoundException("Customer not found");
        }
        bankAccount.setId(UUID.randomUUID().toString());
        bankAccount.setBalance(initialBalance);
        bankAccount.setStatus(AccountStatus.CREATED);
        bankAccount.setCreatedAt(new Date());
        bankAccount.setInterestRate(interestRate);
        bankAccount.setCustomer(customer);
        SavingAccount savingAccount = bankAccountRepository.save(bankAccount);
        return bankAccountMapper.fromSavingAccount(savingAccount);
    }


    @Override
    public List<CustomerDTO> listCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream().map(bankAccountMapper::fromCustomer).toList();
    }

    private BankAccount findBankAccount(String accountId) throws BankAccountNotFoundException {
        return bankAccountRepository.findById(accountId).orElseThrow(() -> new BankAccountNotFoundException("BankAccount not found"));
    }

    @Override
    public BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException {
        BankAccount bankAccount = findBankAccount(accountId);
        return bankAccountMapper.fromBankAccount(bankAccount);
    }

    @Override
    public List<BankAccount> getAccountByCustomer(Long customerId) throws CustomerNotFoundException {
        List<BankAccount> bankAccounts = bankAccountRepository.findByCustomerId(customerId);
        if(bankAccounts.isEmpty()) {
            throw new CustomerNotFoundException("Customer not found");
        }
        return bankAccounts;
    }

    @Override
    public List<BankAccountDTO> bankAccountList() {
        List<BankAccount> bankAccounts = bankAccountRepository.findAll();
        List<BankAccountDTO> bankAccountDTOS = bankAccounts.stream().map(bankAccount -> {
            if (bankAccount instanceof SavingAccount) {
                SavingAccount savingAccount = (SavingAccount) bankAccount;
                return bankAccountMapper.fromSavingBankAccount(savingAccount);
            } else {
                CurrentAccount currentAccount = (CurrentAccount) bankAccount;
                return bankAccountMapper.fromCurrentBankAccount(currentAccount);
            }
        }).collect(Collectors.toList());
        return bankAccountDTOS;
    }

    @Override
    public List<AccountOperationDTO> accountHistory(String accountId) {
        List<AccountOperation> accountOperations = accountOperationRepository.findByBankAccountId(accountId);
        return accountOperations.stream().map(op -> bankAccountMapper.fromAccountOperation(op)).collect(Collectors.toList());
    }


    @Override
    public void debit(String accountId, double amount, String description) throws BalanceNotSufficentException, BankAccountNotFoundException {
        BankAccount bankAccount = findBankAccount(accountId);
        log.info("Debiting {} from account {}", amount, bankAccount.getId());
        if (bankAccount.getBalance() < amount) {
            throw new BalanceNotSufficentException("Insufficient balance");
        }
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setAmount(amount);
        accountOperation.setOperationDate(new Date());
        accountOperation.setOperationType(OperationType.DEBIT);
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);

        bankAccount.setBalance(bankAccount.getBalance() - amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountNotFoundException {
        BankAccount bankAccount = findBankAccount(accountId);
        log.info("Crediting {} to account {}", amount, bankAccount.getId());
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setAmount(amount);
        accountOperation.setOperationDate(new Date());
        accountOperation.setOperationType(OperationType.CREDIT);
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance() + amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficentException {
        debit(accountIdSource, amount, "Transfer to " + accountIdDestination);
        credit(accountIdDestination, amount, "Transfer from " + accountIdSource);
    }

    @Override
    public AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException {
        BankAccount bankAccount = findBankAccount(accountId);
        Page<AccountOperation> accountOperations = accountOperationRepository.findByBankAccountId(accountId, PageRequest.of(page, size));
        AccountHistoryDTO accountHistoryDTO = new AccountHistoryDTO();
        List<AccountOperationDTO> accountOperationDTOS = accountOperations.getContent().stream()
                .map(op -> bankAccountMapper.fromAccountOperation(op))
                .collect(Collectors.toList());
        accountHistoryDTO.setAccountOperationDTOS(accountOperationDTOS);
        accountHistoryDTO.setAccountId(bankAccount.getId());
        accountHistoryDTO.setBalance(bankAccount.getBalance());
        accountHistoryDTO.setCurrentPage(page);
        accountHistoryDTO.setPageSize(size);
        accountHistoryDTO.setTotalPages(accountOperations.getTotalPages());
        return accountHistoryDTO;
    }
}
