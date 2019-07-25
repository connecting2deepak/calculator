package com.discount.calculator.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.discount.calculator.service.DiscountCalculatorService;

@Component
public class ScheduledTasks {

	private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

	private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

	@Autowired
	private DiscountCalculatorService service;

	/**
	 * Schedule task with cron expression.
	 * 
	 * This method is a scheduled task which runs on 9:00 AM CET every day.
	 */
	@Scheduled(cron = "0 0 9 * * ?", zone = "CET")
	public void scheduleTaskWithCronExpression() {
		logger.info("Cron Task :: Execution Time - {}", dateTimeFormatter.format(LocalDateTime.now()));
		service.computeDiscountPrice();
	}

}