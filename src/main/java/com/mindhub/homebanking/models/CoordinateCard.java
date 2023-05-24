package com.mindhub.homebanking.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.mindhub.homebanking.utils.CoordinateCardUtils;

import javax.persistence.*;
import java.util.Map;


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

    @ElementCollection
    private Map<String, Integer> coordinates;

    public CoordinateCard(Client client) {
        this.client = client;
        this.coordinates = CoordinateCardUtils.generateCoordinates();
    }
}
