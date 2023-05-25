package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.CoordinateCard;

import java.util.Map;

public class CoordinateCardDTO {
    private Long id;
    private Map<String, Integer> coordinates;

    public CoordinateCardDTO(){}
    public CoordinateCardDTO(CoordinateCard coordinateCard){
        this.id = coordinateCard.getId();
        this.coordinates = coordinateCard.getCoordinates();
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Map<String, Integer> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Map<String, Integer> coordinates) {
        this.coordinates = coordinates;
    }
}
