package com.codingchallenge.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.codingchallenge.service.EventService;
import com.codingchallenge.service.EventServiceImpl;
import com.codingchallenge.vo.Event;
import com.google.gson.Gson;

public class EventHelper {

	private static final Logger log = LogManager.getLogger(EventHelper.class.getName());

	/**
	 * This method is used to verify if path provided as an Input arguments to
	 * logfile.txt is valid or not
	 * 
	 * @param filePath
	 */
	public static File verifyPath(String filePath) throws FileNotFoundException {
		log.debug("Inside readFile Method..");
		log.info("Path Provided is :" + filePath + "\\logfile.txt");
		File file = new File(filePath + "\\logfile.txt");
		if (!file.exists()) {
			throw new FileNotFoundException(
					"Path specified for logfile.txt is incorrect , Please check and run the command again");
		}
		log.info("File is present at specified path");
		return file;
	}

	/**
	 * This method is used to parse JSON content to Object and Calls the service to
	 * calculate total time taken by event log and
	 * 
	 * @param file
	 *            to read file and parse content to POJO EventPOJO class
	 */
	public static Map<String, Map<String, Object>> parseJsonToObject(File file) {
		log.info("Inside parseJsonToObject Method..");
		Gson gson = new Gson();
		Map<String, Map<String, Object>> eventLogMap = new HashMap<String, Map<String, Object>>();
		LineIterator it = null;
		try {
			it = FileUtils.lineIterator(file, "UTF-8");
			while (it.hasNext()) {
				String s = it.nextLine();
				log.debug("converting json string to event Object : " + s);
				Event event = gson.fromJson(s, Event.class);
				log.debug("Event object : Key :" + event.getId() + " State :" + event.getState() + " timestamp : "
						+ event.getTimestamp());
				if (eventLogMap.containsKey(event.getId())) {
					Map<String, Object> eventDetailsMap = new HashMap<String, Object>();
					log.debug("adding state details against event id in map as Map already contain event id");
					eventDetailsMap = eventLogMap.get(event.getId());
					eventDetailsMap.put(event.getState(), event.getTimestamp());
					eventLogMap.put(event.getId(), eventDetailsMap);
					try {
						Integer result = flagEventDetailsInDB(eventDetailsMap, event.getId());
						if (result == 1) {
							log.info("Record Inserted Succefully for id .. " + event.getId());
							eventLogMap.remove(event.getId());
						}
					} catch (SQLException e) {
						log.error("Exception occured  :", e);
					}

				} else {
					log.debug("putting all event details against event id in map");
					Map<String, Object> eventDetailsMap = new HashMap<String, Object>();
					eventDetailsMap.put(event.getState(), event.getTimestamp());
					eventDetailsMap.put(Constants.TYPE, event.getType());
					eventDetailsMap.put(Constants.HOST, event.getHost());
					eventLogMap.put(event.getId(), eventDetailsMap);
				}
			}
		} catch (IOException e) {
			log.error(" IO Exception occured  :", e);
		} finally {
			try {
				if (null != it) {
					it.close();
				}
			} catch (IOException e) {
				log.error("IO Exception occured  :", e);
			}
		}
		return eventLogMap;
	}

	public static Integer flagEventDetailsInDB(Map<String, Object> eventLogsCollection, String id) throws SQLException {
		log.debug("Inside flagAndStoreEntryinDB method .. ");
		EventService eventService = new EventServiceImpl();
		Integer result = 0;
		long start = 0;
		long finish = 0;
		long timeTakenToFinish = 0;
		String type = null;
		String host = null;
		for (Map.Entry<String, Object> eventLog : eventLogsCollection.entrySet()) {
			if (eventLog.getKey().equalsIgnoreCase(Constants.STARTED)) {
				start = (Long) eventLog.getValue();
			}
			if (eventLog.getKey().equalsIgnoreCase(Constants.FINISHED)) {
				finish = (Long) eventLog.getValue();
			}
			if (eventLog.getKey().equalsIgnoreCase(Constants.TYPE)) {
				type = (String) eventLog.getValue();
			}
			if (eventLog.getKey().equalsIgnoreCase(Constants.HOST)) {
				host = (String) eventLog.getValue();
			}
		}
		boolean flag = calculateTotalTimeTaken(finish, start, id, type, host);
		timeTakenToFinish = finish - start;

		log.debug("for id :" + id + " time taken is : " + timeTakenToFinish + " type : " + type + " host: " + host
				+ " alert : " + flag);

		log.info("inserting record in Database .. ");
		result = eventService.insertRecord(id, timeTakenToFinish, type, host, flag);
		return result;
	}

	public static boolean calculateTotalTimeTaken(long finish, long start, String id, String type, String host) {
		log.info("calculate total time taken to finish event .. ");
		boolean alert = false;
		long timeTakenToFinish = finish - start;
		if (timeTakenToFinish > 4) {
			alert = true;
		} else {
			alert = false;
		}
		return alert;
	}

	public static Properties loadProperties() throws FileNotFoundException, IOException {

		Properties properties = new Properties();

		FileInputStream fileStream = new FileInputStream(
				System.getProperty("user.dir") + "/src/main/resources/config.properties");

		properties.load(fileStream);
		return properties;
	}
}
