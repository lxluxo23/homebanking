package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.ClientLoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RestController;

@RestController
public interface ClientLoanRepository  extends JpaRepository<ClientLoan,Long> {
}
