package cabbageroll.tetr;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cabbageroll.tetr.menus.HomeMenu;
import cabbageroll.tetr.menus.JoinRoomMenu;
import cabbageroll.tetr.menus.MakeRoomMenu;
import cabbageroll.tetr.menus.MultiplayerMenu;
import cabbageroll.tetr.menus.RoomMenu;
import cabbageroll.tetr.menus.SettingsMenu;
import cabbageroll.tetr.menus.SimpleSettingsMenu;
import cabbageroll.tetr.menus.SkinMenu;
import cabbageroll.tetr.menus.SongMenu;

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
