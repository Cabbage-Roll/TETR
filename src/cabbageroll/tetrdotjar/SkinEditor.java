package cabbageroll.tetrdotjar;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SkinEditor {
    public static void openGUI(Player player) {
        Inventory i = Bukkit.createInventory(null, 27, "Main Menu");

        ItemStack BlankGlass = new ItemStack(Material.STAINED_GLASS_PANE, 2, (byte)(Math.random()*14));
        ItemStack KickPlayer = new ItemStack(Material.TNT);
        i.setItem(0, BlankGlass);
        i.setItem(1, BlankGlass);
        i.setItem(2, BlankGlass);
        i.setItem(3, BlankGlass);
        i.setItem(4, BlankGlass);
        i.setItem(5, BlankGlass);
        i.setItem(6, BlankGlass);
        i.setItem(7, BlankGlass);
        i.setItem(8, BlankGlass);
        i.setItem(9, BlankGlass);
        i.setItem(10, KickPlayer);
        i.setItem(17, BlankGlass);
        i.setItem(18, BlankGlass);
        i.setItem(19, BlankGlass);
        i.setItem(20, BlankGlass);
        i.setItem(21, BlankGlass);
        i.setItem(22, BlankGlass);
        i.setItem(23, BlankGlass);
        i.setItem(24, BlankGlass);
        i.setItem(25, BlankGlass);
        i.setItem(26, BlankGlass);
        player.openInventory(i);
    }
}
