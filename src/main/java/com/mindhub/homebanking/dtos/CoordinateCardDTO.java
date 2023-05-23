package com.mindhub.homebanking.dtos;

import java.util.Map;

public class CoordinateCardDTO {
    private Map<String, Integer> coordinates;

    public CoordinateCardDTO(){
    }

    public Map<String, Integer> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Map<String, Integer> coordinates) {
        this.coordinates = coordinates;
    }
}