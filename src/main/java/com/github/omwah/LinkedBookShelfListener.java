package com.github.omwah;

import java.util.List;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
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
     * Checks that the player referenced in the event has permission to use the plugin
     */
    private boolean hasPermission(Player player) {
        if(player.hasPermission("linkedbookshelf.use")) {
            return true;
        } else {
            // Issue the permissions error message this way so at least the player get some feedback
            player.sendMessage("You must have the linkedbookshelf.use permission to use linked bookshelves");
            return false;
        }
    }
    
    /* 
     * Opens InventoryHolders such as chests, hoppers, furnaces, etc
     */

    private void openInventoryHolder(PlayerInteractEvent event, Block block, BlockFace face) {
 
        if(!hasPermission(event.getPlayer())) {
            return;
        }
        
        // Send an a new event to indicate we are opening the InventoryHolder,
        // this way protection plugins have the ability to cancel our action
        PlayerInteractEvent open_event = 
                new PlayerInteractEvent(event.getPlayer(), event.getAction(), event.getItem(), 
                block, face.getOppositeFace());

        plugin.getServer().getPluginManager().callEvent(open_event);

        // Open the InventoryHolder to the player
        // To make sure in the case of a double chest we get the double
        // chest inventory and not the left or right side, we get the holder
        // of the chest then the inventory of the holder
        if(!open_event.isCancelled()) {
            Inventory holder_inv;
            if(block.getType() == Material.ENDER_CHEST) {
                holder_inv = event.getPlayer().getEnderChest();
            } else {
                holder_inv = ((InventoryHolder) block.getState()).getInventory().getHolder().getInventory();
            } 
            event.getPlayer().openInventory(holder_inv);
        }
        
        // Cancel original event since we handled things here, event if our
        // open_event is cancelled, we would still call cancel here
        event.setCancelled(true);      
    }
    
    /*
     * When a player clicks on a bookshelf, if there is a chest touching
     * the chest then its inventory is opened to the player
     */
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        // Quickly ignore everything but a right click on bookshelf
        Block clicked_block = event.getClickedBlock();
        if(event.getAction() != Action.RIGHT_CLICK_BLOCK || event.getPlayer().isSneaking() || clicked_block.getType() != Material.BOOKSHELF) {
            return;
        }
        
        // Test each configured face in the order defined to see if there is a 
        // valid block that can hold inventory
        Block bookshelf = event.getClickedBlock();
        for (BlockFace face : check_faces) {
            Block test_block = bookshelf.getRelative(face);
            if(test_block.getState() instanceof InventoryHolder 
                    || test_block.getType() == Material.ENDER_CHEST
                    && valid_holders.contains(test_block.getType())) {
                
                openInventoryHolder(event, test_block, face);
                // Return so no other blocks are checked
                return;
            }
        }

    }
}
