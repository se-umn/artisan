package org.hotelme.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.hotelme.Main;

public class SystemTestUtils {
	
	public static void dropTheDb() {
		System.out.println("DROP THE ENTIRE DB !");
		try (Connection conn = DriverManager.getConnection(Main.MYSQL_URL, Main.USER, Main.PASS);
				Statement stmt = conn.createStatement();) {
			stmt.executeUpdate("DROP DATABASE IF EXISTS hotelme;");
		} catch (SQLException e) {
			e.printStackTrace();
			org.junit.Assert.fail("Couldn't establish connection to the database.");
		}
	}


	// TODO Does this create problem because it calls itself ?!
	// FOR SOME WEIRD REASON THIS BREAKS ON SHUTTLE PC2 BUT NOT ON MY MAC ... ?!
	public static void dropAndRecreateTheDb() {
	boolean autoCommit = false;
		boolean stopOnError = true;
		try (Connection connection = DriverManager.getConnection(Main.MYSQL_URL, Main.USER, Main.PASS);
				InputStream in = String.class.getResourceAsStream("/emptydb.sql");
				BufferedReader reader = new BufferedReader(new InputStreamReader(in));) {
			ScriptRunner scriptRunner = new ScriptRunner(connection, autoCommit, stopOnError);
			scriptRunner.runScript(reader);

		} catch (SQLException | IOException e) {
			e.printStackTrace();
			org.junit.Assert.fail("Error while setting up the db using " + "/emptydb.sql" );
		}
	}

	public static void dropAndRecreateTheDbFromResource(String resourceName) {
		boolean autoCommit = false;
		boolean stopOnError = true;
		try (Connection connection = DriverManager.getConnection(Main.MYSQL_URL, Main.USER, Main.PASS);
				InputStream in = String.class.getResourceAsStream(resourceName);
				BufferedReader reader = new BufferedReader(new InputStreamReader(in));) {
			ScriptRunner scriptRunner = new ScriptRunner(connection, autoCommit, stopOnError);
			scriptRunner.runScript(reader);

		} catch (SQLException | IOException e) {
			e.printStackTrace();
			org.junit.Assert.fail("Error while setting up the db using " + resourceName );
		}
	}
}
