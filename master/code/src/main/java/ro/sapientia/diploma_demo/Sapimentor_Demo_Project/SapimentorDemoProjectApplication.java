package ro.sapientia.diploma_demo.Sapimentor_Demo_Project;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service.DiplomaServices;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// A ScheduledTaskService osztályban lévő
// @Scheduled annotációk használatához
@EnableScheduling
@SpringBootApplication
public class SapimentorDemoProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(SapimentorDemoProjectApplication.class, args);
		System.out.println("Run Successfully!");
	}

	private static final Long MAX_PDF_FILE_SIZE = 10 * 1024 * 1024L; // 10 MB

	// Informatika_2023 szakos diakok diplomamunkainak feltoltese
	// a CommandLineRunner interfesz implementalas segitsegevel
//	@Bean
//	public CommandLineRunner InformaticPDFUploadRunner(DiplomaServices diplomaServices) {
//		return args -> {
//			String pdfDirectoryPath = "src/main/resources/static/pdf";
//
//			// PDF-fájlok listázása a könyvtárban
//			File pdfDirectory = new File(pdfDirectoryPath);
//			File[] pdfFiles = pdfDirectory.listFiles((dir, name) -> name.toLowerCase().endsWith(".pdf"));
//
//			if (pdfFiles != null) {
//				for (File pdfFile : pdfFiles) {
//					String originalName = pdfFile.getName().replace(".pdf", "");
//					String cleanedName = cleanFileName(originalName);
//					String formattedName = formatName(cleanedName);
//					String topic = "Informatics";
//					String user_Name = "Szotyori Csongor";
//					String year = "2023";
//
//
//					if (pdfFile.length() > MAX_PDF_FILE_SIZE) {
//						System.out.println("PDF file " + formattedName + " is larger than 10MB. Skipping...");
//						continue;
//					}
//					byte[] pdfBytes = Files.readAllBytes(pdfFile.toPath());
//					//System.out.println("PDF file name: " + formattedName + " | PDF file size: " + pdfFile.length() + " bytes");
//					//System.out.println(pdfFile.getName().replace(".pdf", ""));
//
//					Thread.sleep(6000);
//					System.out.println("PDF file " + formattedName + " is smaller than 10MB. Uploading...");
//					diplomaServices.uploadDiplomaThesesPdfByCLR(pdfBytes, formattedName, topic, user_Name, year);
//				}
//			}
//
//			//System.out.println("PDF files found in the directory: " + pdfFiles.length);
//			System.out.println("CommandLineRunner running in the UnsplashApplication class...");
//		};
//	}


	// Szamitastechnika_2023 szakos diakok diplomamunkainak feltoltese
	// a CommandLineRunner interfesz implementalas segitsegevel
//	@Bean
//	public CommandLineRunner CalculatoarePDFUploadRunner(DiplomaServices diplomaServices) {
//		return args -> {
//			String pdfDirectoryPath = "src/main/resources/static/pdf";
//
//			// PDF-fájlok listázása a könyvtárban
//			File pdfDirectory = new File(pdfDirectoryPath);
//			File[] pdfFiles = pdfDirectory.listFiles((dir, name) -> name.toLowerCase().endsWith(".pdf"));
//
//			if (pdfFiles != null) {
//				for (File pdfFile : pdfFiles) {
//					String originalName = pdfFile.getName().replace(".pdf", "");
//					String cleanedName = cleanFileName(originalName);
//					String formattedName = formatName(cleanedName);
//					String topic = "Engineering";
//					String user_Name = "Koncz Andras";
//					String year = "2023";
//
//
//					if (pdfFile.length() > MAX_PDF_FILE_SIZE) {
//						System.out.println("PDF file " + formattedName + " is larger than 10MB. Skipping...");
//						continue;
//					}
//					byte[] pdfBytes = Files.readAllBytes(pdfFile.toPath());
//					//System.out.println("PDF file name: " + formattedName + " | PDF file size: " + pdfFile.length() + " bytes");
//					//System.out.println(pdfFile.getName().replace(".pdf", ""));
//
//					Thread.sleep(3000);
//					System.out.println("PDF file " + formattedName + " is smaller than 10MB. Uploading...");
//					diplomaServices.uploadDiplomaThesesPdfByCLR(pdfBytes, formattedName, topic, user_Name, year);
//				}
//			}
//
//			//System.out.println("PDF files found in the directory: " + pdfFiles.length);
//			System.out.println("CommandLineRunner running in the UnsplashApplication class...");
//		};
//	}


	//////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////


	// Informatika_2022 szakos diakok diplomamunkainak feltoltese
	// a CommandLineRunner interfesz implementalas segitsegevel
//	@Bean
//	public CommandLineRunner InformaticPDFUploadRunner(DiplomaServices diplomaServices) {
//		return args -> {
//			String pdfDirectoryPath = "src/main/resources/static/pdf";
//
//			// PDF-fájlok listázása a könyvtárban
//			File pdfDirectory = new File(pdfDirectoryPath);
//			File[] pdfFiles = pdfDirectory.listFiles((dir, name) -> name.toLowerCase().endsWith(".pdf"));
//
//			if (pdfFiles != null) {
//				for (File pdfFile : pdfFiles) {
//					String originalName = pdfFile.getName().replace(".pdf", "");
//					String cleanedName = cleanFileName(originalName);
//					String formattedName = formatName(cleanedName);
//					String topic = "Informatics";
//					String user_Name = "Szotyori Csongor";
//					String year = "2022";
//
//
//					if (pdfFile.length() > MAX_PDF_FILE_SIZE) {
//						System.out.println("PDF file " + formattedName + " is larger than 10MB. Skipping...");
//						continue;
//					}
//					byte[] pdfBytes = Files.readAllBytes(pdfFile.toPath());
//					//System.out.println("PDF file name: " + formattedName + " | PDF file size: " + pdfFile.length() + " bytes");
//					//System.out.println(pdfFile.getName().replace(".pdf", ""));
//
//					Thread.sleep(2000);
//					System.out.println("PDF file " + formattedName + " is smaller than 10MB. Uploading...");
//					diplomaServices.uploadDiplomaThesesPdfByCLR(pdfBytes, formattedName, topic, user_Name, year);
//				}
//			}
//
//			//System.out.println("PDF files found in the directory: " + pdfFiles.length);
//			System.out.println("CommandLineRunner running in the UnsplashApplication class...");
//		};
//	}


	// Szamitastechnika_2022 szakos diakok diplomamunkainak feltoltese
	// a CommandLineRunner interfesz implementalas segitsegevel
//	@Bean
//	public CommandLineRunner CalculatoarePDFUploadRunner(DiplomaServices diplomaServices) {
//		return args -> {
//			String pdfDirectoryPath = "src/main/resources/static/pdf";
//
//			// PDF-fájlok listázása a könyvtárban
//			File pdfDirectory = new File(pdfDirectoryPath);
//			File[] pdfFiles = pdfDirectory.listFiles((dir, name) -> name.toLowerCase().endsWith(".pdf"));
//
//			if (pdfFiles != null) {
//				for (File pdfFile : pdfFiles) {
//					String originalName = pdfFile.getName().replace(".pdf", "");
//					String cleanedName = cleanFileName(originalName);
//					String formattedName = formatName(cleanedName);
//					String topic = "Engineering";
//					String user_Name = "Koncz Andras";
//					String year = "2022";
//
//
//					if (pdfFile.length() > MAX_PDF_FILE_SIZE) {
//						System.out.println("PDF file " + formattedName + " is larger than 10MB. Skipping...");
//						continue;
//					}
//					byte[] pdfBytes = Files.readAllBytes(pdfFile.toPath());
//					//System.out.println("PDF file name: " + formattedName + " | PDF file size: " + pdfFile.length() + " bytes");
//					//System.out.println(pdfFile.getName().replace(".pdf", ""));
//
//					Thread.sleep(3000);
//					System.out.println("PDF file " + formattedName + " is smaller than 10MB. Uploading...");
//					diplomaServices.uploadDiplomaThesesPdfByCLR(pdfBytes, formattedName, topic, user_Name, year);
//				}
//			}
//
//			//System.out.println("PDF files found in the directory: " + pdfFiles.length);
//			System.out.println("CommandLineRunner running in the UnsplashApplication class...");
//		};
//	}

	//////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////


	// Informatika_2021 szakos diakok diplomamunkainak feltoltese
	// a CommandLineRunner interfesz implementalas segitsegevel
//	@Bean
//	public CommandLineRunner InformaticPDFUploadRunner(DiplomaServices diplomaServices) {
//		return args -> {
//			String pdfDirectoryPath = "src/main/resources/static/pdf";
//
//			// PDF-fájlok listázása a könyvtárban
//			File pdfDirectory = new File(pdfDirectoryPath);
//			File[] pdfFiles = pdfDirectory.listFiles((dir, name) -> name.toLowerCase().endsWith(".pdf"));
//
//			if (pdfFiles != null) {
//				for (File pdfFile : pdfFiles) {
//					String originalName = pdfFile.getName().replace(".pdf", "");
//					String cleanedName = cleanFileName(originalName);
//					String formattedName = formatName(cleanedName);
//					String topic = "Informatics";
//					String user_Name = "Szotyori Csongor";
//					String year = "2021";
//
//
//					if (pdfFile.length() > MAX_PDF_FILE_SIZE) {
//						System.out.println("PDF file " + formattedName + " is larger than 10MB. Skipping...");
//						continue;
//					}
//					byte[] pdfBytes = Files.readAllBytes(pdfFile.toPath());
//					//System.out.println("PDF file name: " + formattedName + " | PDF file size: " + pdfFile.length() + " bytes");
//					//System.out.println(pdfFile.getName().replace(".pdf", ""));
//
//					Thread.sleep(2000);
//					System.out.println("PDF file " + formattedName + " is smaller than 10MB. Uploading...");
//					diplomaServices.uploadDiplomaThesesPdfByCLR(pdfBytes, formattedName, topic, user_Name, year);
//				}
//			}
//
//			//System.out.println("PDF files found in the directory: " + pdfFiles.length);
//			System.out.println("CommandLineRunner running in the UnsplashApplication class...");
//		};
//	}


	// Szamitastechnika_2021 szakos diakok diplomamunkainak feltoltese
	// a CommandLineRunner interfesz implementalas segitsegevel
//	@Bean
//	public CommandLineRunner CalculatoarePDFUploadRunner(DiplomaServices diplomaServices) {
//		return args -> {
//			String pdfDirectoryPath = "src/main/resources/static/pdf";
//
//			// PDF-fájlok listázása a könyvtárban
//			File pdfDirectory = new File(pdfDirectoryPath);
//			File[] pdfFiles = pdfDirectory.listFiles((dir, name) -> name.toLowerCase().endsWith(".pdf"));
//
//			if (pdfFiles != null) {
//				for (File pdfFile : pdfFiles) {
//					String originalName = pdfFile.getName().replace(".pdf", "");
//					String cleanedName = cleanFileName(originalName);
//					String formattedName = formatName(cleanedName);
//					String topic = "Engineering";
//					String user_Name = "Koncz Andras";
//					String year = "2021";
//
//
//					if (pdfFile.length() > MAX_PDF_FILE_SIZE) {
//						System.out.println("PDF file " + formattedName + " is larger than 10MB. Skipping...");
//						continue;
//					}
//					byte[] pdfBytes = Files.readAllBytes(pdfFile.toPath());
//					//System.out.println("PDF file name: " + formattedName + " | PDF file size: " + pdfFile.length() + " bytes");
//					//System.out.println(pdfFile.getName().replace(".pdf", ""));
//
//					Thread.sleep(3000);
//					System.out.println("PDF file " + formattedName + " is smaller than 10MB. Uploading...");
//					diplomaServices.uploadDiplomaThesesPdfByCLR(pdfBytes, formattedName, topic, user_Name, year);
//				}
//			}
//
//			//System.out.println("PDF files found in the directory: " + pdfFiles.length);
//			System.out.println("CommandLineRunner running in the UnsplashApplication class...");
//		};
//	}





		@Bean
	public CommandLineRunner CalculatoarePDFUploadRunner(DiplomaServices diplomaServices) {
		return args -> {
			String pdfDirectoryPath = "src/main/resources/static/pdf";

			// PDF-fájlok listázása a könyvtárban
			File pdfDirectory = new File(pdfDirectoryPath);
			File[] pdfFiles = pdfDirectory.listFiles((dir, name) -> name.toLowerCase().endsWith(".pdf"));

			if (pdfFiles != null) {
				for (File pdfFile : pdfFiles) {
					String originalName = pdfFile.getName().replace(".pdf", "");
					String cleanedName = cleanFileName(originalName);
					String formattedName = formatName(cleanedName);
					String topic = "Engineering";
					String user_Name = "Nem Igen Nem";
					String year = "1999";

					if (pdfFile.length() > MAX_PDF_FILE_SIZE) {
						System.out.println("PDF file " + formattedName + " is larger than 10MB. Skipping...");
						continue;
					}
					byte[] pdfBytes = Files.readAllBytes(pdfFile.toPath());
					//System.out.println("PDF file name: " + formattedName + " | PDF file size: " + pdfFile.length() + " bytes");
					//System.out.println(pdfFile.getName().replace(".pdf", ""));

					try {
						//String filePath = "./PDF/Magyari_Dora_Diplomadolgozat_2023.pdf"; // PDF fájl elérési útvonala
						String searchText = "Abstract";

						System.out.println("Search text: " + searchText);

						PdfReader pdfReader = new PdfReader(new ByteArrayInputStream(pdfBytes));
						//System.out.println("Number of pages: " + pdfReader.getNumberOfPages());

						int abstractPageNumber = findAbstractPageNumber(pdfReader, searchText);
						System.out.println("Abstract page number: " + abstractPageNumber);

						if (abstractPageNumber > 0) {
							PdfReader pdfReaderForigen = new PdfReader(new ByteArrayInputStream(pdfBytes));
							String text = getAbstractText(pdfReaderForigen, abstractPageNumber);
							System.out.println("Abstract found on page " + abstractPageNumber + ":\n" + text);

							///String abstractText = com.itextpdf.text.pdf.parser.PdfTextExtractor.getTextFromPage(pdfDocument, abstractPageNumber);


							String szoveg = "Artificial neural networks (ANN) are widely used in solving problems like image processing, data mining, or classification. Hardware accelerators are used for increasing the performance and efficiency of neural networks. An option for implementing such an accelerator is an FPGA- based system, although developing neural networks for FPGAs is very time-consuming and requires professionals to do it. In this article, we try to tackle this problem by creating a framework that should speed up the process. At the same time, we will take a look at some efficiency optimization and speed-up options as well. The framework is written in Python and generates a C++ code whit HLS directives. This code can be compiled by Vivado HLS into a hardware descriptive language and packaged as an IP. The Vivado tool can generate a bit file that can be uploade onto the FPGA device.Among other things, the dissertation presents a comparison of different approximations of nonlinear transformations (base functions and activation functions) in terms of accuracy, required resource, and delay needed for evaluating the transformation. The generated neural network module was integrated into a system, that we developed. Using that system, we tested the neural network module and compared it with the models implemented in Python.";

							List<String> keywords = extractKeywords(text);
							System.out.println("Kulcsszavak: " + keywords);


						} else {
							System.out.println("Abstract not found in the PDF.");
						}

						pdfReader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}


					Thread.sleep(3000);
					System.out.println("PDF file " + formattedName + " is smaller than 10MB. Uploading...");
					//diplomaServices.uploadDiplomaThesesPdfByCLR(pdfBytes, formattedName, topic, user_Name, year);
				}
			}

			//System.out.println("PDF files found in the directory: " + pdfFiles.length);
			System.out.println("CommandLineRunner running in the UnsplashApplication class...");
		};
	}




	private String cleanFileName(String fileName) {
		// Az "_" és "-" karakterek eltávolítása
		return fileName.replaceAll("[_-]", "");
	}

	private String formatName(String name) {
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


	private static int findAbstractPageNumber(PdfReader pdfReader, String searchText) throws IOException {
		PdfDocument pdfDocument = new PdfDocument(pdfReader);
		//int numberOfPages = pdfDocument.getNumberOfPages();


		for (int page = 1; page <= 10; page++) {
			PdfPage pdfPage = pdfDocument.getPage(page);
			String pageText = PdfTextExtractor.getTextFromPage(pdfPage);
			if (pageText.contains(searchText)) {
				return page;
			}
		}

		return -1; // Abstract not found
	}

	private static String getAbstractText(PdfReader pdfReader, int pageNumber) throws IOException {
		try (PdfDocument pdfDocument = new PdfDocument(pdfReader)) {
			// Lekérjük a megfelelő oldalt a PdfDocument példánytól
			PdfPage page = pdfDocument.getPage(pageNumber);

			// Most már a PdfPage példányon hívjuk meg a getTextFromPage metódust
			return PdfTextExtractor.getTextFromPage(page);
		}
	}


	private static List<String> extractKeywords(String text) throws IOException {
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


}
