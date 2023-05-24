package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.CoordinateCardDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.CoordinateCard;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.enums.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.CoordinateCardRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@Transactional
@RestController
@RequestMapping("/api")
public class TransactionController {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private CoordinateCardRepository coordinateCardRepository;

    @GetMapping("/transactions/coordinate-card")
    public ResponseEntity<List<String>> getCoordinateCard(Authentication authentication) {
        Client client = clientRepository.findByEmail(authentication.getName());
        if (client != null) {
            CoordinateCard coordinateCard = coordinateCardRepository.findByClient(client);
            if (coordinateCard != null) {
                List<String> coordinateKeys = new ArrayList<>(coordinateCard.getCoordinates().keySet());
                List<String> randomCoordinateKeys = getRandomElements(coordinateKeys, 3);
                return new ResponseEntity<>(randomCoordinateKeys, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    private List<String> getRandomElements(List<String> list, int count) {
        List<String> randomElements = new ArrayList<>();
        Random random = new Random();
        while (randomElements.size() < count && !list.isEmpty()) {
            int randomIndex = random.nextInt(list.size());
            randomElements.add(list.get(randomIndex));
            list.remove(randomIndex);
        }
        return randomElements;
    }

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
        return new ResponseEntity<>("The funds have been transferred!", HttpStatus.CREATED);
    }
}
