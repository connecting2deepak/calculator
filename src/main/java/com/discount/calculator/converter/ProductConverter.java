/**
 * 
 */
package com.discount.calculator.converter;

import java.math.BigDecimal;
import java.util.Optional;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.discount.calculator.Entity.Product;

/**
 * @author deepak.s29
 */
@Component
public class ProductConverter implements Converter<Row, Product> {

	final static Logger LOGGER = LoggerFactory.getLogger(ProductConverter.class);

	private final DataFormatter formatter = new DataFormatter();

	/**
	 * Convert.
	 *
	 * @param currentRow the current row
	 * @return the product
	 */
	@Override
	public Product convert(final Row currentRow) {
		Product product = null;
		if (Optional.ofNullable(currentRow).isPresent()) {
			product = new Product();
			product.setProductId(formatValue(0, currentRow));
			product.setListPrice(convertStringToBigDecimal(1, formatValue(1, currentRow), currentRow));
		}
		return product;
	}

	/**
	 * Format value.
	 *
	 * @param columnIndex the column index
	 * @param currentRow  the current row
	 * @return the string
	 */
	private String formatValue(final int columnIndex, final Row currentRow) {
		String formattedString = null;
		try {
			LOGGER.info("Searching the row: {} in the product excel for column index: {}", currentRow.getRowNum(),
					columnIndex);
			formattedString = formatter.formatCellValue(currentRow.getCell(columnIndex)).trim();
		} catch (Exception e) {
			LOGGER.error("Exception occurred while formatting the column index {} on Product excel. Hence setting"
					+ " it to the default value: {}", columnIndex, formattedString);
		}
		return formattedString;
	}

	/**
	 * Convert string to big decimal.
	 *
	 * @param columIndex  the colum index
	 * @param columnValue the column value
	 * @param currentRow  the current row
	 * @return the big decimal
	 */
	private BigDecimal convertStringToBigDecimal(final int columIndex, String columnValue, final Row currentRow) {
		BigDecimal convertedValue = BigDecimal.ZERO;
		try {
			convertedValue = new BigDecimal(columnValue);
		} catch (Exception exp) {
			LOGGER.error(
					"Error while converting the index's {} value {} to BigDecimal for RowNumber {}."
							+ " Hence setting it to the default value {} and Exception is {}",
					columIndex, columnValue, currentRow.getRowNum(), convertedValue, exp.toString());
		}
		return convertedValue;
	}

}
