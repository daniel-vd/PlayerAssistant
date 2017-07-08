package com.mclovesmy.PlayerAssistant;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import com.mclovesmy.PlayerAssistant.tasks.Fight;
import com.mclovesmy.PlayerAssistant.tasks.Plant;

public class PlayerAssistant extends JavaPlugin implements CommandExecutor{
	
	AssistantEntity assistantEntity = new AssistantEntity();
	
	@Override
	public void onEnable() {
		this.getCommand("pa").setExecutor(this);
		this.getCommand("playerassistant").setExecutor(this);
		
		getServer().getPluginManager().registerEvents(new Fight(), this);
		getServer().getPluginManager().registerEvents(new AssistantEntity(), this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			
			if (cmd.getName().equalsIgnoreCase("pa") || cmd.getName().equalsIgnoreCase("PlayerAssistant")) {
				if (args.length == 0) { //Show help when no argument is specified
					sender.sendMessage(ChatColor.BLUE + "PlayerAssistant commands:");
					sender.sendMessage(ChatColor.BLUE + " - /pa spawn | Spawn your assistant");
					sender.sendMessage(ChatColor.BLUE + " - /pa plant <seed;carrot;> | Let your assisant plant");
				} else if (args[0].equalsIgnoreCase("spawn"))  { //Spawn assistant
					assistantEntity.spawnAssistant(player);
				} else if (args[0].equalsIgnoreCase("plant")) { //Plant seeds
					if (assistantEntity.assistant != null) {
						if (args.length == 2) { //Seed must be specified
							Plant plant = new Plant();
						
							plant.plant(player, assistantEntity.assistant, args[1]);
						} else {
							player.sendMessage(ChatColor.BLUE + "Please specify a crop type (carrot/seed)");
						}
					} else {
						player.sendMessage(ChatColor.BLUE + "Please spawn your assistant first with '/pa spawn'");
					}
				}
				
				//List<Location> string = getArenaBlocks(player.getLocation(), 4);
			
				//follow = false;
				
				
				/*while (temp) {
					if (string.size() != 0) {
						Location location = string.get(0);
						villager.teleport(location);
						
						Location loc = location;
						
						loc.setY(loc.getY() + 1);
						
						if (loc.getBlock().getType() == Material.CARROT) {
							break;
						}
						
						loc.getBlock().setType(Material.CARROT);
						
						List<Location> string2 = getArenaBlocks(player.getLocation(), 4);
						
						if (string2.size() > 0) {
							continue;
						}
					}
					
					temp = false;
					follow = true;
				} */
			
			
				/*for (int i = 0; i < string.size(); i++) {
					Location location = string.get(i);
						Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
						     public void run() {
						    	 player.sendMessage("okeeeee");
						     }
						}, (3 * 20));
						 
				
					}*/
				}
			}
			
		
		return true;
	}
	

	
	
	
    private static List<Location> getArenaBlocks(Location l, int radius) {
    	World w = l.getWorld();
    	int xCoord = (int) l.getX();
    	int zCoord = (int) l.getZ();
    	int YCoord = (int) l.getY();
 
    	List<Location> tempList = new ArrayList<Location>();
    	for (int x = -radius; x <= radius; x++) {
    		for (int z = -radius; z <= radius; z++) {
    			for (int y = -radius; y <= radius; y++) {
    				tempList.add(new Location(w, xCoord + x, YCoord + y, zCoord + z));
    			}
    		}
    	}
    	
    	List<Location> tempList2 = new ArrayList<Location>();
    	
    	for (Location location : tempList) {
    		if (location.getBlock().getType() == Material.SOIL) {
    			tempList2.add(location);
    		}
    	}
    	
    	return tempList2;
    }
	
}
