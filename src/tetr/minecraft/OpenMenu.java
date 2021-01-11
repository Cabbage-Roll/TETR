package tetr.minecraft;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import tetr.minecraft.menus.HomeMenu;
import tetr.minecraft.menus.JoinRoomMenu;
import tetr.minecraft.menus.MultiplayerMenu;
import tetr.minecraft.menus.RoomMenu;
import tetr.minecraft.menus.SettingsMenu;
import tetr.minecraft.menus.SimpleSettingsMenu;
import tetr.minecraft.menus.SkinMenu;
import tetr.minecraft.menus.SongMenu;

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
