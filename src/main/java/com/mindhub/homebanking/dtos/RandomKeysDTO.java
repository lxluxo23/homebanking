package com.mindhub.homebanking.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class RandomKeysDTO {
    private double amount;
    private String description;
    private String fromAccountNumber;
    private String toAccountNumber;
    private List<String> randomKeys;
    private List<Integer> randomKeysValues;
    
}
