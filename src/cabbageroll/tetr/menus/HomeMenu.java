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

import cabbageroll.tetr.Table;

public class HomeMenu implements Listener{
    static HashMap<Player, Boolean> isopen=new HashMap<Player, Boolean>();
    public static void openGUI(Player player){
        Inventory inv=Bukkit.createInventory(null, 54, "Skin editor");
        ItemStack border=new ItemStack(Material.THIN_GLASS);
        //fill the border with glass
        for(int i=0;i<9;i++){
            inv.setItem(i, border);
        }
        for(int i=45;i<54;i++){
            inv.setItem(i, border);
        }
        
        //changeable blocks
        for(int i=0;i<7;i++){
            inv.setItem(28+i, Table.blocks[i]);
        }

        for(int i=0;i<7;i++){
            inv.setItem(37+i, Table.blocks[i+9]);
        }
        
        inv.setItem(11, Table.blocks[7]);
        
        
        player.openInventory(inv);
        isopen.put(player, true);
    }
    
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e){

        Player p=(Player)e.getWhoClicked();
        if(isopen.containsKey(p)){
            if(isopen.get(p)){
                if(e.getCurrentItem().getType()==Material.THIN_GLASS){
                    e.setCancelled(true);
                    return;
                }
                
                if(e.getCurrentItem().getType()==Material.AIR && e.getSlot()==11 && e.getCursor().getType()==Material.AIR){
                    Table.transparent=!Table.transparent;
                    p.sendMessage("Transparency turned "+(Table.transparent?"on":"off"));
                    return;
                }
                
                //leave room
                if(e.getSlot()==45){
                    p.closeInventory();
                    isopen.put(p, false);
                    //e.HomeMenu.openGUI(p);
                    return;
                }
            }
        }
    }
}
