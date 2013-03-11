package com.github.omwah;

import java.util.List;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

/*
 * Listens for clicks on bookshelves and then searches for chests to open for
 * the player
 */
public class LinkedBookShelfListener implements Listener {
    private final LinkedBookShelf plugin;
    List<BlockFace> check_faces;
    List<Material> valid_holders;

    /*
     * This listener needs to know about the plugin which it came from
     */
    public LinkedBookShelfListener(LinkedBookShelf plugin, List<BlockFace> check_faces, List<Material> valid_holders) {
        this.plugin = plugin;
        this.check_faces = check_faces;
        this.valid_holders = valid_holders;
        
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
        for (BlockFace face : check_faces) {
            Block test_block = bookshelf.getRelative(face);
            if(test_block.getState() instanceof InventoryHolder && valid_holders.contains(test_block.getType())) {
                // Open the InventoryHolder to the player
                // To make sure in the case of a double chest we get the double
                // chest inventory and not the left or right side, we get the holder
                // of the chest then the inventory of the holder
                if(event.getPlayer().hasPermission("linkedbookshelf.use")) {
                    Inventory chest_inv = ((InventoryHolder)test_block.getState()).getInventory().getHolder().getInventory();
                    event.getPlayer().openInventory(chest_inv);
                    event.setCancelled(true);
                } else {
                    // Issue the permissions error message this way so at least the player get some feedback
                    event.getPlayer().sendMessage("You must have the linkedbookshelf.use permission to use linked bookshelves");
                }
                return;
            }
        }

    }
}
