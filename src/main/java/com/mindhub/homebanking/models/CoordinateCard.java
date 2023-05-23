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
    private Map<String, Integer> coordinates;

    public CoordinateCard(Client client) {
        this.client = client;
        this.coordinates = CoordinateCardUtils.generateCoordinates();
    }

     /*public void printCoordinateCard() {
        System.out.println("Coordinate Card:");

        // Imprimir encabezado de columnas
        System.out.print("   ");
        for (char column = 'A'; column <= 'C'; column++) {
            System.out.print(column + " ");
        }
        System.out.println();

        // Imprimir filas y valores
        for (int row = 1; row <= 3; row++) {
            System.out.print(row + "  ");
            for (char column = 'A'; column <= 'C'; column++) {
                String coordinate = String.valueOf(column) + row;
                int value = coordinates.getOrDefault(coordinate, 0);
                System.out.print(value + " ");
            }
            System.out.println();
        }
    }*/
}
