package com.github.omwah;

import java.text.MessageFormat;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

/*
 * Listens for clicks on bookshelves and then searches for chests to open for
 * the player
 */
public class LinkedBookShelfListener implements Listener {
    private final LinkedBookShelf plugin;
    List<BlockFace> check_faces;

    /*
     * This listener needs to know about the plugin which it came from
     */
    public LinkedBookShelfListener(LinkedBookShelf plugin, List<BlockFace> check_faces) {
        this.plugin = plugin;
        this.check_faces = check_faces;
        
        // Register the listener
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

    }

    /*
     * When a player clicks on a bookshelf, if there is a chest touching
     * the chest then its inventory is opened to the player
     */
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        // Quickly ignore everything but a right click on bookshelf
        Block clicked_block = event.getClickedBlock();
        if(event.getAction() != Action.RIGHT_CLICK_BLOCK || clicked_block.getType() != Material.BOOKSHELF) {
            return;
        }
        
        Block bookshelf = event.getClickedBlock();
        for (BlockFace face : BlockFace.values()) {
            // Ignore any face not configured to be searched
            if(check_faces.contains(face)) {
                Block test_block = bookshelf.getRelative(face);

                if(test_block.getType() == Material.CHEST) {
                    // Open the chest to the player
                    event.getPlayer().openInventory(((Chest)test_block.getState()).getBlockInventory());
                    event.setCancelled(true);
                    return;
                }
            }
        }

    }
}
