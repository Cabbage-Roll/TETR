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
import cabbageroll.tetr.Table;

public class JoinRoomMenu implements InventoryHolder{
    private Inventory inventory=null;
    public JoinRoomMenu(Player player){
        Main.lastui.put(player, "joinroom");
        Inventory inventory=Bukkit.createInventory(this, 54, "Join room");
        ItemStack border=new ItemStack(Material.THIN_GLASS);
        //fill the border with glass
        for(int i=0;i<9;i++){
            inventory.setItem(i, border);
        }
        for(int i=45;i<54;i++){
            inventory.setItem(i, border);
        }
        
        //clickable items
        ItemMeta itemmeta;
        
        ItemStack back=new ItemStack(Material.DIRT);
        itemmeta=back.getItemMeta();
        itemmeta.setDisplayName("BACK");
        back.setItemMeta(itemmeta);
        inventory.setItem(36, back);
        
        ItemStack listroom;
        int i;
        for(Room room: Main.roomlist.values()){
            i=0;
            listroom=new ItemStack(Material.COAL);
            itemmeta=listroom.getItemMeta();
            itemmeta.setDisplayName(room.name);
            listroom.setItemMeta(itemmeta);
            inventory.setItem(9+i, listroom);
            i++;
        }
        
        player.openInventory(inventory);
    }
    
    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
