package com.mclovesmy.PlayerAssistant.tasks;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import com.mclovesmy.PlayerAssistant.AssistantEntity;
import com.mclovesmy.PlayerAssistant.PlayerAssistant;

public class Fight implements Listener {
	
	AssistantEntity assistantEntity = new AssistantEntity();
	
	int count = 0;
	
	int id;
	
	boolean focus = false;
	
	@EventHandler
	public void onEntityDamageEvent(EntityDamageByEntityEvent e) {
		
		if (focus) {
			return;
		}
		
			if (e.getDamager() instanceof Player ) {
				Player player = (Player) e.getDamager();
				Entity entity = e.getEntity();
				
				focus = true;
				
				id = Bukkit.getScheduler().scheduleSyncRepeatingTask(JavaPlugin.getPlugin(PlayerAssistant.class), new Runnable() {
		            @Override
		            public void run() {
		            	
						//Do we want to run this scheduler?
						int distanceBetween = (int) entity.getLocation().distance(AssistantEntity.assistant.getLocation());
						if (entity.isDead() || distanceBetween > 10) {
							Bukkit.getScheduler().cancelTask(id);
							
							count = 0;
							focus = false;
						}
						
		            	Location assistantHead = AssistantEntity.assistant.getLocation();
						
						assistantHead.setY(assistantHead.getY() + 3);
			
						
						Location targetHead = entity.getLocation();
						targetHead.setY(targetHead.getY() + 1);
						
						Vector vector = targetHead.toVector().subtract(assistantHead.toVector());
						
						Location loc = player.getLocation();
						loc.setY(loc.getY() + 2);
						
						//Assistant one block in front of head location
						Vector vec = AssistantEntity.assistant.getLocation().getDirection();
						
						Location assistantFrontLocation = assistantHead.add(vec);
						
						AssistantEntity.assistant.getLocation().setDirection(entity.getLocation()
								.subtract(AssistantEntity.assistant.getLocation()).toVector());
						
						Arrow arrow = player.getLocation().getWorld().spawnArrow(assistantHead, vector, 3, 0);
						arrow.setShooter(player);
						arrow.setKnockbackStrength(0);
						arrow.setBounce(false);
				
						count++;
		            }
		        }, 0L, 100L);
		}
	}
}
