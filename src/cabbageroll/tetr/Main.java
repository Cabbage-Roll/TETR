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
import java.util.ArrayList;
import java.util.HashMap;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import cabbageroll.tetr.menus.*;

public class Main extends JavaPlugin implements Listener{
    public static JavaPlugin plugin;
    public static ConsoleCommandSender console;
    
    public static ArrayList<Room> roomlist=new ArrayList<Room>();
    public static HashMap<String,Room> roommap=new HashMap<String,Room>();
    public static HashMap<Player,String> lastui=new HashMap<Player,String>();
    public static HashMap<Player,String> inwhichroom=new HashMap<Player,String>();
    
    public static File customYml;
    public static FileConfiguration customConfig;
    public static void saveCustomYml(FileConfiguration ymlConfig, File ymlFile){
        try{
            ymlConfig.save(ymlFile);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public static int numberofsongs;
    String[] pathnames;
    String xd;
    static Song[] sarr;
    
    @Override
    public void onEnable(){
        plugin=this;
        console=getServer().getConsoleSender();
        
        customYml = new File(this.getDataFolder()+"/config.yml");
        customConfig = YamlConfiguration.loadConfiguration(customYml);
        System.out.println("Plugin started");
        this.getCommand("tetr").setExecutor(new OpenMenu());
        
        //detect events
        getServer().getPluginManager().registerEvents(new Listen(), this);
        getServer().getPluginManager().registerEvents(this, this);
        
        //trash
        File f = new File(this.getDataFolder()+"/songs");
        f.mkdirs();
        numberofsongs=f.listFiles().length;
        if(numberofsongs>0){
            
            console.sendMessage("§2TETR: "+numberofsongs+" song(s) loaded");
            
            pathnames=new String[numberofsongs];
            sarr=new Song[numberofsongs];
            pathnames = f.list();
            for(int i=0;i<numberofsongs;i++){
                xd=this.getDataFolder()+"/songs/"+pathnames[i];
                sarr[i]=NBSDecoder.parse(new File(xd));
            }
            
            Room.slist=new Playlist(sarr);
            //tRASH end
        }else{
            console.sendMessage("§4TETR: No songs detected. Please add some songs!");
        }
    }
    
    @Override
    public void onDisable(){
        plugin=null;
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player=event.getPlayer();
        lastui.put(player, "home");
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        Player player=event.getPlayer();
        if(lastui.containsKey(player)){
            lastui.remove(player);
        }
        if(inwhichroom.containsKey(player)){
            Main.roommap.get(Main.inwhichroom.get(player)).removePlayer(player);
        }
    }
    
}