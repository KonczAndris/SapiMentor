package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service;

import com.itextpdf.kernel.pdf.*;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller.dto.ExamsLikeDislikeDTO;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Exams;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.ExamsRepository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.UserExamLikeDislikeRepository;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ExamServices {
    private final ExamsRepository examsRepository;
    private final UserExamLikeDislikeRepository userExamLikeDislikeRepository;

    @Autowired
    private ModelMapper modelMapper;

    public ExamServices(ExamsRepository examsRepository,
                        UserExamLikeDislikeRepository userExamLikeDislikeRepository) {
        this.examsRepository = examsRepository;
        this.userExamLikeDislikeRepository = userExamLikeDislikeRepository;
    }

    @Cacheable("getAllExams")
    public List<Exams> getAllExams() {
        return examsRepository.findAll();
    }

//    public List<ExamsDTO> getExamsWithSelectedFields() {
//        List<Exams> exams = examsRepository.findAll();
//        return exams.stream()
//                .map(exam -> modelMapper.map(exam, ExamsDTO.class))
//                .collect(Collectors.toList());
//    }

    @Cacheable("getAllExamImageById")
    public List<Object[]> getAllExamImageById() {
        return examsRepository.findAllExamImageById();
    }

    @Cacheable("getExamPdfById")
    public List<Object[]> getExamPdfById(Long examId) {
        return examsRepository.findExamPDFById(examId);
    }

    //@Cacheable("getExamImage")
//    public List<Object[]> getExamImage(Long examId) {
//        //System.out.println("examId: " + examId);
//        return examsRepository.findExamImageById(examId);
//    }

    @Cacheable("getExamsWithSelectedFields")
    public List<Exams> getExamsWithSelectedFields() {
        List<Object[]> results = examsRepository.findProjectedBy();
        List<Exams> exams = new ArrayList<>();

        for (Object[] result : results) {
            Exams exam = new Exams();
            exam.setId((Long) result[0]);
            exam.setName((String) result[1]);
            exam.setTopic_name((String) result[2]);
            exam.setUser_name((String) result[3]);
            exam.setLike((Integer) result[4]);
            exam.setDislike((Integer) result[5]);
            exam.setUser_id((Long) result[6]);
            exams.add(exam);
        }

        return exams;
    }

    public Page<Exams> getExamsByPageAndSize(int page, int size) {
        // Oldalszám nullától indul, de a felhasználói interfészről származó oldalszámok általában 1-ről indulnak,
        // ezért szükség lehet egy átalakításra az adatbázis lekérdezés során
        Pageable pageable = PageRequest.of(page > 0 ? page - 1 : page, size);

        // Adatok lekérdezése az adatbázisból oldalazva
        return examsRepository.findAll(pageable);
    }

    @Cacheable("getlikeanddislikecounts")
    public Map<String, Integer> getLikeAndDislikeCounts(Long examId) {
//        Exams exam = examsRepository.findById(examId)
//                .orElseThrow(() -> new ResourceNotFoundException("Exam not found with ID: " + examId));

        List<ExamsLikeDislikeDTO> userLikeAndDislikes = examsRepository.findLikeDislikeById(examId);

        // Készíts egy Map objektumot a like és dislike értékekkel
        Map<String, Integer> likeAndDislikeCounts = new HashMap<>();
        for (ExamsLikeDislikeDTO userLikeAndDislike : userLikeAndDislikes) {
            likeAndDislikeCounts.put("like", userLikeAndDislike.getLike());
            likeAndDislikeCounts.put("dislike", userLikeAndDislike.getDislike());
            likeAndDislikeCounts.put("rowId", Math.toIntExact(userLikeAndDislike.getId()));
        }

        //System.out.println("likeAndDislikeCounts: " + likeAndDislikeCounts);

        return likeAndDislikeCounts;
    }


    //Ezzel tudod beallitani hogy mekkora legyen a maximalis meret amit feltolthet a felhasznalo
    private static final long MAX_IMAGE_SIZE = 6 * 1024 * 1024; // 2 MB
    //private static final long MAX_IMAGE_SIZE = 10 * 1024; // 10 KB
    //private static final long MAX_IMAGE_SIZE = 20 * 1024; // 20 KB
    //private static final long MAX_IMAGE_SIZE = 40 * 1024; // 40 KB

    //////////////////////// MODIFY ///////////////////////////////////////////////////////////////////////////////////////////
    public String modifyExam(String name,
                             String topic,
                             String user_name,
                             Long examId) {
        try {
            Exams exam = examsRepository.findById(examId)
                    .orElseThrow(() -> new ResourceNotFoundException("Exam not found"));
            if(exam != null) {
                exam.setName(name);
                exam.setTopic_name(topic);
                exam.setUser_name(user_name);

                examsRepository.save(exam);
            } else {
                return "Not found";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error";
        }
        return null;
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////// DELETE ///////////////////////////////////////////////////////////////////////////////////////////
    public String deleteExam(Long examId) {
        try {
            userExamLikeDislikeRepository.deleteByExamId(examId);
            examsRepository.deleteById(examId);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error";
        }
        return null;
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    // innen folytatni holnap
    public String uploadExamImage(MultipartFile image,
                                  String name,
                                  String topic,
                                  String user_name,
                                  Long examId) {

        if (!image.isEmpty()){
            try{
                if (image.getSize() > MAX_IMAGE_SIZE){
                    return "Too large";
                }

                byte[] originalImageBytes = image.getBytes();

                // itt hozom letre az Exams objektumot
                // es teszem bele a megadott adatokat
                Exams exam = new Exams();
                exam.setName(name);
                exam.setTopic_name(topic);
                exam.setUser_name(user_name);
                exam.setLike(0);
                exam.setDislike(0);
                exam.setUser_id(examId);

                BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(originalImageBytes));
                System.out.println("Original image size: " + originalImage);

                if (originalImage != null) {
                    // itt folytathatod a képfeldolgozást
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    BufferedImage scaledImage = Thumbnails.of(originalImage)
                            .scale(0.85) // itt allitom be hogy mennyire legyen kicsinyitve a kep
                            .outputQuality(0.7)  // itt allitom be a kivant minoseget
                            .asBufferedImage();
                    ImageIO.write(scaledImage, "jpg", baos);
                    byte[] scaledImageBytes = baos.toByteArray();
//                    System.out.println("Scaled image size: " + scaledImageBytes.length);
                    if (scaledImageBytes.length == 0) {
                        return "Wrong type";
                    } else {
                        exam.setExamImage(scaledImageBytes);
                    }

                } else {
                    System.out.println("Error while image loading.");
                    // itt kell a kep helyett a pdf-et kezelni.
                    ByteArrayInputStream pdfInputStream = new ByteArrayInputStream(originalImageBytes);

                    // Tömörítés előkészítése iText segítségével
                    ByteArrayOutputStream compressedPdfStream = new ByteArrayOutputStream();
                    PdfReader pdfReader = new PdfReader(pdfInputStream);
                    PdfWriter pdfWriter = new PdfWriter(compressedPdfStream);

                    PdfDocument pdfDocument = new PdfDocument(pdfReader, pdfWriter);
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
                    exam.setExamPDF(compressedPdfBytes);
                }

                // itt hozza adom a kepet vagy pdf-eket az Exams objektumhoz
                examsRepository.save(exam);

            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }



    public void likeExam(Long examId) {
        Exams exam = examsRepository.findLikeAndDislikeById(examId)
                .orElseThrow(() -> new ResourceNotFoundException("Exam not found with ID: " + examId));

        // Noveljuk a like-ok szamat
        exam.setLike(exam.getLike() + 1);
        exam.setDislike(exam.getDislike());
        // Elmentjuk az uj like erteket az adatbazisba
        examsRepository.update(exam);
    }

    public void dislikeExam(Long examId) {
        Exams exam = examsRepository.findLikeAndDislikeById(examId)
                .orElseThrow(() -> new ResourceNotFoundException("Exam not found with ID: " + examId));

        // Noveljuk a dislike-ok szamat
        exam.setLike(exam.getLike());
        exam.setDislike(exam.getDislike() + 1);
        // Elmentjuk az uj dislike erteket az adatbazisba
        examsRepository.update(exam);
    }

    public void revokeLike(Long examId) {
        Exams exam = examsRepository.findLikeAndDislikeById(examId)
                .orElseThrow(() -> new ResourceNotFoundException("Exam not found with ID: " + examId));

        // Csokkentjuk a like-ok szamat
        exam.setLike(exam.getLike() - 1);
        exam.setDislike(exam.getDislike());
        // Elmentjuk az uj like erteket az adatbazisba
        examsRepository.update(exam);
    }

    public void revokeDislike(Long examId) {
        Exams exam = examsRepository.findLikeAndDislikeById(examId)
                .orElseThrow(() -> new ResourceNotFoundException("Exam not found with ID: " + examId));

        // Csokkentjuk a dislike-ok szamat
        exam.setLike(exam.getLike());
        exam.setDislike(exam.getDislike() - 1);
        // Elmentjuk az uj dislike erteket az adatbazisba
        examsRepository.update(exam);
    }

    public void likeExamAndRevokeDislike(Long examId) {
        Exams exam = examsRepository.findLikeAndDislikeById(examId)
                .orElseThrow(() -> new ResourceNotFoundException("Exam not found with ID: " + examId));

        // Noveljuk a like-ok szamat
        exam.setLike(exam.getLike() + 1);

        // Csokkentjuk a dislike-ok szamat
        exam.setDislike(exam.getDislike() - 1);

        // Elmentjuk az uj like es dislike erteket az adatbazisba
        examsRepository.update(exam);
    }

    public void dislikeExamAndRevokeLike(Long examId) {
        Exams exam = examsRepository.findLikeAndDislikeById(examId)
                .orElseThrow(() -> new ResourceNotFoundException("Exam not found with ID: " + examId));

        // Noveljuk a dislike-ok szamat
        exam.setDislike(exam.getDislike() + 1);

        // Csokkentjuk a like-ok szamat
        exam.setLike(exam.getLike() - 1);

        // Elmentjuk az uj like es dislike erteket az adatbazisba
        examsRepository.update(exam);
    }


}
