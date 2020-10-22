package cabbageroll.tetr;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Commandinput implements CommandExecutor{
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length==2) {
            for(int i=0;i<Integer.parseInt(args[1]);i++)
                Pluginmain.sp.userInput(args[0]);
        }
        else
            Pluginmain.sp.userInput(args[0]);
        return true;
    }
}
