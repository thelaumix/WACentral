package de.thelaumix.wac.Configuration;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;

import de.thelaumix.wac.main;

public class LoadConfigs {
	
	public main plugin;
	
	public LoadConfigs(main pl)
	{
		this.plugin = pl;
		load();
	}
	private void load()
	{
		plugin.ppl = YamlConfiguration.loadConfiguration(main.ppl_file);
	}

}
