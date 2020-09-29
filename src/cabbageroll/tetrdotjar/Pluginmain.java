package cabbageroll.tetrdotjar;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
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
    
    @EventHandler
    public void onItemHeld(PlayerItemHeldEvent event) {
        int itemId = event.getNewSlot();
        ItemStack item = Cmain.player.getInventory().getItem(itemId);
        if (item == null) {
            System.out.println("item is null");
            return;
        }
        
        String name="adgjmptw";
        
        if(item.hasItemMeta()) {
            name = item.getItemMeta().getDisplayName();
            System.out.println("test: "+name);
            Cmain.userInput(name);
            System.out.println("success");
            Cmain.player.getInventory().setHeldItemSlot(8);
        }

    }
}