package com.mindhub.homebanking.models;

import com.mindhub.homebanking.models.enums.CardColor;
import com.mindhub.homebanking.models.enums.CardType;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="client_id")
    private Client client;
    private String cardholder;
    private CardType type;
    private CardColor color;
    private String number;
    private Integer cvv;
    private LocalDateTime thruDate;
    private LocalDateTime formData;
    public Card(Client client, String cardholder, CardType type, CardColor color, String number, Integer cvv, LocalDateTime thruDate, LocalDateTime formData) {
        this.client = client;
        this.cardholder = cardholder;
        this.type = type;
        this.color = color;
        this.number = number;
        this.cvv = cvv;
        this.thruDate = thruDate;
        this.formData = formData;
    }

}
