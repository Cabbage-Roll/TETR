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
import tetr.minecraft.Room;
import tetr.minecraft.xseries.XMaterial;

public class JoinRoomMenu implements InventoryHolder {
    private Inventory inventory=null;
    
    protected final static int BACK_LOCATION = 0;
    protected final static int ROOM_LOCATION_MIN = 9;
    protected final static int pagesize=36;

    protected final static int MINUSPAGE_LOCATION = 45;
    protected final static int PLUSPAGE_LOCATION = 53;
    
    
    public JoinRoomMenu(Player player, int p){
        Main.lastui.put(player, "joinroom");
        Inventory inventory=Bukkit.createInventory(this, 54, "Join room");
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
        if(p>0) {
            inventory.setItem(MINUSPAGE_LOCATION, createItem(XMaterial.ARROW, ChatColor.WHITE + "Previous page"));
        }
        
        Main.joinroompage.put(player, p);
        int page = p;
        int display = 0;
        int counter = 0;
        int i = 0;
        
        Object[] roomlist = Main.roommap.values().toArray();
        while(true) {
            if(i<roomlist.length) {
            Room room = (Room) roomlist[i];
                if(!room.unlisted) {
                    if(counter<pagesize) {
                        if(display==page) {
                            inventory.setItem(ROOM_LOCATION_MIN+counter, createItem(XMaterial.COAL_BLOCK, ChatColor.WHITE + room.id));
                        }
                    }else {
                        if(display==page) {
                            break;
                        }
                        counter = -1;
                        display++;
                        i--;
                    }
                    counter++;
                }
            }else {
                break;
            }
            i++;
        }
        
        if(counter==pagesize) {
            inventory.setItem(PLUSPAGE_LOCATION, createItem(XMaterial.ARROW, ChatColor.WHITE + "Next page"));
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
