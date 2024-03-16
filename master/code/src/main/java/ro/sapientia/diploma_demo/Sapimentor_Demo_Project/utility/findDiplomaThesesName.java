package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.utility;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class findDiplomaThesesName {
    public int findThesesNamePageNumber(PdfReader pdfReader, String thesesName) throws IOException {
        PdfDocument pdfDocument = new PdfDocument(pdfReader);
        //int numberOfPages = pdfDocument.getNumberOfPages();

        for (int page = 1; page <= 10; page++) {
            PdfPage pdfPage = pdfDocument.getPage(page);
            String pageText = PdfTextExtractor.getTextFromPage(pdfPage);
            //System.out.println("Igen: " + pageText);
            if (pageText.contains(thesesName)) {
                return page;
            }
        }

        return -1; // Name not found
    }

    public String getThesesNamePage(PdfReader pdfReader, int pageNumber) throws IOException {
        try (PdfDocument pdfDocument = new PdfDocument(pdfReader)) {
            // Lekérjük a megfelelő oldalt a PdfDocument példánytól
            PdfPage page = pdfDocument.getPage(pageNumber);

            // Most már a PdfPage példányon hívjuk meg a getTextFromPage metódust
            return PdfTextExtractor.getTextFromPage(page);
        }
    }


    public String findThesisName(String text) {
        String[] lines = text.split("\n");

        for (int i = 0; i < lines.length; i++) {
            if (lines[i].contains("BACHELOR THESIS")
                    || lines[i].contains("DIPLOMA THESIS")) {
                // Ha a jelenlegi sor tartalmazza a "BACHELOR THESIS" szöveget,
                // akkor visszatérünk az előző sorral
                if (i > 0) {
                    if(lines[i-1].matches("^[a-z].*")){
                        return lines[i-2] + " " + lines[i-1];
                    } else {
                        if (lines[i-1].length() <= 8) {
                            return lines[i - 2] + " " + lines[i - 1];
                        } else {
                            return lines[i - 1];
                        }
                    }

                } else {
                    // Ha az első sor tartalmazza a "BACHELOR THESIS" szöveget,
                    // akkor nincs előző sor, így csak az első sort adjuk vissza
                    return lines[0];
                }
            } else {
                // Kiss Krisztina Gyongyver - Bachelor thesis
                if(lines[i].contains("Bachelor thesis")
                        ||lines[i].contains("Diploma Thesis")) {
                    if (i > 0) {
                        if(lines[i+2].matches("^[a-z].*")){
                            return lines[i+1] + " " + lines[i+2];
                        } else {
                            return lines[i+1];
                        }

                    } else {
                        return lines[0];
                    }
                }
            }
        }

        // Ha nem találjuk a "BACHELOR THESIS" szöveget, akkor üres stringgel térünk vissza
        return "";
    }


    public String formatText(String inputText) {
        String[] lines = inputText.split("\n");

        for (int i = 0; i < lines.length; i++){
            if(lines[i].contains("PROIECT DE DIPLOMĂ")
                    || lines[i].contains("LUCRARE DE DIPLOMAˇ")){
                if (i > 0){
                    if( lines[i-4].startsWith(" ")){
                        System.out.println("CIM: " + lines[i-3] + " " + lines[i-2] + " " + lines[i-1]);
                        return lines[i-3] + " " + lines[i-2] + " " + lines[i-1];
                    }
                    if (lines[i-5].startsWith(" ")){
                        System.out.println("CIM: " + lines[i-4] + " " + lines[i-3] + " " + lines[i-2] + " " + lines[i-1]);
                        return lines[i-4] + " " + lines[i-3] + " " + lines[i-2] + " " + lines[i-1];
                    }
                    System.out.println("SOROK: " + lines[i]);
                } else {
                    return lines[0];
                }
            }
        }
        return "";
    }

    public String formatTextForCALC2021(String inputText) {
        String[] lines = inputText.split("\n");

        for (int i = 0; i < lines.length; i++){
            if(lines[i].contains("PROIECT DE DIPLOMĂ")
                    || lines[i].contains("LUCRARE DE DIPLOMAˇ")){
                if (i > 0){
                    if( lines[i-4].startsWith(" ")){
                        System.out.println("CIM1: " + lines[i-3] + " " + lines[i-2] + " " + lines[i-1]);
                        return lines[i-3] + " " + lines[i-2] + " " + lines[i-1];
                    }
                    if (lines[i-5].startsWith(" ")){
                        System.out.println("CIM2: " + lines[i-4] + " " + lines[i-3] + " " + lines[i-2] + " " + lines[i-1]);
                        return lines[i-4] + " " + lines[i-3] + " " + lines[i-2] + " " + lines[i-1];
                    }

                    if (lines[i-6].startsWith(" ")){
                        System.out.println("CIM3: " + lines[i-5] + " " + lines[i-4] + " " + lines[i-3] + " " + lines[i-2]);
                        return lines[i-5] + " " + lines[i-4] + " " + lines[i-3] + " " + lines[i-2];
                    }
                    System.out.println("SOROK: " + lines[i]);
                } else {
                    return lines[0];
                }
            }
        }
        return "";
    }

    public String formatTextForCALC2021ForDobriErvin(String inputText) {
        String[] lines = inputText.split("\n");

        for (int i = 0; i < lines.length; i++){
            if(lines[i].contains("PROIECT DE DIPLOMĂ")
                    || lines[i].contains("LUCRARE DE DIPLOMAˇ")){
                if (i > 0){
                    if (lines[i-8].startsWith(" ")){
                        System.out.println("CIM3: " + lines[i-5] + " " + lines[i-4] + " " + lines[i-3] + " " + lines[i-2]);
                        return lines[i-7] + " " + lines[i-6] + " " + lines[i-5] + " " + lines[i-4] + " " + lines[i-3];
                    }
                    System.out.println("SOROK: " + lines[i]);
                } else {
                    return lines[0];
                }
            }
        }
        return "";
    }

    public String formatTextForCALC2021ForBenedekSzab(String inputText) {
        String[] lines = inputText.split("\n");

        for (int i = 0; i < lines.length; i++){
            if(lines[i].contains("DIPLOMA THESIS")
                    || lines[i].contains("LUCRARE DE DIPLOMAˇ")){
                if (i > 0){
                    if (lines[i-9].startsWith(" ")){
                        System.out.println("CIM3: " + lines[i-5] + " " + lines[i-4] + " " + lines[i-3] + " " + lines[i-2]);
                        return lines[i-8];
                    }
                    System.out.println("SOROK: " + lines[i]);
                } else {
                    return lines[0];
                }
            }
        }
        return "";
    }

    public String findThesisNameFromCALC2023(String text) {
        String[] lines = text.split("\n");

        for (int i = 0; i < lines.length; i++) {
            if (lines[i].contains("PROIECT DE DIPLOMĂ")
                    || lines[i].contains("LUCRARE DE DIPLOMAˇ")) {
                // Ha a jelenlegi sor tartalmazza a "BACHELOR THESIS" szöveget,
                // akkor visszatérünk az előző sorral
                if (i > 0) {
                    if(lines[i-1].matches("^[a-z].*")){
                        return lines[i-2] + " " + lines[i-1];
                    } else {
                        if (lines[i-1].length() <= 8) {
                            return lines[i - 2] + " " + lines[i - 1];
                        } else {
                            return lines[i - 1];
                        }
                    }

                } else {
                    // Ha az első sor tartalmazza a "BACHELOR THESIS" szöveget,
                    // akkor nincs előző sor, így csak az első sort adjuk vissza
                    return lines[0];
                }
            } else {
                // Kiss Krisztina Gyongyver - Bachelor thesis
                if(lines[i].contains("Bachelor thesis")) {
                    if (i > 0) {
                        return lines[i+1];
                    } else {
                        return lines[0];
                    }
                }
            }
        }

        // Ha nem találjuk a "BACHELOR THESIS" szöveget, akkor üres stringgel térünk vissza
        return "";
    }

}
