package cabbageroll.tetr;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Editpiece implements CommandExecutor, Listener{
    Inventory inv;
    
    public void openInventory(final HumanEntity ent) {
        ent.openInventory(inv);
    }
    
    protected ItemStack createGuiItem(final Material material, final String name, final String... lore) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();

        // Set the name of the item
        meta.setDisplayName(name);

        // Set the lore of the item
        meta.setLore(Arrays.asList(lore));

        item.setItemMeta(meta);

        return item;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {


        Player player=(Player)sender;
        inv = Bukkit.createInventory(null, 54, args[0]);
        for(int i=0;i<4;i++) {
            for(int j=0;j<4;j++) {
                inv.setItem(j+i*9,createGuiItem(Material.DIAMOND_SWORD, "Example Sword"));
            }
        }
        

        openInventory(player);
        
        return true;
    }
}
