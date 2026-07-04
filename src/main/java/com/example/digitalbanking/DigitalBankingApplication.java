package com.example.digitalbanking;

import com.example.digitalbanking.entities.AccountOperation;
import com.example.digitalbanking.entities.CurrentAccount;
import com.example.digitalbanking.entities.Customer;
import com.example.digitalbanking.entities.SavingAccount;
import com.example.digitalbanking.enums.AccountStatus;
import com.example.digitalbanking.enums.OperationType;
import com.example.digitalbanking.repositories.AccountOperationRepository;
import com.example.digitalbanking.repositories.BankAccountRepository;
import com.example.digitalbanking.repositories.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Currency;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class DigitalBankingApplication {

    public static void main(String[] args) {
        SpringApplication.run(DigitalBankingApplication.class, args);
    }


    @Bean
    CommandLineRunner start(CustomerRepository customerRepository,
                              AccountOperationRepository accountOperationRepository,
                              BankAccountRepository bankAccountRepository) {
        return args -> {
            Stream.of("Hassan", "yassine", "AICHA").forEach(r ->{
                Customer customer=new Customer();
                customer.setName(r);
                customer.setEmail(r+"@gmail.com");
                customerRepository.save(customer);
            });
            customerRepository.findAll().forEach(
                    c->{
                     CurrentAccount currentAccount=new CurrentAccount();
                     currentAccount.setCustomer(c);
                     currentAccount.setBalance(Math.random()*9000);
                     currentAccount.setCreatedAt(new Date());
                     currentAccount.setId(UUID.randomUUID().toString());
                     currentAccount.setCurrency("MAD");
                     currentAccount.setStatus(AccountStatus.CREATED);
                     currentAccount.setOverDraft(9000);
                     bankAccountRepository.save(currentAccount);
                     SavingAccount savingAccount=new SavingAccount();
                     savingAccount.setCustomer(c);
                     savingAccount.setId(UUID.randomUUID().toString());
                     savingAccount.setBalance(Math.random()*9000);
                     savingAccount.setCreatedAt(new Date());
                     savingAccount.setCurrency("MAD");
                     savingAccount.setStatus(AccountStatus.CREATED);
                     savingAccount.setInterestRate(10);
                     bankAccountRepository.save(savingAccount);
                    }
            );
            bankAccountRepository.findAll().forEach(
                    b->{
                        for (int i=0;i<10;i++){
                            AccountOperation accountOperation=new AccountOperation();
                            accountOperation.setBankAccount(b);
                            accountOperation.setAmount(Math.random()*12000);
                            accountOperation.setOperationDate(new Date());
                            accountOperation.setOperationType(Math.random()>0.5?OperationType.DEBIT:OperationType.CREDIT);
                            accountOperationRepository.save(accountOperation);
                        }
                    }
            );
            System.out.println("Hello World!");
        };
    }
}
