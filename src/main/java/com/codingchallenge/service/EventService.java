package com.codingchallenge.service;

import java.io.File;
import java.sql.SQLException;

public interface EventService {

	public Integer createTableIfNotExist();

	public Integer insertRecord(String key, long timeTakenToFinish, String type, String host, boolean alert) throws SQLException;

	public void closeConnection() throws SQLException;

	public void processEvents(File logFileObj);

}
