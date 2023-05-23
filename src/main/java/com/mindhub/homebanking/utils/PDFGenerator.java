package com.mindhub.homebanking.utils;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.*;
import com.lowagie.text.Image;
import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.TransactionDTO;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.text.NumberFormat;

import java.time.format.DateTimeFormatter;
import java.util.Set;

@Component
public class PDFGenerator {
    public byte[] generatePdf(Set<AccountDTO> accountDTOs) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Image logo = Image.getInstance("https://cdn.discordapp.com/attachments/1108838702114938950/1110648421792108665/logoVGS.png");
            Document document = new Document();
            PdfWriter.getInstance(document, baos);
            document.open();
            Font titleFont = new Font(Font.COURIER, 20f, Font.BOLDITALIC, Color.BLUE);
            Font headerFont = new Font(Font.HELVETICA, 16f, Font.BOLD, Color.DARK_GRAY);
            Paragraph title = new Paragraph("Account Summary", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(10);
            document.add(title);
            NumberFormat numberFormat = NumberFormat.getIntegerInstance();
            logo.setAbsolutePosition(document.right() - 120, document.top() - 200);
            document.add(logo);
            for (AccountDTO accountDTO : accountDTOs) {
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
                    String formattedAmmount = numberFormat.format(transactionDTO.getAmount());
                    PdfPCell amountCell = new PdfPCell(new Phrase("$" + formattedAmmount, FontFactory.getFont(FontFactory.HELVETICA, 12f, transactionDTO.getAmount() >= 0 ? Color.GREEN : Color.RED)));
                    table.addCell(amountCell);
                }
                document.add(table);
                document.add(new Paragraph("\n"));
            }
            document.close();
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Error generating the PDF", e);
        }
    }
}
