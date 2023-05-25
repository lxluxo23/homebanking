package com.mindhub.homebanking.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.mindhub.homebanking.utils.CoordinateCardUtils;

import javax.persistence.*;
import java.util.Arrays;
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
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("   ");
        for (int i = 1; i <= 6; i++) {
            sb.append("   ").append((char)('A' + (i-1))).append("   ");
        }
        sb.append("\n");
        for (int i = 1; i <= 6; i++) {
            sb.append(i).append("  ");
            for (int j = 1; j <= 6; j++) {
                String key = (char)('A' + (j-1)) + Integer.toString(i);
                Integer value = coordinates.get(key);
                sb.append(String.format("%-6d", value));
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
