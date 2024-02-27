package ro.sapientia.diploma_demo.Sapimentor_Demo_Project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

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





//		@Bean
//	public CommandLineRunner CalculatoarePDFUploadRunner(DiplomaServices diplomaServices,
//														 findKeywordsInAbstract findKeywordsInAbstract) {
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
//					String cleanedName = findKeywordsInAbstract.cleanFileName(originalName);
//					String formattedName = findKeywordsInAbstract.formatName(cleanedName);
//					String topic = "Engineering";
//					String user_Name = "Koncz Andras";
//					String year = "2023";
//					String finalyKeywords = null;
//
//					if (pdfFile.length() > MAX_PDF_FILE_SIZE) {
//						System.out.println("PDF file " + formattedName + " is larger than 10MB. Skipping...");
//						continue;
//					}
//					byte[] pdfBytes = Files.readAllBytes(pdfFile.toPath());
//					//System.out.println("PDF file name: " + formattedName + " | PDF file size: " + pdfFile.length() + " bytes");
//					//System.out.println(pdfFile.getName().replace(".pdf", ""));
//
//					try {
//						String searchText = "Abstract";
//
//						System.out.println("Search text: " + searchText);
//
//						PdfReader pdfReader = new PdfReader(new ByteArrayInputStream(pdfBytes));
//						//System.out.println("Number of pages: " + pdfReader.getNumberOfPages());
//
//						int abstractPageNumber = findKeywordsInAbstract.findAbstractPageNumber(pdfReader, searchText);
//						System.out.println("Abstract page number: " + abstractPageNumber);
//
//						if (abstractPageNumber > 0) {
//							PdfReader pdfReaderForText = new PdfReader(new ByteArrayInputStream(pdfBytes));
//							String text = findKeywordsInAbstract.getAbstractText(pdfReaderForText, abstractPageNumber);
//							System.out.println("Abstract found on page " + abstractPageNumber + ":\n" + text);
//
//							List<String> keywords = findKeywordsInAbstract.extractKeywords(text);
//							System.out.println("Kulcsszavak: " + keywords);
//
//							if (!keywords.isEmpty()) {
//								finalyKeywords = String.join(", ", keywords);
//								System.out.println("Finaly Keywords (van): " + finalyKeywords);
//							} else {
//								finalyKeywords = "";
//								System.out.println("Finaly Keywords (nincs): " + finalyKeywords);
//							}
//
//
//						} else {
//							System.out.println("Abstract not found in the PDF.");
//						}
//
//						pdfReader.close();
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//
//
//					Thread.sleep(3000);
//					System.out.println("PDF file " + formattedName + " is smaller than 10MB. Uploading...");
//					diplomaServices.uploadDiplomaThesesPdfByCLR(pdfBytes, formattedName, topic, user_Name, year, finalyKeywords);
//				}
//			}
//
//			//System.out.println("PDF files found in the directory: " + pdfFiles.length);
//			System.out.println("CommandLineRunner running in the UnsplashApplication class...");
//		};
//	}

}
