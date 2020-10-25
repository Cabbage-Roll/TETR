package cabbageroll.tetr.menus;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import cabbageroll.tetr.Main;

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
        inventory.setItem(9, new ItemStack(Material.DIRT));
        inventory.setItem(10, new ItemStack(Material.GOLD_BLOCK));
        inventory.setItem(36, new ItemStack(Material.BEDROCK));
        
        player.openInventory(inventory);
    }
    
    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
