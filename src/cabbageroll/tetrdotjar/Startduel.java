package cabbageroll.tetrdotjar;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Startduel implements CommandExecutor{
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Pluginmain.duel=new Duel();

        Player player1 = Pluginmain.plugin.getServer().getPlayer(args[0]);
        Pluginmain.duel.red.gx=Integer.parseInt(args[1]);
        Pluginmain.duel.red.gy=Integer.parseInt(args[2]);
        Pluginmain.duel.red.gz=Integer.parseInt(args[3]);
        Pluginmain.duel.red.player=player1;
        Pluginmain.duel.red.world=player1.getWorld();
        
        Player player2 = Pluginmain.plugin.getServer().getPlayer(args[4]);
        Pluginmain.duel.blue.gx=Integer.parseInt(args[5]);
        Pluginmain.duel.blue.gy=Integer.parseInt(args[6]);
        Pluginmain.duel.blue.gz=Integer.parseInt(args[7]);
        Pluginmain.duel.blue.player=player2;
        Pluginmain.duel.blue.world=player2.getWorld();
        
        Pluginmain.duel.red.initGame();
        Pluginmain.duel.red.playGame();
        
        Pluginmain.duel.blue.initGame();
        Pluginmain.duel.blue.playGame();
        
        return true;
    }
}
