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

public class HomeMenu implements InventoryHolder{
    private Inventory inventory = null;

    protected final static int MULTIPLAYER_LOCATION = 9;
    protected final static int SKINEDITOR_LOCATION = 10;
    
    public HomeMenu(Player player){
        Main.lastui.put(player, "home");
        Inventory inventory=Bukkit.createInventory(this, 54, "Home");
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
        itemmeta.setDisplayName("MULTIPLAYER");
        item.setItemMeta(itemmeta);
        inventory.setItem(MULTIPLAYER_LOCATION, item);
        
        item=new ItemStack(Material.DIRT);
        itemmeta=item.getItemMeta();
        itemmeta.setDisplayName("SKIN EDITOR");
        item.setItemMeta(itemmeta);
        inventory.setItem(SKINEDITOR_LOCATION, item);
        
        player.openInventory(inventory);
    }
    
    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
