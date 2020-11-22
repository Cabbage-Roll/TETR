package cabbageroll.tetr.menus;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.xxmicloxx.NoteBlockAPI.model.Playlist;

import cabbageroll.tetr.Main;
import cabbageroll.tetr.Room;
import xseries.XMaterial;

public class SongMenu implements InventoryHolder {
private Inventory inventory = null;
    
    protected final static int BACK_LOCATION = 0;
    
    public SongMenu(Player player) {
        Main.lastui.put(player, "song");
        Inventory inventory=Bukkit.createInventory(this, 54, "Choose song");
        ItemStack border=XMaterial.GLASS_PANE.parseItem();
        //fill the border with glass
        for(int i=0;i<9;i++){
            inventory.setItem(i, border);
        }
        for(int i=45;i<54;i++){
            inventory.setItem(i, border);
        }
        
        //clickable items
        
        Room room=Main.roommap.get(Main.inwhichroom.get(player));
        Playlist playlist = Room.slist;
        
        inventory.setItem(BACK_LOCATION, createItem(XMaterial.BEDROCK, ChatColor.WHITE + "Back"));
        inventory.setItem(9, createItem(XMaterial.NOTE_BLOCK, ChatColor.YELLOW + "Random"));
        for(int i=0;i<playlist.getCount();i++) {
            inventory.setItem(10+i, createItem(XMaterial.NOTE_BLOCK, ChatColor.WHITE + playlist.get(i).getPath().getName().replaceAll(".nbs$", "")));
        }
        
        
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
