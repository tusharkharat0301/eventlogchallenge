package com.codingchallenge.eventlog.repository;

import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

import com.codingchallenge.repository.EventRepoImpl;

public class EventRepoImplTest {

	Connection connection ;
	
	/**
	 * This test verifies connection with DB
	 */
	@Test
	public void connectionCheckTest() throws SQLException {
		EventRepoImpl daoImpl=new EventRepoImpl();
		connection = daoImpl.getConnection();
		assertTrue(connection!=null);
	}
}
