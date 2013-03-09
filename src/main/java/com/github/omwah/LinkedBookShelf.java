package com.github.omwah;

import java.util.ArrayList;
import java.util.logging.Level;
import org.bukkit.block.BlockFace;
import org.bukkit.plugin.java.JavaPlugin;

/*
 * This is the main class of the plug-in
 */
public class LinkedBookShelf extends JavaPlugin {
    /*
     * Loads the plugin
     */
    @Override
    public void onEnable() {
        // save the configuration file
        saveDefaultConfig();
        
        // Load list of faces to check from config
        ArrayList<BlockFace> check_faces = new ArrayList<BlockFace>();
        for(Object list_obj : this.getConfig().getList("check_faces")) {
            if (list_obj != null && list_obj instanceof String) {
                String face_str = (String) list_obj;
                try {
                    check_faces.add(BlockFace.valueOf(face_str));
                } catch (IllegalArgumentException ex) {
                    getLogger().log(Level.SEVERE, "Illegal string of BlockFace in check_faces config value: {0}", face_str);
                }
            }
        }
        // Make sure some faces are configured
        if(check_faces.size() == 0) {
            getLogger().log(Level.SEVERE, "No faces configured for searching!");
        }
        
        // Create the SampleListener
        new LinkedBookShelfListener(this, check_faces);
    }
    
    /*
     * Shuts down plugin
     */
    @Override
    public void onDisable() {
        
    }

}
