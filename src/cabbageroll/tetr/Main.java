package cabbageroll.tetr;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.xxmicloxx.NoteBlockAPI.model.Playlist;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder;

import cabbageroll.tetr.menus.Listen;
import funcs.Functions;
import funcs.Functions_1_10_R1;
import funcs.Functions_1_11_R1;
import funcs.Functions_1_12_R1;
import funcs.Functions_1_13_R1;
import funcs.Functions_1_13_R2;
import funcs.Functions_1_14_R1;
import funcs.Functions_1_15_R1;
import funcs.Functions_1_16_R1;
import funcs.Functions_1_16_R2;
import funcs.Functions_1_16_R3;
import funcs.Functions_1_8_R1;
import funcs.Functions_1_8_R2;
import funcs.Functions_1_8_R3;
import funcs.Functions_1_9_R1;
import funcs.Functions_1_9_R2;

public class Main extends JavaPlugin implements Listener{
    public static JavaPlugin plugin;
    public static ConsoleCommandSender console;
    
    public static ArrayList<String> roomlist=new ArrayList<String>();
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
    
    public static Functions functions;

    public static int numberofsongs;
    String[] pathnames;
    String xd;
    static Song[] sarr;
    public static String version;
    
    
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
        
        if (setupActionbar()) {

            Bukkit.getPluginManager().registerEvents(this, this);

            getLogger().info("TETR: success");
            

        } else {

            getLogger().severe("TETR: not");
            getLogger().severe("version: "+Bukkit.getServer().getClass().getPackage().getName());

            Bukkit.getPluginManager().disablePlugin(this);
        }
        
        for(Player player: Bukkit.getOnlinePlayers())
            lastui.put(player, "home");
        
        
        ///joke code
        
    }
    
    private boolean setupActionbar() {
        try {

            version=Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

        } catch (ArrayIndexOutOfBoundsException whatVersionAreYouUsingException) {
            return false;
        }

        getLogger().info("Your server is running version " + version);

        if(version.equals("v1_8_R1")){
            functions=new Functions_1_8_R1();
        }else if(version.equals("v1_8_R2")){
            functions=new Functions_1_8_R2();
        }else if(version.equals("v1_8_R3")){
            functions=new Functions_1_8_R3();
        }else if(version.equals("v1_9_R1")){
            functions=new Functions_1_9_R1();
        }else if(version.equals("v1_9_R2")){
            functions=new Functions_1_9_R2();
        }else if(version.equals("v1_10_R1")){
            functions=new Functions_1_10_R1();
        }else if(version.equals("v1_11_R1")){
            functions=new Functions_1_11_R1();
        }else if(version.equals("v1_12_R1")){
            functions=new Functions_1_12_R1();
        }else if(version.equals("v1_13_R1")){
            functions=new Functions_1_13_R1();
        }else if(version.equals("v1_13_R2")){
            functions=new Functions_1_13_R2();
        }else if(version.equals("v1_14_R1")){
            functions=new Functions_1_14_R1();
        }else if(version.equals("v1_15_R1")){
            functions=new Functions_1_15_R1();
        }else if(version.equals("v1_16_R1")){
            functions=new Functions_1_16_R1();
        }else if(version.equals("v1_16_R2")){
            functions=new Functions_1_16_R2();
        }else if(version.equals("v1_16_R3")){
            functions=new Functions_1_16_R3();
        }
        
        return functions!=null;
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