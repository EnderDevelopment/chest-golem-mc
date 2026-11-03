package com.enderdevelopment.plugin;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.type.TrapDoor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ChestGolem {

    private final Location location;
    private final Player owner;
    private boolean isTamed = false;
    private int health = 3;

    public ChestGolem(Location location, Player owner) {
        this.location = location;
        this.owner = owner;
    }

    public boolean createStructure() {
        // Create the stone pillars
        for (int x = -1; x <= 1; x += 2) {
            for (int z = -1; z <= 1; z += 2) {
                for (int y = 0; y < 2; y++) {
                    Block block = location.clone().add(x, y, z).getBlock();
                    block.setType(Material.STONE);
                }
            }
        }

        // Create the flat roof
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                Block block = location.clone().add(x, 2, z).getBlock();
                block.setType(Material.STONE);
            }
        }

        // Create the trap door chest on top
        Block chestBlock = location.clone().add(0, 3, 0).getBlock();
        chestBlock.setType(Material.CHEST);
        Chest chest = (Chest) chestBlock.getState();

        BlockData blockData = Material.IRON_TRAPDOOR.createBlockData();
        if (blockData instanceof Directional) {
            Directional directional = (Directional) blockData;
            directional.setFacing(BlockFace.DOWN);
            chestBlock.setBlockData(directional);
        }

        return true;
    }

    public boolean tame(ItemStack item) {
        if (item.getType() == Material.ROTTEN_FLESH && item.getAmount() >= 3) {
            item.setAmount(item.getAmount() - 3);
            isTamed = true;
            return true;
        }
        return false;
    }

    public void hit() {
        health--;
        if (health <= 0) {
            // Transform into an item in the player's pocket
            owner.getInventory().addItem(new ItemStack(Material.CHEST));
            // Remove the structure
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    for (int y = 0; y < 3; y++) {
                        Block block = location.clone().add(x, y, z).getBlock();
                        block.setType(Material.AIR);
                    }
                }
            }
        }
    }

    public boolean isTamed() {
        return isTamed;
    }

    public Player getOwner() {
        return owner;
    }

    public Location getLocation() {
        return location;
    }
}