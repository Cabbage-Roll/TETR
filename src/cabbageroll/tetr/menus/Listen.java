package cabbageroll.tetr.menus;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import cabbageroll.tetr.Main;
import cabbageroll.tetr.Table;

public class Listen implements Listener{
    @EventHandler
    void onInventoryClick(InventoryClickEvent event){
        Player player=(Player)event.getWhoClicked();
        if(event.getInventory().getHolder() instanceof HomeMenu){
            event.setCancelled(true);
            if(event.getSlot()==9){
                new MultiplayerMenu(player);
            }
        }else if(event.getInventory().getHolder() instanceof MultiplayerMenu){
            event.setCancelled(true);
            if(event.getSlot()==9){
                new MakeRoomMenu(player);
            }else if(event.getSlot()==10){
                new JoinRoomMenu(player);
            }else if(event.getSlot()==36){
                new HomeMenu(player);
            }
        }else if(event.getInventory().getHolder() instanceof MakeRoomMenu){
            event.setCancelled(true);
            if(event.getSlot()==9){
                new RoomMenu(player);
            }else if(event.getSlot()==36){
                new MultiplayerMenu(player);
            }
        }else if(event.getInventory().getHolder() instanceof JoinRoomMenu){
            event.setCancelled(true);
            if(event.getSlot()==9){
                new RoomMenu(player);
            }else if(event.getSlot()==36){
                new MultiplayerMenu(player);
            }
        }else if(event.getInventory().getHolder() instanceof RoomMenu){
            event.setCancelled(true);
            if(event.getSlot()==36){
                new MultiplayerMenu(player);
            }
        }else if(event.getInventory().getHolder() instanceof SkinMenu){
            if(event.getCurrentItem().getType()==Material.THIN_GLASS){
                event.setCancelled(true);
                return;
            }
            
            if(event.getCurrentItem().getType()==Material.AIR && event.getSlot()==11 && event.getCursor().getType()==Material.AIR){
                Table.transparent=!Table.transparent;
                player.sendMessage("Transparency turned "+(Table.transparent?"on":"off"));
                return;
            }
        }   
    }
    
    
    @EventHandler
    public void InvClose(final InventoryCloseEvent e){
        if(e.getInventory().getHolder() instanceof SkinMenu){
            Inventory inv=e.getInventory();
            //save blocks
            for(int i=0;i<7;i++){
                if(inv.getItem(28+i)!=null){
                    Table.blocks[i]=inv.getItem(28+i);
                }else{
                    Table.blocks[i]=new ItemStack(Material.AIR);
                }
            }
            
            //save ghost
            for(int i=0;i<7;i++){
                if(inv.getItem(37+i)!=null){
                    Table.blocks[i+9]=inv.getItem(37+i);
                }else{
                    Table.blocks[i+9]=new ItemStack(Material.AIR);
                }
            }
            
            //other
            if(inv.getItem(11)!=null){
                Table.blocks[7]=inv.getItem(11);
            }else{
                Table.blocks[7]=new ItemStack(Material.AIR);
            }
            
            e.getPlayer().sendMessage("Skin saved");
            
            //saving to file
            Main.customConfig.set("blockZ", Table.blocks[0].serialize());
            Main.customConfig.set("blockL", Table.blocks[1].serialize());
            Main.customConfig.set("blockO", Table.blocks[2].serialize());
            Main.customConfig.set("blockS", Table.blocks[3].serialize());
            Main.customConfig.set("blockI", Table.blocks[4].serialize());
            Main.customConfig.set("blockJ", Table.blocks[5].serialize());
            Main.customConfig.set("blockT", Table.blocks[6].serialize());
            
            Main.customConfig.set("ghostZ", Table.blocks[9].serialize());
            Main.customConfig.set("ghostL", Table.blocks[10].serialize());
            Main.customConfig.set("ghostO", Table.blocks[11].serialize());
            Main.customConfig.set("ghostS", Table.blocks[12].serialize());
            Main.customConfig.set("ghostI", Table.blocks[13].serialize());
            Main.customConfig.set("ghostJ", Table.blocks[14].serialize());
            Main.customConfig.set("ghostT", Table.blocks[15].serialize());
            
            Main.customConfig.set("background", Table.blocks[7].serialize());
            
            Main.saveCustomYml(Main.customConfig, Main.customYml);
        }
    }
}
