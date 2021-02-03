package tetr.minecraft;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.TranslatableComponent;
import tetr.minecraft.menus.HomeMenu;
import tetr.minecraft.menus.JoinRoomMenu;
import tetr.minecraft.menus.MultiplayerMenu;
import tetr.minecraft.menus.RoomMenu;
import tetr.minecraft.menus.SettingsMenu;
import tetr.minecraft.menus.SimpleSettingsMenu;
import tetr.minecraft.menus.SkinMenu;
import tetr.minecraft.menus.SongMenu;
import tetr.normal.Constants;

public class CommandTetr implements CommandExecutor{

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){

        Player player = (Player)sender;
        
        if(args.length==0) {
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
        }else if(args[0].equalsIgnoreCase("controls")) {
            
            TranslatableComponent[] controls = {
                    new TranslatableComponent("key.hotbar.1"),
                    new TranslatableComponent("key.hotbar.2"),
                    new TranslatableComponent("key.hotbar.3"),
                    new TranslatableComponent("key.hotbar.4"),
                    new TranslatableComponent("key.hotbar.5"),
                    new TranslatableComponent("key.hotbar.6"),
                    new TranslatableComponent("key.hotbar.7"),
                    new TranslatableComponent("key.hotbar.8"),
                    new TranslatableComponent("key.sneak")  
            };
            
            String[] descriptions = {
                    new String("Move left: "),
                    new String("\nMove right: "),
                    new String("\nSoft drop: "),
                    new String("\nHard drop: "),
                    new String("\nRotate counterclockwise: "),
                    new String("\nRotate clockwise: "),
                    new String("\nRotate 180: "),
                    new String("\nHold: "),
                    new String("\nZone: "),
                    
            };
            
            //control.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("asd").create()));

            TextComponent message = new TextComponent();
            
            for(int i=0;i<controls.length;i++) {
                message.addExtra(descriptions[i]);
                message.addExtra(controls[i]);
            }
            
            player.spigot().sendMessage(message);
            
        }else if(args[0].equalsIgnoreCase("help")){
            TextComponent message = new TextComponent();
            message.addExtra("My ugly help command!");
            message.addExtra("\n");
            message.addExtra("\n/tetr");
            message.addExtra("\n/tetr help");
            message.addExtra("\n/tetr controls");
            message.addExtra("\n" + ChatColor.BOLD + "Bottom text");
            
            player.spigot().sendMessage(message);
        }else if(args[0].equalsIgnoreCase("spectate") && Constants.iKnowWhatIAmDoing) {
            if(args.length < 2) {
                player.sendMessage("Room id is missing!");
            }else {
                try {
                    Main.roommap.get(args[1]).addSpectator(player);
                }catch (NullPointerException e) {
                    player.sendMessage("Null pointer exception! This room id most likely doesn't exist");
                }
            }
        }else if(args[0].equalsIgnoreCase("fastjoin") && args.length==2 && Constants.iKnowWhatIAmDoing) {
            try {
                Main.roommap.get(args[1]).addPlayer(player);
                Main.lastui.put(player, "room");
            }catch (Exception e) {
                player.sendMessage("Error");
            }
        }else {
            player.sendMessage("/tetr help");
        }
        return true;
    }
}
