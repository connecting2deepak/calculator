/**
 * 
 */
package com.discount.calculator.service;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.discount.calculator.Entity.Discount;
import com.discount.calculator.Entity.ProcessedFiles;
import com.discount.calculator.Entity.Product;
import com.discount.calculator.Repository.DiscountRepository;
import com.discount.calculator.Repository.ProcessedFilesRepository;
import com.discount.calculator.Repository.ProductRepository;
import com.discount.calculator.converter.ProductConverter;
import com.discount.calculator.util.DateUtils;

/**
 * @author deepak.s29
 */
@RunWith(MockitoJUnitRunner.class)
public class DiscountCalculatorServiceTest {

	@InjectMocks
	private DiscountCalculatorService service;

	@Mock
	private ProductConverter productConverter;

	@Mock
	private ProductRepository productRepository;

	@Mock
	private DiscountRepository discountRepository;

	@Mock
	private ProcessedFilesRepository processedFilesRepository;

	private static ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

	private static final String FILE_NAME = "Product.xlsx";

	private static String getFilePath(final ClassLoader classLoader, final String fileName) {
		return new File(classLoader.getResource(fileName).getFile()).getAbsolutePath();
	}

	@Test
	public void processExcelFileTest() throws IOException {

		final File file = new File(getFilePath(classLoader, FILE_NAME));

		ReflectionTestUtils.setField(service, "excelPath", file.getParent());
		ReflectionTestUtils.setField(service, "xmlPath", file.getParent());
		ReflectionTestUtils.setField(service, "xmlFileName", "DiscountPrice");

		final Long lastModified = 0L;
		final List<Product> products = new ArrayList<Product>();
		final Product product = new Product();
		product.setProductId("AB101");
		product.setListPrice(new BigDecimal(101));
		final List<Discount> discounts = new ArrayList<Discount>();

		final Discount discount = new Discount();
		discount.setDiscountId(1L);
		discount.setDiscountPercentage("20");
		discount.setValidFrom(DateUtils.getSqlTimeStamp("2019-07-12 00:00:00"));
		discount.setValidTo(DateUtils.getSqlTimeStamp("2020-12-31 23:59:00"));
		discounts.add(discount);
		product.setDiscounts(discounts);
		products.add(product);

		when(productConverter.convert(any())).thenReturn(product);
		when(productRepository.findOne(anyString())).thenReturn(null);
		when(productRepository.save(products)).thenReturn(products);
		when(productRepository.findAll()).thenReturn(products);

		when(processedFilesRepository.findByFileNameAndProcessedDate(any(), any())).thenReturn(null);
		when(processedFilesRepository.save(new ProcessedFiles())).thenReturn(new ProcessedFiles());

		service.processExcelFile(FILE_NAME, lastModified);

		verify(productRepository, times(1)).findOne(anyString());
		verify(productRepository, times(2)).save(products);
		verify(productRepository, times(1)).findAll();
		verify(processedFilesRepository, times(1)).findByFileNameAndProcessedDate(any(), any());
	}

}
