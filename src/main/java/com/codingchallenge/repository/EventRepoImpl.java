package com.codingchallenge.repository;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.codingchallenge.utils.EventHelper;

public class EventRepoImpl implements EventRepo {

	private static final Logger logger = LogManager.getLogger(EventRepoImpl.class.getName());
	static Connection con = null;

	public Connection getConnection() {

		logger.info("Inside getConnection method of EventRepoImpl.. ");
		Properties prop = null;
		try {
			try {
				prop = EventHelper.loadProperties();
			} catch (FileNotFoundException e) {
				logger.error("File not Found at specified location ..", e);
			} catch (IOException e) {
				logger.error("IO operation error occured..", e);
			}
			if (null != prop) {
				Class.forName(prop.getProperty("driver"));
				con = DriverManager.getConnection(prop.getProperty("url"), prop.getProperty("user"),
						prop.getProperty("pass"));
			} else {
				try {
					prop = EventHelper.loadProperties();
				} catch (IOException e) {
					logger.error("IO operation error occured..", e);
				}
			}

			if (con != null) {
				logger.info("Connection created successfully");
			} else {
				logger.debug("Problem with creating connection");
			}
		} catch (ClassNotFoundException e) {
			logger.error("Class.forname : issue occoured", e);
		} catch (SQLException e) {
			logger.error("Error getting connection", e);
		}
		return con;
	}

	/**
	 * create table for processed event record
	 */
	public Integer createTable() {
		Statement stmt = null;
		int result = 0;
		logger.info("Inside createTable method of EventRepoImpl.. ");
		if (con == null) {
			con = getConnection();
		}
		try {
			stmt = con.createStatement();
			result = stmt.executeUpdate(
					"CREATE TABLE IF NOT EXISTS event_log (id varchar(10), type varchar(50),host varchar(50),totaltime int,alert boolean);");
		} catch (SQLException e) {
			logger.error("Error occured while creating table ", e);
		}
		return result;
	}

	/**
	 * insert processed event record
	 */
	public Integer insertRecord(String key, long totalTime, String type, String host, boolean alert)
			throws SQLException {
		logger.debug("Inside insertRecord method of EventRepoImpl.. ");
		if (con == null) {
			con = getConnection();
		}
		Statement stmt = null;
		int result = 0;
		stmt = con.createStatement();
		logger.debug("details are :" + key + " " + totalTime + " " + type + " " + host + " " + alert);
		result = stmt.executeUpdate("INSERT INTO event_log VALUES ('" + key + "','" + type + "', '" + host + "','"
				+ totalTime + "','" + alert + "');");
		con.commit();
		return result;
	}

	public void closeConnection() throws SQLException {
		logger.info("Inside closeConnection method of EventRepoImpl.. ");
		if (con != null) {
			logger.info("Closing connection..");
			con.close();
			logger.info("Connection Closed..");
		} else {
			logger.info("Connection was not open");
		}
	}
}
