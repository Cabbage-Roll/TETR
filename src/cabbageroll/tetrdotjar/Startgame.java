package cabbageroll.tetrdotjar;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Startgame implements CommandExecutor{
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Pluginmain.sp=new Tplayer();
        Player player=(Player)sender;
        Pluginmain.sp.player=player;
        Pluginmain.sp.world=player.getWorld();
        Pluginmain.sp.gx=Integer.parseInt(args[0]);
        Pluginmain.sp.gy=Integer.parseInt(args[1]);
        Pluginmain.sp.gz=Integer.parseInt(args[2]);
        Pluginmain.sp.initGame();
        Pluginmain.sp.playGame();
        return true;
    }
}
