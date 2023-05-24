/*package com.mindhub.homebanking.controllers;

import com.lowagie.text.DocumentException;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.TransactionSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class TransactionSummaryController {

    @Autowired
    private TransactionSummaryService transactionSummaryService;

    @GetMapping("/transaction-summary/{month}/{year}")
    public ResponseEntity<byte[]> generateTransactionSummaryPDF(@PathVariable int month, @PathVariable int year) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Client client = (Client) authentication.getPrincipal();
            byte[] pdf = transactionSummaryService.generateTransactionSummaryPDF(client, month, year);
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(pdf);
        } catch (IOException | DocumentException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}*/
