package de.thelaumix.wac;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import de.thelaumix.wac.Configuration.LoadConfigs;
import de.thelaumix.wac.Configuration.RefreshContent;
import de.thelaumix.wac.Configuration.ReloadContent;
import de.thelaumix.wac.Database.Database;
import de.thelaumix.wac.Events.PlayerWatch;
import de.thelaumix.wac.Events.Objects.Sensor;

public class main extends JavaPlugin {
	
	public PlayerWatch pwatch = new PlayerWatch(this);
	
	public static File ppl_file = new File("plugins/WACentral", "stations.yml");
	public static FileConfiguration ppl;
	
	public static HashMap<Integer, String> infotable = new HashMap<Integer, String>();
	
	public static String db_host;
	public static String db_user;
	public static String db_pass;
	public static String db_db;
	public static String db_table;
	
	public static ArrayList sensorlist = new ArrayList();
	
	public Database d = new Database();
	
	public Sensor s = new Sensor(this);
	
	@Override
	public void onDisable()
	{
		try
		{
			d.closeConnection();
			o("Datenbankverbindung geschlossen");
		} catch (Exception e) {
			o("Datenbankverbindung konnte nicht geschlossen werden");
		}
		o("deaktiviert");
		
	}
	
	@Override
	public void onEnable()
	{
		
		this.reloadConfig();
		this.getConfig().addDefault("Database.host", "mysql://host:3306/database");
		this.getConfig().addDefault("Database.username", "user");
		this.getConfig().addDefault("Database.password", "pass");
		this.getConfig().addDefault("Database.table", "WA_Average");
		this.getConfig().options().copyDefaults(true);
        this.saveConfig();
        initVars();
        try
        {
        	d.connectToDatabase(db_host, db_user, db_pass, db_table);
        	o("Verbindung zur Datenbank hergestellt");
        } catch (Exception e) {
        	o("Datenbankverbindung konnte nicht hergestellt werden");
        }
        new LoadConfigs(this);
        reload();
		Bukkit.getPluginManager().registerEvents(pwatch,this);
		o("geladen und aktiviert");
				
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String args[]) {
		
		
		CommandSender p = sender;
		String c = cmd.getName();
		
		
		if (c.equalsIgnoreCase("wac"))
		{
			if (!(p.hasPermission("wac.admin")))
			{
				p.sendMessage("[§2WACentral§f] §aKeine Berechtigung.");
				return false;
			}
			if (args.length == 0)
			{
				return true;
			}
			
			if (args[0].equalsIgnoreCase("set"))
			{
				if (!(p instanceof Player))
				{
					p.sendMessage("§a[WACentral] Dies ist nur ingame moeglich.");
					return true;
				}
				Player px = (Player) p;
				try
				{
					Location l = px.getTargetBlock(null, 100).getLocation();
					l = px.getLocation();
					s.addSensor(l, args[1]);
					p.sendMessage("[§2WACentral§f] §aSensor §b" + args[1] + " §awurde erfolgreich gespeichert.");
					p.sendMessage("   (§a X = §b" + l.getBlockX() + " §a; Y = §b" + (l.getBlockY() + 1) + " §a; Z = " + l.getBlockZ() + " §f)");
				} catch(Exception e)
				{
					p.sendMessage("§a/wac set <name>");
				}
				return true;
			}
			if (args[0].equalsIgnoreCase("refresh"))
			{
				try
				{
					p.sendMessage("§6Starte Aktualisierung der Wertetabelle...");
					refresh();
					p.sendMessage("§2Wertetabelle erfolgreich aktualisiert.");
				} catch (Exception e) {
					p.sendMessage("§4Fehler beim Updaten der Wertetabelle.");
				}
				return true;
			}
			if (args[0].equalsIgnoreCase("reload"))
			{
				try
				{
					p.sendMessage("§6Reload gestartet...");
					reload();
					p.sendMessage("§2Reload abgeschlossen!");
				} catch (Exception e) {
					p.sendMessage("§4Fehler beim Reload.");
				}
				return true;
			}
			p.sendMessage("§6Unbekannter Befehl.");
		}
		
		
		return false;
		
	}
	
	public void refresh()
	{
		try {
			new RefreshContent().refreshTable(d);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void reload()
	{
		try {
			new ReloadContent(d, this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void initVars()
	{
		db_host = getConfig().getString("Database.host");
		db_user = getConfig().getString("Database.username");
		db_pass = getConfig().getString("Database.password");
		db_table = getConfig().getString("Database.table");
		String[] h = db_host.split("/");
		db_db = h[(h.length - 1)];
	}
	
	public void o(String msg)
	{
		System.out.println("[WACentral] " + msg);
	}
	
		
	

}