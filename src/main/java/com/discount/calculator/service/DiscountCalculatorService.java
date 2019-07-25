/**
 * 
 */
package com.discount.calculator.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.discount.calculator.Entity.Discount;
import com.discount.calculator.Entity.ProcessedFiles;
import com.discount.calculator.Entity.Product;
import com.discount.calculator.Repository.DiscountRepository;
import com.discount.calculator.Repository.ProcessedFilesRepository;
import com.discount.calculator.Repository.ProductRepository;
import com.discount.calculator.converter.DiscountConverter;
import com.discount.calculator.converter.ProductConverter;
import com.discount.calculator.enums.FileStatus;
import com.discount.calculator.model.ProductIDs;
import com.discount.calculator.model.Products;
import com.discount.calculator.model.Values;
import com.discount.calculator.util.DateUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

/**
 * @author deepak.s29
 */
@Service
public class DiscountCalculatorService {

	private static final Logger LOGGER = LoggerFactory.getLogger(DiscountCalculatorService.class);

	private static final String PRODUCT_FILE_FORMAT = "Product";

	private static final String DISCOUNT_FILE_FORMAT = "Discount";

	@Value("${xml.file.location}")
	private String xmlPath;

	@Value("${xml.file.name}")
	private String xmlFileName;

	@Value("${excel.file.location}")
	private String excelPath;

	@Autowired
	private ProductConverter productConverter;

	@Autowired
	private DiscountConverter discountConverter;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private DiscountRepository discountRepository;

	@Autowired
	private ProcessedFilesRepository processedFilesRepository;

	/**
	 * Process excel file.
	 * 
	 * This service method helps to process the excel file created or copied on the
	 * configurable folder.
	 *
	 * @param fileName the file name
	 */
	public void processExcelFile(final String fileName, final Long lastModified) {
		try {
			final FileInputStream file = new FileInputStream(new File(excelPath + "/" + fileName));
			// Create Workbook instance holding reference to .xlsx file
			final XSSFWorkbook workbook = new XSSFWorkbook(file);
			// Get first/desired sheet from the workbook
			final XSSFSheet sheet = workbook.getSheetAt(0);
			// Iterate through each rows one by one
			final Iterator<Row> rowIterator = sheet.iterator();
			// skip the first row which is the header row
			if (rowIterator.hasNext()) {
				rowIterator.next();
				if (fileName.startsWith(PRODUCT_FILE_FORMAT)) {
					createProductFromExcel(rowIterator, fileName, lastModified);
				} else if (fileName.startsWith(DISCOUNT_FILE_FORMAT)) {
					createDiscountFromExcel(rowIterator, fileName, lastModified);
				} else {
					LOGGER.info("Invalid Excel File dectected on the location {} ", excelPath);
				}
				workbook.close();
				computeDiscountPrice();
			} else {
				LOGGER.info("Invalid Excel File content dectected on the location {} ", excelPath);
			}
		} catch (FileNotFoundException e) {
			LOGGER.error("FileNotFoundException occurred on processExcelFile. Exception :", e);
		} catch (IOException e) {
			LOGGER.error("IOException occurred on processExcelFile. Exception :", e);
		}
	}

	/**
	 * Creates the discount from excel.
	 *
	 * @param rowIterator  the row iterator
	 * @param fileName     the file name
	 * @param lastModified the last modified
	 */
	private void createDiscountFromExcel(final Iterator<Row> rowIterator, final String fileName,
			final Long lastModified) {
		final List<Discount> discounts = new ArrayList<Discount>();
		while (rowIterator.hasNext()) {
			final Discount discount = discountConverter.convert(rowIterator.next());
			if (Optional.ofNullable(discount).isPresent()) {
				discounts.add(discount);
			}
		}
		// Save Discount Data from the Excel
		if (discounts.isEmpty()) {
			LOGGER.debug("No Discount data available in the Excel {} to get saved!", fileName);
		} else {
			discountRepository.save(discounts);
			createProcessedFilesEntry(fileName, lastModified);
		}
		
	}

	/**
	 * Creates the product from excel.
	 *
	 * @param rowIterator  the row iterator
	 * @param fileName     the file name
	 * @param lastModified the last modified
	 */
	private void createProductFromExcel(final Iterator<Row> rowIterator, final String fileName,
			final Long lastModified) {
		final List<Product> products = new ArrayList<Product>();
		while (rowIterator.hasNext()) {
			final Product product = productConverter.convert(rowIterator.next());
			if (Optional.ofNullable(product).isPresent()) {
				products.add(product);
			}
		}
		// Save Product Data from the Excel
		if (products.isEmpty()) {
			LOGGER.debug("No product data available in the Excel {} to get saved!", fileName);
		} else {
			// checks whether the product data already exists in the DB, then skips the
			// record.
			productRepository.save(products.stream().map(product -> {
				final Product productFromDb = productRepository.findOne(product.getProductId().trim());
				return Optional.ofNullable(productFromDb).isPresent() ? productFromDb : product;
			}).collect(Collectors.toList()));

			createProcessedFilesEntry(fileName, lastModified);
		}
	}

	/**
	 * Creates the processed files entry.
	 *
	 * @param fileName     the file name
	 * @param lastModified the last modified
	 */
	private void createProcessedFilesEntry(final String fileName, final Long lastModified) {

		final Timestamp modifiedTimeStamp = DateUtils.getTimeStampFromLong(lastModified);
		final ProcessedFiles processedFilesFromDB = processedFilesRepository.findByFileNameAndProcessedDate(fileName,
				modifiedTimeStamp);
		if (Optional.ofNullable(processedFilesFromDB).isPresent()) {
			processedFilesFromDB.setStatus(FileStatus.RE_PROCESSED.getStatus());
			processedFilesRepository.save(processedFilesFromDB);
			LOGGER.info("File {} was already processed. Changing status to {}", fileName,
					FileStatus.RE_PROCESSED.getStatus());
		} else {
			final ProcessedFiles processedFiles = new ProcessedFiles();
			processedFiles.setFileName(fileName);
			processedFiles.setProcessedDate(DateUtils.getTimeStampFromLong(lastModified));
			processedFiles.setStatus(FileStatus.PROCESSED.getStatus());
			processedFilesRepository.save(processedFiles);
		}
	}

	/**
	 * Compute discount price.
	 * 
	 * This method helps to compute the Discount price for the respective Products
	 * from the DB. This method will be called based on the scheduled cron job or
	 * when a new excel file got placed in the configurable folder.
	 */
	public void computeDiscountPrice() {

		List<Product> products = productRepository.findAll();
		for (final Product product : products) {

			if (Optional.ofNullable(product.getDiscounts()).isPresent() && !product.getDiscounts().isEmpty()) {
				final List<Discount> discounts = product.getDiscounts();
				final Timestamp currentTimeStamp = DateUtils.getCurrentTimeStamp();

				final Optional<Discount> discountOpt = discounts.stream().sorted(
						(mapping1, mapping2) -> Long.compare(mapping2.getDiscountId(), mapping1.getDiscountId()))
						.findFirst();
				if (discountOpt.isPresent()) {
					BigDecimal discountPrice = BigDecimal.ZERO;
					if (discountOpt.get().getValidFrom().getTime() <= currentTimeStamp.getTime()
							&& discountOpt.get().getValidTo().getTime() >= currentTimeStamp.getTime()) {

						discountPrice = product.getListPrice()
								.subtract((new BigDecimal(discountOpt.get().getDiscountPercentage())
										.multiply(product.getListPrice())).divide(new BigDecimal(100), 2,
												BigDecimal.ROUND_HALF_UP));
						product.setDiscountPrice(discountPrice);
					}
				}
			} else {
				LOGGER.info("No Discount data available for the product : {}", product.getProductId());
			}
		}
		if (products.isEmpty()) {
			LOGGER.debug("No product Data available to compute the Discount price!");
		} else {
			products = productRepository.save(products);
			generateDiscountPriceXml(products);
		}
	}

	/**
	 * Generate discount price xml.
	 * 
	 * This method helps to create the xml file from the pojo object
	 *
	 * @param products the products
	 */
	private void generateDiscountPriceXml(final List<Product> products) {

		final Products productsModel = createXmlModel(products);
		XmlMapper xmlMapper = new XmlMapper();
		xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
		String xml = null;
		try {
			xml = xmlMapper.writeValueAsString(productsModel);
		} catch (JsonProcessingException e) {
			LOGGER.error("JsonProcessingException occurred while creating the XML file. Exception : {}", e);
		}
		// write XML string to file
		final File xmlOutput = new File(xmlPath + "/" + xmlFileName + ".xml");
		final FileWriter fileWriter;
		try {
			fileWriter = new FileWriter(xmlOutput);
			fileWriter.write(xml);
			fileWriter.close();
		} catch (IOException e) {
			LOGGER.error("IOException occurred while creating the XML file. Exception : {}", e);
		}
	}

	/**
	 * Creates the xml pojo model object.
	 *
	 * @param products the products
	 * @return the products model
	 */
	private Products createXmlModel(final List<Product> products) {
		final Products productsModel = new Products();
		final List<ProductIDs> productModels = new ArrayList<ProductIDs>();
		for (final Product product : products) {
			final ProductIDs productModel = new ProductIDs();
			productModel.setProductId(product.getProductId());
			final Values values = new Values();
			values.setSalesPrice(product.getDiscountPrice());
			values.setLastUpdated(String.valueOf(DateUtils.getCurrentTimeStamp()));
			productModel.setValues(values);
			productModels.add(productModel);
		}
		productsModel.setProductIDs(productModels);
		return productsModel;
	}

	/**
	 * Check whether file is processed.
	 *
	 * @param fileName     the file name
	 * @param lastModified the last modified
	 */
	public void checkWhetherFileIsProcessed(final String fileName, final Long lastModified) {

		final Timestamp modifiedTimeStamp = DateUtils.getTimeStampFromLong(lastModified);
		final List<ProcessedFiles> files = processedFilesRepository.findAllByFileNameAndProcessedDate(fileName,
				modifiedTimeStamp);
		if (files.isEmpty()) {
			LOGGER.info("File : {} is not yet processed. Initiating file processing..", fileName);
			processExcelFile(fileName, lastModified);
		} else {
			LOGGER.info("File : {} got processed already. Discarding excel file processing.", fileName);
		}
	}

}
