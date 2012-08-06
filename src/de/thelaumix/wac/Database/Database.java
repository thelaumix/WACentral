package de.thelaumix.wac.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import de.thelaumix.wac.main;


public class Database {
	
	
	private static Connection con;
	
	public static void connectToDatabase(String host, String username, String password, String table) throws SQLException {
		
		try {
	        Class.forName("com.mysql.jdbc.Driver");
	    } catch (ClassNotFoundException e) {
	        e.printStackTrace();
	    } 
		
	    con = DriverManager.getConnection(
	                         "jdbc:" + host,
	                         username,
	                         password);
	    
	    try
	    {
	    	writeDatabase("CREATE TABLE IF NOT EXISTS `" + main.db_table + "` (`id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY ,`item` INT NOT NULL ,`price_min` DOUBLE NOT NULL ,`price_max` DOUBLE NOT NULL ,`price_a` DOUBLE NOT NULL)");
	    } catch (Exception e) {
	    	new main().o("Tabelle '" + main.db_table + "' in der Datenbank '" + main.db_db + "' konnte nicht erzeugt werden.");
	    }
	    	
	}
	
	public static void closeConnection() throws SQLException
	{
		con.close();
	}
	
	public void clearTable() throws SQLException
	{
		writeDatabase("DROP TABLE `" + main.db_table + "`");
		writeDatabase("CREATE TABLE IF NOT EXISTS `" + main.db_table + "` (`id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY ,`item` INT NOT NULL ,`price_min` DOUBLE NOT NULL ,`price_max` DOUBLE NOT NULL ,`price_a` DOUBLE NOT NULL)");
	}
	
	public static ResultSet readDatabase(String query) throws SQLException
	{
		Statement stmt = con.createStatement();
	    ResultSet rs = stmt.executeQuery(query);
	    
	    return rs;
	    
	}
	public static void writeDatabase(String query) throws SQLException
	{
		Statement stmt = con.createStatement();
	    stmt.execute(query);
	    
	}
	
}
