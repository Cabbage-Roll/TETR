package cabbageroll.tetr;

import org.bukkit.plugin.java.JavaPlugin;
import com.xxmicloxx.NoteBlockAPI.model.Playlist;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder;

import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import cabbageroll.tetr.menus.*;

public class Main extends JavaPlugin implements Listener{

    //room list
    public static HashMap<String,Room> roomlist=new HashMap<String,Room>();
    public static HashMap<Player,String> lastui=new HashMap<Player,String>();
    
    public static File customYml;
    public static FileConfiguration customConfig;
    public static void saveCustomYml(FileConfiguration ymlConfig, File ymlFile){
        try{
            ymlConfig.save(ymlFile);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    static JavaPlugin plugin;
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
        this.getCommand("tetr").setExecutor(new OpenMenu());
        
        //detect events
        getServer().getPluginManager().registerEvents(new Listen(), this);
        getServer().getPluginManager().registerEvents(this, this);
        
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
        event.setJoinMessage("Welcome "+event.getPlayer().getName()+"! Use /tetr");
        lastui.put(event.getPlayer(), "home");
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        Player p=event.getPlayer();
        lastui.remove(p);
    }
    
}