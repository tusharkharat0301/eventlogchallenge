package com.codingchallenge.eventlog;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.codingchallenge.service.EventService;
import com.codingchallenge.service.EventServiceImpl;
import com.codingchallenge.utils.EventHelper;

public class EventLogApplication {

	private static final Logger logger = LogManager.getLogger(EventLogApplication.class.getName());

	public static void main(String[] args) throws IOException, SQLException {
		EventService eventService = new EventServiceImpl();
		logger.info("Execution Started ..");
		File logFileObj = EventHelper.verifyPath(System.getProperty("logFilePath"));
		eventService.createTableIfNotExist();
		eventService.processEvents(logFileObj);
		eventService.closeConnection();
		logger.info("Execution Completed ..");
	}
}
