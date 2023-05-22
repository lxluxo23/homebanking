package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.enums.CardColor;
import com.mindhub.homebanking.models.enums.CardType;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.MessageService;
import com.mindhub.homebanking.utils.CardUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class CardController {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private MessageService messageService;
    @GetMapping("/cards/{id}")
    public CardDTO getAccount(@PathVariable Long id) {
        return cardRepository.findById(id).map(CardDTO::new).orElse(null);
    }

    @GetMapping("/clients/current/cards")
    public String getCurrentClientCards() {
        return "obtener targetas del cliente actual";
    }

    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> postCard(
            Authentication authentication,
            @RequestParam CardType cardType,
            @RequestParam CardColor cardColor) {
        Client client = clientRepository.findByEmail(authentication.getName());
        if (cardType == null || cardColor == null) {
            return new ResponseEntity<>("Missing data", HttpStatus.BAD_REQUEST);
        }
        if (client == null) {
            return new ResponseEntity<>("No user found", HttpStatus.CONFLICT);
        }
        try {
            int numCards = (int) client.getCards()
                    .stream()
                    .filter(card -> card.getType() == cardType)
                    .count();
            if (numCards >= 3) {
                return new ResponseEntity<>("More than three cards of same type", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Card card = new Card(
                client,
                client.getFullName(),
                cardType,
                cardColor,
                CardUtils.generateCardNumber(),
                CardUtils.createCVVNumber(),
                LocalDateTime.now(),
                LocalDateTime.now().plusYears(5));
        cardRepository.save(card);
        String Message = cardColor+" " +cardType+" card successfully created with the number: "+card.getNumber() ;
        this.messageService.sendWhatsapp(
                "+56953618681",
                Message
        );
        return new ResponseEntity<>("Card created! ", HttpStatus.CREATED);
    }

}
