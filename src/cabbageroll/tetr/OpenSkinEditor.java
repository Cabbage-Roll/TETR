package cabbageroll.tetr;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cabbageroll.tetr.menus.SkinMenu;

public class OpenSkinEditor implements CommandExecutor{
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p=(Player)sender;
        SkinMenu.openGUI(p);
        return true;
    }
}
