package cabbageroll.tetr;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RoomControls implements CommandExecutor{
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        switch(args[0]){
        case "start":
            Pluginmain.roomlist.get(0).startRoom();
            break;
        case "stop":
            Pluginmain.roomlist.get(0).stopRoom();
            break;
        default:
            sender.sendMessage("valid commands: start, stop");
        }
        return true;
    }
}
