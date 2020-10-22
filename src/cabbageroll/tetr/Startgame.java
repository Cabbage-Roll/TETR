package cabbageroll.tetr;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Startgame implements CommandExecutor{
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        
        Player player=(Player)sender;
        
        if(args.length!=3 && args.length!=9){
            player.sendMessage("Command must have 3 or 9 arguments");
            return true;
        }
        
        for(int i=0;i<args.length;i++){
            try{
                Integer.parseInt(args[i]);
            }catch (NumberFormatException e) {
                player.sendMessage(args[i]+" must be a number!");
                return true;
            }
        }
        
        Table.rsp.addPlayer(player);
        
        Pluginmain.sp.player=player;
        Pluginmain.sp.world=player.getWorld();
        
        Pluginmain.sp.gx=Integer.parseInt(args[0]);
        Pluginmain.sp.gy=Integer.parseInt(args[1]);
        Pluginmain.sp.gz=Integer.parseInt(args[2]);
        
        if(args.length==9){
            Pluginmain.sp.m1x=Integer.parseInt(args[3]);
            Pluginmain.sp.m1y=Integer.parseInt(args[4]);
            Pluginmain.sp.m2x=Integer.parseInt(args[5]);
            Pluginmain.sp.m2y=Integer.parseInt(args[6]);
            Pluginmain.sp.m3x=Integer.parseInt(args[7]);
            Pluginmain.sp.m3y=Integer.parseInt(args[8]);
        }
        
        Pluginmain.sp.initGame();
        return true;
    }
}
