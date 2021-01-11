package tetr.minecraft.menus;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;
import tetr.minecraft.Main;
import tetr.minecraft.xseries.XMaterial;

public class RoomMenu implements InventoryHolder {
    private Inventory inventory = null;
    
    protected final static int BACK_LOCATION = 0;
    protected final static int GAME_LOCATION = 49;
    protected final static int SONG_LOCATION = 52;
    protected final static int SETTINGS_LOCATION = 53;
    
    public RoomMenu(Player player){
        Main.lastui.put(player, "room");
        Inventory inventory=Bukkit.createInventory(this, 54, Main.inwhichroom.get(player).name);
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
        
        item=XMaterial.PLAYER_HEAD.parseItem();
        int i=0;
        for(Player p: Main.inwhichroom.get(player).playerlist){
            itemmeta=item.getItemMeta();
            itemmeta.setDisplayName(ChatColor.WHITE + p.getName());
            if(Main.inwhichroom.get(player).host.equals(p)){
                itemmeta.setLore(Arrays.asList(ChatColor.DARK_RED + "HOST"));
            }else{
                itemmeta.setLore(null);
            }

            item.setItemMeta(itemmeta);
            inventory.setItem(9+i, item);
            i++;
        }
        
        if(Main.inwhichroom.get(player).host.equals(player)){
            if(Main.inwhichroom.get(player).running){
                inventory.setItem(GAME_LOCATION, createItem(XMaterial.ANVIL, ChatColor.WHITE + "ABORT"));
            }else{
                inventory.setItem(GAME_LOCATION, createItem(XMaterial.DIAMOND_SWORD, ChatColor.WHITE + "START"));
            }
        }else{
            inventory.setItem(GAME_LOCATION, createItem(XMaterial.BARRIER, ChatColor.WHITE + "YOU ARE NOT THE HOST"));
        }
        
        inventory.setItem(BACK_LOCATION, createItem(XMaterial.BEDROCK, ChatColor.WHITE + "Back"));
        inventory.setItem(SONG_LOCATION, createItem(XMaterial.NOTE_BLOCK, ChatColor.WHITE + "Song"));
        inventory.setItem(SETTINGS_LOCATION, createItem(XMaterial.COMPASS, ChatColor.WHITE + "Table settings"));
        
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
