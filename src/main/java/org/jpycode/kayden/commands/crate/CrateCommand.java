package org.jpycode.kayden.commands.crate;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jpycode.kayden.managers.CrateManager;

import javax.swing.plaf.SplitPaneUI;

public class CrateCommand implements CommandExecutor {
    private final CrateManager crateManager;

    public CrateCommand(CrateManager crateManager) {
        this.crateManager = crateManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command");
            return true;
        }

        if(!player.hasPermission("crates.create")) {
            player.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
            return true;
        }

        if(args.length != 2 || args[0].equalsIgnoreCase("create")) {
            player.sendMessage(ChatColor.RED + "Usage: /crates create <name>");
            return true;
        }

        String crateName = args[1];
        if(crateManager.crateExists(crateName)) {
            player.sendMessage(ChatColor.RED + "A crate with that name already exists!");
            return true;
        }

        crateManager.createCrate(crateName);

        player.sendMessage(ChatColor.GREEN + "Success created crate: " + crateName);


        return true;
    }
}
