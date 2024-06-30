package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.utility;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfCanvasProcessor;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import com.itextpdf.kernel.pdf.canvas.parser.listener.ITextExtractionStrategy;
import com.itextpdf.kernel.pdf.canvas.parser.listener.SimpleTextExtractionStrategy;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import org.springframework.stereotype.Component;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.SapimentorDemoProjectApplication;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class findKeywordsInAbstract {

    public String cleanFileName(String fileName) {
        // Az "_" és "-" karakterek eltávolítása
        return fileName.replaceAll("[_-]", "");
    }

    public String formatName(String name) {
        // "Diplomadolgozat" és "2023" szavak eltávolítása
        name = name.replace("Diplomadolgozat", "").replace("2023", "");

        // Név formázása "Nagy Marton Hunor" formátummá
        String[] nameParts = name.split("(?=[A-Z])");
        StringBuilder formattedName = new StringBuilder();
        for (String part : nameParts) {
            formattedName.append(part).append(" ");
        }
        return formattedName.toString().trim();
    }

    public String formatName_CALC2021(String name) {
        // "Lucrare" és "2021" szavak eltávolítása
        name = name.replace("Lucrare", "").replace("2021", "");

        // Név formázása "Nagy Marton Hunor" formátummá
        String[] nameParts = name.split("(?=[A-Z])");
        StringBuilder formattedName = new StringBuilder();
        for (String part : nameParts) {
            formattedName.append(part).append(" ");
        }
        return formattedName.toString().trim();
    }

    public String formatName_INF2021(String name) {
        // "Dolgozat" és "2021" szavak eltávolítása
        name = name.replace("Dolgozat", "").replace("2021", "");

        // Név formázása "Nagy Marton Hunor" formátummá
        String[] nameParts = name.split("(?=[A-Z])");
        StringBuilder formattedName = new StringBuilder();
        for (String part : nameParts) {
            formattedName.append(part).append(" ");
        }
        return formattedName.toString().trim();
    }

    public String formatName_CALC2022(String name) {
        // "Docum" es "Dokum"  és "2022" szavak eltávolítása
        name = name.replace("Docum", "").replace("Dokum", "").replace("2021", "");

        // Név formázása "Nagy Marton Hunor" formátummá
        String[] nameParts = name.split("(?=[A-Z])");
        StringBuilder formattedName = new StringBuilder();
        for (String part : nameParts) {
            formattedName.append(part).append(" ");
        }
        return formattedName.toString().trim();
    }

    public int findAbstractPageNumber(PdfReader pdfReader, String searchText) throws IOException {
        PdfDocument pdfDocument = new PdfDocument(pdfReader);

        for (int page = 4; page <= 15; page++) {
            PdfPage pdfPage = pdfDocument.getPage(page);
            String pageText = PdfTextExtractor.getTextFromPage(pdfPage);
            if (pageText.contains(searchText)) {
                return page;
            }
        }

        return -1; // Abstract not found
    }

    public int findAbstractPageNumberForBenedekSzab2021CALC(PdfReader pdfReader, String searchText) throws IOException {
        PdfDocument pdfDocument = new PdfDocument(pdfReader);

        for (int page = 9; page <= 15; page++) {
            PdfPage pdfPage = pdfDocument.getPage(page);
            String pageText = PdfTextExtractor.getTextFromPage(pdfPage);
            if (pageText.contains(searchText)) {
                return page;
            }
        }

        return -1; // Abstract not found
    }

    public String getAbstractText(PdfReader pdfReader, int pageNumber) throws IOException {
        try (PdfDocument pdfDocument = new PdfDocument(pdfReader)) {
            // Lekérjük a megfelelő oldalt a PdfDocument példánytól
            PdfPage page = pdfDocument.getPage(pageNumber);

            // Most már a PdfPage példányon hívjuk meg a getTextFromPage metódust
            return PdfTextExtractor.getTextFromPage(page);
        }
    }

    public String getAbstract_HU_Text(PdfReader pdfReader, int pageNumber) throws IOException {
        StringBuilder formattedText = new StringBuilder();
        try (PdfDocument pdfDocument = new PdfDocument(pdfReader)) {
            // Lekérjük a megfelelő oldalt a PdfDocument példánytól
            PdfPage page = pdfDocument.getPage(pageNumber);

            ITextExtractionStrategy strategy = new SimpleTextExtractionStrategy();
            PdfCanvasProcessor parser = new PdfCanvasProcessor(strategy);
            parser.processPageContent(page);

            formattedText.append(strategy.getResultantText());
            return formattedText.toString();
        }
    }

    public List<String> extractKeywords(String text) throws IOException {
        TokenizerModel tokenizerModel;
        try (InputStream modelIn = SapimentorDemoProjectApplication.class.getResourceAsStream("/en-token.bin")) {
            tokenizerModel = new TokenizerModel(modelIn);
        }

        Tokenizer tokenizer = new TokenizerME(tokenizerModel);
        String[] tokens = tokenizer.tokenize(text);

        // Szavak gyakoriságának elemzése
        Map<String, Integer> wordFrequency = new HashMap<>();
        for (String token : tokens) {
            // Egyszerűen csak a hosszabb szavakat veszi figyelembe (ezt a részt finomíthatod)
            if (token.length() >= 5) {
                wordFrequency.put(token, wordFrequency.getOrDefault(token, 0) + 1);
            }
        }

        // Gyakoriság alapján kulcsszavak kiválasztása
        int threshold = 3; // Egy szó akkor válik kulcsszavá, ha legalább ennyiszer szerepel
        return wordFrequency.entrySet().stream()
                .filter(entry -> entry.getValue() >= threshold)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }


    public String removeKeywords(String text) {
        String[] lines = text.split("\n");
        StringBuilder resultBuilder = new StringBuilder();

        boolean skipNextLines = false;
        for (String line : lines) {
            if (line.startsWith("Keywords:")
                    || line.startsWith("Key words:")
                    || line.startsWith("Keywords—")
                    || line.startsWith("Keyword:")
                    || line.startsWith("Keywords")) {
                skipNextLines = true;
                continue;
            }

            if (!skipNextLines) {
                resultBuilder.append(line).append("\n");
            } else {
                skipNextLines = false;
            }
        }

        return resultBuilder.toString().trim();
    }

    public String getKeywordsForCSV(String text) {
        String[] lines = text.split("\n");
        StringBuilder resultBuilder = new StringBuilder();

        boolean foundKeywords = false;
        for (String line : lines) {
            if (foundKeywords) {
                resultBuilder.append(line.trim());
                break;
            }

            if (line.startsWith("Keywords:")
                    || line.startsWith("Key words:")
                    || line.startsWith("Keywords—")
                    || line.startsWith("Keyword:")) {
                foundKeywords = true;
                String keywords = line.replace("Keywords:", "")
                        .replace("Key words:", "")
                        .replace("Keywords—", "").trim();
                resultBuilder.append(keywords);
            }
        }
        return resultBuilder.toString().trim();
    }


}
