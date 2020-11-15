package cabbageroll.tetr.menus;

import java.util.ArrayList;

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

public class SimpleSettingsMenu implements InventoryHolder {
    private Inventory inventory=null;
    
    protected final static int BACK_LOCATION = 0;
    protected final static int TORCH_LOCATION = 8;
    
    public SimpleSettingsMenu(Player player){
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
        ItemStack item;
        ItemMeta itemmeta;
        
        item=new ItemStack(XMaterial.BEDROCK.parseItem());
        itemmeta=item.getItemMeta();
        itemmeta.setDisplayName(ChatColor.WHITE + "Back");
        item.setItemMeta(itemmeta);
        inventory.setItem(BACK_LOCATION, item);
        
        item=new ItemStack(XMaterial.TORCH.parseItem());
        itemmeta=item.getItemMeta();
        itemmeta.setDisplayName(ChatColor.YELLOW + "This is standard settings menu");
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(ChatColor.DARK_RED + "" + ChatColor.BOLD + "Click to go to advanced menu");
        itemmeta.setLore(lore);
        item.setItemMeta(itemmeta);
        inventory.setItem(TORCH_LOCATION, item);
        
        item=new ItemStack(XMaterial.RED_WOOL.parseItem());
        itemmeta=item.getItemMeta();
        itemmeta.setDisplayName(ChatColor.WHITE + "Move X");
        item.setItemMeta(itemmeta);
        inventory.setItem(21, item);
        
        item=new ItemStack(XMaterial.GREEN_WOOL.parseItem());
        itemmeta=item.getItemMeta();
        itemmeta.setDisplayName(ChatColor.WHITE + "Move Y");
        item.setItemMeta(itemmeta);
        inventory.setItem(22, item);
        
        item=new ItemStack(XMaterial.BLUE_WOOL.parseItem());
        itemmeta=item.getItemMeta();
        itemmeta.setDisplayName(ChatColor.WHITE + "Move Z");
        item.setItemMeta(itemmeta);
        inventory.setItem(23, item);
        
        item=new ItemStack(XMaterial.RED_CARPET.parseItem());
        itemmeta=item.getItemMeta();
        itemmeta.setDisplayName(ChatColor.WHITE + "Rotate X");
        item.setItemMeta(itemmeta);
        inventory.setItem(30, item);
        
        item=new ItemStack(XMaterial.GREEN_CARPET.parseItem());
        itemmeta=item.getItemMeta();
        itemmeta.setDisplayName(ChatColor.WHITE + "Rotate Y");
        item.setItemMeta(itemmeta);
        inventory.setItem(31, item);
        
        item=new ItemStack(XMaterial.BLUE_CARPET.parseItem());
        itemmeta=item.getItemMeta();
        itemmeta.setDisplayName(ChatColor.WHITE + "Rotate Z");
        item.setItemMeta(itemmeta);
        inventory.setItem(32, item);
        
        player.openInventory(inventory);
    }
    
    @Override
    public Inventory getInventory(){
        return inventory;
    }
}
