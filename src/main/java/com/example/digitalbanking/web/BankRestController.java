package com.example.digitalbanking.web;

import com.example.digitalbanking.Exceptions.BalanceNotSufficentException;
import com.example.digitalbanking.Exceptions.BankAccountNotFoundException;
import com.example.digitalbanking.Exceptions.CustomerNotFoundException;
import com.example.digitalbanking.dtos.*;
import com.example.digitalbanking.entities.BankAccount;
import com.example.digitalbanking.services.BankService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@AllArgsConstructor
@CrossOrigin("*")
public class BankRestController {

    private final BankService bankService;

    @GetMapping("/{accountId}")
    public BankAccountDTO getBankAccount(@PathVariable String accountId) throws BankAccountNotFoundException {
        return bankService.getBankAccount(accountId);
    }
    @GetMapping("/customer/{id}")
    public List<BankAccount> getAccountByCustomer(@PathVariable(name = "id") Long customerId) throws CustomerNotFoundException {
        return bankService.getAccountByCustomer(customerId);
    }

    @GetMapping("")
    public List<BankAccountDTO> listAccounts(){
        return bankService.bankAccountList();
    }
    @GetMapping("/{accountId}/operations")
    public List<AccountOperationDTO> getHistory(@PathVariable String accountId){
        return bankService.accountHistory(accountId);
    }

    @GetMapping("/{accountId}/pageOperations")
    public AccountHistoryDTO getAccountHistory(
            @PathVariable String accountId,
            @RequestParam(name="page",defaultValue = "0") int page,
            @RequestParam(name="size",defaultValue = "5")int size) throws BankAccountNotFoundException {
        return bankService.getAccountHistory(accountId,page,size);
    }
    @PostMapping("/debit")
    public DebitDTO debit(@RequestBody DebitDTO debitDTO) throws BankAccountNotFoundException, BalanceNotSufficentException {
        this.bankService.debit(debitDTO.getAccountId(),debitDTO.getAmount(),debitDTO.getDescription());
        return debitDTO;
    }
    @PostMapping("/credit")
    public CreditDTO credit(@RequestBody CreditDTO creditDTO) throws BankAccountNotFoundException {
        this.bankService.credit(creditDTO.getAccountId(),creditDTO.getAmount(),creditDTO.getDescription());
        return creditDTO;
    }
    @PostMapping("/transfer")
    public void transfer(@RequestBody TransferRequestDTO transferRequestDTO) throws BankAccountNotFoundException, BalanceNotSufficentException {
        this.bankService.transfer(
                transferRequestDTO.getAccountSource(),
                transferRequestDTO.getAccountDestination(),
                transferRequestDTO.getAmount());
    }

}
