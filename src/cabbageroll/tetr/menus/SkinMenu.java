package cabbageroll.tetr.menus;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import cabbageroll.tetr.Main;
import cabbageroll.tetr.Table;
import xseries.XMaterial;

public class SkinMenu implements InventoryHolder{
    private Inventory inventory=null;
    public SkinMenu(Player player){
        Main.lastui.put(player, "skin");
        Inventory inventory=Bukkit.createInventory(this, 54, "Skin editor");
        ItemStack border=XMaterial.GLASS_PANE.parseItem();
        //fill the border with glass
        for(int i=0;i<9;i++){
            inventory.setItem(i, border);
        }
        for(int i=45;i<54;i++){
            inventory.setItem(i, border);
        }
        for(int j=1;j<5;j++){
            inventory.setItem(9*j, border);
        }
        for(int j=1;j<5;j++){
            inventory.setItem(9*j+8, border);
        }
        for(int i=19;i<26;i++){
            inventory.setItem(i, border);
        }
        for(int i=10;i<18;i++){
            inventory.setItem(i, border);
        }
        for(int i=12;i<17;i++){
            inventory.setItem(i, border);
        }
        inventory.setItem(10, border);
        
        //changeable blocks
        for(int i=0;i<7;i++){
            inventory.setItem(28+i, Table.blocks[i]);
        }

        for(int i=0;i<7;i++){
            inventory.setItem(37+i, Table.blocks[i+9]);
        }
        
        inventory.setItem(11, Table.blocks[7]);
        
        
        player.openInventory(inventory);
    }
    
    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
