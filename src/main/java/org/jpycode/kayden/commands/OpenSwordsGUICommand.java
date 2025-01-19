package org.jpycode.kayden.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jpycode.kayden.gui.SwordsGUI;

public class OpenSwordsGUICommand implements CommandExecutor {
    private final SwordsGUI swordsGUI;

    public OpenSwordsGUICommand(SwordsGUI swordsGUI) {
        this.swordsGUI = swordsGUI;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player p) {
            swordsGUI.openGUI(p);
            return true;
        }
        return false;
    }
}
