package com.codingchallenge.repo;

import java.sql.Connection;
import java.sql.SQLException;

public interface EventRepo {

	public Connection getConnection();

	public Integer createTable();

	public Integer insertRecord(String key, long totalTime, String type, String host, boolean alert) throws SQLException;

	public void closeConnection() throws SQLException;

}
