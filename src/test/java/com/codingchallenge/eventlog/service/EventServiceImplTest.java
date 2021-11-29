package com.codingchallenge.eventlog.service;

import static org.junit.Assert.assertTrue;

import java.sql.SQLException;

import org.junit.Test;

import com.codingchallenge.service.EventServiceImpl;

public class EventServiceImplTest {
	
	/**
	 * This test verifies that create table if not exist is working fine
	 */
	@Test
	public void createTable() {
		EventServiceImpl serviceImpl = new EventServiceImpl();
		Integer result = serviceImpl.createTableIfNotExist();
		assertTrue(result==0);
	}

	/**
	 * This test verifies that insert record in table working fine
	 * @throws SQLException
	 */
	@Test
	public void insertRecord() throws SQLException {
		EventServiceImpl serviceImpl = new EventServiceImpl();
		Integer result = serviceImpl.insertRecord("1",5,"APPLICATION_LOG","12334",true);
		assertTrue(result==1);
	}

}
