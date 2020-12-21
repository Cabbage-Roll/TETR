package tetr.minecraft.menus;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import tetr.minecraft.Main;
import tetr.minecraft.Table;
import tetr.minecraft.xseries.XMaterial;

public class SimpleSettingsMenu implements InventoryHolder {
    private Inventory inventory=null;
    
    protected final static int BACK_LOCATION = 0;
    protected final static int TORCH_LOCATION = 8;
    
    public SimpleSettingsMenu(Player player) {
        Main.lastui.put(player, "simsettings");
        Inventory inventory=Bukkit.createInventory(this, 54, "Settings");
        ItemStack border=XMaterial.GLASS_PANE.parseItem();
        //fill the border with glass
        for(int i=0;i<9;i++){
            inventory.setItem(i, border);
        }
        for(int i=45;i<54;i++){
            inventory.setItem(i, border);
        }
        
        //clickable items
        

        Table table=Main.inwhichroom.get(player).playerboards.get(player);
        
        inventory.setItem(BACK_LOCATION, createItem(XMaterial.BEDROCK, ChatColor.WHITE + "Back"));
        inventory.setItem(TORCH_LOCATION, createItem(XMaterial.TORCH, ChatColor.YELLOW + "This is standard settings menu", ChatColor.DARK_RED + "" + ChatColor.BOLD + "Click to go to advanced menu"));
        
        inventory.setItem(21, createItem(XMaterial.RED_WOOL, ChatColor.WHITE + "Move X", "X: " + table.getGx()));
        inventory.setItem(22, createItem(XMaterial.GREEN_WOOL, ChatColor.WHITE + "Move Y", "Y: " + table.getGy()));
        inventory.setItem(23, createItem(XMaterial.BLUE_WOOL, ChatColor.WHITE + "Move Z", "Z: " + table.getGz()));
        
        inventory.setItem(30, createItem(XMaterial.RED_CARPET, ChatColor.WHITE + "Rotate X"));
        inventory.setItem(31, createItem(XMaterial.GREEN_CARPET, ChatColor.WHITE + "Rotate Y"));
        inventory.setItem(32, createItem(XMaterial.BLUE_CARPET, ChatColor.WHITE + "Rotate Z"));
        
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
