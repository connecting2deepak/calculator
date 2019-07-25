/**
 * 
 */
package com.discount.calculator.converter;

import java.util.Optional;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.discount.calculator.Entity.Discount;
import com.discount.calculator.Entity.Product;
import com.discount.calculator.Repository.ProductRepository;
import com.discount.calculator.util.DateUtils;

/**
 * @author deepak.s29
 */
@Component
public class DiscountConverter implements Converter<Row, Discount> {

	final static Logger LOGGER = LoggerFactory.getLogger(DiscountConverter.class);

	private final DataFormatter formatter = new DataFormatter();

	@Autowired
	private ProductRepository productRepository;

	/**
	 * Convert.
	 *
	 * @param currentRow the current row
	 * @return the discount
	 */
	@Override
	public Discount convert(final Row currentRow) {
		Discount discount = null;
		if (Optional.ofNullable(currentRow).isPresent()) {
			discount = new Discount();
			final String productId = formatValue(0, currentRow);
			if (Optional.ofNullable(productId).isPresent()) {
				final Product product = productRepository.findOne(productId);
				if (Optional.ofNullable(product).isPresent()) {
					discount.setProduct(product);
					discount.setDiscountPercentage(formatValue(1, currentRow));
					discount.setValidFrom(DateUtils.getSqlTimeStamp(formatValue(2, currentRow)));
					discount.setValidTo(DateUtils.getSqlTimeStamp(formatValue(3, currentRow)));
				} else {
					LOGGER.info(
							"No Product data available for the Product. Hence skipping the Discount data for the productId: {}",
							productId);
				}
			}
		}
		return discount;
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
			LOGGER.info("Searching the row: {} in the Discount excel for column index: {}", currentRow.getRowNum(),
					columnIndex);
			formattedString = formatter.formatCellValue(currentRow.getCell(columnIndex)).trim();
		} catch (Exception e) {
			LOGGER.error("Exception occurred while formatting the column index {} on Discount excel. Hence setting"
					+ " it to the default value: {}", columnIndex, formattedString);
		}
		return formattedString;
	}

}
