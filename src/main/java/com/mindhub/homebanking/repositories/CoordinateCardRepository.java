package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.CoordinateCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface CoordinateCardRepository extends JpaRepository<CoordinateCard, Long> {
    CoordinateCard findByClient (Client client);
}
