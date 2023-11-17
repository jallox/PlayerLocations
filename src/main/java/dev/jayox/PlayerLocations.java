package dev.jayox;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class PlayerLocations extends JavaPlugin {
    public String version = getDescription().getVersion();
    public String defaultPrefix = "&f[&6Player&eLocations &f v" + version + "]: ";

    public String rutaConfig;
    @Override
    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', defaultPrefix + "&6     ____  __                      __                     __  _                 "));
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', defaultPrefix +"&e   / __ \\/ /___ ___  _____  _____/ /   ____  _________ _/ /_(_)___  ____  _____"));
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', defaultPrefix +"&6  / /_/ / / __ `/ / / / _ \\/ ___/ /   / __ \\/ ___/ __ `/ __/ / __ \\/ __ \\/ ___/"));
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', defaultPrefix +"&e / ____/ / /_/ / /_/ /  __/ /  / /___/ /_/ / /__/ /_/ / /_/ / /_/ / / / (__  ) "));
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', defaultPrefix +"&6/_/   /_/\\__,_/\\__, /\\___/_/  /_____/\\____/\\___/\\__,_/\\__/_/\\____/_/ /_/____/"));
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', defaultPrefix +" "));
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', defaultPrefix +"&aThanks for using &6Player&eLocations!"));
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', defaultPrefix +"&fDeveloped by &6JayoX"));
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', defaultPrefix +"&fRunning version: &e"+version));
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', defaultPrefix +" "));
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', defaultPrefix+" Checking for config.yml..."));
        configManager("1");
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', defaultPrefix +"&aThanks for using &6Player&eLocations!"));
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', defaultPrefix +"&fDeveloped by &6JayoX"));
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', defaultPrefix +"&cGood Bye!"));
    }

    public void configManager (String lastCfigVersion) {
        FileConfiguration configf = this.getConfig();
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', defaultPrefix+"Looking for config version "+lastCfigVersion));
        File config = new File(this.getDataFolder(),"config.yml");
        rutaConfig = config.getPath();
        if(!config.exists()){
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', defaultPrefix+"&cConfig.yml not found! Creating new one in /plugins/PlayerLocations/config.yml"));
            this.getConfig().options().copyDefaults(true);
            saveConfig();
        }

        if(configf.getString("ConfigVersion").equals(lastCfigVersion)) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', defaultPrefix+"&aYour config file is the last config file version!"));
        } else {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', defaultPrefix+"&cYou are not using a correct version of the config. Please delete the Config.yml and restart the server. The plugin will shut down now"));
            Bukkit.getPluginManager().disablePlugin(this);
        }

    }
}
