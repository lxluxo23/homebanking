package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.models.enums.CardColor;
import com.mindhub.homebanking.models.enums.CardType;
import com.mindhub.homebanking.models.enums.TransactionType;
import com.mindhub.homebanking.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}
	@Bean
	public CommandLineRunner initData(
			ClientRepository clientRepository,
			AccountRepository accountRepository,
			TransactionRepository transactionRepository,
			LoanRepository loanRepository,
			ClientLoanRepository clientLoanRepository,
			CardRepository cardRepository) {
		return (args) -> {
			Client melba = new Client("Melba", "Morel","melba@mindhub.com");
			clientRepository.save(melba);

			Client luis = new Client("Luis", "Céspedes","a@a.cl");
			clientRepository.save(luis);

			Account cuenta1 = new Account("VIN001", LocalDateTime.now(), 5000 ,melba);
			Account cuenta2 = new Account("VIN002", LocalDateTime.now().plusDays(1), 7500 ,melba);
			Account cuenta3 = new Account("VIN003", LocalDateTime.now(), 10000 ,luis);

			accountRepository.save(cuenta1);
			accountRepository.save(cuenta2);
			accountRepository.save(cuenta3);

			Transaction ts1 = new Transaction(TransactionType.DEBIT,-7500.0,"cine hoyts",LocalDateTime.now(), cuenta1 );
			Transaction ts2 = new Transaction(TransactionType.DEBIT,-12000.0,"netflix",LocalDateTime.now(), cuenta1 );
			Transaction ts3 = new Transaction(TransactionType.DEBIT,-4500.0,"spotify",LocalDateTime.now(), cuenta1 );
			Transaction ts4 = new Transaction(TransactionType.CREDIT,600000.0,"pago sueldo",LocalDateTime.now(), cuenta1 );
			Transaction ts5 = new Transaction(TransactionType.CREDIT,120000.0,"devolucion impuesto",LocalDateTime.now(), cuenta1 );
			Transaction ts6 = new Transaction(TransactionType.DEBIT,-400000.0,"Taller mecanico",LocalDateTime.now(), cuenta2 );
			Transaction ts7 = new Transaction(TransactionType.DEBIT,-500.0,"Super 8",LocalDateTime.now(), cuenta3 );

			transactionRepository.save(ts1);
			transactionRepository.save(ts2);
			transactionRepository.save(ts3);
			transactionRepository.save(ts4);
			transactionRepository.save(ts5);
			transactionRepository.save(ts6);
			transactionRepository.save(ts7);

			List<Integer> paymentsHipotecario = List.of(12,24,36,48,60);
			List<Integer> paymentsPersonal = List.of(6,12,24);
			List<Integer> paymentsAutomotriz = List.of(6,12,24,36);

			Loan loan1 = new Loan("Hipotecario",500.000,paymentsHipotecario);
			Loan loan2 = new Loan("Personal",500.000,paymentsHipotecario);
			Loan loan3 = new Loan("Automotriz",500.000,paymentsHipotecario);

			loanRepository.save(loan1);
			loanRepository.save(loan2);
			loanRepository.save(loan3);

			ClientLoan clientLoan1 = new ClientLoan(melba,loan1,400.000,60);
			ClientLoan clientLoan2 = new ClientLoan(melba,loan2,50.000,12);
			ClientLoan clientLoan3 = new ClientLoan(luis,loan2,100.000,24);
			ClientLoan clientLoan4 = new ClientLoan(luis,loan3,200.000,36);

			clientLoanRepository.save(clientLoan1);
			clientLoanRepository.save(clientLoan2);
			clientLoanRepository.save(clientLoan3);
			clientLoanRepository.save(clientLoan4);

			Card card1 = new Card(melba,melba.getFullName(), CardType.DEBIT, CardColor.GOLD,"3325-6445-7876-4445",773,LocalDateTime.now(),LocalDateTime.now().plusYears(5));
			Card card2 = new Card(melba,melba.getFullName(),CardType.CREDIT,CardColor.TITANIUM,"4120-0309-6658-7789",145,LocalDateTime.now(),LocalDateTime.now().plusYears(5));

			cardRepository.save(card1);
			cardRepository.save(card2);
	};
	}
}
