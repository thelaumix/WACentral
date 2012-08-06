package de.thelaumix.wac.Events.Objects;

import java.io.IOException;

import org.bukkit.Location;

import de.thelaumix.wac.main;

public class Sensor {
	
	final main plugin;
	
	public Sensor(main pl)
	{
		this.plugin = pl;
	}
	
	public void addSensor(Location l, String name) throws IOException
	{
		String x = name + ".x";
		String y = name + ".y";
		String z = name + ".z";
		plugin.ppl.set(name, l.getBlockX() + "|" + (l.getBlockY() + 1) + "|" + l.getBlockZ());
		plugin.ppl.save(main.ppl_file);
	}

}
