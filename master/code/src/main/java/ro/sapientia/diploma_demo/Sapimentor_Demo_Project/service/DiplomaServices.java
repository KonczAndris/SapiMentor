package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service;

//import com.itextpdf.kernel.pdf.*;


import com.itextpdf.kernel.pdf.*;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller.dto.Diploma_TLikeDislike_DTO;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Diploma_Theses;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.DiplomaThesesRepository;

import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.*;


@Service
@Transactional
public class DiplomaServices {
    private final DiplomaThesesRepository diplomaThesesRepository;

    public DiplomaServices(DiplomaThesesRepository diplomaThesesRepository) {
        this.diplomaThesesRepository = diplomaThesesRepository;
    }

    public List<Diploma_Theses> getAllDiplomaTheses() {
        return diplomaThesesRepository.findAll();
    }


//    @Async
//    @Cacheable(value = "getAllDiplomaPdfById")
//    public List<Object[]> getAllDiplomaPdfById() {
//        return diplomaThesesRepository.findAllDiplomaPDFById();
//    }
//
//    ///////////
//    @Async
//    @Cacheable(value = "getDiplomaPdfById")
//    public byte[] getDiplomaPdfById(Long diplomaId) {
//        return diplomaThesesRepository.findDiplomaPDFById(diplomaId);
//    }
//    ///////////



    //@Cacheable("getAllDiplomaThesesWithSelectedFields")
    public List<Diploma_Theses> getAllDiplomaThesesWithSelectedFields() {
        List<Object[]> results = diplomaThesesRepository.findProjectedBy();
        List<Diploma_Theses> diploma_theses = new ArrayList<>();

        //System.out.println("results: " + results);
        for (Object[] result : results) {
            Diploma_Theses diplomas = new Diploma_Theses();
            diplomas.setId((Long) result[0]);
            diplomas.setName((String) result[1]);
            diplomas.setYear((String) result[2]);
            diplomas.setTopic_name((String) result[3]);
            diplomas.setUser_name((String) result[4]);
            diplomas.setLike((Integer) result[5]);
            diplomas.setDislike((Integer) result[6]);
            diploma_theses.add(diplomas);
        }

        return diploma_theses;
    }

    @Cacheable("getLikeAndDislikeCounts")
    public Map<String, Integer> getLikeAndDislikeCounts(Long diplomaId) {
        //System.out.println("diplomaId: " + diplomaId);
//        Diploma_Theses diploma_theses = diplomaThesesRepository.findLikeDislikeById(diplomaId)
//                .orElseThrow(() -> new ResourceNotFoundException("Diploma Theses not found with ID: " + diplomaId));

        List<Diploma_TLikeDislike_DTO> userLikeAndDislikes = diplomaThesesRepository.findLikeDislikeById(diplomaId);

        // Készíts egy Map objektumot a like és dislike értékekkel
        Map<String, Integer> likeAndDislikeCounts = new HashMap<>();
        for (Diploma_TLikeDislike_DTO dto : userLikeAndDislikes) {
//            System.out.println("ID: " + dto.getId());
//            System.out.println("User ID: " + dto.getUser_id());
//            System.out.println("Diploma ID: " + dto.getDiploma_id());
//            System.out.println("Like: " + dto.getLike());
//            System.out.println("Dislike: " + dto.getDislike());
//            System.out.println("------------------------");
            likeAndDislikeCounts.put("like", dto.getLike());
            likeAndDislikeCounts.put("dislike", dto.getDislike());
            likeAndDislikeCounts.put("rowId", Math.toIntExact(dto.getId()));
        }

        return likeAndDislikeCounts;
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
                                     String user_name,
                                     String year){
    if(!pdf.isEmpty()) {
        try {
//            System.out.println("Pdf size: " + pdf.getSize());
//            System.out.println("MAX_PDF_SIZE: " + MAX_PDF_SIZE);
//            System.out.println("Pdf name: " + name);
//            System.out.println("Pdf topic: " + topic);
//            System.out.println("Pdf user_name: " + user_name);

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

            //System.out.println("Compressed PDF size: " + compressedPdfBytes.length);

            // itt hozom letre a Diploma_Theses objektumot
            // es teszem bele a megadott adatokat
            Diploma_Theses diploma_theses = new Diploma_Theses();
            diploma_theses.setName(name);
            diploma_theses.setTopic_name(topic);
            diploma_theses.setUser_name(user_name);
            diploma_theses.setYear(year);
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

    // TODO: likeDiploma
    public void likeDiploma(Long diplomaId) {
        Diploma_Theses diploma_theses = diplomaThesesRepository.findLikeAndDislikeById(diplomaId)
                .orElseThrow(() -> new ResourceNotFoundException("Diploma Theses not found with Id:" + diplomaId));

        diploma_theses.setLike(diploma_theses.getLike() + 1);
        diploma_theses.setDislike(diploma_theses.getDislike());

        diplomaThesesRepository.update(diploma_theses);
    }

    // TODO: dislikeDiploma
    public void dislikeDiploma(Long diplomaId) {
        Diploma_Theses diploma_theses = diplomaThesesRepository.findLikeAndDislikeById(diplomaId)
                .orElseThrow(() -> new ResourceNotFoundException("Diploma Theses not found with Id:" + diplomaId));

        diploma_theses.setLike(diploma_theses.getLike());
        diploma_theses.setDislike(diploma_theses.getDislike() + 1);

        diplomaThesesRepository.update(diploma_theses);
    }

    // TODO: revokeLike
    public void revokeLike(Long diplomaId) {
        Diploma_Theses diploma_theses = diplomaThesesRepository.findLikeAndDislikeById(diplomaId)
                .orElseThrow(() -> new ResourceNotFoundException("Diploma Theses not found with Id:" + diplomaId));

        diploma_theses.setLike(diploma_theses.getLike() - 1);
        diploma_theses.setDislike(diploma_theses.getDislike());

        diplomaThesesRepository.update(diploma_theses);
    }

    // TODO: revokeDislike
    public void revokeDislike(Long diplomaId) {
        Diploma_Theses diploma_theses = diplomaThesesRepository.findLikeAndDislikeById(diplomaId)
                .orElseThrow(() -> new ResourceNotFoundException("Diploma Theses not found with Id:" + diplomaId));

        diploma_theses.setLike(diploma_theses.getLike());
        diploma_theses.setDislike(diploma_theses.getDislike() - 1);

        diplomaThesesRepository.update(diploma_theses);
    }

    // TODO: likeDiplomaAndRevokeDislike
    public void likeDiplomaAndRevokeDislike(Long diplomaId) {
        Diploma_Theses diploma_theses = diplomaThesesRepository.findLikeAndDislikeById(diplomaId)
                .orElseThrow(() -> new ResourceNotFoundException("Diploma Theses not found with Id:" + diplomaId));

        diploma_theses.setLike(diploma_theses.getLike() + 1);
        diploma_theses.setDislike(diploma_theses.getDislike() - 1);

        diplomaThesesRepository.update(diploma_theses);
    }

    // TODO: dislikeDiplomaAndRevokeLike
    public void dislikeDiplomaAndRevokeLike(Long diplomaId) {
        Diploma_Theses diploma_theses = diplomaThesesRepository.findLikeAndDislikeById(diplomaId)
                .orElseThrow(() -> new ResourceNotFoundException("Diploma Theses not found with Id:" + diplomaId));

        diploma_theses.setDislike(diploma_theses.getDislike() + 1);
        diploma_theses.setLike(diploma_theses.getLike() - 1);

        diplomaThesesRepository.update(diploma_theses);
    }

    public List<Object[]> getDiplomaById(Long diplomaId) {
        return diplomaThesesRepository.findDiplomaPDFById(diplomaId);
    }

}
