package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController

@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @GetMapping("/accounts")
    public List<AccountDTO> getAccounts() {
        return accountRepository.findAll().stream().map(AccountDTO::new).collect(toList());
    }

    @GetMapping("/accounts/{id}")
    public AccountDTO getAccountById(@PathVariable Long id) {
        return accountRepository.findById(id).map(AccountDTO::new).orElse(null);
    }

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> createAcountCurrentClient(Authentication authentication) {
        Client client = clientRepository.findByEmail(authentication.getName());
        int numAccounts = client.getAccounts().size();
        if (numAccounts >= 3) {
            return new ResponseEntity<>("The user already has at least 3 accounts ", HttpStatus.CONFLICT);
        }
        try {
            Account account = new Account(AccountUtils.generateVinNumber(), LocalDateTime.now(), 0, client);
            accountRepository.save(account);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("Account created", HttpStatus.CREATED);
    }
    @GetMapping("/clients/current/accounts")
    public List<AccountDTO> getAccounts(Authentication authentication){
        Client client = this.clientRepository.findByEmail(authentication.getName());
        return client.getAccounts().stream().map(AccountDTO::new).collect(toList());
    }
    /*
     * @GetMapping("/clients/current/accounts")
     * public AccountDTO getCurrentAccount(Authentication authentication){
     * return new
     * AccountDTO(accountRepository.findByEmail(authentication.getName()));
     * public String getCurrentClientAccounts(){
     * return "el json con las cuentas";
     * }
     */

}
