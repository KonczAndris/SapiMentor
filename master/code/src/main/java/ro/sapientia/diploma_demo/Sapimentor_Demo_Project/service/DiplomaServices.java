package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service;

import com.itextpdf.kernel.pdf.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Diploma_Theses;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.DiplomaThesesRepository;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;


@Service
public class DiplomaServices {
    private final DiplomaThesesRepository diplomaThesesRepository;

    public DiplomaServices(DiplomaThesesRepository diplomaThesesRepository) {
        this.diplomaThesesRepository = diplomaThesesRepository;
    }

    private static final long MAX_PDF_SIZE = 10 * 1024 * 1024; // 10 MB(10485760)
    //private static final long MAX_PDF_SIZE = 5 * 1024 * 1024; // 5 MB
    //private static final long MAX_PDF_SIZE = 3 * 1024 * 1024; // 3 MB

//    public String uploadDiplomaThesesPdf(MultipartFile pdf,
//                                         String name,
//                                         String topic,
//                                         String user_name){
//        if(!pdf.isEmpty()) {
//            try {
//                System.out.println("Pdf size: " + pdf.getSize());
//                System.out.println("MAX_PDF_SIZE: " + MAX_PDF_SIZE);
//                System.out.println("Pdf name: " + name);
//                System.out.println("Pdf topic: " + topic);
//                System.out.println("Pdf user_name: " + user_name);
//
//                if (pdf.getSize() > MAX_PDF_SIZE) {
//                    return "Too large";
//                }
//
//                byte[] originalPdfBytes = pdf.getBytes();
//
//                // Először alakítsd át InputStream-re
//                ByteArrayInputStream pdfInputStream = new ByteArrayInputStream(originalPdfBytes);
//                // Tömörítés előkészítése iText segítségével
//                ByteArrayOutputStream compressedPdfStream = new ByteArrayOutputStream();
//
//                PdfReader pdfReader = new PdfReader(pdfInputStream);
//                PdfWriter pdfWriter = new PdfWriter(compressedPdfStream);
//
//                PdfDocument pdfDocument = new PdfDocument(pdfReader, pdfWriter);
//
//                // Itt végrehajthatod a tömörítési műveleteket, például a képek minőségének csökkentését
//                // A képek minőségének csökkentése 80%-ra
//                int numPages = pdfDocument.getNumberOfPages();
//                for (int i = 1; i <= numPages; i++) {
//                    pdfDocument.getPage(i).getPdfObject().setCompressionLevel(9);
//                }
//
//                pdfDocument.close();
//                pdfReader.close();
//
//                byte[] compressedPdfBytes = compressedPdfStream.toByteArray();
//
//                // itt hozom letre a Diploma_Theses objektumot
//                // es teszem bele a megadott adatokat
//                Diploma_Theses diploma_theses = new Diploma_Theses();
//                diploma_theses.setName(name);
//                diploma_theses.setTopic_name(topic);
//                diploma_theses.setUser_name(user_name);
//                diploma_theses.setLike(0);
//                diploma_theses.setDislike(0);
//
////                // itt adom hozzá a tömörítetlen PDF-et az objektumhoz
////                diploma_theses.setDiploma_theses_file(originalPdfBytes);
//
//                // Tömörített PDF hozzáadása az objektumhoz
//                diploma_theses.setDiploma_theses_file(compressedPdfBytes);
//
//                //diplomaThesesRepository.save(diploma_theses);
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return null;
//    }

public String uploadDiplomaThesesPdf(MultipartFile pdf,
                                     String name,
                                     String topic,
                                     String user_name){
    if(!pdf.isEmpty()) {
        try {
            System.out.println("Pdf size: " + pdf.getSize());
            System.out.println("MAX_PDF_SIZE: " + MAX_PDF_SIZE);
            System.out.println("Pdf name: " + name);
            System.out.println("Pdf topic: " + topic);
            System.out.println("Pdf user_name: " + user_name);

            if (pdf.getSize() > MAX_PDF_SIZE) {
                return "Too large";
            }

            byte[] originalPdfBytes = pdf.getBytes();

            // Először alakítsd át InputStream-re
            ByteArrayInputStream pdfInputStream = new ByteArrayInputStream(originalPdfBytes);
            // Tömörítés előkészítése iText segítségével
            ByteArrayOutputStream compressedPdfStream = new ByteArrayOutputStream();

            PdfReader pdfReader = new PdfReader(pdfInputStream);
            PdfWriter pdfWriter = new PdfWriter(compressedPdfStream);

            PdfDocument pdfDocument = new PdfDocument(pdfReader, pdfWriter);

            // Itt végrehajthatod a tömörítési műveleteket, például a képek minőségének csökkentését
            // A képek minőségének csökkentése 80%-ra
            int numPages = pdfDocument.getNumberOfPages();
            for (int i = 1; i <= numPages; i++) {
                PdfPage page = pdfDocument.getPage(i);
                PdfDictionary resources = page.getPdfObject().getAsDictionary(PdfName.Resources);
                if (resources != null) {
                    PdfDictionary xObject = resources.getAsDictionary(PdfName.XObject);
                    if (xObject != null) {
                        PdfDictionary imageDict = xObject.getAsDictionary(PdfName.Image);
                        if (imageDict != null) {
                            imageDict.put(PdfName.Filter, PdfName.DCTDecode);
                        }
                    }
                }
            }

            pdfDocument.close();
            pdfReader.close();

            byte[] compressedPdfBytes = compressedPdfStream.toByteArray();

            System.out.println("Compressed PDF size: " + compressedPdfBytes.length);

            // itt hozom letre a Diploma_Theses objektumot
            // es teszem bele a megadott adatokat
            Diploma_Theses diploma_theses = new Diploma_Theses();
            diploma_theses.setName(name);
            diploma_theses.setTopic_name(topic);
            diploma_theses.setUser_name(user_name);
            diploma_theses.setLike(0);
            diploma_theses.setDislike(0);

//            // itt adom hozzá a tömörítetlen PDF-et az objektumhoz
//            diploma_theses.setDiploma_theses_file(originalPdfBytes);

            // Tömörített PDF hozzáadása az objektumhoz
            diploma_theses.setDiploma_theses_file(compressedPdfBytes);

            diplomaThesesRepository.save(diploma_theses);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    return null;
}

}
