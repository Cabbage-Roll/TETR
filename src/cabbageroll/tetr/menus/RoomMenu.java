package cabbageroll.tetr.menus;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import cabbageroll.tetr.Pluginmain;
import cabbageroll.tetr.Room;
import cabbageroll.tetr.Table;
import cabbageroll.tetr.menus.*;

public class RoomMenu implements Listener{
    static HashMap<Player, Boolean> isopen=new HashMap<Player, Boolean>();
    public static void openGUI(Player player){
        Inventory inv=Bukkit.createInventory(null, 54, "Room");
        
        
        for(HashMap.Entry<String, Room> entry: Pluginmain.roomlist.entrySet()){
            player.sendMessage(entry.getKey());
        }
            
        player.openInventory(inv);
        isopen.put(player, true);
    }
    
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e){

        Player p=(Player)e.getWhoClicked();
        if(isopen.containsKey(p)){
            if(isopen.get(p)){
                e.setCancelled(true);
                
              //leave room
                if(e.getSlot()==45){
                    p.closeInventory();
                    isopen.put(p, false);
                    HomeMenu.openGUI(p);
                    return;
                }
                
              //start
                if(e.getSlot()==48){
                    return;
                }
                
              //stop
                if(e.getSlot()==50){
                    return;
                }
                
              //unused
                if(e.getSlot()==53){
                    return;
                }
                
                
            }
        }
    }
}
