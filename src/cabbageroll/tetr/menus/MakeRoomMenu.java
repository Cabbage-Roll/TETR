package cabbageroll.tetr.menus;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import cabbageroll.tetr.Main;

public class MakeRoomMenu implements InventoryHolder{
    private Inventory inventory=null;
    public MakeRoomMenu(Player player){
        Main.lastui.put(player, "makeroom");
        Inventory inventory=Bukkit.createInventory(this, 54, "MakeRoomMenu");
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
        
        ItemStack newroom=new ItemStack(Material.COAL);
        itemmeta=newroom.getItemMeta();
        itemmeta.setDisplayName("new room with random name");
        newroom.setItemMeta(itemmeta);
        inventory.setItem(9, newroom);
        
        player.openInventory(inventory);
    }
    
    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
