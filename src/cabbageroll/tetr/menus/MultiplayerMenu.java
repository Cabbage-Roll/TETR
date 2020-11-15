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

public class MultiplayerMenu implements InventoryHolder{
    private Inventory inventory=null;
    
    protected final static int BACK_LOCATION = 36;
    protected final static int CREATEROOM_LOCATION = 9;
    protected final static int LISTROOMS_LOCATION = 10;
    
    public MultiplayerMenu(Player player){
        Main.lastui.put(player, "multiplayer");
        Inventory inventory=Bukkit.createInventory(this, 54, "Multiplayer");
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
        itemmeta.setDisplayName("CREATE ROOM");
        item.setItemMeta(itemmeta);
        inventory.setItem(CREATEROOM_LOCATION, item);
        
        item=new ItemStack(Material.DIRT);
        itemmeta=item.getItemMeta();
        itemmeta.setDisplayName("ROOM LISTING");
        item.setItemMeta(itemmeta);
        inventory.setItem(LISTROOMS_LOCATION, item);
        
        item=new ItemStack(Material.DIRT);
        itemmeta=item.getItemMeta();
        itemmeta.setDisplayName("BACK");
        item.setItemMeta(itemmeta);
        inventory.setItem(BACK_LOCATION, item);
        
        player.openInventory(inventory);
    }
    
    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
