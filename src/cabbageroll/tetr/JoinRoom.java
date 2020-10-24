package cabbageroll.tetr;

import java.util.HashMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JoinRoom implements CommandExecutor{
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        Player p=(Player)sender;
        if(args.length==0){
            p.sendMessage("roomlist:");

            for(Room room: Pluginmain.roomlist){
                p.sendMessage(room.name);
            }
            
            for(HashMap.Entry<String, Room> entry: Pluginmain.roomlist.entrySet()){
                p.sendMessage(entry.getKey());
            }
                
                
        }else if(args.length==1){
            for(Room room: Pluginmain.roomlist){
                if(args[0].equalsIgnoreCase(room.name)){
                    room.addPlayer(p);
                    p.sendMessage("JOINED");
                    return true;
                }
            }

            p.sendMessage("NO SUCH ROOM");
            p.sendMessage("roomlist:");
            for(Room room: Pluginmain.roomlist){
                p.sendMessage(room.name);
            }
        }
        return true;
    }
}
