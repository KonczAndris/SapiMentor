package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service;

//import com.itextpdf.kernel.pdf.*;


import com.itextpdf.kernel.pdf.*;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.json.JSONObject;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller.dto.Diploma_TLikeDislike_DTO;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Diploma_Theses;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.DiplomaThesesRepository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.UserDiplomaLikeDislikeRepository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.utility.GPT3Service;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.utility.findKeywordsInAbstract;

import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
@Transactional
public class DiplomaServices {
    private final DiplomaThesesRepository diplomaThesesRepository;
    private final findKeywordsInAbstract findKeywordsInAbstract;
    private final GPT3Service gpt3Service;
    private final UserDiplomaLikeDislikeRepository userDiplomaLikeDislikeRepository;

    public DiplomaServices(DiplomaThesesRepository diplomaThesesRepository,
                           findKeywordsInAbstract findKeywordsInAbstract,
                           GPT3Service gpt3Service,
                           UserDiplomaLikeDislikeRepository userDiplomaLikeDislikeRepository) {
        this.diplomaThesesRepository = diplomaThesesRepository;
        this.findKeywordsInAbstract = findKeywordsInAbstract;
        this.gpt3Service = gpt3Service;
        this.userDiplomaLikeDislikeRepository = userDiplomaLikeDislikeRepository;
    }

    public List<Diploma_Theses> getAllDiplomaTheses() {
        return diplomaThesesRepository.findAll();
    }

    @Cacheable("getAllDiplomaThesesWithSelectedFields")
    public List<Diploma_Theses> getAllDiplomaThesesWithSelectedFields() {
        List<Object[]> results = diplomaThesesRepository.findProjectedBy();
        List<Diploma_Theses> diploma_theses = new ArrayList<>();
        for (Object[] result : results) {
            Diploma_Theses diplomas = new Diploma_Theses();
            diplomas.setId((Long) result[0]);
            diplomas.setName((String) result[1]);
            diplomas.setYear((String) result[2]);
            diplomas.setTopic_name((String) result[3]);
            diplomas.setUser_name((String) result[4]);
            diplomas.setLike((Integer) result[5]);
            diplomas.setDislike((Integer) result[6]);
            diplomas.setKeywords((String) result[7]);
            diplomas.setUser_id((Long) result[8]);
            diploma_theses.add(diplomas);
        }
        return diploma_theses;
    }

    @Cacheable("getAllFilteredDiplomaThesesByNameWithSelectedFields")
    public List<Diploma_Theses> getAllFilteredDiplomaThesesByNameWithSelectedFields(Model model,
                                                                    String name,
                                                                    String[] selectedValues) {
        List<Object[]> resultDiplomaTheses = diplomaThesesRepository.findAllByNameAndTopicName(name, selectedValues);
        List<Diploma_Theses> filteredDiploma_Theses = new ArrayList<>();
        for (Object[] result : resultDiplomaTheses) {
            Diploma_Theses filteredDiplomasByName = new Diploma_Theses();
            filteredDiplomasByName.setId((Long) result[0]);
            filteredDiplomasByName.setName((String) result[1]);
            filteredDiplomasByName.setYear((String) result[2]);
            filteredDiplomasByName.setTopic_name((String) result[3]);
            filteredDiplomasByName.setUser_name((String) result[4]);
            filteredDiplomasByName.setLike((Integer) result[5]);
            filteredDiplomasByName.setDislike((Integer) result[6]);
            filteredDiplomasByName.setKeywords((String) result[7]);
            filteredDiplomasByName.setUser_id((Long) result[8]);
            filteredDiploma_Theses.add(filteredDiplomasByName);
        }
        return filteredDiploma_Theses;
    }

    @Cacheable("getAllFilteredDiplomaThesesByKeywordWithSelectedFields")
    public List<Diploma_Theses> getAllFilteredDiplomaThesesByKeywordWithSelectedFields(Model model,
                                                                                    String keyword,
                                                                                    String[] selectedValues) {
        List<Object[]> resultDiplomaTheses = diplomaThesesRepository.findAllByKeywordAndTopicName(keyword, selectedValues);
        List<Diploma_Theses> filteredDiploma_Theses = new ArrayList<>();
        for (Object[] result : resultDiplomaTheses) {
            Diploma_Theses filteredDiplomasByName = new Diploma_Theses();
            filteredDiplomasByName.setId((Long) result[0]);
            filteredDiplomasByName.setName((String) result[1]);
            filteredDiplomasByName.setYear((String) result[2]);
            filteredDiplomasByName.setTopic_name((String) result[3]);
            filteredDiplomasByName.setUser_name((String) result[4]);
            filteredDiplomasByName.setLike((Integer) result[5]);
            filteredDiplomasByName.setDislike((Integer) result[6]);
            filteredDiplomasByName.setKeywords((String) result[7]);
            filteredDiplomasByName.setUser_id((Long) result[8]);
            filteredDiploma_Theses.add(filteredDiplomasByName);
        }
        return filteredDiploma_Theses;
    }

    @Cacheable("getLikeAndDislikeCounts")
    public Map<String, Integer> getLikeAndDislikeCounts(Long diplomaId) {
        List<Diploma_TLikeDislike_DTO> userLikeAndDislikes = diplomaThesesRepository.findLikeDislikeById(diplomaId);

        // Készíts egy Map objektumot a like és dislike értékekkel
        Map<String, Integer> likeAndDislikeCounts = new HashMap<>();
        for (Diploma_TLikeDislike_DTO dto : userLikeAndDislikes) {
            likeAndDislikeCounts.put("like", dto.getLike());
            likeAndDislikeCounts.put("dislike", dto.getDislike());
            likeAndDislikeCounts.put("rowId", Math.toIntExact(dto.getId()));
        }

        return likeAndDislikeCounts;
    }

    private static final long MAX_PDF_SIZE = 30 * 1024 * 1024; // 10 MB(11500000)
    //private static final long MAX_PDF_SIZE = 5 * 1024 * 1024; // 5 MB
    //private static final long MAX_PDF_SIZE = 3 * 1024 * 1024; // 3 MB


    public String modifyDiplomaThesesPdf(String name,
                                         String topic,
                                         String user_name,
                                         String year,
                                         Long diplomaId) {
            try {
                Diploma_Theses optionalDiplomaTheses = diplomaThesesRepository.findById(diplomaId)
                        .orElseThrow(() -> new ResourceNotFoundException("Diploma Theses not found"));
                if(optionalDiplomaTheses != null){
                    optionalDiplomaTheses.setName(name);
                    optionalDiplomaTheses.setTopic_name(topic);
                    optionalDiplomaTheses.setUser_name(user_name);
                    optionalDiplomaTheses.setYear(year);

                    diplomaThesesRepository.save(optionalDiplomaTheses);
                } else {
                    return "Not found";
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "Error";
            }
        return null;
    }

    //////////////////////// ez kell a tobbihez is /////////////////////////////////////////////////////////////////
    public String deleteDiplomaThesesPdf(Long diplomaId) {
        try {
            userDiplomaLikeDislikeRepository.deleteByDiplomaId(diplomaId);
            diplomaThesesRepository.deleteById(diplomaId);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error";
        }
        return null;
    }


    // TODO: ezen javitani a CLR-hez hasonloan
// oldalon a felhasznalo altal hasznalt metodus
public String uploadDiplomaThesesPdf(MultipartFile pdf,
                                     String name,
                                     String topic,
                                     String user_name,
                                     String year,
                                     Long user_id){
    if(!pdf.isEmpty()) {
        try {

            if (pdf.getSize() > MAX_PDF_SIZE) {
                return "Too large";
            }
            String finalyKeywords = null;
            StringBuilder allKeywords = new StringBuilder();

            byte[] originalPdfBytes = pdf.getBytes();

            ByteArrayInputStream pdfInputStream = new ByteArrayInputStream(originalPdfBytes);

            ByteArrayOutputStream compressedPdfStream = new ByteArrayOutputStream();

            PdfReader pdfReader = new PdfReader(pdfInputStream);
            PdfWriter pdfWriter = new PdfWriter(compressedPdfStream);

            PdfDocument pdfDocument = new PdfDocument(pdfReader, pdfWriter);

            try {
                String searchText = "Abstract";
                PdfReader pdfReaderForAbstract = new PdfReader(pdf.getInputStream());
                int abstractPageNumber = findKeywordsInAbstract.findAbstractPageNumber(pdfReaderForAbstract, searchText);

                if (abstractPageNumber > 0) {
                    PdfReader pdfReaderForAbstractText = new PdfReader(pdf.getInputStream());
                    String realAbstractText = findKeywordsInAbstract.getAbstractText(pdfReaderForAbstractText, abstractPageNumber);

                    String abstractText = "Get 5 or 3 keywords from this text: " + realAbstractText;
                    abstractText = abstractText.replaceAll("[\\r\\n]+", "");

                    //////// Itt van a GPT3-as verzio ////////////

                    String gpt3Response = gpt3Service.getKeywordsFromAbstractWithGPT3(abstractText);

                    if(gpt3Response.contains("Error GPT-3 API")){
                        finalyKeywords = "No keywords found";
                    } else {
                        JSONObject gpt3JsonResponse = new JSONObject(gpt3Response);

                        String gpt3Keywords = gpt3JsonResponse.getJSONArray("choices")
                                .getJSONObject(0)
                                .getJSONObject("message")
                                .getString("content");

                        /////// Idaig van a GPT3-as verzio //////////

                        String patternString = "^\\d+\\. (.+)";
                        Pattern pattern = Pattern.compile(patternString, Pattern.MULTILINE);

                        if (!gpt3Keywords.isEmpty()) {
                            Matcher matcher = pattern.matcher(gpt3Keywords);
                            while (matcher.find()){
                                allKeywords.append(matcher.group(1)).append(", ");
                            }
                            finalyKeywords = allKeywords.toString();
                        } else {
                            allKeywords.append("No keywords found");
                            finalyKeywords = "No keywords found";
                        }
                    }
                } else {
                    System.out.println("Abstract not found in the PDF.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

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

            Diploma_Theses diploma_theses = new Diploma_Theses();
            diploma_theses.setName(name);
            diploma_theses.setTopic_name(topic);
            diploma_theses.setUser_name(user_name);
            diploma_theses.setYear(year);
            diploma_theses.setLike(0);
            diploma_theses.setDislike(0);
            diploma_theses.setKeywords(allKeywords.toString());
            diploma_theses.setUser_id(user_id);
            diploma_theses.setDiploma_theses_file(compressedPdfBytes);

            diplomaThesesRepository.save(diploma_theses);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    return null;
}

// a CommandLineRunner altal hasznalt metodus
public String uploadDiplomaThesesPdfByCLR(byte[] pdfBytes,
                                          String name,
                                          String topic,
                                          String user_name,
                                          String year,
                                          String keywords,
                                          byte[] abstract_file_en,
                                          byte[] abstract_file_hu) {
    try {
        ByteArrayInputStream pdfInputStream = new ByteArrayInputStream(pdfBytes);
        ByteArrayOutputStream compressedPdfStream = new ByteArrayOutputStream();
        PdfReader pdfReader = new PdfReader(pdfInputStream);
        PdfWriter pdfWriter = new PdfWriter(compressedPdfStream);
        PdfDocument pdfDocument = new PdfDocument(pdfReader, pdfWriter);

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

        Diploma_Theses pdfNameAndTopic = diplomaThesesRepository.findNameAndTopic(name,topic,year);
        if (pdfNameAndTopic.getName().equals(name) && pdfNameAndTopic.getTopic_name().equals(topic) && pdfNameAndTopic.getYear().equals(year)) {
            pdfNameAndTopic.setAbstract_file_en(abstract_file_en);
            pdfNameAndTopic.setAbstract_file_hu(abstract_file_hu);
            diplomaThesesRepository.save(pdfNameAndTopic);
        } else {
            Diploma_Theses diploma_theses = new Diploma_Theses();
            diploma_theses.setName(name);
            diploma_theses.setTopic_name(topic);
            diploma_theses.setUser_name(user_name);
            diploma_theses.setYear(year);
            diploma_theses.setLike(0);
            diploma_theses.setDislike(0);
            diploma_theses.setKeywords(keywords);
            diploma_theses.setDiploma_theses_file(compressedPdfBytes);
            diplomaThesesRepository.save(diploma_theses);
        }
    } catch (Exception e) {
        e.printStackTrace();
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

    @Cacheable("getDiplomaById")
    public List<Object[]> getDiplomaById(Long diplomaId) {
        return diplomaThesesRepository.findDiplomaPDFById(diplomaId);
    }


    public Diploma_Theses downloadDiplomaThesePdf(Long diplomaId) {
        return diplomaThesesRepository.findById(diplomaId)
                .orElseThrow(() -> new ResourceNotFoundException("Diploma Theses not found with Id:" + diplomaId));
    }

}





//    @Bean
//    public CommandLineRunner InformaticsPDFUploadRunner(DiplomaServices diplomaServices,
//                                                        findKeywordsInAbstract findKeywordsInAbstract,
//                                                        GPT3Service gpt3Service) {
//        return args -> {
//            String pdfDirectoryPath = "src/main/resources/static/pdf";
//
//            // PDF-fájlok listázása a könyvtárban
//            File pdfDirectory = new File(pdfDirectoryPath);
//            File[] pdfFiles = pdfDirectory.listFiles((dir, name) -> name.toLowerCase().endsWith(".pdf"));
//
//            if (pdfFiles != null) {
//                for (File pdfFile : pdfFiles) {
//                    String originalName = pdfFile.getName().replace(".pdf", "");
//                    String cleanedName = findKeywordsInAbstract.cleanFileName(originalName);
//                    String formattedName = findKeywordsInAbstract.formatName(cleanedName);
//                    String topic = "Informatics";
//                    String user_Name = "Szotyori Csongor";
//                    String year = "2023";
//                    String finalyKeywords = null;
//                    StringBuilder allKeywords = new StringBuilder();
//                    String finalKeywordsString = null;
//                    byte[] abstractPdfBytes_en = null;
//                    byte[] abstractPdfBytes_hu = null;
//
//                    byte[] pdfBytes = Files.readAllBytes(pdfFile.toPath());
//
//                    try {
//                        String searchText = "Abstract";
//                        String searchText_HU = "Kivonat";
//
//                        System.out.println("Search text: " + searchText);
//
//                        /////////////////////// Angol kivonat kinyerese /////////////////////////////////////////////////////////////////
//                        PdfReader pdfReader = new PdfReader(new ByteArrayInputStream(pdfBytes));
//                        int abstractPageNumber = findKeywordsInAbstract.findAbstractPageNumber(pdfReader, searchText);
//                        //System.out.println("Abstract page number: " + abstractPageNumber);
//
//                        if (abstractPageNumber > 0) {
//                            PdfReader pdfReaderForText = new PdfReader(new ByteArrayInputStream(pdfBytes));
//                            String abstractTextForCSV = findKeywordsInAbstract.getAbstractText(pdfReaderForText, abstractPageNumber);
//                            System.out.println("Angol kivonat: " + abstractTextForCSV + "\n");
//
//                            ByteArrayOutputStream out_en = new ByteArrayOutputStream();
//                            PdfWriter writer_en = new PdfWriter(out_en);
//                            PdfDocument pdfDoc_en = new PdfDocument(writer_en);
//                            Document document_en = new Document(pdfDoc_en);
//
//                            document_en.add(new Paragraph(abstractTextForCSV));
//
//                            document_en.close();
//
//                            // Byte tömb a PDF adatokkal
//                            abstractPdfBytes_en = out_en.toByteArray();
//
////							String finalabstractText = findKeywordsInAbstract.removeKeywords(abstractTextForCSV);
////							String abstractText = "Get 5 or 3 keywords from this text: " + finalabstractText;
////							abstractText = abstractText.replaceAll("[\\r\\n]+", " ")
////									.replaceAll("\"", "'")
////									.replaceAll("-", " ");
////
////							System.out.println("Abstract text for ChatGPT: " + abstractText);
//
//                            //////// Itt van a GPT3-as verzio ////////////
//
////							String gpt3Response = gpt3Service.getKeywordsFromAbstractWithGPT3(abstractText);
////
////							//System.out.println("GPT-3 response: " + gpt3Response);
////							if(gpt3Response.contains("Error GPT-3 API")){
////								System.out.println("Error GPT-3 API");
////								finalyKeywords = findKeywordsInAbstract.extractKeywords(finalabstractText).toString();
////								//continue;
////							} else {
////								// ide teszem be a gpt3 tol kapott valaszt
////								JSONObject gpt3JsonResponse = new JSONObject(gpt3Response);
////
////								// itt kiszedem a hasznos reszt a valaszbol
////								String gpt3Keywords = gpt3JsonResponse.getJSONArray("choices")
////										.getJSONObject(0)
////										.getJSONObject("message")
////										.getString("content");
////
////								/////// Idaig van a GPT3-as verzio //////////
////
////								System.out.println("Keywords: " + gpt3Keywords);
////
////								String patternString = "^\\d+\\. (.+)";
////								Pattern pattern = Pattern.compile(patternString, Pattern.MULTILINE);
////
////								if (!gpt3Keywords.isEmpty()) {
////									Matcher matcher = pattern.matcher(gpt3Keywords);
////									while (matcher.find()){
////										allKeywords.append(matcher.group(1)).append(", ");
////									}
////									if(allKeywords.length() > 0){
////										System.out.println("GPT-3 keywords: " + allKeywords.toString());
////										finalyKeywords = allKeywords.toString();
////									} else {
////										finalKeywordsString = gpt3Keywords.replaceAll("Keywords:\\s*", "");
////										System.out.println("GPT-3 final keywords: " + finalKeywordsString);
////										System.out.println("-----------------------------------------------------------------------------------\n");
////										finalyKeywords = finalKeywordsString;
////									}
////
////								} else {
////									allKeywords.append("No keywords found");
////									finalyKeywords = "No keywords found";
////								}
////							}
//
//                        } else {
//                            System.out.println("Abstract not found in the PDF.");
//                        }
//
//                        /////////////////////// Magyar kivonat kinyerese /////////////////////////////////////////////////////////////////
//                        PdfReader pdfReaderForHuAbstract = new PdfReader(new ByteArrayInputStream(pdfBytes));
//                        int huAbstractPageNumber = findKeywordsInAbstract.findAbstractPageNumber(pdfReaderForHuAbstract, searchText_HU);
//                        //System.out.println("Abstract page number: " + huAbstractPageNumber);
//
//                        if (huAbstractPageNumber > 0) {
//                            PdfReader pdfReaderForTextHU = new PdfReader(new ByteArrayInputStream(pdfBytes));
//                            String huAbstractTextForCSV = findKeywordsInAbstract.getAbstract_HU_Text(pdfReaderForTextHU, huAbstractPageNumber);
//                            System.out.println("Magyar kivonat: " + huAbstractTextForCSV + "\n");
//
//                            ByteArrayOutputStream out_hu = new ByteArrayOutputStream();
//                            PdfWriter writer_hu = new PdfWriter(out_hu);
//                            PdfDocument pdfDoc_hu = new PdfDocument(writer_hu);
//                            Document document_hu = new Document(pdfDoc_hu);
//
//                            document_hu.add(new Paragraph(huAbstractTextForCSV));
//
//                            document_hu.close();
//
//                            // Byte tömb a PDF adatokkal
//                            abstractPdfBytes_hu = out_hu.toByteArray();
//
//
//                        } else {
//                            System.out.println("Kivonat not found in the PDF.");
//                        }
//
//                        pdfReader.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//                    Thread.sleep(2000);
//                    System.out.println("PDF file " + formattedName + " is smaller than 10MB. Uploading...");
//                    diplomaServices.uploadDiplomaThesesPdfByCLR(pdfBytes, formattedName, topic, user_Name, year, finalyKeywords, abstractPdfBytes_en, abstractPdfBytes_hu);
//                }
//            }
//
//            //System.out.println("PDF files found in the directory: " + pdfFiles.length);
//            System.out.println("CommandLineRunner running in the UnsplashApplication class...");
//        };
//    }