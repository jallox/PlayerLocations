package dev.jayox;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class mainCommand implements CommandExecutor {
    private PlayerLocations mainclass;
    public mainCommand(PlayerLocations mainclass) {
        this.mainclass = mainclass;
    }
    public String pluginprefix = "&f[&6Player&eLocations&f]: ";

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String str, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', pluginprefix+ "&aAttempting to reload config"));
            mainclass.reloadConfig();
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', pluginprefix+ "&aConfig reloaded succesfully"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', pluginprefix+ "&aIPSTACK KEY: &e"+mainclass.getConfig().getString("key")));

        }else{
                if(sender.hasPermission("playerlocations.command.playerlocations")) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', pluginprefix+ "&aAttempting to reload config"));
                    mainclass.reloadConfig();
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', pluginprefix+ "&aConfig reloaded succesfully"));
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', pluginprefix+ "&aIPSTACK KEY: &e"+mainclass.getConfig().getString("key")));

                }else{
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', mainclass.getConfig().getString("NoPermission")));
                }
        }
        return true;
    }
}
