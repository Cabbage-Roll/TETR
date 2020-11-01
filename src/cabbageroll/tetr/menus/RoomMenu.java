package cabbageroll.tetr.menus;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import cabbageroll.tetr.Main;

public class RoomMenu implements InventoryHolder{
    private Inventory inventory=null;
    public RoomMenu(Player player){
        Main.lastui.put(player, "room");
        Inventory inventory=Bukkit.createInventory(this, 54, "Room - "+Main.inwhichroom.get(player));
        ItemStack border=new ItemStack(Material.THIN_GLASS);
        //fill the border with glass
        for(int i=0;i<9;i++){
            inventory.setItem(i, border);
        }
        for(int i=45;i<54;i++){
            inventory.setItem(i, border);
        }
        
        //clickable items
        ItemStack item;
        ItemMeta itemmeta;
        
        ItemStack head=new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        int i=0;
        for(Player p: Main.roommap.get(Main.inwhichroom.get(player)).playerlist){
            itemmeta=head.getItemMeta();
            itemmeta.setDisplayName(p.getName());
            if(Main.roommap.get(Main.inwhichroom.get(player)).host.equals(p)){
                itemmeta.setLore(Arrays.asList("HOST"));
            }else{
                itemmeta.setLore(null);
            }

            head.setItemMeta(itemmeta);
            inventory.setItem(9+i, head);
            i++;
        }
        
        if(Main.roommap.get(Main.inwhichroom.get(player)).host.equals(player)){
            item=new ItemStack(Material.DIAMOND_SWORD);
            itemmeta=item.getItemMeta();
            itemmeta.setDisplayName("START");
        }else{
            item=new ItemStack(Material.BARRIER);
            itemmeta=item.getItemMeta();
            itemmeta.setDisplayName("YOU ARE NOT THE HOST");
        }
        

        item.setItemMeta(itemmeta);
        inventory.setItem(49, item);
        
        ItemStack back=new ItemStack(Material.DIRT);
        itemmeta=back.getItemMeta();
        itemmeta.setDisplayName("BACK");
        back.setItemMeta(itemmeta);
        inventory.setItem(36, back);
        
        item=new ItemStack(Material.COMPASS);
        itemmeta=item.getItemMeta();
        itemmeta.setDisplayName("Settings!");
        item.setItemMeta(itemmeta);
        inventory.setItem(53, item);
        
        player.openInventory(inventory);
    }
    
    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
