/*package com.mindhub.homebanking.services;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class TransactionSummaryService {

    private final TransactionRepository transactionRepository;

    public TransactionSummaryService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public byte[] generateTransactionSummaryPDF(Client client, int month, int year) throws IOException, DocumentException {
        LocalDateTime startDateTime = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime endDateTime = startDateTime.withDayOfMonth(startDateTime.getMonth().maxLength());
        List<Transaction> transactions = transactionRepository.findByAccountClientIdAndDateBetween(client.getId(), startDateTime, endDateTime);

        LocalDateTime now = LocalDateTime.now();
        String currentDateTime = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        Document document = new Document();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, outputStream);

        document.open();

        Paragraph title = new Paragraph("Transaction Summary");
        title.setFont(new Font(Font.BOLD));
        document.add(title);

        Paragraph clientInfo = new Paragraph("Client: " + client.getFullName());

        clientInfo.setFont(new Font(Font.BOLDITALIC));
        document.add(clientInfo);

        Paragraph dateInfo = new Paragraph("Date: " + currentDateTime);
        dateInfo.setFont(new Font(Font.BOLDITALIC));
        document.add(dateInfo);

        if (!transactions.isEmpty()) {
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.addCell("Date");
            table.addCell("Description");
            table.addCell("Amount");
            table.addCell("Type");

            for (Transaction transaction : transactions) {
                table.addCell(transaction.getDate().toString());
                table.addCell(transaction.getDescription());
                table.addCell(String.valueOf(transaction.getAmount()));
                table.addCell(transaction.getType().toString());
            }

            document.add(table);
        } else {
            Paragraph noTransactions = new Paragraph("No transactions found for the specified month and year.");
            document.add(noTransactions);
        }

        document.close();

        return outputStream.toByteArray();
    }
}*/
