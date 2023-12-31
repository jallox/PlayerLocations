package dev.jayox;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class playerip implements CommandExecutor {
    private PlayerLocations mainclass;
    public playerip(PlayerLocations mainclass) { this.mainclass = mainclass; }
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
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', pluginprefix + "&aPLayerIP: &e"+playerIP));





                }
            }
        }else{
            if(sender.hasPermission("playerlocations.command.getip")) {
                if (str.length == 0) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', pluginprefix + "&cYou must give a player!"));
                } else {
                    String playerName = str[0];


                    Player targetPlayer = mainclass.getServer().getPlayer(playerName);

                    if (targetPlayer == null) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', pluginprefix + "&cInvalid player specified!"));
                    } else {

                        String playerIP = targetPlayer.getAddress().getAddress().getHostAddress();
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', pluginprefix + "&aPLayerIP: &e"+playerIP));}
                }
            }else{
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', mainclass.getConfig().getString("NoPermission")));
            }
        }
        return false;
    }
}
