package de.thelaumix.wac.Configuration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import de.thelaumix.wac.main;
import de.thelaumix.wac.Database.Database;

public class RefreshContent {
	
	private PrintWriter out;
	
	private String lg;
	
	public RefreshContent()
	{
		lg = "##################################################\n##         LOGFILE DES DATENBANKUPDATES         ##\n##################################################";
	}
	
	public void refreshTable(Database d) throws SQLException, SecurityException, IOException
	{
		
		
		
		main m = new main();
		
				m.o("Starte Datenbankupdate...");
		Timestamp thetime = new Timestamp(System.currentTimeMillis());
		long time_old = thetime.getTime();
				l("Logging des Updates aktiviert.");
				m.o("Logging aktiv");
				m.o("Leere alte Tabelle...");
				l("Leere die alte Tabelle " + main.db_table);
		d.clearTable();
				l("Tabelle " + main.db_table + " geleert.");
				m.o("Lese Webauction-Datenbank...");
				l("Beginne mit dem Lesen der Webauctiondatenbank...");
		ResultSet rs = d.readDatabase("SELECT * FROM `WA_Auctions`");
				l("Erfolgreich.");
				m.o("Vorbereiten der Hashmaps...");
				l("Bereite Hashmaps vor...");
		HashMap<Integer, Double> h_priceA = new HashMap<Integer, Double>();
		HashMap<Integer, Double> h_priceMin = new HashMap<Integer, Double>();
		HashMap<Integer, Double> h_priceMax = new HashMap<Integer, Double>();
		HashMap<Integer, Integer> h_count = new HashMap<Integer, Integer>();
				l("Hashmaps aktiv.");
				m.o("Beginne Auswertung...");
				l("\nStarte Auswertung der Daten:\n");
		while (rs.next())
		{
			int name = rs.getInt("name");
			//Counter
			int count = 0;
			try{count = h_count.get(name);}
			  catch(Exception e){count = 0;}
			count++;
			h_count.put(name, count);
			
			//Preis
			double price = 0.0;
			try{price = h_priceA.get(name);}
			  catch(Exception e){price = 0.0;}
					
			double gprice = rs.getDouble("price");
					la("ITEM[" + name + "] x" + count + ":\n     Bisheriger Durchschnittspreis:" + String.valueOf(price) + "\n     Itempreis:" + String.valueOf(gprice));
			
			try{if (price < h_priceMin.get(name)){h_priceMin.put(name, price);l("     =>Neuer Minimalpreis!<=");}}
			  catch(Exception e){h_priceMin.put(name, gprice);}
			try{if (price > h_priceMax.get(name)){h_priceMax.put(name, price);l("     =>Neuer Maximalpreis!<=");}}
			  catch(Exception e){h_priceMax.put(name, gprice);}
			
			price += gprice;
			
			h_priceA.put(name, price);
			l("");
		}
		
				l(">> Auslesung der Datenwerte beendet. <<");
		Set keys = h_priceA.keySet();
				l("Existierende Keyvalues sortiert und geordnet.");
		
				m.o("Beginne Eintragungen in neue Datenbank...");
				l("Beginne das Einschreiben in die neue Tabelle:");
				
		for (Iterator i = keys.iterator(); i.hasNext();)
		{
			int name = (Integer) i.next();
			double priceA = h_priceA.get(name);
			priceA = priceA / h_count.get(name);
			
			double priceMin = h_priceMin.get(name);
			double priceMax = h_priceMax.get(name);
			
			try {
				d.writeDatabase("INSERT INTO `" + main.db_table + "` (`item`, `price_min`, `price_max`, `price_a`) VALUES ('" + name + "', '" + String.valueOf(priceMin) + "', '" + String.valueOf(priceMax) + "', '" + String.valueOf(priceA) + "')");
				l(" => ITEM " + name + " mit x<<:" + String.valueOf(priceMin) + " x>>:" + String.valueOf(priceMax) + " a::" + String.valueOf(priceA));
			} catch (SQLException e) {
				m.o("Konnte Datenwert nicht speichern: " + name + " (" + e.getMessage() + ")");
				w("  X=> FEHLER BEIM SPEICHERN VON ITEM " + name + ": " + e.getMessage());
			}
			
		}
		
		thetime = new Timestamp(System.currentTimeMillis());
		long time_new = thetime.getTime();
		int seconds = (int) (time_new - time_old);
		
				m.o("Die Datenbank wurde erfolgreich aktualisiert! (" + String.valueOf(seconds) + "ms)");
				m.o("WA-Hashwert: " + rs.hashCode());
				l("\nDie Datenbank wurde mit dem Hashwert " + rs.hashCode() + " aktualisiert.");
				
				saveLog(lg);
				
		
	}
	
	private void l(String msg)
	{
		lg += msg + "\n";
	}
	private void la(String msg)
	{
		lg += msg;
	}
	private void w(String msg)
	{
		lg += "[WARNUNG] " + msg + "\n";
	}
	
	private void saveLog(String log)
	{
		try{
			  // Create file 
			  FileWriter fstream = new FileWriter("plugins/WACentral/refresh.log");
			  BufferedWriter out = new BufferedWriter(fstream);
			  out.write(log);
			  //Close the output stream
			  out.close();
			  }catch (Exception e){//Catch exception if any
			  System.err.println("Error: " + e.getMessage());
			  }
	}

}
