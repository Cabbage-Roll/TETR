package cabbageroll.tetrdotjar;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Duelinput implements CommandExecutor{
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player=(Player)sender;
        
        if(player==Pluginmain.duel.red.player) {
            if(args.length==2) {
                for(int i=0;i<Integer.parseInt(args[1]);i++)
                    Pluginmain.duel.red.userInput(args[0]);
            }
            else
                Pluginmain.duel.red.userInput(args[0]);
        }else {
            if(args.length==2) {
                for(int i=0;i<Integer.parseInt(args[1]);i++)
                    Pluginmain.duel.blue.userInput(args[0]);
            }
            else
                Pluginmain.duel.blue.userInput(args[0]);
        }
        
        return true;
    }
}
