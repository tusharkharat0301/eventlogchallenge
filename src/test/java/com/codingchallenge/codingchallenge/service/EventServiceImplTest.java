package com.codingchallenge.codingchallenge.service;

import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.sql.SQLException;

import org.junit.Test;

import com.codingchallenge.service.EventServiceImpl;

public class EventServiceImplTest {
	
	/**
	 * This test verifies that create table if not exist is working fine
	 */
	@Test
	public void createTable() {
		EventServiceImpl serviceImpl=new EventServiceImpl();
		Integer result = serviceImpl.createTableIfNotExist();
		assertTrue(result==0 || result==1);
	}

	/**
	 * This test verifies that insert record in table working fine
	 * @throws SQLException
	 */
	@Test
	public void insertRecord() throws SQLException {
		EventServiceImpl serviceImpl=new EventServiceImpl();
		Integer result = serviceImpl.insertRecord("1",5,"APPLICATION_LOG","12334",true);
		assertTrue(result==1);
	}

}
