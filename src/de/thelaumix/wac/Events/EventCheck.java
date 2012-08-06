package de.thelaumix.wac.Events;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import de.thelaumix.wac.main;

public class EventCheck extends Thread{
	
	private String name;
	
	public EventCheck(String n)
	{
		this.name = n;
	}
	
	public void run()
	{
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		try
		{
			Player p = Bukkit.getPlayer(name);
			Location lp = p.getLocation();
			if (!(main.sensorlist.contains(lp.getBlockX() + "|" + (lp.getBlockY() + 1) + "|" + lp.getBlockZ())))
			{
				return;
			}
			BlockState b = p.getWorld().getBlockAt(lp.getBlockX(), lp.getBlockY() + 1, lp.getBlockZ()).getState();
			if (b instanceof Sign)
			{
				Sign s = (Sign)b;
				
				s.setLine(0, "§f [§1INFO§f]");
				int name = p.getItemInHand().getTypeId();
				String iteminfos = main.infotable.get(name);
				if (iteminfos == null)
				{
					s.setLine(1, "");
					s.setLine(2, "§4 Keine Angabe");
					s.setLine(3, "");
					s.update();
					return;
				}
				String[] ii = iteminfos.split(";");
				s.setLine(1, "§2 N: $" + ii[0]);
				s.setLine(2, "§4 H: $" + ii[1]);
				s.setLine(3, "§3 A: $" + ii[2]);
				s.update();
				lp.getWorld().playEffect(lp, Effect.CLICK2, 0);
			}
		} catch (Exception ex)
		{
			
		}
	}

}
