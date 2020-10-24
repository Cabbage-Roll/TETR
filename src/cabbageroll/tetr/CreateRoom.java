package cabbageroll.tetr;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreateRoom implements CommandExecutor{
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        Player p=(Player)sender;
        if(args.length==0){
            p.sendMessage("Please enter room name");
            return true;
        }
        /*Pluginmain.roomlist.add(new Room(args[0],p));
        p.sendMessage("made room");*/
        return true;
    }
}
