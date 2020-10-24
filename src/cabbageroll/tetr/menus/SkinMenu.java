package cabbageroll.tetr.menus;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import cabbageroll.tetr.Pluginmain;
import cabbageroll.tetr.Table;

public class SkinMenu implements Listener{
    static HashMap<Player, Boolean> isopen=new HashMap<Player, Boolean>();
    public static void openGUI(Player player){
        Inventory inv=Bukkit.createInventory(null, 54, "Skin editor");
        ItemStack border=new ItemStack(Material.THIN_GLASS);
        //fill the border with glass
        for(int i=0;i<9;i++){
            inv.setItem(i, border);
        }
        for(int i=45;i<54;i++){
            inv.setItem(i, border);
        }
        for(int j=1;j<5;j++){
            inv.setItem(9*j, border);
        }
        for(int j=1;j<5;j++){
            inv.setItem(9*j+8, border);
        }
        for(int i=19;i<26;i++){
            inv.setItem(i, border);
        }
        for(int i=10;i<18;i++){
            inv.setItem(i, border);
        }
        for(int i=12;i<17;i++){
            inv.setItem(i, border);
        }
        inv.setItem(10, border);
        
        //changeable blocks
        for(int i=0;i<7;i++){
            inv.setItem(28+i, Table.blocks[i]);
        }

        for(int i=0;i<7;i++){
            inv.setItem(37+i, Table.blocks[i+9]);
        }
        
        inv.setItem(11, Table.blocks[7]);
        
        
        player.openInventory(inv);

        isopen.put(player, true);
    }
    
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e){

        Player p=(Player)e.getWhoClicked();
        if(isopen.containsKey(p)){
            if(isopen.get(p)){
                if(e.getCurrentItem().getType()==Material.THIN_GLASS){
                    e.setCancelled(true);
                    return;
                }
                
                if(e.getCurrentItem().getType()==Material.AIR && e.getSlot()==11 && e.getCursor().getType()==Material.AIR){
                    Table.transparent=!Table.transparent;
                    p.sendMessage("Transparency turned "+(Table.transparent?"on":"off"));
                    return;
                }
            }
        }
    }
    
    @EventHandler
    public void InvClose(final InventoryCloseEvent e){
        Inventory inv=e.getInventory();
        Player p=(Player)e.getPlayer();
        if(isopen.containsKey(p)){
            if(isopen.get(e.getPlayer())){
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
                isopen.put(p, false);
                
                //saving to file
                Pluginmain.customConfig.set("blockZ", Table.blocks[0].serialize());
                Pluginmain.customConfig.set("blockL", Table.blocks[1].serialize());
                Pluginmain.customConfig.set("blockO", Table.blocks[2].serialize());
                Pluginmain.customConfig.set("blockS", Table.blocks[3].serialize());
                Pluginmain.customConfig.set("blockI", Table.blocks[4].serialize());
                Pluginmain.customConfig.set("blockJ", Table.blocks[5].serialize());
                Pluginmain.customConfig.set("blockT", Table.blocks[6].serialize());
                
                Pluginmain.customConfig.set("ghostZ", Table.blocks[9].serialize());
                Pluginmain.customConfig.set("ghostL", Table.blocks[10].serialize());
                Pluginmain.customConfig.set("ghostO", Table.blocks[11].serialize());
                Pluginmain.customConfig.set("ghostS", Table.blocks[12].serialize());
                Pluginmain.customConfig.set("ghostI", Table.blocks[13].serialize());
                Pluginmain.customConfig.set("ghostJ", Table.blocks[14].serialize());
                Pluginmain.customConfig.set("ghostT", Table.blocks[15].serialize());
                
                Pluginmain.customConfig.set("background", Table.blocks[7].serialize());
                
                Pluginmain.saveCustomYml(Pluginmain.customConfig, Pluginmain.customYml);
            }
        }
    }
}
