package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.enums.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Transactional
@RestController
@RequestMapping("/api")
public class TransactionController {
    @Autowired
    private MessageService messageService;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AccountRepository accountRepository;
    @PostMapping("/transactions")
    public ResponseEntity<Object> createTransaction(
            @RequestParam Double amount,
            @RequestParam String description,
            @RequestParam String fromAccountNumber,
            @RequestParam String toAccountNumber,
            Authentication authentication) {
        Client client = clientRepository.findByEmail(authentication.getName());
        Account desAccount = accountRepository.findByNumber(toAccountNumber);
        Account oriAccount = accountRepository.findByNumber(fromAccountNumber);

        if (amount == null || description.isEmpty() || fromAccountNumber.isEmpty()
                || toAccountNumber.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        if (fromAccountNumber.equals(toAccountNumber)) {
            return new ResponseEntity<>("The destination account cannot be the same as the source account.",
                    HttpStatus.FORBIDDEN);
        }
        if (desAccount == null) {
            return new ResponseEntity<>("The destination account does not exist.", HttpStatus.FORBIDDEN);
        }
        if (oriAccount == null) {
            return new ResponseEntity<>("The source account does not exist.", HttpStatus.FORBIDDEN);
        }
        if (!oriAccount.getClient().equals(client)) {
            return new ResponseEntity<>("The account does not belong to the authenticated client", HttpStatus.FORBIDDEN);
        }
        if (oriAccount.getBalance()<amount){
            return new ResponseEntity<>("The balance is insufficient for the transaction", HttpStatus.FORBIDDEN);
        }

        Transaction ts1 = new Transaction(TransactionType.DEBIT,-amount,description, LocalDateTime.now(), oriAccount);
        Transaction ts2 = new Transaction(TransactionType.CREDIT,amount,description,LocalDateTime.now(), desAccount );

        transactionRepository.save(ts1);
        transactionRepository.save(ts2);

        oriAccount.setBalance(oriAccount.getBalance()-amount);
        desAccount.setBalance(desAccount.getBalance()+amount);

        accountRepository.save(oriAccount);
        accountRepository.save(desAccount);
        String amountMessage = "a total of "+ amount +" has been transferred";
        String destinationAccountMessage = " to the destination account number: " + toAccountNumber + " with the description: " +description;
        String Message = "transfer successfully completed " +amountMessage+" "+ destinationAccountMessage;


        this.messageService.sendWhatsapp(
                client.getPhone(),
                Message
        );

        /*
        this.messageService.sendSms(
                "+56953618681",
                Message
        );
         */

        return new ResponseEntity<>("The funds have been transferred!", HttpStatus.CREATED);
    }
}
