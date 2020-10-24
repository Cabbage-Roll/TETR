package cabbageroll.tetr;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import com.xxmicloxx.NoteBlockAPI.model.Playlist;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer;
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder;

import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;


public class Pluginmain extends JavaPlugin implements Listener{

    //room list
    public static ArrayList<Room> roomlist=new ArrayList<Room>();
    
    public static File customYml;
    public static FileConfiguration customConfig;
    public static void saveCustomYml(FileConfiguration ymlConfig, File ymlFile) {
        try {
        ymlConfig.save(ymlFile);
        } catch (IOException e) {
        e.printStackTrace();
        }
    }

    static JavaPlugin plugin;
    public static Duel match;
    public static int numberofsongs;
    String[] pathnames;
    String xd;
    static Song[] sarr;
    @Override
    public void onEnable() {
        plugin=this;
        
        
        customYml = new File(plugin.getDataFolder()+"/config.yml");
        
        customConfig = YamlConfiguration.loadConfiguration(customYml);

        
        
        System.out.println("Plugin started");
        getServer().getPluginManager().registerEvents(this, this);
        this.getCommand("skineditor").setExecutor(new OpenSkinEditor());
        this.getCommand("createroom").setExecutor(new CreateRoom());
        this.getCommand("joinroom").setExecutor(new JoinRoom());
        this.getCommand("room").setExecutor(new RoomControls());
        
        getServer().getPluginManager().registerEvents(new SkinEditor(), this);
        //trash
        File f = new File(plugin.getDataFolder()+"\\songs");
        f.mkdirs();
        numberofsongs=f.listFiles().length;
        if(numberofsongs>0){
            System.out.print("[TETR] "+numberofsongs+" song(s) loaded");
            
            pathnames=new String[numberofsongs];
            sarr=new Song[numberofsongs];
            pathnames = f.list();
            for(int i=0;i<numberofsongs;i++){
                xd=plugin.getDataFolder()+"\\songs\\"+pathnames[i];
                sarr[i]=NBSDecoder.parse(new File(xd));
            }
            
            Room.slist=new Playlist(sarr);
            //tRASH end
        }else{
            Bukkit.getLogger().warning("[TETR] No songs detected. Please add some songs!");
        }
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        event.setJoinMessage("Welcome " + event.getPlayer().getName() + "!");
    }
    
    @EventHandler
    public void onItemHeld(PlayerItemHeldEvent event){
        Player p=event.getPlayer();
        if(Pluginmain.roomlist.get(0).playerlist.get(p).task!=null){
            int itemId = event.getNewSlot();
            switch(itemId){
            case 0:
                Pluginmain.roomlist.get(0).playerlist.get(p).userInput("y");
                break;
            case 1:
                Pluginmain.roomlist.get(0).playerlist.get(p).userInput("x");
                break;
            case 2:
                Pluginmain.roomlist.get(0).playerlist.get(p).userInput("c");
                break;
            case 3:
                Pluginmain.roomlist.get(0).playerlist.get(p).userInput("space");
                break;
            case 4:
                Pluginmain.roomlist.get(0).playerlist.get(p).userInput("up");
                break;
            case 5:
                Pluginmain.roomlist.get(0).playerlist.get(p).userInput("instant");
                break;
            case 6:
                Pluginmain.roomlist.get(0).playerlist.get(p).userInput("left");
                break;
            case 7:
                Pluginmain.roomlist.get(0).playerlist.get(p).userInput("right");
                break;
            case 8:
                return;
            }
            p.getInventory().setHeldItemSlot(8);
        }
    }
    /*
    @EventHandler
    public void playerMoveEvent(PlayerMoveEvent e) {
        
        double dx=e.getFrom().getX()-e.getTo().getX();
        double dy=e.getFrom().getY()-e.getTo().getY();
        double dz=e.getFrom().getZ()-e.getTo().getZ();
        Vector hey =  new Vector(dx, dy, dz);
        Pluginmain.sp.player.sendMessage("velocity "+hey);
    }*/
}