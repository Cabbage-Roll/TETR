package cabbageroll.tetr.menus;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import cabbageroll.tetr.Main;
import cabbageroll.tetr.Table;
import xseries.XMaterial;

public class SkinMenu implements InventoryHolder {
    private Inventory inventory = null;

    protected final static int BACK_LOCATION = 0;
    protected final static int TORCH_LOCATION = 8;
    
    public static int test = 0;
    
    public SkinMenu(Player player){
        
        Main.lastui.put(player, "skin");
        Inventory inventory=Bukkit.createInventory(this, 54, "Skin editor");
        ItemStack border=XMaterial.GLASS_PANE.parseItem();
        //fill the border with glass
        for(int i=0;i<54;i++){
            inventory.setItem(i, border);
        }
        
        //changeable blocks
        for(int i=0;i<7;i++){
            inventory.setItem(28+i, Table.blocks[i]);
        }

        for(int i=0;i<7;i++){
            inventory.setItem(37+i, Table.blocks[i+9]);
        }
        
        inventory.setItem(11, Table.blocks[7]);
        
        inventory.setItem(BACK_LOCATION, createItem(XMaterial.BEDROCK, ChatColor.WHITE + "Back"));
        inventory.setItem(TORCH_LOCATION, createItem(XMaterial.TORCH, ChatColor.WHITE + "" + (test==0?ChatColor.BOLD:"") + "Placeholder", ChatColor.WHITE + "" + (test==1?ChatColor.BOLD:"") + "forty two"));
        
        player.openInventory(inventory);
    }
    
    static ItemStack createItem(final XMaterial material, final String name, final String... lore) {
        ItemStack item = material.parseItem();
        ItemMeta meta;
        meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore));
        item.setItemMeta(meta);
        return item;
    }
    
    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
