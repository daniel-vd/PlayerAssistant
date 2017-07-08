package com.mclovesmy.PlayerAssistant.tasks;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.mclovesmy.PlayerAssistant.AssistantEntity;
import com.mclovesmy.PlayerAssistant.PlayerAssistant;

public class Plant{
	
	AssistantEntity assistantEntity = new AssistantEntity();
	
	Server server = Bukkit.getServer();
	
	Material crop;
	Material cropName;
	
	final BlockFace[] faces = {
			    BlockFace.DOWN,
			    BlockFace.UP,
			    BlockFace.NORTH,
			    BlockFace.EAST,
			    BlockFace.SOUTH,
			    BlockFace.WEST
			};
	
	public void plant (Player player, Entity villager, String cropArg) {;
		
		Block block = player.getTargetBlock((Set<Material>) null, 5);
		
		if (block.getType() != Material.SOIL) {
			player.sendMessage("That is not a soil block!");
			return;
		}
		if (!AssistantEntity.hashmap.containsKey(player.getUniqueId())) {
			player.sendMessage(ChatColor.BLUE + "Spawn your assistant first: /pa spawn");
		}
		
 	   //This will be extended in the feature, should be a switch then
 	   if (cropArg.equalsIgnoreCase("carrot")) {
 		   crop = Material.CARROT;
 	   } else if (cropArg.equalsIgnoreCase("seed")) {
 		   crop = Material.CROPS;
 	   }
 	  
		
		Set<Block> connectedBlocks = getConnectedblocks(block);
 	   
		if (crop == Material.CARROT) {
			cropName = Material.CARROT_ITEM;
		} else if (crop == Material.CROPS) {
			cropName = Material.SEEDS;
		}
		
 	   //Does the player own the specified crop
 	   if (!player.getInventory().contains(cropName, connectedBlocks.size())) {
 		   player.sendMessage("Oops. You don't have enough " + cropArg + " in your inventory!");
 		   return;
 	   }
 	      
	   //Remove the crops
	   player.getInventory().removeItem(new ItemStack(cropName, connectedBlocks.size()));
 	   
		for (Block blockje : connectedBlocks) {
			
		     Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(JavaPlugin.getPlugin(PlayerAssistant.class), new Runnable(){
                
                   public void run(){
   					
                	   //Set location of the crop on top of the soil block
                	   Location loc = blockje.getLocation();
                	   loc.setY(loc.getY() + 1);
   					
                	   //Place crop, teleport villager there
                	   loc.getBlock().setType(crop);
                	   villager.teleport(loc);
   					
                	   //Make sure the assistant will stay at the crop while planting
                	   assistantEntity.follow = false;
                   } 
           }, 10 * connectedBlocks.size());
		}
		player.sendMessage(ChatColor.BLUE + "Your assistant will finish planting these seeds soon!");
		
		assistantEntity.follow = true;
	}
	
	private void getConnectedblocks(Block block, Set<Block> results, List<Block> todo) {
	    //Here I collect all blocks that are directly connected to variable 'block'.
	    //(Shouldn't be more than 6, because a block has 6 sides)
	    Set<Block> result = results;

	    //Loop through all block faces (All 6 sides around the block)
	    for(BlockFace face : faces) {
	        Block b = block.getRelative(face);
	        //Check if they're both of the same type
	        if(b.getType() == block.getType()) {
	            //Add the block if it wasn't added already
	            if(result.add(b)) {

	                //Add this block to the list of blocks that are yet to be done.
	                todo.add(b);
	            }
	        }
	    }
	}
	
    public Set<Block> getConnectedblocks(Block block) {
    Set<Block> set = new HashSet<>();
    LinkedList<Block> list = new LinkedList<>();

    //Add the current block to the list of blocks that are yet to be done
    list.add(block);

    //Execute this method for each block in the 'todo' list
    while((block = list.poll()) != null) {
        getConnectedblocks(block, set, list);
    }
    return set;
}
	
} 
