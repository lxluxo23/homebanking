package com.mindhub.homebanking.models;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="client_id")
    private Client client;

    private String number;
    private LocalDateTime creationDate;
    private double balance;

    @OneToMany(mappedBy="account", fetch=FetchType.EAGER)
    private Set<Transaction> transactions = new HashSet<>();
    public Account( String number, LocalDateTime creationDate, double balance, Client client) {
        this.client = client;
        this.number = number;
        this.creationDate = creationDate;
        this.balance = balance;
    }
}

