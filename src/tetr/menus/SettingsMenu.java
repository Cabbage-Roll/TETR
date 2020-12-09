package tetr.menus;


import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import tetr.Main;
import tetr.Table;
import xseries.XMaterial;

public class SettingsMenu implements InventoryHolder {
    private Inventory inventory=null;
    
    protected final static int BACK_LOCATION = 0;
    protected final static int TORCH_LOCATION = 8;
    
    public SettingsMenu(Player player){
        Main.lastui.put(player, "settings");
        Inventory inventory=Bukkit.createInventory(this, 54, "Settings");
        ItemStack border=XMaterial.GLASS_PANE.parseItem();
        //fill the border with glass
        for(int i=0;i<9;i++){
            inventory.setItem(i, border);
        }
        for(int i=45;i<54;i++){
            inventory.setItem(i, border);
        }
        

        Table table=Main.inwhichroom.get(player).playerboards.get(player);
        
        inventory.setItem(BACK_LOCATION, createItem(XMaterial.BEDROCK, ChatColor.WHITE + "Back"));
        inventory.setItem(TORCH_LOCATION, createItem(XMaterial.REDSTONE_TORCH, ChatColor.DARK_RED + "This is advanced settings menu", ChatColor.YELLOW + "" + ChatColor.BOLD + "Click to go to standard menu"));
        
        inventory.setItem(11, createItem(XMaterial.DIRT, "your pos"));
        inventory.setItem(12, createItem(XMaterial.DIRT, "GX: "+table.getGx()));
        inventory.setItem(13, createItem(XMaterial.DIRT, "GY: "+table.getGy()));
        inventory.setItem(14, createItem(XMaterial.DIRT, "GZ: "+table.getGz()));
        inventory.setItem(37, createItem(XMaterial.DIRT, "M1X: "+table.m1x));
        inventory.setItem(38, createItem(XMaterial.DIRT, "M2X: "+table.m2x));
        inventory.setItem(39, createItem(XMaterial.DIRT, "M3X: "+table.m3x));
        inventory.setItem(41, createItem(XMaterial.DIRT, "M1Y: "+table.m1y));
        inventory.setItem(42, createItem(XMaterial.DIRT, "M2Y: "+table.m2y));
        inventory.setItem(43, createItem(XMaterial.DIRT, "M3Y: "+table.m3y));
        inventory.setItem(53, createItem(XMaterial.DIRT, "BACKFIRE: "+Main.inwhichroom.get(player).backfire));
        
        
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
    public Inventory getInventory(){
        return inventory;
    }

}
