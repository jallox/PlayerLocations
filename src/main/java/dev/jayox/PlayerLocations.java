package dev.jayox;

//! Do not remove unused imports
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;

public final class PlayerLocations extends JavaPlugin implements Listener{
    public String version = getDescription().getVersion();
    public String defaultPrefix = getConfig().getString("prefix").replace("{0}", version);

    public String rutaConfig;

    @Override
    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage(" ");
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', defaultPrefix +"&6    ____  __                      __                     __  _                 "));
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', defaultPrefix +"&e   / __ \\/ /___ ___  _____  _____/ /   ____  _________ _/ /_(_)___  ____  _____"));
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', defaultPrefix +"&6  / /_/ / / __ `/ / / / _ \\/ ___/ /   / __ \\/ ___/ __ `/ __/ / __ \\/ __ \\/ ___/"));
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', defaultPrefix +"&e / ____/ / /_/ / /_/ /  __/ /  / /___/ /_/ / /__/ /_/ / /_/ / /_/ / / / (__  ) "));
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', defaultPrefix +"&6/_/   /_/\\__,_/\\__, /\\___/_/  /_____/\\____/\\___/\\__,_/\\__/_/\\____/_/ /_/____/"));
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', defaultPrefix +" "));
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', defaultPrefix +"&aThanks for using &6Player&eLocations!"));
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', defaultPrefix +"&fDeveloped by &6JayoX"));
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', defaultPrefix +"&fRunning version: &e"+version));
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', defaultPrefix +" "));
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', defaultPrefix +"Checking for config.yml..."));
        configManager("4");
        this.getCommand("playerlocations").setExecutor(new mainCommand(this));
        this.getCommand("country").setExecutor(new country(this));
        checkForKey();
        getServer().getPluginManager().registerEvents(this, this);
        Bukkit.getConsoleSender().sendMessage(" ");
        Bukkit.getConsoleSender().sendMessage("§aStarted succesfully!");


    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', defaultPrefix +"&aThanks for using &6Player&eLocations!"));
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', defaultPrefix +"&fDeveloped by &6JayoX"));
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', defaultPrefix + "&fDisconecting from service..."));
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', defaultPrefix +"&cGood Bye!"));
    }

    /*
    Config management
    if config not exists, create it
    if config version is old, shut down the plugin
    if everything is ok, continue loading the plugin
     */
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
    /*
    When a player joins, executes this function
    This functions calls getCountryFromIP() and CheckForVPN()
    the result is printed
     */
    FileConfiguration configf = this.getConfig();
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String playerIP = event.getPlayer().getAddress().getAddress().getHostAddress();
        String country = getCountryFromIP(playerIP);
        boolean isVPN = checkForVPN(playerIP);
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', defaultPrefix + "Player IP: &6" + playerIP + " &fConnection from: &6" + country + " &fPlayer Name: &6"+event.getPlayer().getDisplayName() + " &fVPN: &6"+isVPN)  );
        if(configf.getString("novpn.enabled").equals(true)) {
            if (isVPN == true) {
                event.getPlayer().kickPlayer(ChatColor.translateAlternateColorCodes('&', configf.getString("novpn.message")));
                Bukkit.getConsoleSender().sendMessage(defaultPrefix+"Player using VPN has been kicked!");
            }
        }
    }

    /*
    Uses ipstack.com service to have the knowledge of the player's country
     */
    String getCountryFromIP(String ip) {
        try {

            URL url = new URL("http://api.ipstack.com/" + ip + "?access_key=" + configf.getString("key"));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();


            JsonObject jsonObject = JsonParser.parseString(response.toString()).getAsJsonObject();


            String country = jsonObject.get("country_name").getAsString();
            String continent = jsonObject.get("continent_name").getAsString();


            String formated = country + " (" + continent + ")"; //? Is redundant, but i dont want to change it xd
            return formated;

        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "Error while getting country information: " + e.getMessage()));
            return "Unknown";
        }
    }

    /*
    We check if the player is using a VPN using ipstack.com
     */
    boolean checkForVPN(String ipAddress) {
        try {
            URL url = new URL("http://api.ipstack.com/" + ipAddress + "?access_key=" + configf.getString("key"));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();

            boolean isVPN = response.toString().contains("\"vpn\":true");
            return isVPN;
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "Error while getting country information: " + e.getMessage()));
            return false;
        }
    }

    public void checkForKey() {
        FileConfiguration configf = this.getConfig();
        if(configf.getString("key").equals("null") || configf.getString("key").equals("YOUR_IPSTACK_API_KEY")) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', defaultPrefix+"&cThe KEY field is empty, please select an api key"));
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }




}
