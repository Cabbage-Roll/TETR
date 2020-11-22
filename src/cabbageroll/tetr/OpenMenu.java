package cabbageroll.tetr;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cabbageroll.tetr.menus.*;

public class OpenMenu implements CommandExecutor{

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        Player p=(Player)sender;
        switch(Main.lastui.get(p)){
        case "home":
            new HomeMenu(p);
            break;
        case "multiplayer":
            new MultiplayerMenu(p);
            break;
        case "makeroom":
            new MakeRoomMenu(p);
            break;
        case "joinroom":
            new JoinRoomMenu(p);
            break;
        case "room":
            new RoomMenu(p);
            break;
        case "skin":
            new SkinMenu(p);
            break;
        case "settings":
            new SettingsMenu(p);
            break;
        case "simsettings":
            new SimpleSettingsMenu(p);
            break;
        case "song":
            new SongMenu(p);
            break;
        }
        return true;
    }
}
