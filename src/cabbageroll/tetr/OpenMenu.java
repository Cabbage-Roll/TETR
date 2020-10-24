package cabbageroll.tetr;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cabbageroll.tetr.menus.*;

public class OpenMenu implements CommandExecutor{

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        Player p=(Player)sender;
        switch(Pluginmain.lastui.get(p)){
        case "home":
            HomeMenu.openGUI(p);
            break;
        case "room":
            RoomMenu.openGUI(p);
            break;
        case "skin":
            SkinMenu.openGUI(p);
            break;
        case "make":
            MakeRoomMenu.openGUI(p);
            break;
        case "join":
            JoinRoomMenu.openGUI(p);
            break;
        }
        return true;
    }
}
