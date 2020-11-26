package cabbageroll.tetr;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cabbageroll.tetr.menus.*;

public class OpenMenu implements CommandExecutor{

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        Player player=(Player)sender;
        switch(Main.lastui.get(player)){
        case "home":
            new HomeMenu(player);
            break;
        case "multiplayer":
            new MultiplayerMenu(player);
            break;
        case "makeroom":
            new MakeRoomMenu(player);
            break;
        case "joinroom":
            new JoinRoomMenu(player, Main.joinroompage.get(player));
            break;
        case "room":
            new RoomMenu(player);
            break;
        case "skin":
            new SkinMenu(player);
            break;
        case "settings":
            new SettingsMenu(player);
            break;
        case "simsettings":
            new SimpleSettingsMenu(player);
            break;
        case "song":
            new SongMenu(player);
            break;
        }
        return true;
    }
}
