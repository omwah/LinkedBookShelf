package com.github.omwah;

import org.bukkit.plugin.java.JavaPlugin;

/*
 * This is the main class of the sample plug-in
 */
public class LinkedBookShelf extends JavaPlugin {
    /*
     * This is called when your plug-in is enabled
     */
    @Override
    public void onEnable() {
        // save the configuration file
        saveDefaultConfig();
        
        // Create the SampleListener
        new LinkedBookShelfListener(this);
        
        // set the command executor for sample
        this.getCommand("sample").setExecutor(new LinkedBookShelfCommandExecutor(this));
    }
    
    /*
     * This is called when your plug-in shuts down
     */
    @Override
    public void onDisable() {
        
    }

}
