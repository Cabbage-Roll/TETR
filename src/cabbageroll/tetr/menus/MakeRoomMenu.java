package cabbageroll.tetr.menus;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import cabbageroll.tetr.Main;
import xseries.XMaterial;

public class MakeRoomMenu implements InventoryHolder{
    private Inventory inventory=null;
    
    protected final static int BACK_LOCATION = 36;
    protected final static int NEWROOM_LOCATION = 9;
    
    public MakeRoomMenu(Player player){
        Main.lastui.put(player, "makeroom");
        Inventory inventory=Bukkit.createInventory(this, 54, "Create new room");
        ItemStack border=XMaterial.GLASS_PANE.parseItem();
        //fill the border with glass
        for(int i=0;i<9;i++){
            inventory.setItem(i, border);
        }
        for(int i=45;i<54;i++){
            inventory.setItem(i, border);
        }
        
        //clickable items
        ItemMeta itemmeta;
        ItemStack item;
        
        item=new ItemStack(Material.DIRT);
        itemmeta=item.getItemMeta();
        itemmeta.setDisplayName("BACK");
        item.setItemMeta(itemmeta);
        inventory.setItem(BACK_LOCATION, item);
        
        item=new ItemStack(Material.COAL_BLOCK);
        itemmeta=item.getItemMeta();
        itemmeta.setDisplayName("NEW ROOM");
        item.setItemMeta(itemmeta);
        inventory.setItem(NEWROOM_LOCATION, item);
        
        player.openInventory(inventory);
    }
    
    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
