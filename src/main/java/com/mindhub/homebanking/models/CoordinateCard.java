package com.mindhub.homebanking.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CoordinateCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "client_id")
    private Client client;
    private int[][] coordinates;

    public CoordinateCard(Client client, int[][] coordinates) {
        this.client = client;
        this.coordinates = coordinates;
    }
}
