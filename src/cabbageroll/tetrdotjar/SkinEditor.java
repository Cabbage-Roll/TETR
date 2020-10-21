package cabbageroll.tetrdotjar;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import java.util.HashMap;

public class SkinEditor implements Listener {
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
        for(int j=1;j<5;j++){
            inv.setItem(9*j, border);
        }
        for(int j=1;j<5;j++){
            inv.setItem(9*j+8, border);
        }
        for(int i=19;i<26;i++){
            inv.setItem(i, border);
        }
        for(int i=10;i<18;i+=2){
            inv.setItem(i, border);
        }
        
        //changeable blocks
        for(int i=0;i<7;i++){
            inv.setItem(28+i, Table.blocks[i]);
        }
        inv.setItem(11, Table.blocks[7]);
        inv.setItem(13, Table.blocks[8]);
        inv.setItem(15, Table.blocks[9]);
        
        player.openInventory(inv);

        isopen.put(player, true);
    }
    
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e){
        if(e.getCurrentItem().getType()==Material.THIN_GLASS){
            e.setCancelled(true);
            return;
        }
    }
    
    @EventHandler
    public void InvClose(final InventoryCloseEvent e){
        Inventory inv=e.getInventory();
        Player p=(Player)e.getPlayer();
        if(isopen.containsKey(p)){
            if(inv.getName().equalsIgnoreCase("Skin editor") && isopen.get(e.getPlayer())){
                for(int i=0;i<7;i++){
                    if(inv.getItem(28+i)!=null){
                        Table.blocks[i]=inv.getItem(28+i);
                    }else{
                        Table.blocks[i]=new ItemStack(Material.AIR);
                    }
                }
                for(int i=0;i<3;i++){
                    if(inv.getItem(11+2*i)!=null){
                        Table.blocks[7+i]=inv.getItem(11+2*i);
                    }else{
                        Table.blocks[7+i]=new ItemStack(Material.AIR);
                    }
                }
                e.getPlayer().sendMessage("Skin saved");
                isopen.put(p, false);
            }
        }
    }
}
