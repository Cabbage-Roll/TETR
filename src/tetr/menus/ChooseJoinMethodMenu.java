package tetr.menus;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;
import xseries.XMaterial;

public class ChooseJoinMethodMenu implements InventoryHolder {
    private Inventory inventory = null;
    
    protected final static int BACK_LOCATION = 0;
    protected final static int ROOMLIST_LOCATION = 9;
    protected final static int ID_LOCATION = 10;
    
    public ChooseJoinMethodMenu(Player player) {
        Inventory inventory=Bukkit.createInventory(this, 54, "Multiplayer");
        ItemStack border=XMaterial.GLASS_PANE.parseItem();
        //fill the border with glass
        for(int i=0;i<9;i++){
            inventory.setItem(i, border);
        }
        for(int i=45;i<54;i++){
            inventory.setItem(i, border);
        }
        
        inventory.setItem(BACK_LOCATION, createItem(XMaterial.BEDROCK, ChatColor.WHITE + "Back"));
        inventory.setItem(ROOMLIST_LOCATION, createItem(XMaterial.COAL_BLOCK, ChatColor.WHITE + "Public room listing"));
        inventory.setItem(ID_LOCATION, createItem(XMaterial.NAME_TAG, ChatColor.WHITE + "Enter room ID"));
        
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
