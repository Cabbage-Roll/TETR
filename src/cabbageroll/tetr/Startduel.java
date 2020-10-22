package cabbageroll.tetr;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Startduel implements CommandExecutor{
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(Pluginmain.match!=null){
            Pluginmain.match.stop();
            Pluginmain.match=null;
        }
        
        Table temp=new Table();
        Player player1 = Pluginmain.plugin.getServer().getPlayer(args[0]);
        temp.gx=Integer.parseInt(args[1]);
        temp.gy=Integer.parseInt(args[2]);
        temp.gz=Integer.parseInt(args[3]);
        temp.player=player1;
        temp.world=player1.getWorld();
        
        Table temp2=new Table();
        Player player2 = Pluginmain.plugin.getServer().getPlayer(args[4]);
        temp2.gx=Integer.parseInt(args[5]);
        temp2.gy=Integer.parseInt(args[6]);
        temp2.gz=Integer.parseInt(args[7]);
        temp2.player=player2;
        temp2.world=player2.getWorld();
     
        Pluginmain.match=new Duel(temp,temp2);
        
        return true;
    }
}
