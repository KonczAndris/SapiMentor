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


	// Informatika_2023 szakos diakok diplomamunka adatainak a CSV-be irasa
	// a CommandLineRunner interfesz implementalas segitsegevel
//	@Bean
//	public CommandLineRunner InformaticsGetDiplomaThesesData(DiplomaServices diplomaServices,
//													   findKeywordsInAbstract findKeywordsInAbstract,
//													   GPT3Service gpt3Service,
//													   findDiplomaThesesName findDiplomaThesesName,
//													   CSVFileWriter csvService) {
//		return args -> {
//			String pdfDirectoryPath = "src/main/resources/static/pdf";
//
//			// PDF-fájlok listázása a könyvtárban
//			File pdfDirectory = new File(pdfDirectoryPath);
//			File[] pdfFiles = pdfDirectory.listFiles((dir, name) -> name.toLowerCase().endsWith(".pdf"));
//
//			if (pdfFiles != null) {
//				List<String[]> data = new ArrayList<>();
//				for (File pdfFile : pdfFiles) {
//					String originalName = pdfFile.getName().replace(".pdf", "");
//					String cleanedName = findKeywordsInAbstract.cleanFileName(originalName);
//					String formattedName = findKeywordsInAbstract.formatName(cleanedName);
//					String year = "2023";
//					String finalyKeywords = null;
//					StringBuilder allKeywords = new StringBuilder();
//					String finalKeywordsString = null;
//
//					byte[] pdfBytes = Files.readAllBytes(pdfFile.toPath());
//
//					/// ez kell a csv-hez fontos
//
//					PdfReader pdfReaderCSV = new PdfReader(new ByteArrayInputStream(pdfBytes));
//					String searchName = "BACHELOR THESIS";
//					String searchName2 = "DIPLOMA THESIS";
//					String searchName3 = "Bachelor thesis";
//
//					int thesesNamePageNumber = findDiplomaThesesName.findThesesNamePageNumber(pdfReaderCSV, searchName);
//					if (thesesNamePageNumber == -1) {
//						PdfReader pdfReaderForPageNumber2 = new PdfReader(new ByteArrayInputStream(pdfBytes));
//						thesesNamePageNumber = findDiplomaThesesName.findThesesNamePageNumber(pdfReaderForPageNumber2, searchName2);
//					}
//					if (thesesNamePageNumber == -1){
//						PdfReader pdfReaderForPageNumber3 = new PdfReader(new ByteArrayInputStream(pdfBytes));
//						thesesNamePageNumber = findDiplomaThesesName.findThesesNamePageNumber(pdfReaderForPageNumber3, searchName3);
//					}
//					System.out.println("Theses name page number: " + thesesNamePageNumber);
//
//					PdfReader pdfReaderForName = new PdfReader(new ByteArrayInputStream(pdfBytes));
//					String thesesNamePageText = findDiplomaThesesName.getThesesNamePage(pdfReaderForName, thesesNamePageNumber);
//					//thesesName = thesesName.replaceAll("[\\r\\n]+", "");
//					System.out.println("Theses name page text: " + thesesNamePageText);
//
//					String thesesName = findDiplomaThesesName.findThesisName(thesesNamePageText);
//					System.out.println("Theses name: " + thesesName);
//
//					System.out.println("-----------------------------------------------------------------------------------\n");
//
//					/// idaig
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
//							String abstractTextForCSV = findKeywordsInAbstract.getAbstractText(pdfReaderForText, abstractPageNumber);
//
//							System.out.println("Keyword-al: " + abstractTextForCSV);
//							System.out.println("-----------------------------------------------------------------------------------\n");
//
//							// ide rakom a keyword kinyereset ha van
//							String keywordsForCSV = findKeywordsInAbstract.getKeywordsForCSV(abstractTextForCSV);
//							System.out.println("KeywordsCSV: " + keywordsForCSV);
//
//
//							String finalabstractText = findKeywordsInAbstract.removeKeywords(abstractTextForCSV);
//							//System.out.println("Keyword nelkul: " + finalabstractText);
//
//							System.out.println("-----------------------------------------------------------------------------------\n");
//							System.out.println("-----------------------------------------------------------------------------------\n");
//
//							// ez a CHAT-nek kell
//							// ide kell a + dolog
//							PdfReader pdfReaderForText2 = new PdfReader(new ByteArrayInputStream(pdfBytes));
//							// ez a sima
//							//String abstractText = "Get 5 or 3 keywords from this text: " + findKeywordsInAbstract.getAbstractText(pdfReaderForText2, abstractPageNumber);
//							String abstractText = "Get 5 or 3 keywords from this text: " + finalabstractText;
//							abstractText = abstractText.replaceAll("[\\r\\n]+", " ")
//									.replaceAll("\"", "'")
//									.replaceAll("-", " ");
//
//							System.out.println("Abstract text for ChatGPT: " + abstractText);
//
//							//////// Itt van a GPT3-as verzio ////////////
//							 String gpt3Response = gpt3Service.getKeywordsFromAbstractWithGPT3(abstractText);
//
//							// System.out.println("GPT-3 response: " + gpt3Response);
//							if(gpt3Response.contains("Error GPT-3 API")){
//								System.out.println("Error GPT-3 API");
//								continue;
//							} else {
//								// ide teszem be a gpt3 tol kapott valaszt
//								JSONObject gpt3JsonResponse = new JSONObject(gpt3Response);
//
//								// itt kiszedem a hasznos reszt a valaszbol
//								String gpt3Keywords = gpt3JsonResponse.getJSONArray("choices")
//										.getJSONObject(0)
//										.getJSONObject("message")
//										.getString("content");
//
//								/////// Idaig van a GPT3-as verzio //////////
//
//								// System.out.println("Keywords: " + gpt3Keywords);
//
//								String patternString = "^\\d+\\. (.+)";
//								Pattern pattern = Pattern.compile(patternString, Pattern.MULTILINE);
//
//								if (!gpt3Keywords.isEmpty()) {
//									Matcher matcher = pattern.matcher(gpt3Keywords);
//									while (matcher.find()){
//										allKeywords.append(matcher.group(1)).append(", ");
//									}
//									if(allKeywords.length() > 0){
//										System.out.println("GPT-3 keywords: " + allKeywords.toString());
//										finalyKeywords = allKeywords.toString();
//									} else {
//										finalKeywordsString = gpt3Keywords.replaceAll("Keywords:\\s*", "");
//										System.out.println("GPT-3 final keywords: " + finalKeywordsString);
//										finalyKeywords = finalKeywordsString;
//									}
//
//								} else {
//									allKeywords.append("No keywords found");
//									finalyKeywords = "No keywords found";
//								}
//
//							}
//							System.out.println("-----------------------------------------------------------------------------------\n");
//							System.out.println("-----------------------------------------------------------------------------------\n");
//							data.add(new String[]{formattedName, thesesName, finalabstractText, keywordsForCSV, finalyKeywords, year});
//						} else {
//							System.out.println("Abstract not found in the PDF.");
//						}
//
//
//						pdfReader.close();
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//					//Thread.sleep(30000);
//					Thread.sleep(23000);
//					//System.out.println("PDF file " + formattedName + " is smaller than 10MB. Uploading...");
//				}
//
//				csvService.writeDataToCSV(data, "/Users/szotyoricsongor-botond/Documents/GitHub/SapiMentor/master/code/Diploma_Theses_Data.csv");
//			}
//
//			//System.out.println("PDF files found in the directory: " + pdfFiles.length);
//			System.out.println("CommandLineRunner running in the UnsplashApplication class...");
//		};
//	}


	// Informatika_2023 szakos diakok diplomamunkainak feltoltese
	// a CommandLineRunner interfesz implementalas segitsegevel

//	@Bean
//	public CommandLineRunner InformaticsPDFUploadRunner(DiplomaServices diplomaServices,
//														findKeywordsInAbstract findKeywordsInAbstract,
//														GPT3Service gpt3Service) {
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
//					String topic = "Informatics";
//					String user_Name = "Szotyori Csongor";
//					String year = "2023";
//					String finalyKeywords = null;
//					StringBuilder allKeywords = new StringBuilder();
//					String finalKeywordsString = null;
//
//					byte[] pdfBytes = Files.readAllBytes(pdfFile.toPath());
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
//							String abstractTextForCSV = findKeywordsInAbstract.getAbstractText(pdfReaderForText, abstractPageNumber);
//
//							String finalabstractText = findKeywordsInAbstract.removeKeywords(abstractTextForCSV);
//							String abstractText = "Get 5 or 3 keywords from this text: " + finalabstractText;
//							abstractText = abstractText.replaceAll("[\\r\\n]+", " ")
//									.replaceAll("\"", "'")
//									.replaceAll("-", " ");
//
//							System.out.println("Abstract text for ChatGPT: " + abstractText);
//
//							//////// Itt van a GPT3-as verzio ////////////
//
//							String gpt3Response = gpt3Service.getKeywordsFromAbstractWithGPT3(abstractText);
//
//							//System.out.println("GPT-3 response: " + gpt3Response);
//							if(gpt3Response.contains("Error GPT-3 API")){
//								System.out.println("Error GPT-3 API");
//								finalyKeywords = findKeywordsInAbstract.extractKeywords(finalabstractText).toString();
//								//continue;
//							} else {
//								// ide teszem be a gpt3 tol kapott valaszt
//								JSONObject gpt3JsonResponse = new JSONObject(gpt3Response);
//
//								// itt kiszedem a hasznos reszt a valaszbol
//								String gpt3Keywords = gpt3JsonResponse.getJSONArray("choices")
//										.getJSONObject(0)
//										.getJSONObject("message")
//										.getString("content");
//
//								/////// Idaig van a GPT3-as verzio //////////
//
//								System.out.println("Keywords: " + gpt3Keywords);
//
//								String patternString = "^\\d+\\. (.+)";
//								Pattern pattern = Pattern.compile(patternString, Pattern.MULTILINE);
//
//								if (!gpt3Keywords.isEmpty()) {
//									Matcher matcher = pattern.matcher(gpt3Keywords);
//									while (matcher.find()){
//										allKeywords.append(matcher.group(1)).append(", ");
//									}
//									if(allKeywords.length() > 0){
//										System.out.println("GPT-3 keywords: " + allKeywords.toString());
//										finalyKeywords = allKeywords.toString();
//									} else {
//										finalKeywordsString = gpt3Keywords.replaceAll("Keywords:\\s*", "");
//										System.out.println("GPT-3 final keywords: " + finalKeywordsString);
//										System.out.println("-----------------------------------------------------------------------------------\n");
//										finalyKeywords = finalKeywordsString;
//									}
//
//								} else {
//									allKeywords.append("No keywords found");
//									finalyKeywords = "No keywords found";
//								}
//							}
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
//					Thread.sleep(23000);
//					System.out.println("PDF file " + formattedName + " is smaller than 10MB. Uploading...");
//					diplomaServices.uploadDiplomaThesesPdfByCLR(pdfBytes, formattedName, topic, user_Name, year, finalyKeywords);
//				}
//			}
//
//			//System.out.println("PDF files found in the directory: " + pdfFiles.length);
//			System.out.println("CommandLineRunner running in the UnsplashApplication class...");
//		};
//	}


	//////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////

	// Szamitastechnika_2023 szakos diakok diplomamunka adatainak a CSV-be irasa
	// a CommandLineRunner interfesz implementalas segitsegevel
//	@Bean
//	public CommandLineRunner EngineerGetDiplomaThesesData(DiplomaServices diplomaServices,
//															 findKeywordsInAbstract findKeywordsInAbstract,
//															 GPT3Service gpt3Service,
//															 findDiplomaThesesName findDiplomaThesesName,
//															 CSVFileWriter csvService) {
//		return args -> {
//			String pdfDirectoryPath = "src/main/resources/static/pdf";
//
//			// PDF-fájlok listázása a könyvtárban
//			File pdfDirectory = new File(pdfDirectoryPath);
//			File[] pdfFiles = pdfDirectory.listFiles((dir, name) -> name.toLowerCase().endsWith(".pdf"));
//
//			if (pdfFiles != null) {
//				List<String[]> data = new ArrayList<>();
//				for (File pdfFile : pdfFiles) {
//					String originalName = pdfFile.getName().replace(".pdf", "");
//					String cleanedName = findKeywordsInAbstract.cleanFileName(originalName);
//					String formattedName = findKeywordsInAbstract.formatName(cleanedName);
//					String year = "2023";
//					String finalyKeywords = null;
//					StringBuilder allKeywords = new StringBuilder();
//					String finalKeywordsString = null;
//
//					byte[] pdfBytes = Files.readAllBytes(pdfFile.toPath());
//
//					/// ez kell a csv-hez fontos
//
//					PdfReader pdfReaderCSV = new PdfReader(new ByteArrayInputStream(pdfBytes));
//					String searchName = "PROIECT DE DIPLOMĂ";
//					String searchName2 = "Extras";
//					String searchName3 = "LUCRARE DE DIPLOMAˇ";
//
//					int thesesNamePageNumber = findDiplomaThesesName.findThesesNamePageNumber(pdfReaderCSV, searchName);
//
//
//					if (thesesNamePageNumber == -1) {
//						PdfReader pdfReaderForPageNumber2 = new PdfReader(new ByteArrayInputStream(pdfBytes));
//						thesesNamePageNumber = findDiplomaThesesName.findThesesNamePageNumber(pdfReaderForPageNumber2, searchName2);
//					}
//					if (thesesNamePageNumber == -1){
//						PdfReader pdfReaderForPageNumber3 = new PdfReader(new ByteArrayInputStream(pdfBytes));
//						thesesNamePageNumber = findDiplomaThesesName.findThesesNamePageNumber(pdfReaderForPageNumber3, searchName3);
//					}
//					if(thesesNamePageNumber == -1) {
//						thesesNamePageNumber = 1;
//					}
//					System.out.println("Name: " + formattedName);
//					//System.out.println("Theses name page number: " + thesesNamePageNumber);
//
//					PdfReader pdfReaderForName = new PdfReader(new ByteArrayInputStream(pdfBytes));
//					String thesesNamePageText = findDiplomaThesesName.getThesesNamePage(pdfReaderForName, thesesNamePageNumber);
//					//thesesName = thesesName.replaceAll("[\\r\\n]+", "");
//					System.out.println("Theses name page text: " + thesesNamePageText);
//					//System.out.println("-----------------------------------------------------------------------------------\n");
//
//					String formattedText = findDiplomaThesesName.formatText(thesesNamePageText);
//					//System.out.println("Fromated thesis name: " + formattedText);
//
//					String thesesName = findDiplomaThesesName.findThesisNameFromCALC2023(thesesNamePageText);
//					if(thesesName.length() < formattedText.length()) {
//						thesesName = formattedText;
//					}
//					System.out.println("Theses name: " + thesesName);
//
//					//System.out.println("-----------------------------------------------------------------------------------\n");
//
//					/// idaig
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
//							String abstractTextForCSV = findKeywordsInAbstract.getAbstractText(pdfReaderForText, abstractPageNumber);
//
//							System.out.println("Keyword-al: " + abstractTextForCSV);
//							System.out.println("-----------------------------------------------------------------------------------\n");
//
//							// ide rakom a keyword kinyereset ha van
//							String keywordsForCSV = findKeywordsInAbstract.getKeywordsForCSV(abstractTextForCSV);
//							System.out.println("KeywordsCSV: " + keywordsForCSV);
//
//
//							String finalabstractText = findKeywordsInAbstract.removeKeywords(abstractTextForCSV);
//							//System.out.println("Keyword nelkul: " + finalabstractText);
//
//							System.out.println("-----------------------------------------------------------------------------------\n");
//							System.out.println("-----------------------------------------------------------------------------------\n");
//
//							// ez a CHAT-nek kell
//							// ide kell a + dolog
//							PdfReader pdfReaderForText2 = new PdfReader(new ByteArrayInputStream(pdfBytes));
//							// ez a sima
//							//String abstractText = "Get 5 or 3 keywords from this text: " + findKeywordsInAbstract.getAbstractText(pdfReaderForText2, abstractPageNumber);
//							String abstractText = "Get 5 or 3 keywords from this text: " + finalabstractText;
//							abstractText = abstractText.replaceAll("[\\r\\n]+", " ")
//									.replaceAll("\"", "'")
//									.replaceAll("-", " ");
//
//							System.out.println("Abstract text for ChatGPT: " + abstractText);
//
//							//////// Itt van a GPT3-as verzio ////////////
//							 String gpt3Response = gpt3Service.getKeywordsFromAbstractWithGPT3(abstractText);
//
//							// System.out.println("GPT-3 response: " + gpt3Response);
//							if(gpt3Response.contains("Error GPT-3 API")){
//								System.out.println("Error GPT-3 API");
//								continue;
//							} else {
//								// ide teszem be a gpt3 tol kapott valaszt
//								JSONObject gpt3JsonResponse = new JSONObject(gpt3Response);
//
//								// itt kiszedem a hasznos reszt a valaszbol
//								String gpt3Keywords = gpt3JsonResponse.getJSONArray("choices")
//										.getJSONObject(0)
//										.getJSONObject("message")
//										.getString("content");
//
//								/////// Idaig van a GPT3-as verzio //////////
//
//								// System.out.println("Keywords: " + gpt3Keywords);
//
//								String patternString = "^\\d+\\. (.+)";
//								Pattern pattern = Pattern.compile(patternString, Pattern.MULTILINE);
//
//								if (!gpt3Keywords.isEmpty()) {
//									Matcher matcher = pattern.matcher(gpt3Keywords);
//									while (matcher.find()){
//										allKeywords.append(matcher.group(1)).append(", ");
//									}
//									if(allKeywords.length() > 0){
//										System.out.println("GPT-3 keywords: " + allKeywords.toString());
//										finalyKeywords = allKeywords.toString();
//									} else {
//										finalKeywordsString = gpt3Keywords.replaceAll("Keywords:\\s*", "");
//										System.out.println("GPT-3 final keywords: " + finalKeywordsString);
//										finalyKeywords = finalKeywordsString;
//									}
//
//								} else {
//									allKeywords.append("No keywords found");
//									finalyKeywords = "No keywords found";
//								}
//
//							}
//							System.out.println("-----------------------------------------------------------------------------------\n");
//							System.out.println("-----------------------------------------------------------------------------------\n");
//							data.add(new String[]{formattedName, thesesName, finalabstractText, keywordsForCSV, finalyKeywords, year});
//						} else {
//							System.out.println("Abstract not found in the PDF.");
//						}
//
//
//						pdfReader.close();
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//					Thread.sleep(23000);
//					//Thread.sleep(2000);
//					//System.out.println("PDF file " + formattedName + " is smaller than 10MB. Uploading...");
//				}
//
//				csvService.writeDataToCSV(data, "/Users/szotyoricsongor-botond/Documents/GitHub/SapiMentor/master/code/Diploma_Theses_Data.csv");
//			}
//
//			//System.out.println("PDF files found in the directory: " + pdfFiles.length);
//			System.out.println("CommandLineRunner running in the UnsplashApplication class...");
//		};
//	}


//	 Szamitastechnika_2023 szakos diakok diplomamunkainak feltoltese
//	 a CommandLineRunner interfesz implementalas segitsegevel
//	@Bean
//	public CommandLineRunner CalculatoarePDFUploadRunner(DiplomaServices diplomaServices,
//														 findKeywordsInAbstract findKeywordsInAbstract,
//														 GPT3Service gpt3Service) {
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
//					StringBuilder allKeywords = new StringBuilder();
//					String finalKeywordsString = null;
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
//							String abstractTextForCSV = findKeywordsInAbstract.getAbstractText(pdfReaderForText, abstractPageNumber);
//
//							String finalabstractText = findKeywordsInAbstract.removeKeywords(abstractTextForCSV);
//
//							String abstractText = "Get 5 or 3 keywords from this text: " + finalabstractText;
//							abstractText = abstractText.replaceAll("[\\r\\n]+", " ")
//									.replaceAll("\"", "'")
//									.replaceAll("-", " ");
//
//							System.out.println("Abstract text for ChatGPT: " + abstractText);
//
//
//							//////// Itt van a GPT3-as verzio ////////////
//
//							String gpt3Response = gpt3Service.getKeywordsFromAbstractWithGPT3(abstractText);
//
//							//System.out.println("GPT-3 response: " + gpt3Response);
//							if(gpt3Response.contains("Error GPT-3 API")){
//								System.out.println("Error GPT-3 API");
//								finalyKeywords = findKeywordsInAbstract.extractKeywords(finalabstractText).toString();
//								//continue;
//							} else {
//								// ide teszem be a gpt3 tol kapott valaszt
//								JSONObject gpt3JsonResponse = new JSONObject(gpt3Response);
//
//								// itt kiszedem a hasznos reszt a valaszbol
//								String gpt3Keywords = gpt3JsonResponse.getJSONArray("choices")
//										.getJSONObject(0)
//										.getJSONObject("message")
//										.getString("content");
//
//								/////// Idaig van a GPT3-as verzio //////////
//
//								System.out.println("Keywords: " + gpt3Keywords);
//
//								String patternString = "^\\d+\\. (.+)";
//								Pattern pattern = Pattern.compile(patternString, Pattern.MULTILINE);
//
//								if (!gpt3Keywords.isEmpty()) {
//									Matcher matcher = pattern.matcher(gpt3Keywords);
//									while (matcher.find()){
//										allKeywords.append(matcher.group(1)).append(", ");
//									}
//									if(allKeywords.length() > 0){
//										System.out.println("GPT-3 keywords: " + allKeywords.toString());
//										finalyKeywords = allKeywords.toString();
//									} else {
//										finalKeywordsString = gpt3Keywords.replaceAll("Keywords:\\s*", "");
//										System.out.println("GPT-3 final keywords: " + finalKeywordsString);
//										finalyKeywords = finalKeywordsString;
//									}
//
//								} else {
//									allKeywords.append("No keywords found");
//									finalyKeywords = "No keywords found";
//								}
//							}
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
//					Thread.sleep(23000);
//					System.out.println("PDF file " + formattedName + " is smaller than 10MB. Uploading...");
//					diplomaServices.uploadDiplomaThesesPdfByCLR(pdfBytes, formattedName, topic, user_Name, year, finalyKeywords);
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
	//////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////

	// Informatika_2022 szakos diakok diplomamunka adatainak a CSV-be irasa
	// a CommandLineRunner interfesz implementalas segitsegevel
//		@Bean
//	public CommandLineRunner InformaticsGetDiplomaThesesData(DiplomaServices diplomaServices,
//															 findKeywordsInAbstract findKeywordsInAbstract,
//															 GPT3Service gpt3Service,
//															 findDiplomaThesesName findDiplomaThesesName,
//															 CSVFileWriter csvService) {
//		return args -> {
//			String pdfDirectoryPath = "src/main/resources/static/pdf";
//
//			// PDF-fájlok listázása a könyvtárban
//			File pdfDirectory = new File(pdfDirectoryPath);
//			File[] pdfFiles = pdfDirectory.listFiles((dir, name) -> name.toLowerCase().endsWith(".pdf"));
//
//			if (pdfFiles != null) {
//				List<String[]> data = new ArrayList<>();
//				for (File pdfFile : pdfFiles) {
//					String originalName = pdfFile.getName().replace(".pdf", "");
//					String cleanedName = findKeywordsInAbstract.cleanFileName(originalName);
//					String formattedName = findKeywordsInAbstract.formatName(cleanedName);
//					String year = "2022";
//					String finalyKeywords = null;
//					StringBuilder allKeywords = new StringBuilder();
//					String finalKeywordsString = null;
//
//					byte[] pdfBytes = Files.readAllBytes(pdfFile.toPath());
//
//					/// ez kell a csv-hez fontos
//
//					PdfReader pdfReaderCSV = new PdfReader(new ByteArrayInputStream(pdfBytes));
//					String searchName = "BACHELOR THESIS";
//					String searchName2 = "DIPLOMA THESIS";
//					String searchName3 = "Bachelor thesis";
//
//					int thesesNamePageNumber = findDiplomaThesesName.findThesesNamePageNumber(pdfReaderCSV, searchName);
//					if (thesesNamePageNumber == -1) {
//						PdfReader pdfReaderForPageNumber2 = new PdfReader(new ByteArrayInputStream(pdfBytes));
//						thesesNamePageNumber = findDiplomaThesesName.findThesesNamePageNumber(pdfReaderForPageNumber2, searchName2);
//					}
//					if (thesesNamePageNumber == -1){
//						PdfReader pdfReaderForPageNumber3 = new PdfReader(new ByteArrayInputStream(pdfBytes));
//						thesesNamePageNumber = findDiplomaThesesName.findThesesNamePageNumber(pdfReaderForPageNumber3, searchName3);
//					}
//					System.out.println("Theses name page number: " + thesesNamePageNumber);
//
//					PdfReader pdfReaderForName = new PdfReader(new ByteArrayInputStream(pdfBytes));
//					String thesesNamePageText = findDiplomaThesesName.getThesesNamePage(pdfReaderForName, thesesNamePageNumber);
//					//thesesName = thesesName.replaceAll("[\\r\\n]+", "");
//					System.out.println("Theses name page text: " + thesesNamePageText);
//
//					String thesesName = findDiplomaThesesName.findThesisName(thesesNamePageText);
//					System.out.println("Theses name: " + thesesName);
//
//					System.out.println("-----------------------------------------------------------------------------------\n");
//
//					/// idaig
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
//							String abstractTextForCSV = findKeywordsInAbstract.getAbstractText(pdfReaderForText, abstractPageNumber);
//
//							System.out.println("Keyword-al: " + abstractTextForCSV);
//							System.out.println("-----------------------------------------------------------------------------------\n");
//
//							// ide rakom a keyword kinyereset ha van
//							String keywordsForCSV = findKeywordsInAbstract.getKeywordsForCSV(abstractTextForCSV);
//							System.out.println("KeywordsCSV: " + keywordsForCSV);
//
//
//							String finalabstractText = findKeywordsInAbstract.removeKeywords(abstractTextForCSV);
//							//System.out.println("Keyword nelkul: " + finalabstractText);
//
//							System.out.println("-----------------------------------------------------------------------------------\n");
//							System.out.println("-----------------------------------------------------------------------------------\n");
//
//							// ez a CHAT-nek kell
//							// ide kell a + dolog
//							PdfReader pdfReaderForText2 = new PdfReader(new ByteArrayInputStream(pdfBytes));
//							// ez a sima
//							//String abstractText = "Get 5 or 3 keywords from this text: " + findKeywordsInAbstract.getAbstractText(pdfReaderForText2, abstractPageNumber);
//							String abstractText = "Get 5 or 3 keywords from this text: " + finalabstractText;
//							abstractText = abstractText.replaceAll("[\\r\\n]+", " ")
//									.replaceAll("\"", "'")
//									.replaceAll("-", " ");
//
//							System.out.println("Abstract text for ChatGPT: " + abstractText);
//
//							//////// Itt van a GPT3-as verzio ////////////
//							 String gpt3Response = gpt3Service.getKeywordsFromAbstractWithGPT3(abstractText);
//
//							// System.out.println("GPT-3 response: " + gpt3Response);
//							if(gpt3Response.contains("Error GPT-3 API")){
//								System.out.println("Error GPT-3 API");
//								continue;
//							} else {
//								// ide teszem be a gpt3 tol kapott valaszt
//								JSONObject gpt3JsonResponse = new JSONObject(gpt3Response);
//
//								// itt kiszedem a hasznos reszt a valaszbol
//								String gpt3Keywords = gpt3JsonResponse.getJSONArray("choices")
//										.getJSONObject(0)
//										.getJSONObject("message")
//										.getString("content");
//
//								/////// Idaig van a GPT3-as verzio //////////
//
//								// System.out.println("Keywords: " + gpt3Keywords);
//
//								String patternString = "^\\d+\\. (.+)";
//								Pattern pattern = Pattern.compile(patternString, Pattern.MULTILINE);
//
//								if (!gpt3Keywords.isEmpty()) {
//									Matcher matcher = pattern.matcher(gpt3Keywords);
//									while (matcher.find()){
//										allKeywords.append(matcher.group(1)).append(", ");
//									}
//									if(allKeywords.length() > 0){
//										System.out.println("GPT-3 keywords: " + allKeywords.toString());
//										finalyKeywords = allKeywords.toString();
//									} else {
//										finalKeywordsString = gpt3Keywords.replaceAll("Keywords:\\s*", "");
//										System.out.println("GPT-3 final keywords: " + finalKeywordsString);
//										finalyKeywords = finalKeywordsString;
//									}
//
//								} else {
//									allKeywords.append("No keywords found");
//									finalyKeywords = "No keywords found";
//								}
//
//							}
//							System.out.println("-----------------------------------------------------------------------------------\n");
//							System.out.println("-----------------------------------------------------------------------------------\n");
//							data.add(new String[]{formattedName, thesesName, finalabstractText, keywordsForCSV, finalyKeywords, year});
//						} else {
//							System.out.println("Abstract not found in the PDF.");
//						}
//
//
//						pdfReader.close();
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//					Thread.sleep(23000);
//					//Thread.sleep(2000);
//					//System.out.println("PDF file " + formattedName + " is smaller than 10MB. Uploading...");
//				}
//
//				csvService.writeDataToCSV(data, "/Users/szotyoricsongor-botond/Documents/GitHub/SapiMentor/master/code/Diploma_Theses_Data.csv");
//			}
//
//			//System.out.println("PDF files found in the directory: " + pdfFiles.length);
//			System.out.println("CommandLineRunner running in the UnsplashApplication class...");
//		};
//	}


	// Informatika_2022 szakos diakok diplomamunkainak feltoltese
	// a CommandLineRunner interfesz implementalas segitsegevel
//	@Bean
//	public CommandLineRunner InformaticPDFUploadRunner(DiplomaServices diplomaServices,
//													   findKeywordsInAbstract findKeywordsInAbstract,
//													   GPT3Service gpt3Service	) {
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
//					String topic = "Informatics";
//					String user_Name = "Szotyori Csongor";
//					String year = "2022";
//					String finalyKeywords = null;
//					StringBuilder allKeywords = new StringBuilder();
//					String finalKeywordsString = null;
//
//
////					if (pdfFile.length() > MAX_PDF_FILE_SIZE) {
////						System.out.println("PDF file " + formattedName + " is larger than 10MB. Skipping...");
////						continue;
////					}
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
//							String abstractTextForCSV = findKeywordsInAbstract.getAbstractText(pdfReaderForText, abstractPageNumber);
//
//							String finalabstractText = findKeywordsInAbstract.removeKeywords(abstractTextForCSV);
//
//							String abstractText = "Get 5 or 3 keywords from this text: " + finalabstractText;
//							abstractText = abstractText.replaceAll("[\\r\\n]+", " ")
//									.replaceAll("\"", "'")
//									.replaceAll("-", " ");
//
//							System.out.println("Abstract text for ChatGPT: " + abstractText);
//
//
//							//////// Itt van a GPT3-as verzio ////////////
//
//							String gpt3Response = gpt3Service.getKeywordsFromAbstractWithGPT3(abstractText);
//
//							//System.out.println("GPT-3 response: " + gpt3Response);
//							if(gpt3Response.contains("Error GPT-3 API")){
//								System.out.println("Error GPT-3 API");
//								finalyKeywords = findKeywordsInAbstract.extractKeywords(finalabstractText).toString();
//								// continue;
//							} else {
//								// ide teszem be a gpt3 tol kapott valaszt
//								JSONObject gpt3JsonResponse = new JSONObject(gpt3Response);
//
//								// itt kiszedem a hasznos reszt a valaszbol
//								String gpt3Keywords = gpt3JsonResponse.getJSONArray("choices")
//										.getJSONObject(0)
//										.getJSONObject("message")
//										.getString("content");
//
//								/////// Idaig van a GPT3-as verzio //////////
//
//								System.out.println("Keywords: " + gpt3Keywords);
//
//								String patternString = "^\\d+\\. (.+)";
//								Pattern pattern = Pattern.compile(patternString, Pattern.MULTILINE);
//
//								if (!gpt3Keywords.isEmpty()) {
//									Matcher matcher = pattern.matcher(gpt3Keywords);
//									while (matcher.find()){
//										allKeywords.append(matcher.group(1)).append(", ");
//									}
//									if(allKeywords.length() > 0){
//										System.out.println("GPT-3 keywords: " + allKeywords.toString());
//										finalyKeywords = allKeywords.toString();
//									} else {
//										finalKeywordsString = gpt3Keywords.replaceAll("Keywords:\\s*", "");
//										System.out.println("GPT-3 final keywords: " + finalKeywordsString);
//										finalyKeywords = finalKeywordsString;
//									}
//
//								} else {
//									allKeywords.append("No keywords found");
//									finalyKeywords = "No keywords found";
//								}
//							}
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
//					Thread.sleep(23000);
//					System.out.println("PDF file " + formattedName + " is smaller than 10MB. Uploading...");
//					diplomaServices.uploadDiplomaThesesPdfByCLR(pdfBytes, formattedName, topic, user_Name, year, finalyKeywords);
//				}
//			}
//
//			//System.out.println("PDF files found in the directory: " + pdfFiles.length);
//			System.out.println("CommandLineRunner running in the UnsplashApplication class...");
//		};
//	}

	//////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////

	// Szamitastechnika_2022 szakos diakok diplomamunka adatainak a CSV-be irasa
	// a CommandLineRunner interfesz implementalas segitsegevel
//	@Bean
//	public CommandLineRunner CalculatoareGetDiplomaThesesData(DiplomaServices diplomaServices,
//															  findKeywordsInAbstract findKeywordsInAbstract,
//															  GPT3Service gpt3Service,
//															  findDiplomaThesesName findDiplomaThesesName,
//															  CSVFileWriter csvService) {
//		return args -> {
//			String pdfDirectoryPath = "src/main/resources/static/pdf";
//
//			// PDF-fájlok listázása a könyvtárban
//			File pdfDirectory = new File(pdfDirectoryPath);
//			File[] pdfFiles = pdfDirectory.listFiles((dir, name) -> name.toLowerCase().endsWith(".pdf"));
//
//			if (pdfFiles != null) {
//				List<String[]> data = new ArrayList<>();
//				for (File pdfFile : pdfFiles) {
//					String originalName = pdfFile.getName().replace(".pdf", "");
//					String cleanedName = findKeywordsInAbstract.cleanFileName(originalName);
//					String formattedName = findKeywordsInAbstract.formatName_CALC2022(cleanedName);
//					String year = "2022";
//					String finalyKeywords = null;
//					StringBuilder allKeywords = new StringBuilder();
//					String finalKeywordsString = null;
//
//					byte[] pdfBytes = Files.readAllBytes(pdfFile.toPath());
//
//					/// ez kell a csv-hez fontos
//
//
//					PdfReader pdfReaderCSV = new PdfReader(new ByteArrayInputStream(pdfBytes));
//					String searchName = "BACHELOR THESIS";
//					String searchName2 = "DIPLOMA THESIS";
//					String searchName3 = "Bachelor thesis";
//					String searchName4 = "Diploma Thesis";
//					String searchName5 = "PROIECT DE DIPLOMĂ";
//					String searchName6 = "Extras";
//
//					int thesesNamePageNumber = findDiplomaThesesName.findThesesNamePageNumber(pdfReaderCSV, searchName);
//
//					if (thesesNamePageNumber == -1) {
//						PdfReader pdfReaderForPageNumber2 = new PdfReader(new ByteArrayInputStream(pdfBytes));
//						thesesNamePageNumber = findDiplomaThesesName.findThesesNamePageNumber(pdfReaderForPageNumber2, searchName2);
//					}
//					if (thesesNamePageNumber == -1){
//						PdfReader pdfReaderForPageNumber3 = new PdfReader(new ByteArrayInputStream(pdfBytes));
//						thesesNamePageNumber = findDiplomaThesesName.findThesesNamePageNumber(pdfReaderForPageNumber3, searchName3);
//					}
//					if (thesesNamePageNumber == -1){
//						PdfReader pdfReaderForPageNumber4 = new PdfReader(new ByteArrayInputStream(pdfBytes));
//						thesesNamePageNumber = findDiplomaThesesName.findThesesNamePageNumber(pdfReaderForPageNumber4, searchName4);
//					}
//
//					if (thesesNamePageNumber == -1){
//						PdfReader pdfReaderForPageNumber5 = new PdfReader(new ByteArrayInputStream(pdfBytes));
//						thesesNamePageNumber = findDiplomaThesesName.findThesesNamePageNumber(pdfReaderForPageNumber5, searchName5);
//					}
//					if (thesesNamePageNumber == -1) {
//						PdfReader pdfReaderForPageNumber6 = new PdfReader(new ByteArrayInputStream(pdfBytes));
//						thesesNamePageNumber = findDiplomaThesesName.findThesesNamePageNumber(pdfReaderForPageNumber6, searchName6);
//					}
//					System.out.println("User Name: " + formattedName);
//					System.out.println("Theses name page number: " + thesesNamePageNumber);
//
//					PdfReader pdfReaderForName = new PdfReader(new ByteArrayInputStream(pdfBytes));
//					String thesesNamePageText = findDiplomaThesesName.getThesesNamePage(pdfReaderForName, thesesNamePageNumber);
//					//thesesName = thesesName.replaceAll("[\\r\\n]+", "");
//					System.out.println("Theses name page text: " + thesesNamePageText);
//
//					String thesesName = findDiplomaThesesName.findThesisName(thesesNamePageText);
//					if (thesesName.isEmpty()) {
//						String formattedText = findDiplomaThesesName.formatText(thesesNamePageText);
//						//System.out.println("Fromated thesis name: " + formattedText);
//
//						thesesName = findDiplomaThesesName.findThesisNameFromCALC2023(thesesNamePageText);
//						if(thesesName.length() < formattedText.length()) {
//							thesesName = formattedText;
//						}
//						//System.out.println("Theses name: " + thesesName);
//					}
//					System.out.println("Theses name: " + thesesName);
//
//					System.out.println("-----------------------------------------------------------------------------------\n");
//
//					/// idaig
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
//							String abstractTextForCSV = findKeywordsInAbstract.getAbstractText(pdfReaderForText, abstractPageNumber);
//
//							System.out.println("Keyword-al: " + abstractTextForCSV);
//							System.out.println("-----------------------------------------------------------------------------------\n");
//
//							// ide rakom a keyword kinyereset ha van
//							String keywordsForCSV = findKeywordsInAbstract.getKeywordsForCSV(abstractTextForCSV);
//							System.out.println("KeywordsCSV: " + keywordsForCSV);
//
//
//							String finalabstractText = findKeywordsInAbstract.removeKeywords(abstractTextForCSV);
//							//System.out.println("Keyword nelkul: " + finalabstractText);
//
//							System.out.println("-----------------------------------------------------------------------------------\n");
//							System.out.println("-----------------------------------------------------------------------------------\n");
//
//							// ez a CHAT-nek kell
//							// ide kell a + dolog
//							//PdfReader pdfReaderForText2 = new PdfReader(new ByteArrayInputStream(pdfBytes));
//							// ez a sima
//							//String abstractText = "Get 5 or 3 keywords from this text: " + findKeywordsInAbstract.getAbstractText(pdfReaderForText2, abstractPageNumber);
//							String abstractText = "Get 5 or 3 keywords from this text: " + finalabstractText;
//							abstractText = abstractText.replaceAll("[\\r\\n]+", " ")
//									.replaceAll("\"", "'")
//									.replaceAll("-", " ");
//
//							System.out.println("Abstract text for ChatGPT: " + abstractText);
//
//							//////// Itt van a GPT3-as verzio ////////////
//							 String gpt3Response = gpt3Service.getKeywordsFromAbstractWithGPT3(abstractText);
//
//							// System.out.println("GPT-3 response: " + gpt3Response);
//							if(gpt3Response.contains("Error GPT-3 API")){
//								System.out.println("Error GPT-3 API");
//								continue;
//							} else {
//								// ide teszem be a gpt3 tol kapott valaszt
//								JSONObject gpt3JsonResponse = new JSONObject(gpt3Response);
//
//								// itt kiszedem a hasznos reszt a valaszbol
//								String gpt3Keywords = gpt3JsonResponse.getJSONArray("choices")
//										.getJSONObject(0)
//										.getJSONObject("message")
//										.getString("content");
//
//								/////// Idaig van a GPT3-as verzio //////////
//
//								// System.out.println("Keywords: " + gpt3Keywords);
//
//								String patternString = "^\\d+\\. (.+)";
//								Pattern pattern = Pattern.compile(patternString, Pattern.MULTILINE);
//
//								if (!gpt3Keywords.isEmpty()) {
//									Matcher matcher = pattern.matcher(gpt3Keywords);
//									while (matcher.find()){
//										allKeywords.append(matcher.group(1)).append(", ");
//									}
//									if(allKeywords.length() > 0){
//										System.out.println("GPT-3 keywords: " + allKeywords.toString());
//										finalyKeywords = allKeywords.toString();
//									} else {
//										finalKeywordsString = gpt3Keywords.replaceAll("Keywords:\\s*", "");
//										System.out.println("GPT-3 final keywords: " + finalKeywordsString);
//										finalyKeywords = finalKeywordsString;
//									}
//
//								} else {
//									allKeywords.append("No keywords found");
//									finalyKeywords = "No keywords found";
//								}
//
//							}
//							System.out.println("-----------------------------------------------------------------------------------\n");
//							System.out.println("-----------------------------------------------------------------------------------\n");
//							data.add(new String[]{formattedName, thesesName, finalabstractText, keywordsForCSV, finalyKeywords, year});
//						} else {
//							System.out.println("Abstract not found in the PDF.");
//						}
//
//
//						pdfReader.close();
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//					Thread.sleep(23000);
//					//Thread.sleep(2000);
//					//System.out.println("PDF file " + formattedName + " is smaller than 10MB. Uploading...");
//				}
//
//				csvService.writeDataToCSV(data, "/Users/szotyoricsongor-botond/Documents/GitHub/SapiMentor/master/code/Diploma_Theses_Data.csv");
//			}
//
//			//System.out.println("PDF files found in the directory: " + pdfFiles.length);
//			System.out.println("CommandLineRunner running in the UnsplashApplication class...");
//		};
//	}



	// Szamitastechnika_2022 szakos diakok diplomamunkainak feltoltese
	// a CommandLineRunner interfesz implementalas segitsegevel
//	@Bean
//	public CommandLineRunner CalculatoarePDFUploadRunner(DiplomaServices diplomaServices,
//														 findKeywordsInAbstract findKeywordsInAbstract,
//														 GPT3Service gpt3Service) {
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
//					String formattedName = findKeywordsInAbstract.formatName_CALC2022(cleanedName);
//					String topic = "Engineering";
//					String user_Name = "Koncz Andras";
//					String year = "2022";
//					String finalyKeywords = null;
//					StringBuilder allKeywords = new StringBuilder();
//					String finalKeywordsString = null;
//
//
////					if (pdfFile.length() > MAX_PDF_FILE_SIZE) {
////						System.out.println("PDF file " + formattedName + " is larger than 10MB. Skipping...");
////						continue;
////					}
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
//							String abstractTextForCSV = findKeywordsInAbstract.getAbstractText(pdfReaderForText, abstractPageNumber);
//
//							String finalabstractText = findKeywordsInAbstract.removeKeywords(abstractTextForCSV);
//
//							String abstractText = "Get 5 or 3 keywords from this text: " + finalabstractText;
//							abstractText = abstractText.replaceAll("[\\r\\n]+", " ")
//									.replaceAll("\"", "'")
//									.replaceAll("-", " ");
//
//							System.out.println("Abstract text for ChatGPT: " + abstractText);
//
//							//////// Itt van a GPT3-as verzio ////////////
//
//							String gpt3Response = gpt3Service.getKeywordsFromAbstractWithGPT3(abstractText);
//
//							//System.out.println("GPT-3 response: " + gpt3Response);
//							if(gpt3Response.contains("Error GPT-3 API")){
//								System.out.println("Error GPT-3 API");
//								finalyKeywords = findKeywordsInAbstract.extractKeywords(finalabstractText).toString();
//								// continue;
//							} else {
//								// ide teszem be a gpt3 tol kapott valaszt
//								JSONObject gpt3JsonResponse = new JSONObject(gpt3Response);
//
//								// itt kiszedem a hasznos reszt a valaszbol
//								String gpt3Keywords = gpt3JsonResponse.getJSONArray("choices")
//										.getJSONObject(0)
//										.getJSONObject("message")
//										.getString("content");
//
//								/////// Idaig van a GPT3-as verzio //////////
//
//								System.out.println("Keywords: " + gpt3Keywords);
//
//								String patternString = "^\\d+\\. (.+)";
//								Pattern pattern = Pattern.compile(patternString, Pattern.MULTILINE);
//
//								if (!gpt3Keywords.isEmpty()) {
//									Matcher matcher = pattern.matcher(gpt3Keywords);
//									while (matcher.find()){
//										allKeywords.append(matcher.group(1)).append(", ");
//									}
//									if(allKeywords.length() > 0){
//										System.out.println("GPT-3 keywords: " + allKeywords.toString());
//										finalyKeywords = allKeywords.toString();
//									} else {
//										finalKeywordsString = gpt3Keywords.replaceAll("Keywords:\\s*", "");
//										System.out.println("GPT-3 final keywords: " + finalKeywordsString);
//										finalyKeywords = finalKeywordsString;
//									}
//
//								} else {
//									allKeywords.append("No keywords found");
//									finalyKeywords = "No keywords found";
//								}
//							}
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
//					Thread.sleep(23000);
//					System.out.println("PDF file " + formattedName + " is smaller than 10MB. Uploading...");
//					diplomaServices.uploadDiplomaThesesPdfByCLR(pdfBytes, formattedName, topic, user_Name, year, finalyKeywords);
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
	//////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////


	// Informatika_2021 szakos diakok diplomamunka adatainak a CSV-be irasa
	// a CommandLineRunner interfesz implementalas segitsegevel
//	@Bean
//	public CommandLineRunner InformaticsGetDiplomaThesesData(DiplomaServices diplomaServices,
//															 findKeywordsInAbstract findKeywordsInAbstract,
//															 GPT3Service gpt3Service,
//															 findDiplomaThesesName findDiplomaThesesName,
//															 CSVFileWriter csvService) {
//		return args -> {
//			String pdfDirectoryPath = "src/main/resources/static/pdf";
//
//			// PDF-fájlok listázása a könyvtárban
//			File pdfDirectory = new File(pdfDirectoryPath);
//			File[] pdfFiles = pdfDirectory.listFiles((dir, name) -> name.toLowerCase().endsWith(".pdf"));
//
//			if (pdfFiles != null) {
//				List<String[]> data = new ArrayList<>();
//				for (File pdfFile : pdfFiles) {
//					String originalName = pdfFile.getName().replace(".pdf", "");
//					String cleanedName = findKeywordsInAbstract.cleanFileName(originalName);
//					String formattedName = findKeywordsInAbstract.formatName_INF2021(cleanedName);
//					String year = "2021";
//					String finalyKeywords = null;
//					StringBuilder allKeywords = new StringBuilder();
//					String finalKeywordsString = null;
//
//					byte[] pdfBytes = Files.readAllBytes(pdfFile.toPath());
//
//					/// ez kell a csv-hez fontos
//
//					PdfReader pdfReaderCSV = new PdfReader(new ByteArrayInputStream(pdfBytes));
//					String searchName = "BACHELOR THESIS";
//					String searchName2 = "DIPLOMA THESIS";
//					String searchName3 = "Bachelor thesis";
//
//					System.out.println("User Name: " + formattedName);
//
//					int thesesNamePageNumber = findDiplomaThesesName.findThesesNamePageNumber(pdfReaderCSV, searchName);
//					if (thesesNamePageNumber == -1) {
//						PdfReader pdfReaderForPageNumber2 = new PdfReader(new ByteArrayInputStream(pdfBytes));
//						thesesNamePageNumber = findDiplomaThesesName.findThesesNamePageNumber(pdfReaderForPageNumber2, searchName2);
//					}
//					if (thesesNamePageNumber == -1){
//						PdfReader pdfReaderForPageNumber3 = new PdfReader(new ByteArrayInputStream(pdfBytes));
//						thesesNamePageNumber = findDiplomaThesesName.findThesesNamePageNumber(pdfReaderForPageNumber3, searchName3);
//					}
//					System.out.println("Theses name page number: " + thesesNamePageNumber);
//
//					PdfReader pdfReaderForName = new PdfReader(new ByteArrayInputStream(pdfBytes));
//					String thesesNamePageText = findDiplomaThesesName.getThesesNamePage(pdfReaderForName, thesesNamePageNumber);
//					//thesesName = thesesName.replaceAll("[\\r\\n]+", "");
//					System.out.println("Theses name page text: " + thesesNamePageText);
//
//					String thesesName = findDiplomaThesesName.findThesisName(thesesNamePageText);
//					System.out.println("Theses name: " + thesesName);
//
//					System.out.println("-----------------------------------------------------------------------------------\n");
//
//					/// idaig
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
//							String abstractTextForCSV = findKeywordsInAbstract.getAbstractText(pdfReaderForText, abstractPageNumber);
//
//							System.out.println("Keyword-al: " + abstractTextForCSV);
//							System.out.println("-----------------------------------------------------------------------------------\n");
//
//							// ide rakom a keyword kinyereset ha van
//							String keywordsForCSV = findKeywordsInAbstract.getKeywordsForCSV(abstractTextForCSV);
//							System.out.println("KeywordsCSV: " + keywordsForCSV);
//
//
//							String finalabstractText = findKeywordsInAbstract.removeKeywords(abstractTextForCSV);
//							//System.out.println("Keyword nelkul: " + finalabstractText);
//
//							System.out.println("-----------------------------------------------------------------------------------\n");
//							System.out.println("-----------------------------------------------------------------------------------\n");
//
//							// ez a CHAT-nek kell
//							// ide kell a + dolog
//							PdfReader pdfReaderForText2 = new PdfReader(new ByteArrayInputStream(pdfBytes));
//							// ez a sima
//							//String abstractText = "Get 5 or 3 keywords from this text: " + findKeywordsInAbstract.getAbstractText(pdfReaderForText2, abstractPageNumber);
//							String abstractText = "Get 5 or 3 keywords from this text: " + finalabstractText;
//							abstractText = abstractText.replaceAll("[\\r\\n]+", " ")
//									.replaceAll("\"", "'")
//									.replaceAll("-", " ");
//
//							System.out.println("Abstract text for ChatGPT: " + abstractText);
//
//							//////// Itt van a GPT3-as verzio ////////////
//							 String gpt3Response = gpt3Service.getKeywordsFromAbstractWithGPT3(abstractText);
//
//							// System.out.println("GPT-3 response: " + gpt3Response);
//							if(gpt3Response.contains("Error GPT-3 API")){
//								System.out.println("Error GPT-3 API");
//								continue;
//							} else {
//								// ide teszem be a gpt3 tol kapott valaszt
//								JSONObject gpt3JsonResponse = new JSONObject(gpt3Response);
//
//								// itt kiszedem a hasznos reszt a valaszbol
//								String gpt3Keywords = gpt3JsonResponse.getJSONArray("choices")
//										.getJSONObject(0)
//										.getJSONObject("message")
//										.getString("content");
//
//								/////// Idaig van a GPT3-as verzio //////////
//
//								// System.out.println("Keywords: " + gpt3Keywords);
//
//								String patternString = "^\\d+\\. (.+)";
//								Pattern pattern = Pattern.compile(patternString, Pattern.MULTILINE);
//
//								if (!gpt3Keywords.isEmpty()) {
//									Matcher matcher = pattern.matcher(gpt3Keywords);
//									while (matcher.find()){
//										allKeywords.append(matcher.group(1)).append(", ");
//									}
//									if(allKeywords.length() > 0){
//										System.out.println("GPT-3 keywords: " + allKeywords.toString());
//										finalyKeywords = allKeywords.toString();
//									} else {
//										finalKeywordsString = gpt3Keywords.replaceAll("Keywords:\\s*", "");
//										System.out.println("GPT-3 final keywords: " + finalKeywordsString);
//										finalyKeywords = finalKeywordsString;
//									}
//
//								} else {
//									allKeywords.append("No keywords found");
//									finalyKeywords = "No keywords found";
//								}
//
//							}
//							System.out.println("-----------------------------------------------------------------------------------\n");
//							System.out.println("-----------------------------------------------------------------------------------\n");
//							data.add(new String[]{formattedName, thesesName, finalabstractText, keywordsForCSV, finalyKeywords, year});
//						} else {
//							System.out.println("Abstract not found in the PDF.");
//						}
//
//
//						pdfReader.close();
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//					Thread.sleep(23000);
//					//Thread.sleep(2000);
//					//System.out.println("PDF file " + formattedName + " is smaller than 10MB. Uploading...");
//				}
//
//				csvService.writeDataToCSV(data, "/Users/szotyoricsongor-botond/Documents/GitHub/SapiMentor/master/code/Diploma_Theses_Data.csv");
//			}
//
//			//System.out.println("PDF files found in the directory: " + pdfFiles.length);
//			System.out.println("CommandLineRunner running in the UnsplashApplication class...");
//		};
//	}


	// Informatika_2021 szakos diakok diplomamunkainak feltoltese
	// a CommandLineRunner interfesz implementalas segitsegevel
//	@Bean
//	public CommandLineRunner InformaticPDFUploadRunner(DiplomaServices diplomaServices
//														, findKeywordsInAbstract findKeywordsInAbstract,
//													   GPT3Service gpt3Service) {
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
//					String formattedName = findKeywordsInAbstract.formatName_INF2021(cleanedName);
//					String topic = "Informatics";
//					String user_Name = "Szotyori Csongor";
//					String year = "2021";
//					String finalyKeywords = null;
//					StringBuilder allKeywords = new StringBuilder();
//					String finalKeywordsString = null;
//
//
////					if (pdfFile.length() > MAX_PDF_FILE_SIZE) {
////						System.out.println("PDF file " + formattedName + " is larger than 10MB. Skipping...");
////						continue;
////					}
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
//							String abstractTextForCSV = findKeywordsInAbstract.getAbstractText(pdfReaderForText, abstractPageNumber);
//
//							String finalabstractText = findKeywordsInAbstract.removeKeywords(abstractTextForCSV);
//
//							String abstractText = "Get 5 or 3 keywords from this text: " + finalabstractText;
//							abstractText = abstractText.replaceAll("[\\r\\n]+", " ")
//									.replaceAll("\"", "'")
//									.replaceAll("-", " ");
//
//							System.out.println("Abstract text for ChatGPT: " + abstractText);
//
//							//////// Itt van a GPT3-as verzio ////////////
//
//							String gpt3Response = gpt3Service.getKeywordsFromAbstractWithGPT3(abstractText);
//
//							//System.out.println("GPT-3 response: " + gpt3Response);
//							if(gpt3Response.contains("Error GPT-3 API")){
//								System.out.println("Error GPT-3 API");
//								finalyKeywords = findKeywordsInAbstract.extractKeywords(finalabstractText).toString();
//								// continue;
//							} else {
//								// ide teszem be a gpt3 tol kapott valaszt
//								JSONObject gpt3JsonResponse = new JSONObject(gpt3Response);
//
//								// itt kiszedem a hasznos reszt a valaszbol
//								String gpt3Keywords = gpt3JsonResponse.getJSONArray("choices")
//										.getJSONObject(0)
//										.getJSONObject("message")
//										.getString("content");
//
//								/////// Idaig van a GPT3-as verzio //////////
//
//								System.out.println("Keywords: " + gpt3Keywords);
//
//								String patternString = "^\\d+\\. (.+)";
//								Pattern pattern = Pattern.compile(patternString, Pattern.MULTILINE);
//
//								if (!gpt3Keywords.isEmpty()) {
//									Matcher matcher = pattern.matcher(gpt3Keywords);
//									while (matcher.find()){
//										allKeywords.append(matcher.group(1)).append(", ");
//									}
//									if(allKeywords.length() > 0){
//										System.out.println("GPT-3 keywords: " + allKeywords.toString());
//										finalyKeywords = allKeywords.toString();
//									} else {
//										finalKeywordsString = gpt3Keywords.replaceAll("Keywords:\\s*", "");
//										System.out.println("GPT-3 final keywords: " + finalKeywordsString);
//										finalyKeywords = finalKeywordsString;
//									}
//
//								} else {
//									allKeywords.append("No keywords found");
//									finalyKeywords = "No keywords found";
//								}
//							}
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
//					Thread.sleep(23000);
//					System.out.println("PDF file " + formattedName + " is smaller than 10MB. Uploading...");
//					diplomaServices.uploadDiplomaThesesPdfByCLR(pdfBytes, formattedName, topic, user_Name, year, finalyKeywords);
//				}
//			}
//
//			//System.out.println("PDF files found in the directory: " + pdfFiles.length);
//			System.out.println("CommandLineRunner running in the UnsplashApplication class...");
//		};
//	}



	//////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////

	// Szamitastechnika_2021 szakos diakok diplomamunka adatainak a CSV-be irasa
	// a CommandLineRunner interfesz implementalas segitsegevel
//	@Bean
//	public CommandLineRunner CalculatoareGetDiplomaThesesData(DiplomaServices diplomaServices,
//															  findKeywordsInAbstract findKeywordsInAbstract,
//															  GPT3Service gpt3Service,
//															  findDiplomaThesesName findDiplomaThesesName,
//															  CSVFileWriter csvService) {
//		return args -> {
//			String pdfDirectoryPath = "src/main/resources/static/pdf";
//
//			// PDF-fájlok listázása a könyvtárban
//			File pdfDirectory = new File(pdfDirectoryPath);
//			File[] pdfFiles = pdfDirectory.listFiles((dir, name) -> name.toLowerCase().endsWith(".pdf"));
//
//			if (pdfFiles != null) {
//				List<String[]> data = new ArrayList<>();
//				for (File pdfFile : pdfFiles) {
//					String originalName = pdfFile.getName().replace(".pdf", "");
//					String cleanedName = findKeywordsInAbstract.cleanFileName(originalName);
//					String formattedName = findKeywordsInAbstract.formatName_CALC2021(cleanedName);
//					String year = "2022";
//					String finalyKeywords = null;
//					StringBuilder allKeywords = new StringBuilder();
//					String finalKeywordsString = null;
//
//					byte[] pdfBytes = Files.readAllBytes(pdfFile.toPath());
//
//					/// ez kell a csv-hez fontos
//
//
//					PdfReader pdfReaderCSV = new PdfReader(new ByteArrayInputStream(pdfBytes));
//					String searchName = "BACHELOR THESIS";
//					String searchName2 = "DIPLOMA THESIS";
//					String searchName3 = "Bachelor thesis";
//					String searchName4 = "Diploma Thesis";
//					String searchName5 = "PROIECT DE DIPLOMĂ";
//					String searchName6 = "Extras";
//
//					int thesesNamePageNumber = findDiplomaThesesName.findThesesNamePageNumber(pdfReaderCSV, searchName);
//
//					if (thesesNamePageNumber == -1) {
//						PdfReader pdfReaderForPageNumber2 = new PdfReader(new ByteArrayInputStream(pdfBytes));
//						thesesNamePageNumber = findDiplomaThesesName.findThesesNamePageNumber(pdfReaderForPageNumber2, searchName2);
//					}
//					if (thesesNamePageNumber == -1){
//						PdfReader pdfReaderForPageNumber3 = new PdfReader(new ByteArrayInputStream(pdfBytes));
//						thesesNamePageNumber = findDiplomaThesesName.findThesesNamePageNumber(pdfReaderForPageNumber3, searchName3);
//					}
//					if (thesesNamePageNumber == -1){
//						PdfReader pdfReaderForPageNumber4 = new PdfReader(new ByteArrayInputStream(pdfBytes));
//						thesesNamePageNumber = findDiplomaThesesName.findThesesNamePageNumber(pdfReaderForPageNumber4, searchName4);
//					}
//
//					if (thesesNamePageNumber == -1){
//						PdfReader pdfReaderForPageNumber5 = new PdfReader(new ByteArrayInputStream(pdfBytes));
//						thesesNamePageNumber = findDiplomaThesesName.findThesesNamePageNumber(pdfReaderForPageNumber5, searchName5);
//					}
//					if (thesesNamePageNumber == -1) {
//						PdfReader pdfReaderForPageNumber6 = new PdfReader(new ByteArrayInputStream(pdfBytes));
//						thesesNamePageNumber = findDiplomaThesesName.findThesesNamePageNumber(pdfReaderForPageNumber6, searchName6);
//					}
//					System.out.println("User Name: " + formattedName);
//					System.out.println("Theses name page number: " + thesesNamePageNumber);
//
//					PdfReader pdfReaderForName = new PdfReader(new ByteArrayInputStream(pdfBytes));
//					String thesesNamePageText = findDiplomaThesesName.getThesesNamePage(pdfReaderForName, thesesNamePageNumber);
//					//thesesName = thesesName.replaceAll("[\\r\\n]+", "");
//					System.out.println("Theses name page text: " + thesesNamePageText);
//
//					String thesesName = findDiplomaThesesName.findThesisName(thesesNamePageText);
//					if (thesesName.isEmpty()) {
//						String formattedText = null;
//						if (formattedName.equals("Dobri Ervinlucrare")){
//							formattedText = findDiplomaThesesName.formatTextForCALC2021ForDobriErvin(thesesNamePageText);
//						}else if (formattedName.equals("Benedek Szabolcs")){
//							formattedText = findDiplomaThesesName.formatTextForCALC2021ForBenedekSzab(thesesNamePageText);
//						}else {
//							formattedText = findDiplomaThesesName.formatTextForCALC2021(thesesNamePageText);
//						}
//
//
//						//System.out.println("Fromated thesis name: " + formattedText);
//
//						thesesName = findDiplomaThesesName.findThesisNameFromCALC2023(thesesNamePageText);
//						if(thesesName.length() < formattedText.length()) {
//							thesesName = formattedText;
//						}
//						//System.out.println("Theses name: " + thesesName);
//					}
//					System.out.println("Theses name: " + thesesName);
//
//					System.out.println("-----------------------------------------------------------------------------------\n");
//
//					/// idaig
//
//					try {
//						String searchText = "Abstract";
//
//						System.out.println("Search text: " + searchText);
//
//						PdfReader pdfReader = new PdfReader(new ByteArrayInputStream(pdfBytes));
//						//System.out.println("Number of pages: " + pdfReader.getNumberOfPages());
//						int abstractPageNumber = 0;
//						if (formattedName.equals("Benedek Szabolcs")) {
//							abstractPageNumber = findKeywordsInAbstract.findAbstractPageNumberForBenedekSzab2021CALC(pdfReader, searchText);
//						} else {
//							abstractPageNumber = findKeywordsInAbstract.findAbstractPageNumber(pdfReader, searchText);
//						}
//
//						System.out.println("Abstract page number: " + abstractPageNumber);
//
//						if (abstractPageNumber > 0) {
//							PdfReader pdfReaderForText = new PdfReader(new ByteArrayInputStream(pdfBytes));
//							String abstractTextForCSV = findKeywordsInAbstract.getAbstractText(pdfReaderForText, abstractPageNumber);
//
//							System.out.println("Keyword-al: " + abstractTextForCSV);
//							System.out.println("-----------------------------------------------------------------------------------\n");
//
//							// ide rakom a keyword kinyereset ha van
//							String keywordsForCSV = findKeywordsInAbstract.getKeywordsForCSV(abstractTextForCSV);
//							System.out.println("KeywordsCSV: " + keywordsForCSV);
//
//
//							String finalabstractText = findKeywordsInAbstract.removeKeywords(abstractTextForCSV);
//							//System.out.println("Keyword nelkul: " + finalabstractText);
//
//							System.out.println("-----------------------------------------------------------------------------------\n");
//							System.out.println("-----------------------------------------------------------------------------------\n");
//
//							// ez a CHAT-nek kell
//							// ide kell a + dolog
//							//PdfReader pdfReaderForText2 = new PdfReader(new ByteArrayInputStream(pdfBytes));
//							// ez a sima
//							//String abstractText = "Get 5 or 3 keywords from this text: " + findKeywordsInAbstract.getAbstractText(pdfReaderForText2, abstractPageNumber);
//							String abstractText = "Get 5 or 3 keywords from this text: " + finalabstractText;
//							abstractText = abstractText.replaceAll("[\\r\\n]+", " ")
//									.replaceAll("\"", "'")
//									.replaceAll("-", " ");
//
//							System.out.println("Abstract text for ChatGPT: " + abstractText);
//
//							//////// Itt van a GPT3-as verzio ////////////
//							 String gpt3Response = gpt3Service.getKeywordsFromAbstractWithGPT3(abstractText);
//
//							// System.out.println("GPT-3 response: " + gpt3Response);
//							if(gpt3Response.contains("Error GPT-3 API")){
//								System.out.println("Error GPT-3 API");
//								continue;
//							} else {
//								// ide teszem be a gpt3 tol kapott valaszt
//								JSONObject gpt3JsonResponse = new JSONObject(gpt3Response);
//
//								// itt kiszedem a hasznos reszt a valaszbol
//								String gpt3Keywords = gpt3JsonResponse.getJSONArray("choices")
//										.getJSONObject(0)
//										.getJSONObject("message")
//										.getString("content");
//
//								/////// Idaig van a GPT3-as verzio //////////
//
//								// System.out.println("Keywords: " + gpt3Keywords);
//
//								String patternString = "^\\d+\\. (.+)";
//								Pattern pattern = Pattern.compile(patternString, Pattern.MULTILINE);
//
//								if (!gpt3Keywords.isEmpty()) {
//									Matcher matcher = pattern.matcher(gpt3Keywords);
//									while (matcher.find()){
//										allKeywords.append(matcher.group(1)).append(", ");
//									}
//									if(allKeywords.length() > 0){
//										System.out.println("GPT-3 keywords: " + allKeywords.toString());
//										finalyKeywords = allKeywords.toString();
//									} else {
//										finalKeywordsString = gpt3Keywords.replaceAll("Keywords:\\s*", "");
//										System.out.println("GPT-3 final keywords: " + finalKeywordsString);
//										finalyKeywords = finalKeywordsString;
//									}
//
//								} else {
//									allKeywords.append("No keywords found");
//									finalyKeywords = "No keywords found";
//								}
//
//							}
//							System.out.println("-----------------------------------------------------------------------------------\n");
//							System.out.println("-----------------------------------------------------------------------------------\n");
//							data.add(new String[]{formattedName, thesesName, finalabstractText, keywordsForCSV, finalyKeywords, year});
//						} else {
//							System.out.println("Abstract not found in the PDF.");
//						}
//
//
//						pdfReader.close();
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//					Thread.sleep(23000);
//					//Thread.sleep(2000);
//					//System.out.println("PDF file " + formattedName + " is smaller than 10MB. Uploading...");
//				}
//
//				csvService.writeDataToCSV(data, "/Users/szotyoricsongor-botond/Documents/GitHub/SapiMentor/master/code/Diploma_Theses_Data.csv");
//			}
//
//			//System.out.println("PDF files found in the directory: " + pdfFiles.length);
//			System.out.println("CommandLineRunner running in the UnsplashApplication class...");
//		};
//	}


	// Szamitastechnika_2021 szakos diakok diplomamunkainak feltoltese
	// a CommandLineRunner interfesz implementalas segitsegevel
//	@Bean
//	public CommandLineRunner CalculatoarePDFUploadRunner(DiplomaServices diplomaServices
//														, findKeywordsInAbstract findKeywordsInAbstract,
//														 GPT3Service gpt3Service) {
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
//					String formattedName = findKeywordsInAbstract.formatName_CALC2021(cleanedName);
//					String topic = "Engineering";
//					String user_Name = "Koncz Andras";
//					String year = "2021";
//					String finalyKeywords = null;
//					StringBuilder allKeywords = new StringBuilder();
//					String finalKeywordsString = null;
//
//
////					if (pdfFile.length() > MAX_PDF_FILE_SIZE) {
////						System.out.println("PDF file " + formattedName + " is larger than 10MB. Skipping...");
////						continue;
////					}
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
//							String abstractTextForCSV = findKeywordsInAbstract.getAbstractText(pdfReaderForText, abstractPageNumber);
//
//							String finalabstractText = findKeywordsInAbstract.removeKeywords(abstractTextForCSV);
//
//							String abstractText = "Get 5 or 3 keywords from this text: " + finalabstractText;
//							abstractText = abstractText.replaceAll("[\\r\\n]+", " ")
//									.replaceAll("\"", "'")
//									.replaceAll("-", " ");
//
//							//////// Itt van a GPT3-as verzio ////////////
//
//							String gpt3Response = gpt3Service.getKeywordsFromAbstractWithGPT3(abstractText);
//
//							//System.out.println("GPT-3 response: " + gpt3Response);
//							if(gpt3Response.contains("Error GPT-3 API")){
//								System.out.println("Error GPT-3 API");
//								finalyKeywords = findKeywordsInAbstract.extractKeywords(finalabstractText).toString();
//								// continue;
//							} else {
//								// ide teszem be a gpt3 tol kapott valaszt
//								JSONObject gpt3JsonResponse = new JSONObject(gpt3Response);
//
//								// itt kiszedem a hasznos reszt a valaszbol
//								String gpt3Keywords = gpt3JsonResponse.getJSONArray("choices")
//										.getJSONObject(0)
//										.getJSONObject("message")
//										.getString("content");
//
//								/////// Idaig van a GPT3-as verzio //////////
//
//								System.out.println("Keywords: " + gpt3Keywords);
//
//								String patternString = "^\\d+\\. (.+)";
//								Pattern pattern = Pattern.compile(patternString, Pattern.MULTILINE);
//
//								if (!gpt3Keywords.isEmpty()) {
//									Matcher matcher = pattern.matcher(gpt3Keywords);
//									while (matcher.find()){
//										allKeywords.append(matcher.group(1)).append(", ");
//									}
//									if(allKeywords.length() > 0){
//										System.out.println("GPT-3 keywords: " + allKeywords.toString());
//										finalyKeywords = allKeywords.toString();
//									} else {
//										finalKeywordsString = gpt3Keywords.replaceAll("Keywords:\\s*", "");
//										System.out.println("GPT-3 final keywords: " + finalKeywordsString);
//										finalyKeywords = finalKeywordsString;
//									}
//
//								} else {
//									allKeywords.append("No keywords found");
//									finalyKeywords = "No keywords found";
//								}
//							}
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
//					Thread.sleep(23000);
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
