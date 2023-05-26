package com.mindhub.homebanking.utils;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.*;
import com.lowagie.text.Image;
import com.mindhub.homebanking.dtos.*;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.text.NumberFormat;

import java.time.format.DateTimeFormatter;

@Component
public class PDFGenerator {
    public byte[] generatePdf(ClientDTO clientDTO) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Image logo = Image.getInstance("https://cdn.discordapp.com/attachments/1108838702114938950/1110648421792108665/logoVGS.png");
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, baos);
            LogoPageEvent event = new LogoPageEvent(logo);
            writer.setPageEvent(event); 
            document.open();
            Font titleFont = new Font(Font.COURIER, 20f, Font.BOLDITALIC, Color.BLUE);
            Font subtitleFont = new Font(Font.COURIER, 20f, Font.BOLDITALIC, Color.BLUE);
            Font headerFont = new Font(Font.HELVETICA, 16f, Font.BOLD, Color.DARK_GRAY);
            Paragraph title = new Paragraph("Account Summary", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(10);
            document.add(title);
            NumberFormat numberFormat = NumberFormat.getIntegerInstance();
            //logo.setAbsolutePosition(document.right() - 120, document.top() - 200);
            //document.add(logo);

            for (AccountDTO accountDTO : clientDTO.getAccounts()) {
                String formattedBalance = numberFormat.format(accountDTO.getBalance());
                Chunk accountNumberChunk = new Chunk(accountDTO.getNumber(), headerFont);
                Chunk balanceChunk = new Chunk("Balance: $" + formattedBalance, headerFont);
                Paragraph header = new Paragraph();
                header.add(accountNumberChunk);
                header.add(Chunk.NEWLINE);
                header.add(balanceChunk);
                header.setAlignment(Element.ALIGN_CENTER);
                header.setSpacingAfter(10);
                document.add(header);
                PdfPTable table = new PdfPTable(3);
                table.addCell("Date");
                table.addCell("Description");
                table.addCell("Amount");
                for (TransactionDTO transactionDTO : accountDTO.getTransactions()) {
                    String pattern = "yyyy-MM-dd HH:mm:ss";
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
                    String formattedDate = transactionDTO.getDate().format(formatter);
                    table.addCell(formattedDate);
                    table.addCell(transactionDTO.getDescription());
                    String formattedAmount = numberFormat.format(transactionDTO.getAmount());
                    PdfPCell amountCell = new PdfPCell(new Phrase("$" + formattedAmount, FontFactory.getFont(FontFactory.HELVETICA, 12f, transactionDTO.getAmount() >= 0 ? Color.GREEN : Color.RED)));
                    table.addCell(amountCell);
                }
                document.add(table);
                document.add(new Paragraph("\n"));

            }
            if (clientDTO.getCards().size() > 0) {
                Paragraph cards = new Paragraph("Cards", titleFont);
                cards.setAlignment(Element.ALIGN_LEFT);
                cards.setSpacingAfter(10);
                document.add(cards);
                for (CardDTO cardDTO : clientDTO.getCards()) {
                    Paragraph header = new Paragraph();
                    Chunk cardNumberChunk = new Chunk("Card Number: " + cardDTO.getNumber(), headerFont);
                    Chunk cardTypeChunk = new Chunk("Card Type: " + cardDTO.getType(), headerFont);
                    header.add(cardNumberChunk);
                    header.add(Chunk.NEWLINE);
                    header.add(cardTypeChunk);
                    header.setAlignment(Element.ALIGN_LEFT);
                    header.setSpacingAfter(10);
                    document.add(header);
                }
            }
            if (clientDTO.getLoans().size() > 0) {
                Paragraph loans = new Paragraph("Loans", titleFont);
                loans.setAlignment(Element.ALIGN_LEFT);
                loans.setSpacingAfter(10);
                document.add(loans);
                for (ClientLoanDTO clientLoanDTO : clientDTO.getLoans()) {
                    Paragraph header = new Paragraph();
                    Chunk loanNameChunk = new Chunk("Loan name: " + clientLoanDTO.getName(), headerFont);
                    Chunk loanAmountChunk = new Chunk("Amount: $" + numberFormat.format(clientLoanDTO.getAmount()), headerFont);
                    Chunk loanPaymentsChunk = new Chunk("Payments: " + clientLoanDTO.getPayments(), headerFont);
                    header.add(loanNameChunk);
                    header.add(Chunk.NEWLINE);
                    header.add(loanAmountChunk);
                    header.add(Chunk.NEWLINE);
                    header.add(loanPaymentsChunk);
                    header.add(Chunk.NEWLINE);
                    header.setAlignment(Element.ALIGN_LEFT);
                    header.setSpacingAfter(10);
                    document.add(header);
                }
            }

            document.close();
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Error generating the PDF", e);
        }
    }


}
