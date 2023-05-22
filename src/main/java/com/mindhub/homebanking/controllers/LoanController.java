package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.enums.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientLoanRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.LoanRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;

import com.mindhub.homebanking.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class LoanController {
    @Autowired
    private MessageService messageService;
    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    ClientLoanRepository clientLoanRepository;
    
    @Autowired
    private TransactionRepository transactionRepository;

    @GetMapping("/loans")
    public List<LoanDTO> getLoans(){
        return loanRepository.findAll().stream().map(LoanDTO::new).collect(Collectors.toList());
    }
    @Transactional
    @PostMapping ("/loans")
    ResponseEntity<Object> createLoan(
            @RequestBody
            LoanApplicationDTO loanApplicationDTO,
            Authentication authentication){

        if (loanApplicationDTO.getLoanId() == null || loanApplicationDTO.getAmount() == null ||loanApplicationDTO.getToAccountNumber().isEmpty()){
            return new ResponseEntity<>("Missing data", HttpStatus.BAD_REQUEST);
        }
        Client client = clientRepository.findByEmail(authentication.getName());
        Account clientAccount = accountRepository.findByNumber(loanApplicationDTO.getToAccountNumber());
        Loan loan = loanRepository.findById(loanApplicationDTO.getLoanId()).orElse(null);
        if (loan==null){
            return new ResponseEntity<>("The loan type does not exist.", HttpStatus.FORBIDDEN);
        }
        if (clientAccount == null){
            return new ResponseEntity<>("The destination account does not exist.", HttpStatus.FORBIDDEN);
        }
        if (!clientAccount.getClient().equals(client)){
            return new ResponseEntity<>("The account does not belong to the authenticated client", HttpStatus.FORBIDDEN);
        }
        if (loanApplicationDTO.getAmount() == 0){
            return new ResponseEntity<>("The amount cannot be zero.", HttpStatus.FORBIDDEN);
        }
        if (loanApplicationDTO.getPayments() == 0){
            return new ResponseEntity<>("The payments cannot be zero.", HttpStatus.FORBIDDEN);
        }
        if (!loan.getPayments().contains(loanApplicationDTO.getPayments())){
            return new ResponseEntity<>("The quotas are not valid", HttpStatus.FORBIDDEN);
        }
        if (loanApplicationDTO.getAmount()>loan.getMaxAmount()){
            return new ResponseEntity<>("The requested amount exceeds the maximum allowed for this loan.", HttpStatus.FORBIDDEN);
        }
        double finalAmount = loanApplicationDTO.getAmount() + (loanApplicationDTO.getAmount()* 0.2);
        ClientLoan clientLoan = new ClientLoan(client,loan,finalAmount,loanApplicationDTO.getPayments());
        clientLoanRepository.save(clientLoan);

        Transaction transaction = new Transaction(TransactionType.CREDIT,loanApplicationDTO.getAmount(),"loan approved", LocalDateTime.now(),clientAccount );
        transactionRepository.save(transaction);

        clientAccount.setBalance(clientAccount.getBalance() + loanApplicationDTO.getAmount());
        accountRepository.save(clientAccount);

        this.messageService.sendWhatsapp(
                "+56953618681",
                "A loan of" + loanApplicationDTO.getAmount()+ "has been approved to account number "+ loanApplicationDTO.getToAccountNumber()
                );

        
        return new ResponseEntity<>("Loan application successfully completed.", HttpStatus.CREATED);
    }
}
