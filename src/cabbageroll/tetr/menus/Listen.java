package cabbageroll.tetr.menus;


import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import cabbageroll.tetr.Main;
import cabbageroll.tetr.Room;
import cabbageroll.tetr.Table;
import xseries.XMaterial;

public class Listen implements Listener {
    @EventHandler
    void onInventoryClick(InventoryClickEvent event){
        Player player=(Player)event.getWhoClicked();
        int slot=event.getSlot();
        if(event.getInventory().getHolder() instanceof HomeMenu){
            event.setCancelled(true);
            if(slot==HomeMenu.MULTIPLAYER_LOCATION){
                new MultiplayerMenu(player);
            }else if(slot==HomeMenu.SKINEDITOR_LOCATION){
                new SkinMenu(player);
            }
        }else if(event.getInventory().getHolder() instanceof MultiplayerMenu){
            event.setCancelled(true);
            if(slot==MultiplayerMenu.CREATEROOM_LOCATION){
                new MakeRoomMenu(player);
            }else if(slot==MultiplayerMenu.LISTROOMS_LOCATION){
                new JoinRoomMenu(player);
            }else if(slot==MultiplayerMenu.BACK_LOCATION){
                new HomeMenu(player);
            }
        }else if(event.getInventory().getHolder() instanceof MakeRoomMenu){
            event.setCancelled(true);
            if(slot==MakeRoomMenu.NEWROOM_LOCATION){
                new Room(player);
                new RoomMenu(player);
            }else if(slot==MakeRoomMenu.BACK_LOCATION){
                new MultiplayerMenu(player);
            }
        }else if(event.getInventory().getHolder() instanceof JoinRoomMenu){
            event.setCancelled(true);
            if(JoinRoomMenu.ROOM_LOCATION_MIN<=slot && slot<=JoinRoomMenu.ROOM_LOCATION_MIN+JoinRoomMenu.pagesize){
                Main.roommap.get(event.getCurrentItem().getItemMeta().getDisplayName()).addPlayer(player);
                new RoomMenu(player);
            }else if(slot==JoinRoomMenu.BACK_LOCATION){
                new MultiplayerMenu(player);
            }
        }else if(event.getInventory().getHolder() instanceof RoomMenu){
            event.setCancelled(true);
            if(slot==RoomMenu.BACK_LOCATION){
                Main.roommap.get(Main.inwhichroom.get(player)).removePlayer(player);
                new MultiplayerMenu(player);
            }else if(slot==49){
                ItemMeta itemmeta;
                if(Main.roommap.get(Main.inwhichroom.get(player)).host.equals(player)){
                    if(Main.roommap.get(Main.inwhichroom.get(player)).running){
                        Main.roommap.get(Main.inwhichroom.get(player)).stopRoom();
                        ItemStack start=new ItemStack(XMaterial.DIAMOND_SWORD.parseMaterial());
                        itemmeta=start.getItemMeta();
                        itemmeta.setDisplayName("START");
                        start.setItemMeta(itemmeta);
                        event.getInventory().setItem(49, start);
                    }else{
                        Main.roommap.get(Main.inwhichroom.get(player)).startRoom();
                        ItemStack item=new ItemStack(XMaterial.ANVIL.parseMaterial());
                        itemmeta=item.getItemMeta();
                        itemmeta.setDisplayName("ABORT");
                        item.setItemMeta(itemmeta);
                        event.getInventory().setItem(49, item);
                    }
                }
            }else if(slot==RoomMenu.SETTINGS_LOCATION){
                new SimpleSettingsMenu(player);
            }
        }else if(event.getInventory().getHolder() instanceof SkinMenu){
            if(event.getCurrentItem()==null){
                event.setCancelled(true);
            }else if(event.getCurrentItem().getType()==XMaterial.GLASS_PANE.parseMaterial()){
                event.setCancelled(true);
            }else if(event.getCurrentItem().getType()==XMaterial.AIR.parseMaterial()){
                if(slot==11 && event.getCursor().getType()==XMaterial.AIR.parseMaterial()){
                    Table.transparent=!Table.transparent;
                    player.sendMessage("Transparency turned "+(Table.transparent?"on":"off"));
                    return;
                }
            }else if(event.getSlot()==SkinMenu.BACK_LOCATION || event.getSlot()==SkinMenu.TORCH_LOCATION) {
                event.setCancelled(true);
            }
            
            if(event.getSlot()==SkinMenu.TORCH_LOCATION) {
                SkinMenu.test++;
                SkinMenu.test%=2;
                new SkinMenu(player);
            }
            
            if(event.getSlot()==SkinMenu.BACK_LOCATION) {
                Inventory inv=event.getInventory();
                //save blocks
                for(int i=0;i<7;i++){
                    if(inv.getItem(28+i)!=null){
                        Table.blocks[i]=inv.getItem(28+i);
                    }else{
                        Table.blocks[i]=new ItemStack(XMaterial.AIR.parseMaterial());
                    }
                }
                
                //save ghost
                for(int i=0;i<7;i++){
                    if(inv.getItem(37+i)!=null){
                        Table.blocks[i+9]=inv.getItem(37+i);
                    }else{
                        Table.blocks[i+9]=new ItemStack(XMaterial.AIR.parseMaterial());
                    }
                }
                
                //other
                if(inv.getItem(11)!=null){
                    Table.blocks[7]=inv.getItem(11);
                }else{
                    Table.blocks[7]=new ItemStack(XMaterial.AIR.parseMaterial());
                }
                
                player.sendMessage("Skin saved");
                
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
                
                new HomeMenu(player);
                
                return;
            }
        }else if(event.getInventory().getHolder() instanceof SettingsMenu) {
            event.setCancelled(true);
            
            int by=0;
            if(event.getClick()==ClickType.LEFT){
                by=+1;
            }else if(event.getClick()==ClickType.RIGHT){
                by=-1;
            }
             
            ItemStack item=event.getInventory().getItem(event.getSlot());
            if(item!=null){
                ItemMeta itemmeta=item.getItemMeta();
                
                Table table=Main.roommap.get(Main.inwhichroom.get(player)).playerboards.get(player);
                
                switch(event.getSlot()){
                case SettingsMenu.BACK_LOCATION:
                    new RoomMenu(player);
                    return;
                case SettingsMenu.TORCH_LOCATION:
                    new SimpleSettingsMenu(player);
                    return;
                case 11:
                    table.gx=player.getLocation().getBlockX();
                    table.gy=player.getLocation().getBlockY();
                    table.gz=player.getLocation().getBlockZ();
                    break;
                case 12:
                    table.gx+=by;
                    break;
                case 13:
                    table.gy+=by;
                    break;
                case 14:
                    table.gz+=by;
                    break;
                case 37:
                    table.m1x+=by;
                    break;
                case 38:
                    table.m2x+=by;
                    break;
                case 39:
                    table.m3x+=by;
                    break;
                case 41:
                    table.m1y+=by;
                    break;
                case 42:
                    table.m2y+=by;
                    break;
                case 43:
                    table.m3y+=by;
                    break;
                case 53:
                    Main.roommap.get(Main.inwhichroom.get(player)).backfire=!Main.roommap.get(Main.inwhichroom.get(player)).backfire;
                    break;
                default:
                    return;
                }
                
                item.setItemMeta(itemmeta);
                event.getInventory().setItem(event.getSlot(), item);
                new SettingsMenu(player);
            }
        }else if(event.getInventory().getHolder() instanceof SimpleSettingsMenu) {
            event.setCancelled(true);
            
            int by=0;
            if(event.getClick()==ClickType.LEFT){
                by=+1;
            }else if(event.getClick()==ClickType.RIGHT){
                by=-1;
            }
            
            Table table=Main.roommap.get(Main.inwhichroom.get(player)).playerboards.get(player);
            
            switch(event.getSlot()) {
            case SimpleSettingsMenu.BACK_LOCATION:
                new RoomMenu(player);
                return;
            case SimpleSettingsMenu.TORCH_LOCATION:
                new SettingsMenu(player);
                return;
            case 21:
                table.gx+=by;
                break;
            case 22:
                table.gy+=by;
                break;
            case 23:
                table.gz+=by;
                break;
            case 30:
                table.rotateTable("X");
                break;
            case 31:
                table.rotateTable("Y");
                break;
            case 32:
                table.rotateTable("Z");
                break;
            default:
                return;
            }
            
            new SimpleSettingsMenu(player);
        }
    }
    
    
    @EventHandler
    public void onItemHeld(PlayerItemHeldEvent event){
        Player player=event.getPlayer();
        if(Main.inwhichroom.containsKey(player)){
            Table table=Main.roommap.get(Main.inwhichroom.get(player)).playerboards.get(player);
            if(table!=null){
                if(table.task!=null){
                    int itemId=event.getNewSlot();
                    switch(itemId){
                    case 0:
                        table.userInput("left");
                        break;
                    case 1:
                        table.userInput("right");
                        break;
                    case 2:
                        table.userInput("instant");
                        break;
                    case 3:
                        table.userInput("space");
                        break;
                    case 4:
                        table.userInput("y");
                        break;
                    case 5:
                        table.userInput("x");
                        break;
                    case 6:
                        table.userInput("up");
                        break;
                    case 7:
                        table.userInput("c");
                        break;
                    case 8:
                        return;
                    }
                    player.getInventory().setHeldItemSlot(8);
                }
            }
        }
    }
}
