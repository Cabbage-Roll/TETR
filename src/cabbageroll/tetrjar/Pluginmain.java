package cabbageroll.tetrjar;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;


public class Pluginmain extends JavaPlugin implements Listener{
    static JavaPlugin plugin;
    @Override
    public void onEnable() {
        plugin=this;
        System.out.println("Plugin started");
        getServer().getPluginManager().registerEvents(this, this);
        this.getCommand("startgame").setExecutor(new Startgame());
        this.getCommand("sendinput").setExecutor(new Commandinput());

    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        event.setJoinMessage("" + event.getPlayer().getName() + "!");
        Cmain.player=event.getPlayer();
        Cmain.world=Cmain.player.getWorld();
        Cmain.initGame();
    }
}