package de.thelaumix.wac.Configuration;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

import org.bukkit.configuration.file.FileConfiguration;

import de.thelaumix.wac.main;
import de.thelaumix.wac.Database.Database;

public class ReloadContent {
	
	private Database d;
	private main plugin;
	
	public ReloadContent(Database dat, main P)
	{
		d = dat;
		this.plugin = P;
		try
		{
			rel();
		} catch (Exception e)
		{
			
		}
	}
	
	@SuppressWarnings("unchecked")
	private void rel() throws SQLException
	{
		ResultSet rs = d.readDatabase("SELECT * FROM `" + main.db_table + "`");
		
		System.out.println("[WACentral] Pluginreload...");
		
		while (rs.next())
		{
			String price = String.valueOf(rs.getDouble("price_min")) + ";" + String.valueOf(rs.getDouble("price_max")) + ";" + String.valueOf(rs.getDouble("price_a"));
			main.infotable.put(rs.getInt("item"), price);
		}
		
		plugin.reloadConfig();
		Set<String> k = plugin.ppl.getKeys(false);
		
		String[] keys = k.toArray(new String[0]);
		
		for (int i = 0; i < keys.length ; i++)
		{
			@SuppressWarnings("static-access")
			String a = plugin.ppl.getString(keys[i]);
			main.sensorlist.add(a);
		}
		
	}

}
