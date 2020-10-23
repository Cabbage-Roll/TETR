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
        
        Pluginmain.sp.gx=Byte.parseByte(args[0]);
        Pluginmain.sp.gy=Byte.parseByte(args[1]);
        Pluginmain.sp.gz=Byte.parseByte(args[2]);
        
        if(args.length==9){
            Pluginmain.sp.m1x=Byte.parseByte(args[3]);
            Pluginmain.sp.m1y=Byte.parseByte(args[4]);
            Pluginmain.sp.m2x=Byte.parseByte(args[5]);
            Pluginmain.sp.m2y=Byte.parseByte(args[6]);
            Pluginmain.sp.m3x=Byte.parseByte(args[7]);
            Pluginmain.sp.m3y=Byte.parseByte(args[8]);
        }
        
        Pluginmain.sp.initGame();
        return true;
    }
}
