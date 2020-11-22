package cabbageroll.tetr.menus;


import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import cabbageroll.tetr.Main;
import cabbageroll.tetr.Room;
import cabbageroll.tetr.Table;
import net.md_5.bungee.api.ChatColor;
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
                Main.roommap.get(ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName())).addPlayer(player);
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
            }else if(slot==RoomMenu.SONG_LOCATION){
                new SongMenu(player);
            }
        }else if(event.getInventory().getHolder() instanceof SkinMenu){
            if(Main.skineditorver.get(player)==0) {
                event.setCancelled(true);
                File customYml = new File(Main.plugin.getDataFolder() + "/userdata/" + player.getUniqueId() + ".yml");
                FileConfiguration customConfig = YamlConfiguration.loadConfiguration(customYml);
                customConfig.set("useSkinSlot", 0);

                Main.saveCustomYml(customConfig, customYml);
            }
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
                Main.skineditorver.put(player, Main.skineditorver.get(player)+1);
                Main.skineditorver.put(player, Main.skineditorver.get(player)%2);
                new SkinMenu(player);
            }
            
            if(event.getSlot()==SkinMenu.BACK_LOCATION) {
                ItemStack[] blocks = Main.skinmap.get(player);
                if(Main.skineditorver.get(player)==1) {
                    Inventory inv=event.getInventory();
                    //save blocks
                    for(int i=0;i<7;i++){
                        if(inv.getItem(28+i)!=null){
                            blocks[i]=inv.getItem(28+i);
                        }else{
                            blocks[i]=new ItemStack(XMaterial.AIR.parseMaterial());
                        }
                    }
                    
                    //save ghost
                    for(int i=0;i<7;i++){
                        if(inv.getItem(37+i)!=null){
                            blocks[i+9]=inv.getItem(37+i);
                        }else{
                            blocks[i+9]=new ItemStack(XMaterial.AIR.parseMaterial());
                        }
                    }
                    
                    //other
                    if(inv.getItem(11)!=null){
                        blocks[7]=inv.getItem(11);
                    }else{
                        blocks[7]=new ItemStack(XMaterial.AIR.parseMaterial());
                    }
                    
                    player.sendMessage("Skin saved");
                    
                    File customYml = new File(Main.plugin.getDataFolder() + "/userdata/" + player.getUniqueId() + ".yml");
                    FileConfiguration customConfig = YamlConfiguration.loadConfiguration(customYml);
                    
                    //saving to file
                    customConfig.set("blockZ", blocks[0]);
                    customConfig.set("blockL", blocks[1]);
                    customConfig.set("blockO", blocks[2]);
                    customConfig.set("blockS", blocks[3]);
                    customConfig.set("blockI", blocks[4]);
                    customConfig.set("blockJ", blocks[5]);
                    customConfig.set("blockT", blocks[6]);
                    customConfig.set("background", blocks[7]);
                    customConfig.set("garbage", blocks[8]);
                    customConfig.set("ghostZ", blocks[9]);
                    customConfig.set("ghostL", blocks[10]);
                    customConfig.set("ghostO", blocks[11]);
                    customConfig.set("ghostS", blocks[12]);
                    customConfig.set("ghostI", blocks[13]);
                    customConfig.set("ghostJ", blocks[14]);
                    customConfig.set("ghostT", blocks[15]);
                    customConfig.set("useSkinSlot", 1);
                    
                    Main.saveCustomYml(customConfig, customYml);
                    
                }
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
        }else if(event.getInventory().getHolder() instanceof SongMenu) {
            event.setCancelled(true);
            Room room = Main.roommap.get(Main.inwhichroom.get(player));
            if(event.getSlot()==SongMenu.BACK_LOCATION) {
                new RoomMenu(player);
            }else if(event.getSlot()==9) {
                room.israndom = true;
            }else if(event.getSlot()-10<Room.slist.getCount()) {
                room.israndom = false;
                room.index = event.getSlot() - 10;
                player.sendMessage("boom");
            }
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
    
    @EventHandler
    public void onPlayerToggleSneakEvent(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        if(player.isSneaking()) {
            Main.roommap.get(Main.inwhichroom.get(player)).playerboards.get(player).startZone();
        }
    }
}
