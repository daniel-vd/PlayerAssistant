package com.mclovesmy.PlayerAssistant;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_12_R1.EntityInsentient;
import net.minecraft.server.v1_12_R1.PathEntity;

public class AssistantEntity implements Listener {
	
	public static Entity assistant;
	
	public boolean follow = true;

	public static HashMap<UUID, UUID> hashmap = new HashMap<UUID, UUID>();
	
	int id;
	
	public void follow (Player player, Entity entity) {
		
		UUID uuid = player.getUniqueId();
		
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(JavaPlugin.getPlugin(PlayerAssistant.class), new Runnable() {
            @Override
            public void run() {
            	
            	if (!assistant.isValid()) {
            		Bukkit.getScheduler().cancelTask(id);
            	hashmap.remove(uuid);
            	return;
            	}
            		        
            	if (!player.isOnline()) {
            		Bukkit.getScheduler().cancelTask(id);
            	assistant.remove();
            	hashmap.remove(uuid);
            	 return;
            	}
            	
            	if (follow == false) {
            		Bukkit.getScheduler().cancelTask(id);
            	}
            	
            	
            	Object petObject = ((CraftEntity)entity).getHandle();
            	Location loc = player.getLocation();
            	
            	PathEntity path = ((EntityInsentient)petObject).getNavigation().a(loc.getX() + 1.0D, loc.getY(), loc.getZ() + 1.0D);
            	
                if (path != null) {
                	((EntityInsentient)petObject).getNavigation().a(path, 1.0D);
            	    ((EntityInsentient)petObject).getNavigation().a(2.0D);
            	}
                
            	if (!player.getWorld().getName().equalsIgnoreCase(entity.getWorld().getName())) {
            		hashmap.remove(uuid);
            		entity.remove();
            	} else {
            		int distance = (int)loc.distance(entity.getLocation());
            	           
            		if ((distance > 10) && (!entity.isDead()) && (player.isOnGround())) {
            			entity.teleport(loc);
            		}
            	}
            }
        }, 0L, 20L);
	}
	
	public void spawnAssistant (Player player) {
		UUID uuid = player.getUniqueId();
		
		if (!hashmap.containsKey(uuid)) {
			
			assistant = (Entity) player.getWorld().spawnEntity(player.getLocation(), EntityType.VILLAGER);
			assistant.setCustomName(player.getDisplayName() + "'s assistant");
			((Ageable) assistant).setBreed(false);
			assistant.setInvulnerable(true);
			((LivingEntity) assistant).setRemoveWhenFarAway(false);

			UUID assistantUUID = assistant.getUniqueId();
			
			hashmap.put(uuid, assistantUUID);
			
			player.sendMessage(ChatColor.BLUE + "entity uuid: " + assistantUUID);
			
			follow(player, assistant);
		} else {
			boolean test = hashmap.containsValue(assistant.getUniqueId());
			player.sendMessage(ChatColor.BLUE + "Your assistant already exists! " + test + " uuid = " + assistant.getUniqueId());
		}
	}
	
	@EventHandler
	public void onEntityDamage(EntityDamageEvent e) {
		Entity assistant = e.getEntity();
		UUID assistantUUID = assistant.getUniqueId();
	    
		if (hashmap.containsValue(assistantUUID)) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onEntityTarget(EntityTargetEvent e) {
		Entity assistant = e.getEntity();
		UUID assistantUUID = assistant.getUniqueId();
	    
		if (((e.getTarget() instanceof LivingEntity)) && 
			(hashmap.containsValue(assistantUUID))) {
			e.setCancelled(true);
		}
	}
}
