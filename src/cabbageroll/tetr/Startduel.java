package cabbageroll.tetr;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Startduel implements CommandExecutor{
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        /*if(Pluginmain.match!=null){
            Pluginmain.match.stop();
            Pluginmain.match=null;
        }
        
        */
        return true;
    }
}
