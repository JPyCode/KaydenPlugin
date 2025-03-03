package com.jpycode.kayden.rpg.status;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import com.jpycode.kayden.rpg.status.gui.StatusGUI;

public class OpenStatusGUICommand implements CommandExecutor {
    private final StatusGUI statusGUI;
    public OpenStatusGUICommand(StatusGUI statusGUI) {
        this.statusGUI = statusGUI;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(sender instanceof Player p) statusGUI.openStatusGUI(p);
        else sender.sendMessage("Apenas jogadores podem usar este comando.");

        return true;
    }
}
