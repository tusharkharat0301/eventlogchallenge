package com.codingchallenge.service;

import java.io.File;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.codingchallenge.repo.EventRepoImpl;
import com.codingchallenge.utils.EventHelper;

public class EventServiceImpl implements EventService {
	private static final Logger logger = LogManager.getLogger(EventServiceImpl.class.getName());
	EventRepoImpl eventDao;

	public Integer createTableIfNotExist() {
		logger.debug("Inside createTableIfNotExist method of EventServiceImpl.. ");
		eventDao = new EventRepoImpl();
		Integer result = eventDao.createTable();
		return result;
	}

	public Integer insertRecord(String key, long timeTakenToFinish, String type, String host, boolean alert)
			throws SQLException {
		logger.debug("Inside insertRecord method of EventServiceImpl.. ");
		eventDao = new EventRepoImpl();
		Integer result = eventDao.insertRecord(key, timeTakenToFinish, type, host, alert);
		return result;
	}

	public void closeConnection() throws SQLException {
		logger.debug("Inside closeConnection method of EventServiceImpl.. ");
		eventDao.closeConnection();
	}

	public void processEvents(File logFileObj) {
		logger.debug("Inside processEventLog method of EventServiceImpl.. ");
		EventHelper.parseJsonToObject(logFileObj);

	}

}
