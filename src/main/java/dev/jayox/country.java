package dev.jayox;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class country implements CommandExecutor {

    private PlayerLocations mainclass;
    public country(PlayerLocations mainclass) { this.mainclass = mainclass; }
    public String pluginprefix = "&f[&6Player&eLocations&f]: ";
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] str) {
        if(!(sender instanceof Player)) {
            if (str.length == 0) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', pluginprefix + "&cYou must give a player!"));
            } else {
                String playerName = str[0];


                Player targetPlayer = mainclass.getServer().getPlayer(playerName);

                if (targetPlayer == null) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', pluginprefix + "&cInvalid player specified!"));
                } else {

                    String playerIP = targetPlayer.getAddress().getAddress().getHostAddress();


                    String country = mainclass.getCountryFromIP(playerIP);


                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', pluginprefix + "&aThe player's country is: &e" + country));
                }
            }
        }else{
            if(sender.hasPermission("playerlocations.command.country")) {
                if (str.length == 0) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', pluginprefix + "&cYou must give a player!"));
                } else {
                    String playerName = str[0];


                    Player targetPlayer = mainclass.getServer().getPlayer(playerName);

                    if (targetPlayer == null) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', pluginprefix + "&cInvalid player specified!"));
                    } else {

                        String playerIP = targetPlayer.getAddress().getAddress().getHostAddress();


                        String country = mainclass.getCountryFromIP(playerIP);


                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', pluginprefix + "&aThe player's country is: &e" + country));
                    }
                }
            }else{
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', mainclass.getConfig().getString("NoPermission")));
            }
        }
        return false;
    }
}
