package com.github.omwah;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;

/*
 * This is the main class of the plug-in
 */
public class LinkedBookShelf extends JavaPlugin {
    private static String[] DEFAULT_FACES = {"UP", "DOWN", "NORTH", "SOUTH", "EAST", "WEST"};
    private static String[] DEFAULT_HOLDERS = {"BREWING_STAND", "CHEST", "DISPENSER", "FURNACE"};
    
    /*
     * Loads the plugin
     */
    @Override
    public void onEnable() {
        // save the configuration file
        saveDefaultConfig();
        
        // Load list of faces to check from config
        ArrayList<BlockFace> check_faces = new ArrayList<BlockFace>();
        for(Object list_obj : this.getConfig().getList("check_faces", Arrays.asList(DEFAULT_FACES))) {
            if (list_obj != null && list_obj instanceof String) {
                String face_str = (String) list_obj;
                try {
                    check_faces.add(BlockFace.valueOf(face_str));
                } catch (IllegalArgumentException ex) {
                    getLogger().log(Level.SEVERE, "Illegal BlockFace string in check_faces config value: {0}", face_str);
                }
            }
        }
        // Make sure some faces are configured
        if(check_faces.isEmpty()) {
            getLogger().log(Level.SEVERE, "No faces configured for searching!");
        } else {
            getLogger().info("Configured block faces: " + check_faces);
        }
        
        // Load list of the types of InventoryHolders to allow linking with bookshelves
        ArrayList<Material> valid_holders = new ArrayList<Material>();
        for(Object list_obj : this.getConfig().getList("valid_holders", Arrays.asList(DEFAULT_HOLDERS))) {
            if (list_obj != null && list_obj instanceof String) {
                String holder_str = (String) list_obj;
                try {
                    valid_holders.add(Material.valueOf(holder_str));
                } catch (IllegalArgumentException ex) {
                    getLogger().log(Level.SEVERE, "Illegal Material string in valid_holders config value: {0}", holder_str);
                }
            }
        }
        // Make sure holders are configured
        if(valid_holders.isEmpty()) {
            getLogger().log(Level.SEVERE, "No valid holders configured. Bookshelves can not be linked to anything!");
        } else {
            getLogger().info("Configured holders: " + valid_holders);
        }
        
        
        // Create the SampleListener
        new LinkedBookShelfListener(this, check_faces, valid_holders);
        
        // Try and send metrics to MCStats
        try {
            Metrics metrics = new Metrics(this);
            metrics.start();
        } catch (IOException e) {
            getLogger().log(Level.SEVERE, "Could not send data to MCStats!");
        }
    }
    
    /*
     * Shuts down plugin
     */
    @Override
    public void onDisable() {
        
    }

}
