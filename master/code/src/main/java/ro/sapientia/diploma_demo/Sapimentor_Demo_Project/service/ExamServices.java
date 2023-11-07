package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service;

import net.coobird.thumbnailator.Thumbnails;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Exams;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.ExamsRepository;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExamServices {
    private final ExamsRepository examsRepository;

    public ExamServices(ExamsRepository examsRepository) {
        this.examsRepository = examsRepository;
    }

    @Cacheable(value = "examsCache")
    public List<Exams> getAllExams() {
        return examsRepository.findAll();
    }

    public Map<String, Integer> getLikeAndDislikeCounts(Long examId) {
        Exams exam = examsRepository.findById(examId)
                .orElseThrow(() -> new ResourceNotFoundException("Exam not found with ID: " + examId));

        // Készíts egy Map objektumot a like és dislike értékekkel
        Map<String, Integer> likeAndDislikeCounts = new HashMap<>();
        likeAndDislikeCounts.put("like", exam.getLike());
        likeAndDislikeCounts.put("dislike", exam.getDislike());
        likeAndDislikeCounts.put("rowId", exam.getId().intValue());

        //System.out.println("likeAndDislikeCounts: " + likeAndDislikeCounts);

        return likeAndDislikeCounts;
    }


    //Ezzel tudod beallitani hogy mekkora legyen a maximalis meret amit feltolthet a felhasznalo
    private static final long MAX_IMAGE_SIZE = 2 * 1024 * 1024; // 2 MB
    //private static final long MAX_IMAGE_SIZE = 10 * 1024; // 10 KB
    //private static final long MAX_IMAGE_SIZE = 20 * 1024; // 20 KB
    //private static final long MAX_IMAGE_SIZE = 40 * 1024; // 40 KB

    // innen folytatni holnap
    public String uploadExamImage(MultipartFile image,
                                  String name,
                                  String topic,
                                  String user_name){

        if (!image.isEmpty()){
            try{
//                System.out.println("Image size: " + image.getSize());
//                System.out.println("MAX_IMAGE_SIZE: " + MAX_IMAGE_SIZE);
//                System.out.println("Image name: " + name);
//                System.out.println("Image topic: " + topic);
//                System.out.println("Image user_name: " + user_name);
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

//                // itt skalazom a kepet a megadott meretekre
//                BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(originalImageBytes));
//
//                int minDimension = Math.min(originalImage.getWidth(), originalImage.getHeight());
//                int x = (originalImage.getWidth() - minDimension) / 2;
//                int y = (originalImage.getHeight() - minDimension) / 2;
//
//                BufferedImage croppedImage = originalImage.getSubimage(x, y, minDimension, minDimension);
//
//                BufferedImage scaledImage = Thumbnails.of(croppedImage)
//                        .scale(1.0) // 100%-os méretarány (eredeti méret megtartva)
//                        .outputQuality(0.8)  // Itt állítsd be a kívánt minőséget
//                        .asBufferedImage();

                BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(originalImageBytes));
//                System.out.println("Original image size: " + originalImageBytes.length);

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
//                        ByteArrayOutputStream pngBaos = new ByteArrayOutputStream();
//                        BufferedImage scaledImage2 = Thumbnails.of(originalImage)
//                                .scale(0.85) // itt allitom be hogy mennyire legyen kicsinyitve a kep
//                                .outputQuality(0.7)  // itt allitom be a kivant minoseget
//                                .outputFormat("png") // Megőrzi az eredeti PNG formátumot
//                                .asBufferedImage();
//                        ImageIO.write(scaledImage2, "png", pngBaos);
//
//                        byte[] scaledImageBytesJPEG = pngBaos.toByteArray();
//
//                        System.out.println("Scaled image size2: " + scaledImageBytesJPEG.length);
                        //exam.setExamImage(scaledImageBytesJPEG);

                        return "Wrong type";
                    } else {
                        exam.setExamImage(scaledImageBytes);
                    }

                } else {
                    System.out.println("Hiba a kép betöltésekor.");
                }

                // itt hozza adom a kepet az Exams objektumhoz

                examsRepository.save(exam);

            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }



    public void likeExam(Long examId) {
        Exams exam = examsRepository.findById(examId)
                .orElseThrow(() -> new ResourceNotFoundException("Exam not found with ID: " + examId));

        // Noveljuk a like-ok szamat
        exam.setLike(exam.getLike() + 1);
        exam.setDislike(exam.getDislike());
        // Elmentjuk az uj like erteket az adatbazisba
        examsRepository.save(exam);
    }

    public void dislikeExam(Long examId) {
        Exams exam = examsRepository.findById(examId)
                .orElseThrow(() -> new ResourceNotFoundException("Exam not found with ID: " + examId));

        // Noveljuk a dislike-ok szamat
        exam.setDislike(exam.getDislike() + 1);
        // Elmentjuk az uj dislike erteket az adatbazisba
        examsRepository.save(exam);
    }

    public void revokeLike(Long examId) {
        Exams exam = examsRepository.findById(examId)
                .orElseThrow(() -> new ResourceNotFoundException("Exam not found with ID: " + examId));

        // Csokkentjuk a like-ok szamat
        exam.setLike(exam.getLike() - 1);
        // Elmentjuk az uj like erteket az adatbazisba
        examsRepository.save(exam);
    }

    public void revokeDislike(Long examId) {
        Exams exam = examsRepository.findById(examId)
                .orElseThrow(() -> new ResourceNotFoundException("Exam not found with ID: " + examId));

        // Csokkentjuk a dislike-ok szamat
        exam.setDislike(exam.getDislike() - 1);
        // Elmentjuk az uj dislike erteket az adatbazisba
        examsRepository.save(exam);
    }

    public void likeExamAndRevokeDislike(Long examId) {
        Exams exam = examsRepository.findById(examId)
                .orElseThrow(() -> new ResourceNotFoundException("Exam not found with ID: " + examId));

        // Noveljuk a like-ok szamat
        exam.setLike(exam.getLike() + 1);

        // Csokkentjuk a dislike-ok szamat
        exam.setDislike(exam.getDislike() - 1);

        // Elmentjuk az uj like es dislike erteket az adatbazisba
        examsRepository.save(exam);
    }

    public void dislikeExamAndRevokeLike(Long examId) {
        Exams exam = examsRepository.findById(examId)
                .orElseThrow(() -> new ResourceNotFoundException("Exam not found with ID: " + examId));

        // Noveljuk a dislike-ok szamat
        exam.setDislike(exam.getDislike() + 1);

        // Csokkentjuk a like-ok szamat
        exam.setLike(exam.getLike() - 1);

        // Elmentjuk az uj like es dislike erteket az adatbazisba
        examsRepository.save(exam);
    }


}
