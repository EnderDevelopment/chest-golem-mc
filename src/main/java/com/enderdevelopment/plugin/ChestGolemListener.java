package com.enderdevelopment.plugin;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ChestGolemListener implements Listener {

    private final Main plugin;

    public ChestGolemListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlockPlaced();

        if (block.getType() == Material.CHEST) {
            // Check if the structure is complete
            if (isCompleteStructure(block.getLocation())) {
                ChestGolem golem = new ChestGolem(block.getLocation(), player);
                golem.createStructure();
                player.sendMessage("Chest Golem created!");
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player player = event.getPlayer();
            Block block = event.getClickedBlock();

            if (block != null && block.getType() == Material.CHEST) {
                // Check if it's a chest golem structure
                if (isChestGolemStructure(block.getLocation())) {
                    ChestGolem golem = getChestGolem(block.getLocation());
                    if (golem != null && !golem.isTamed()) {
                        ItemStack item = player.getInventory().getItemInMainHand();
                        if (golem.tame(item)) {
                            player.sendMessage("Chest Golem tamed!");
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            if (player.getInventory().getItemInMainHand().getType() == Material.CHEST) {
                // Place the chest golem item
                Block targetBlock = player.getTargetBlock(null, 5);
                if (targetBlock != null && targetBlock.getType() == Material.AIR) {
                    ChestGolem golem = new ChestGolem(targetBlock.getLocation(), player);
                    golem.createStructure();
                    player.getInventory().getItemInMainHand().setAmount(
                        player.getInventory().getItemInMainHand().getAmount() - 1);
                    player.sendMessage("Chest Golem placed!");
                }
            }
        }
    }

    private boolean isCompleteStructure(Location location) {
        // Check for stone pillars
        for (int x = -1; x <= 1; x += 2) {
            for (int z = -1; z <= 1; z += 2) {
                for (int y = 0; y < 2; y++) {
                    Block block = location.clone().add(x, y, z).getBlock();
                    if (block.getType() != Material.STONE) {
                        return false;
                    }
                }
            }
        }

        // Check for flat roof
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                Block block = location.clone().add(x, 2, z).getBlock();
                if (block.getType() != Material.STONE) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean isChestGolemStructure(Location location) {
        // Check if the block is part of a chest golem structure
        // This is a simplified check
        Block block = location.getBlock();
        if (block.getType() == Material.CHEST) {
            Block below = block.getRelative(BlockFace.DOWN);
            if (below.getType() == Material.STONE) {
                return true;
            }
        }
        return false;
    }

    private ChestGolem getChestGolem(Location location) {
        // In a real implementation, you would track active chest golems
        // For this example, we'll just create a new one
        return new ChestGolem(location, null);
    }
}