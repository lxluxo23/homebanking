package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.RandomKeysDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.CoordinateCard;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.enums.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.CoordinateCardRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.services.MessageService;
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
    private MessageService messageService;
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
            @RequestBody RandomKeysDTO randomKeysDTO,
            Authentication authentication) {
        Client client = clientRepository.findByEmail(authentication.getName());
        Account desAccount = accountRepository.findByNumber(randomKeysDTO.getToAccountNumber());
        Account oriAccount = accountRepository.findByNumber(randomKeysDTO.getFromAccountNumber());

        if (randomKeysDTO.getAmount() == 0 || randomKeysDTO.getDescription().isEmpty() || randomKeysDTO.getFromAccountNumber().isEmpty()
                || randomKeysDTO.getToAccountNumber().isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        if (randomKeysDTO.getFromAccountNumber().equals(randomKeysDTO.getToAccountNumber())) {
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
        if (oriAccount.getBalance()< randomKeysDTO.getAmount()){
            return new ResponseEntity<>("The balance is insufficient for the transaction", HttpStatus.FORBIDDEN);
        }
        CoordinateCard coordinateCard = coordinateCardRepository.findByClient(client);
        List<String> randomKeys = randomKeysDTO.getRandomKeys();
        List<Integer> randomKeysValues = randomKeysDTO.getRandomKeysValues();

        if (coordinateCard == null) {
            return new ResponseEntity<>("Coordinate card not found", HttpStatus.NOT_FOUND);
        }

        if (randomKeys.size() != randomKeysValues.size()) {
            return new ResponseEntity<>("Invalid coordinates", HttpStatus.FORBIDDEN);
        }

        Map<String, Integer> coordinates = coordinateCard.getCoordinates();

        for (int i = 0; i < randomKeys.size(); i++) {
            String key = randomKeys.get(i);
            int value = randomKeysValues.get(i);

            if (!coordinates.containsKey(key) || coordinates.get(key) != value) {
                return new ResponseEntity<>("Invalid coordinates", HttpStatus.FORBIDDEN);
            }
        }

        Transaction ts1 = new Transaction(TransactionType.DEBIT,-randomKeysDTO.getAmount(), randomKeysDTO.getDescription(), LocalDateTime.now(), oriAccount);
        Transaction ts2 = new Transaction(TransactionType.CREDIT, randomKeysDTO.getAmount(), randomKeysDTO.getDescription(), LocalDateTime.now(), desAccount );

        transactionRepository.save(ts1);
        transactionRepository.save(ts2);

        oriAccount.setBalance(oriAccount.getBalance()- randomKeysDTO.getAmount());
        desAccount.setBalance(desAccount.getBalance()+ randomKeysDTO.getAmount());

        accountRepository.save(oriAccount);
        accountRepository.save(desAccount);
        String amountMessage = "a total of "+ randomKeysDTO.getAmount() +" has been transferred";
        String destinationAccountMessage = " to the destination account number: " + randomKeysDTO.getToAccountNumber() + " with the description: " +randomKeysDTO.getDescription();
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
