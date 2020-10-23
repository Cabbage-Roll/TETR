package cabbageroll.tetr;

import org.bukkit.plugin.java.JavaPlugin;

import com.xxmicloxx.NoteBlockAPI.model.Playlist;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer;
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder;

import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;


public class Pluginmain extends JavaPlugin implements Listener{

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
    public static Table sp=new Table();
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
        this.getCommand("sendinput").setExecutor(new Commandinput());
        this.getCommand("startgame").setExecutor(new Startgame());
        this.getCommand("skineditor").setExecutor(new OpenSkinEditor());
        getServer().getPluginManager().registerEvents(new SkinEditor(), this);
        //trash
        File f = new File("plugins\\TETR\\songs");
        numberofsongs=f.listFiles().length;

        System.out.print(numberofsongs+" song(s) loaded");
        
        pathnames=new String[numberofsongs];
        sarr=new Song[numberofsongs];
        pathnames = f.list();
        for(int i=0;i<numberofsongs;i++){
            xd="plugins\\TETR\\songs\\"+pathnames[i];
            sarr[i]=NBSDecoder.parse(new File(xd));
        }
        
        Table.slist=new Playlist(sarr);
        Table.rsp=new RadioSongPlayer(Table.slist);
        //tRASH end
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        event.setJoinMessage("Welcome " + event.getPlayer().getName() + "!");
    }
    
    @EventHandler
    public void onItemHeld(PlayerItemHeldEvent event){
        if(Pluginmain.sp.task!=null){
            int itemId = event.getNewSlot();
            switch(itemId){
            case 0:
                Pluginmain.sp.userInput("y");
                break;
            case 1:
                Pluginmain.sp.userInput("x");
                break;
            case 2:
                Pluginmain.sp.userInput("c");
                break;
            case 3:
                Pluginmain.sp.userInput("space");
                break;
            case 4:
                Pluginmain.sp.userInput("up");
                break;
            case 5:
                Pluginmain.sp.userInput("instant");
                break;
            case 6:
                Pluginmain.sp.userInput("left");
                break;
            case 7:
                Pluginmain.sp.userInput("right");
                break;
            case 8:
                return;
            }
            Pluginmain.sp.player.getInventory().setHeldItemSlot(8);
        }
    }
}