package tetr.core.minecraft;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.gson.Gson;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.TranslatableComponent;
import tetr.core.Constants;
import tetr.core.Main;
import tetr.core.minecraft.menus.HomeMenu;
import tetr.core.minecraft.menus.JoinRoomMenu;
import tetr.core.minecraft.menus.MultiplayerMenu;
import tetr.core.minecraft.menus.RoomMenu;
import tetr.core.minecraft.menus.SettingsMenu;
import tetr.core.minecraft.menus.SimpleSettingsMenu;
import tetr.core.minecraft.menus.SkinMenu;
import tetr.core.minecraft.menus.SongMenu;

public class CommandTetr implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = null;

        if (sender instanceof Player) {
            player = (Player) sender;
        }

        if (args.length == 0 && player != null) {
            switch (Main.lastui.get(player)) {
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
        } else if (args[0].equalsIgnoreCase("controls")) {

            TranslatableComponent[] controls = { new TranslatableComponent("key.hotbar.1"),
                    new TranslatableComponent("key.hotbar.2"), new TranslatableComponent("key.hotbar.3"),
                    new TranslatableComponent("key.hotbar.4"), new TranslatableComponent("key.hotbar.5"),
                    new TranslatableComponent("key.hotbar.6"), new TranslatableComponent("key.hotbar.7"),
                    new TranslatableComponent("key.hotbar.8"), new TranslatableComponent("key.sneak") };

            String[] descriptions = { new String("Move left: "), new String("\nMove right: "),
                    new String("\nSoft drop: "), new String("\nHard drop: "), new String("\nRotate counterclockwise: "),
                    new String("\nRotate clockwise: "), new String("\nRotate 180: "), new String("\nHold: "),
                    new String("\nZone: "),

            };

            // control.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new
            // ComponentBuilder("asd").create()));

            TextComponent message = new TextComponent();

            for (int i = 0; i < controls.length; i++) {
                message.addExtra(descriptions[i]);
                message.addExtra(controls[i]);
            }

            sender.spigot().sendMessage(message);

        } else if (args[0].equalsIgnoreCase("help")) {
            TextComponent message = new TextComponent();
            message.addExtra("Dynamic help command");
            if (sender.hasPermission("tetr.developer") && Constants.iKnowWhatIAmDoing) {
                message.addExtra("\n" + ChatColor.RED + "" + ChatColor.BOLD + "You are the developer");
            }
            if (sender.hasPermission("tetr.reload")) {
                message.addExtra("\n" + "/tetr reload - reloads the plugin (not implemented)");
            }

            message.addExtra("\n" + "/tetr - open game window");
            message.addExtra("\n" + "/tetr help - shows this help page");
            message.addExtra("\n" + "/tetr controls - shows guide on how to set the controls");
            message.addExtra("\n" + "/tetr tetrachannel <nickname> - get stats of a player from ch.tetr.io");
            message.addExtra("\n" + ChatColor.BOLD + "The end (for now)");

            sender.spigot().sendMessage(message);
        } else if (args[0].equalsIgnoreCase("spectate") && Main.allowUnsafe(sender) && player != null) {
            if (args.length < 2) {
                player.sendMessage("Room id is missing!");
            } else {
                try {
                    Main.roommap.get(args[1]).addSpectator(player);
                } catch (NullPointerException e) {
                    player.sendMessage("Null pointer exception! This room id most likely doesn't exist");
                }
            }
        } else if (args[0].equalsIgnoreCase("fastjoin") && Main.allowUnsafe(sender) && player != null) {
            try {
                Main.roommap.get(args[1]).addPlayer(player);
                Main.lastui.put(player, "room");
            } catch (Exception e) {
                player.sendMessage("Error");
            }

        } else if (args[0].equalsIgnoreCase("ban") && Main.allowUnsafe(sender) && player != null) {
            if (player.hasPermission("tetr.admin")) {
                if (args.length < 2) {
                    player.sendMessage("too few args");
                } else if (args.length == 2) {
                    player.sendMessage("just right args");
                    player.sendMessage("do something (ban)");
                } else {
                    player.sendMessage("too many args");
                }
            } else {
                player.sendMessage("no permission");
            }
        } else if (args[0].equalsIgnoreCase("unban") && Main.allowUnsafe(sender) && player != null) {
            if (player.hasPermission("tetr.admin")) {
                if (args.length < 2) {
                    player.sendMessage("too few args");
                } else if (args.length == 2) {
                    player.sendMessage("just right args");
                    player.sendMessage("do something (unban)");
                } else {
                    player.sendMessage("too many args");
                }
            } else {
                player.sendMessage("no permission");
            }
        } else if (args[0].equalsIgnoreCase("tetrachannel")) {
            if (args.length > 1) {
                new Thread() {
                    @Override
                    public void run() {
                        URLConnection connection = null;
                        try {
                            connection = new URL("https://ch.tetr.io/api/users/" + args[1].toLowerCase())
                                    .openConnection();
                        } catch (MalformedURLException e1) {
                            e1.printStackTrace();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        connection.setRequestProperty("User-Agent", "TETR, executed by " + sender.getName());

                        try {
                            connection.connect();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }

                        BufferedReader reader = null;
                        try {
                            reader = new BufferedReader(
                                    new InputStreamReader(connection.getInputStream(), Charset.forName("UTF-8")));
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        StringBuilder sb = new StringBuilder();
                        int idk;
                        try {
                            while ((idk = reader.read()) != -1) {
                                sb.append((char) idk);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        String jsonString = sb.toString();
                        Gson gson = new Gson();
                        Map<?, ?> map = gson.fromJson(jsonString, Map.class);
                        Map<?, ?> data = (Map<?, ?>) map.get("data");
                        Map<?, ?> user = (Map<?, ?>) data.get("user");
                        Map<?, ?> league = (Map<?, ?>) user.get("league");
                        sender.sendMessage("nickname: " + user.get("username"));
                        sender.sendMessage("country: " + user.get("country"));
                        sender.sendMessage("rank: " + league.get("rank") + ", " + league.get("rating") + "TR");
                        sender.sendMessage("glicko: " + league.get("glicko") + "±" + league.get("rd"));
                        sender.sendMessage(
                                league.get("apm") + "APM " + league.get("pps") + "PPS " + league.get("vs") + "VS");
                    }
                }.start();
            } else {
                sender.sendMessage("You need to specify a nickname to look up");
            }
        } else {
            sender.sendMessage("/tetr help");
        }
        return true;
    }
}
