package de.thelaumix.wac.Events;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import de.thelaumix.wac.main;

public class PlayerWatch implements Listener{
	
	final main plugin;
	
	public PlayerWatch(main pl)
	{
		this.plugin = pl;
	}
	
	@EventHandler
	public void Druckplattensensor(PlayerInteractEvent e)
	{
		if (e.getAction() == Action.PHYSICAL)
		{
			EventCheck x = new EventCheck(e.getPlayer().getName());
			x.start();
		}
	}

}
