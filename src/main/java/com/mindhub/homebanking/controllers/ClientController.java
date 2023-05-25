package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.CoordinateCard;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.EmailService;
import com.mindhub.homebanking.repositories.CoordinateCardRepository;
import com.mindhub.homebanking.utils.AccountUtils;
import com.mindhub.homebanking.utils.PDFGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.mindhub.homebanking.dtos.ClientDTO;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static org.hibernate.tool.schema.SchemaToolingLogging.LOGGER;

@RestController
@RequestMapping("/api")
public class ClientController {
    @Autowired
    private EmailService emailService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PDFGenerator pdfGenerator;
    @Autowired
    private CoordinateCardRepository coordinateCardRepository;

    @GetMapping("/clients")
    public List<ClientDTO> getClients() {
        return clientRepository.findAll().stream().map(ClientDTO::new).collect(toList());
    }

    @GetMapping("clients/{id}")
    public ClientDTO getClientById(@PathVariable Long id) {
        return clientRepository.findById(id).map(ClientDTO::new).orElse(null);
    }

    @PostMapping("/clients")
    public ResponseEntity<Object> register(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam String phone,
            @RequestParam String password) {
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.BAD_REQUEST);
        }
        if (clientRepository.findByEmail(email) != null) {
            return new ResponseEntity<>("User already register!", HttpStatus.CONFLICT);
        }
        try {
            Client client = new Client(firstName, lastName, email, phone, passwordEncoder.encode(password));
            Account account = new Account(AccountUtils.generateVinNumber(), LocalDateTime.now(), 0, client);
            CoordinateCard coordinateCard = new CoordinateCard(client);
            clientRepository.save(client);
            accountRepository.save(account);
            coordinateCardRepository.save(coordinateCard);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/clients/current")
    public ClientDTO getByCurrent(Authentication authentication) {
        return new ClientDTO(clientRepository.findByEmail(authentication.getName()));
    }

    @GetMapping("/clients/current/sendinfo")
    public ResponseEntity<Object> sendInfoToEmail(Authentication authentication) {
        try {
            Client client = clientRepository.findByEmail(authentication.getName());
            ClientDTO clientDTO = new ClientDTO(client);
            //Set<AccountDTO> accountDTO = client.getAccounts().stream().map(AccountDTO::new).collect(Collectors.toSet());
            PDFGenerator pdfGenerator = new PDFGenerator();
            byte[] pdfBytes = pdfGenerator.generatePdf(clientDTO);
            String from = "no-reply@sense-it.cl";
            String to = "lxluxo23@gmail.com";
            String subject = "Account Summary";
            String text = "Dear " + client.getFullName() + ",\n\nPlease find attached the account summary.";
            emailService.send(from, to, subject, text, pdfBytes, "account_summary.pdf");
            return new ResponseEntity<>("Email sent successfully",HttpStatus.CREATED);
        } catch (Exception e) {
            LOGGER.error("Error sending email");
            LOGGER.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send email");
        }
    }
}

