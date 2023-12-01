package ro.sapientia.diploma_demo.Sapimentor_Demo_Project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class SapimentorDemoProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(SapimentorDemoProjectApplication.class, args);
		System.out.println("Run Successfully!");
	}

	private static final Long MAX_PDF_FILE_SIZE = 10 * 1024 * 1024L; // 10 MB

	// Informatika szakos diakok diplomamunkainak feltoltese
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


	// Szamitastechnika szakos diakok diplomamunkainak feltoltese
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

}
