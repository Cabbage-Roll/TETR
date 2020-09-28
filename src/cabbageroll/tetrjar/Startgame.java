package cabbageroll.tetrjar;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Startgame implements CommandExecutor{
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Printing.gx=Integer.parseInt(args[0]);
        Printing.gy=Integer.parseInt(args[1]);
        Printing.gz=Integer.parseInt(args[2]);
        Cmain.initGame();
        Playgame.playgame();
        return true;
    }
}
