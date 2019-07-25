package com.discount.calculator;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Optional;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.discount.calculator.enums.ValidType;
import com.discount.calculator.service.DiscountCalculatorService;

@SpringBootApplication
@EnableScheduling
public class CalculatorApplication implements CommandLineRunner {

	private static final Logger LOGGER = LoggerFactory.getLogger(CalculatorApplication.class);

	@Value("${excel.file.location}")
	private String excelPath;

	@Autowired
	private DiscountCalculatorService service;

	public static void main(String[] args) {
		SpringApplication.run(CalculatorApplication.class, args);
	}

	/**
	 * Overriding the run method to implement the watchService API.
	 *
	 * @param strings the strings
	 * @throws Exception the exception
	 */
	@Override
	public void run(String... strings) throws Exception {

		processUnreadFiles();
		LOGGER.info("Starting Watch Service on the location -> " + excelPath);
		final Path path = Paths.get(excelPath);
		WatchService watchService = null;
		try {
			// create a watchService instance.
			watchService = FileSystems.getDefault().newWatchService();
			// register the watchService events
			path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
			// key returns the next queued watch key any of whose events have occurred or
			// null if no registered events have occurred.
			WatchKey key;
			while ((key = watchService.take()) != null) {
				for (WatchEvent<?> event : key.pollEvents()) {
					LOGGER.info("Event kind: {}. File affected: {}", event.kind(), event.context());
					// get the fileName created/Copied on the temp folder
					final String fileName = String.valueOf(event.context());
					File file = new File(excelPath + "/" + fileName);

					// validate the file extension and content.
					if (Optional.ofNullable(fileName).isPresent() && validateFileExtension(fileName)
							&& validateFileContent(file)) {
						service.processExcelFile(fileName, file.lastModified());
					} else {
						LOGGER.info("Unsupported File dectected on the location: {} ", excelPath);
					}
				}
				key.reset();
			}
		} catch (InterruptedException e) {
			LOGGER.error("InterruptedException occurred on Watch service. Exception :", e);
		} catch (IOException e) {
			LOGGER.error("IOException occurred on Watch service. Exception :", e);
		}
	}

	/**
	 * Process unread files.
	 * 
	 * This method reads all files in the configurable folder and checks for files
	 * which are not processed.
	 */
	private void processUnreadFiles() {

		LOGGER.info("Checking for UnProcessed files in the location: ", excelPath);
		final File folder = new File(excelPath);
		final File[] listOfFiles = folder.listFiles();
		ArrayUtils.reverse(listOfFiles);
		try {
			for (final File file : listOfFiles) {
				if (file.isFile() && Optional.ofNullable(file.getName()).isPresent()
						&& validateFileExtension(file.getName()) && validateFileContent(file)) {
					service.checkWhetherFileIsProcessed(file.getName(), file.lastModified());
				} else {
					LOGGER.info("Invalid File: {} at location {}.", file.getName(), excelPath);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Validate file extension.
	 *
	 * @param fileName the file name
	 * @return true, if successful
	 */
	private boolean validateFileExtension(final String fileName) {
		return fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()).equalsIgnoreCase("xlsx")
				|| fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()).equalsIgnoreCase("xls");
	}

	/**
	 * Validate file content.
	 * 
	 * This method helps to validate the file content with the file type to check
	 * whether the an unsupported file's extension is changed to excel's type.
	 *
	 * @param file the file
	 * @return the boolean
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private Boolean validateFileContent(final File file) throws IOException {
		Boolean isValid = Boolean.FALSE;
		final Tika tika = new Tika();
		// gets the mimeType of file.
		final String mimeType = tika.detect(file);
		if (!ValidType.isInvalidType(mimeType)) {
			isValid = Boolean.TRUE;
		}
		return isValid;
	}

}
