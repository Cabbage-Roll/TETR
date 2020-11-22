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
import cabbageroll.tetr.constants.Blocks;
import xseries.XMaterial;

public class SkinMenu implements InventoryHolder {
    private Inventory inventory = null;

    protected final static int BACK_LOCATION = 0;
    protected final static int TORCH_LOCATION = 8;
    
    public SkinMenu(Player player){
        
        Main.lastui.put(player, "skin");
        Inventory inventory=Bukkit.createInventory(this, 54, "Skin editor");
        ItemStack border=XMaterial.GLASS_PANE.parseItem();
        //fill the border with glass
        for(int i=0;i<54;i++){
            inventory.setItem(i, border);
        }
        
        ItemStack blocks[] = Main.skinmap.get(player);
        //changeable blocks
        for(int i=0;i<7;i++){
            if(Main.skineditorver.get(player)==0) {
                inventory.setItem(28+i, Blocks.blocks[i]);
            }else if(Main.skineditorver.get(player)==1) {
                inventory.setItem(28+i, blocks[i]);
            }
        }

        for(int i=0;i<7;i++){
            if(Main.skineditorver.get(player)==0) {
                inventory.setItem(37+i, Blocks.blocks[i+9]);
            }else if(Main.skineditorver.get(player)==1) {
                inventory.setItem(37+i, blocks[i+9]);
            }
        }
        
        if(Main.skineditorver.get(player)==0) {
            inventory.setItem(11, Blocks.blocks[7]);
        }else if(Main.skineditorver.get(player)==1) {
            inventory.setItem(11, blocks[7]);
        }
        
        inventory.setItem(BACK_LOCATION, createItem(XMaterial.BEDROCK, ChatColor.WHITE + "Back"));
        inventory.setItem(TORCH_LOCATION, createItem(XMaterial.TORCH, ChatColor.WHITE + "" + (Main.skineditorver.get(player)==0?ChatColor.BOLD:"") + "Default", ChatColor.WHITE + "" + (Main.skineditorver.get(player)==1?ChatColor.BOLD:"") + "Custom"));
        
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
