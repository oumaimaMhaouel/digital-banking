package com.example.digitalbanking.mappers;

import com.example.digitalbanking.dtos.AccountOperationDTO;
import com.example.digitalbanking.dtos.BankAccountDTO;
import com.example.digitalbanking.dtos.CurrentAccountDTO;
import com.example.digitalbanking.dtos.CustomerDTO;
import com.example.digitalbanking.dtos.SavingAccountDTO;
import com.example.digitalbanking.entities.AccountOperation;
import com.example.digitalbanking.entities.BankAccount;
import com.example.digitalbanking.entities.CurrentAccount;
import com.example.digitalbanking.entities.Customer;
import com.example.digitalbanking.entities.SavingAccount;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class bankAccountMapper {

    public CustomerDTO fromCustomer(Customer customer){
        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(customer, customerDTO);
        return customerDTO;
    }

    public Customer fromCustomerDTO(CustomerDTO customerDTO){
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO, customer);
        return customer;
    }

    public CurrentAccountDTO fromCurrentAccount(CurrentAccount currentAccount){
        CurrentAccountDTO dto = new CurrentAccountDTO();
        BeanUtils.copyProperties(currentAccount, dto);
        dto.setCustomerId(currentAccount.getCustomer().getId());
        dto.setCustomerName(currentAccount.getCustomer().getName());
        return dto;
    }

    public SavingAccountDTO fromSavingAccount(SavingAccount savingAccount){
        SavingAccountDTO dto = new SavingAccountDTO();
        BeanUtils.copyProperties(savingAccount, dto);
        dto.setCustomerId(savingAccount.getCustomer().getId());
        dto.setCustomerName(savingAccount.getCustomer().getName());
        return dto;
    }

    public BankAccountDTO fromBankAccount(BankAccount bankAccount){
        BankAccountDTO dto = new BankAccountDTO();
        BeanUtils.copyProperties(bankAccount, dto);
        dto.setCustomerId(bankAccount.getCustomer().getId());
        dto.setCustomerName(bankAccount.getCustomer().getName());
        if(bankAccount instanceof CurrentAccount){
            dto.setType("CA");
        } else if(bankAccount instanceof SavingAccount){
            dto.setType("SA");
        }
        return dto;
    }

    public BankAccountDTO fromSavingBankAccount(SavingAccount savingAccount){
        BankAccountDTO dto = new BankAccountDTO();
        BeanUtils.copyProperties(savingAccount, dto);
        dto.setCustomerId(savingAccount.getCustomer().getId());
        dto.setCustomerName(savingAccount.getCustomer().getName());
        dto.setType("SA");
        return dto;
    }

    public BankAccountDTO fromCurrentBankAccount(CurrentAccount currentAccount){
        BankAccountDTO dto = new BankAccountDTO();
        BeanUtils.copyProperties(currentAccount, dto);
        dto.setCustomerId(currentAccount.getCustomer().getId());
        dto.setCustomerName(currentAccount.getCustomer().getName());
        dto.setType("CA");
        return dto;
    }

    public AccountOperationDTO fromAccountOperation(AccountOperation accountOperation){
        AccountOperationDTO dto = new AccountOperationDTO();
        BeanUtils.copyProperties(accountOperation, dto);
        dto.setBankAccountId(accountOperation.getBankAccount().getId());
        return dto;
    }

}
