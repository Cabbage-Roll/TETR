package cabbageroll.tetr.menus;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import cabbageroll.tetr.Main;

public class HomeMenu implements InventoryHolder{
    private Inventory inventory=null;
    public HomeMenu(Player player){
        Main.lastui.put(player, "home");
        Inventory inventory=Bukkit.createInventory(this, 54, "Home");
        ItemStack border=new ItemStack(Material.THIN_GLASS);
        //fill the border with glass
        for(int i=0;i<9;i++){
            inventory.setItem(i, border);
        }
        for(int i=45;i<54;i++){
            inventory.setItem(i, border);
        }
        
        //clickable items
        inventory.setItem(9, new ItemStack(Material.APPLE));
        
        
        player.openInventory(inventory);
    }
    
    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
