package cabbageroll.tetrdotjar;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Duelinput implements CommandExecutor{
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player=(Player)sender;
        Tplayer temp;
        
        for(int g=0;g<Duel.num;g++){
            temp=Pluginmain.match.plist.get(g);
            if(temp.player.equals(player)){
                if(args.length==2){
                    for(int i=0;i<Integer.parseInt(args[1]);i++){
                        temp.userInput(args[0]);
                    }
                }else{
                    temp.userInput(args[0]);
                }
                return true;
            }
        }
        
        return false;
    }
}
