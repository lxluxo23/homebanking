package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.mindhub.homebanking.dtos.ClientDTO;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class ClientController {
    @Autowired

    private PasswordEncoder passwordEncoder;
    @Autowired
    private ClientRepository clientRepository;
    @RequestMapping("/clients")
    public List<ClientDTO> getClients() {
        return clientRepository.findAll().stream().map(ClientDTO::new).collect(toList());
    }
    @RequestMapping("clients/{id}")
    public ClientDTO getClientById(@PathVariable Long id){
        return clientRepository.findById(id).map(ClientDTO::new).orElse(null);
    }
    @GetMapping("/clients/current")
    public ClientDTO getByCurrent(Authentication authentication){
        return new ClientDTO(clientRepository.findByEmail(authentication.getName()));
    }
    @PostMapping("/clients")
    public ResponseEntity<Object> register(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam String password){
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.BAD_REQUEST);
        }
        if (clientRepository.findByEmail(email) !=  null) {
            return new ResponseEntity<>("User already register!", HttpStatus.CONFLICT);
        }
        try {
            Client client = new Client(firstName,lastName,email,passwordEncoder.encode(password));
            clientRepository.save(client);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


}
