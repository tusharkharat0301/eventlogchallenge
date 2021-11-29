package com.codingchallenge.eventlog.utils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;

import org.junit.Test;

import com.codingchallenge.utils.EventHelper;

public class EventHelperTest {

	/**
	 * Verify that file with correct path is processing
	 * 
	 * @throws FileNotFoundException
	 */
	@Test
	public void readFileWithValidPath() throws FileNotFoundException {
		String filePath = System.getProperty("user.dir") + "/src/main/resources";
		File logFile = EventHelper.verifyPath(filePath);
		assertTrue(logFile.exists());

	}

	/**
	 * Verify that file with wrong path is giving exception and not processing
	 * 
	 * @throws FileNotFoundException
	 */
	@Test(expected = FileNotFoundException.class)
	public void readFileWithWrongPath() throws FileNotFoundException {
		String filePath = System.getProperty("user.dir") + "/invalidPath";
		EventHelper.verifyPath(filePath);

	}

	/**
	 * Verify that always eventMap size should be zero as we are removing records
	 * from map after succesful insertion in DB just to avoid memory issue
	 * 
	 * @throws FileNotFoundException
	 */
	@Test
	public void parseJsonAndProcessDBOperation() throws FileNotFoundException {
		String filePath = System.getProperty("user.dir") + "/src/main/resources";
		File logFile = EventHelper.verifyPath(filePath);
		Map<String, Map<String, Object>> eventMap = EventHelper.parseJsonToObject(logFile);
		assertTrue(eventMap.size() == 0);
	}

	/**
	 * This test case is to verify alert flag=false if time taken for Event id <=4
	 * second in Below test , 1st condition check:time duration is 4 seconds
	 * (1491377495218-1491377495216=2) <4 2nd condition checktime duration is 4
	 * seconds (1491377495222-1491377495218=4) =4 time duration is 4 seconds
	 * (1491377495222-1491377495218=5)
	 * 
	 */
	@Test
	public void alertFalseTest() {
		boolean flag;
		flag = EventHelper.calculateTotalTimeTaken(1491377495218l, 1491377495216l, "scsmbstgrc", "APPLICATION_LOG",
				"12345");
		assertFalse(flag);
		flag = EventHelper.calculateTotalTimeTaken(1491377495222l, 1491377495218l, "scsmbstgrc", "APPLICATION_LOG",
				"12345");
		assertFalse(flag);
	}

	/**
	 * This test case is to verify alert flag=true if time taken for Event id >4
	 * second in below test , time duration is 5 seconds
	 * (1491377495223-1491377495218=5)
	 * 
	 */
	@Test
	public void alertTrueTest() {
		boolean flag = EventHelper.calculateTotalTimeTaken(1491377495223l, 1491377495218l, "scsmbstgrc",
				"APPLICATION_LOG", "12345");
		assertTrue(flag);
	}

}
