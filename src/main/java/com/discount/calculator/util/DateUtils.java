/**
 * 
 */
package com.discount.calculator.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author deepak.s29
 */
public final class DateUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(DateUtils.class);
	
	/**
	 * Gets the sql time stamp.
	 *
	 * @param timeStamp the time stamp
	 * @return the sql time stamp
	 */
	public static Timestamp getSqlTimeStamp(final String timeStamp) {

		Date date = null;
		final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
		try {
			date = formatter.parse(timeStamp);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new Timestamp(date.getTime());
	}
	
	/**
	 * Gets the current time stamp.
	 *
	 * @return the current time stamp
	 */
	public static Timestamp getCurrentTimeStamp() {
		
		final Date date = new Date();
		return new Timestamp(date.getTime());
	}
	
	/**
	 * Gets the time stamp from long.
	 *
	 * @param timeInMilliSeconds the time in milli seconds
	 * @return the time stamp from long
	 */
	public static Timestamp getTimeStampFromLong(final Long timeInMilliSeconds) {
		Timestamp sqlTimeStamp = null;
		try {
			final Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(timeInMilliSeconds);
			final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
			final String dateString = formatter.format(calendar.getTime());
			final Date date = formatter.parse(dateString);
			sqlTimeStamp = new Timestamp(date.getTime());
		} catch (Exception e) {
			LOGGER.error("Exception occurred while parsing the time in milli seconds: {} to time stamp. "
					+ "Exception message: {}. {}", timeInMilliSeconds, e.getMessage(), e);
			sqlTimeStamp = new Timestamp(timeInMilliSeconds);
		}
		return sqlTimeStamp;
	}

}
