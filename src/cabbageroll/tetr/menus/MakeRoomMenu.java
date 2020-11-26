package cabbageroll.tetr.menus;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import cabbageroll.tetr.Main;
import net.md_5.bungee.api.ChatColor;
import xseries.XMaterial;

public class MakeRoomMenu implements InventoryHolder{
    private Inventory inventory=null;
    
    protected final static int BACK_LOCATION = 0;
    protected final static int NEWROOM_LOCATION = 9;
    protected final static int NEWPRIVATEROOM_LOCATION = 10;
    
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

        inventory.setItem(BACK_LOCATION, createItem(XMaterial.BEDROCK, ChatColor.WHITE + "Back"));
        inventory.setItem(NEWROOM_LOCATION, createItem(XMaterial.COAL_BLOCK, ChatColor.WHITE + "New room"));
        inventory.setItem(NEWPRIVATEROOM_LOCATION, createItem(XMaterial.COAL_BLOCK, ChatColor.WHITE + "New room (private)"));
        
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
