package com.mindhub.homebanking.utils;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.*;


public class LogoPageEvent extends PdfPageEventHelper {
    private Image logo;

    public LogoPageEvent(Image logo) {
        this.logo = logo;
    }
    @Override
    public void onStartPage(PdfWriter writer, Document document) {
        try {
            float scaleFactor = 0.7f;
            logo.scaleAbsolute(logo.getWidth() * scaleFactor, logo.getHeight() * scaleFactor);
            logo.setAbsolutePosition(document.right() - 80, document.top() - 150);
            PdfContentByte content = writer.getDirectContent();
            content.addImage(logo);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        try {
            Font font = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL);
            Phrase phrase = new Phrase("Your security is important", font);
            float pageWidth = document.right() - document.left();
            float x = (document.left() + document.right() + document.leftMargin() - document.rightMargin()) / 2;
            float y = document.bottomMargin() - 20;
            PdfContentByte content = writer.getDirectContent();
            ColumnText.showTextAligned(content, Element.ALIGN_CENTER, phrase, x, y, 0);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }


}
