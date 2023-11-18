package dev.jayox;

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

public final class PlayerLocations extends JavaPlugin implements Listener, CommandExecutor{
    public String version = getDescription().getVersion();
    public String defaultPrefix = "&f[&6Player&eLocations &f v" + version + "]: ";

    public String rutaConfig;

    @Override
    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', defaultPrefix + "&6    ____  __                      __                     __  _                 "));
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
        configManager("2");
        this.getCommand("playerlocations").setExecutor(new mainCommand(this));
        this.getCommand("country").setExecutor(new country(this));
        checkForKey();
        getServer().getPluginManager().registerEvents(this, this);

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
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String playerIP = event.getPlayer().getAddress().getAddress().getHostAddress();
        String country = getCountryFromIP(playerIP);
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', defaultPrefix + "Player IP: &6" + playerIP + " &fConnection from: &6" + country + " &fPlayer Name: &6"+event.getPlayer().getDisplayName())  );
    }

    String getCountryFromIP(String ip) {
        try {
            FileConfiguration configf = this.getConfig();
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

            // Parsear la respuesta JSON con Gson
            JsonObject jsonObject = JsonParser.parseString(response.toString()).getAsJsonObject();

            // Obtener el valor del campo 'country' en JSON
            String country = jsonObject.get("country_name").getAsString();
            String continent = jsonObject.get("continent_name").getAsString();


            String formated = country + " (" + continent + ")";
            return formated;

        } catch (Exception e) {
            getLogger().warning("Error while getting country information: " + e.getMessage());
            return "Unknown";
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
