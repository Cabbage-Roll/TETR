package cabbageroll.tetr.menus;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import cabbageroll.tetr.Main;
import cabbageroll.tetr.Room;
import xseries.XMaterial;

public class JoinRoomMenu implements InventoryHolder{
    private Inventory inventory=null;
    public JoinRoomMenu(Player player){
        Main.lastui.put(player, "joinroom");
        Inventory inventory=Bukkit.createInventory(this, 54, "Join room");
        ItemStack border=XMaterial.GLASS_PANE.parseItem();
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
        
        item=new ItemStack(Material.DIRT);
        itemmeta=item.getItemMeta();
        itemmeta.setDisplayName("BACK");
        item.setItemMeta(itemmeta);
        inventory.setItem(36, item);
        
        int i=0;
        for(Room room: Main.roommap.values()){
            item=new ItemStack(Material.COAL_BLOCK);
            itemmeta=item.getItemMeta();
            itemmeta.setDisplayName(room.id);
            item.setItemMeta(itemmeta);
            inventory.setItem(9+i, item);
            i++;
        }
        
        player.openInventory(inventory);
    }
    
    @Override
    public Inventory getInventory(){
        return inventory;
    }
}
