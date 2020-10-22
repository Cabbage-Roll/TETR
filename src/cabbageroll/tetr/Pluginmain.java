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
        
        
        customYml = new File(plugin.getDataFolder()+"/tetr.yml");
        
        customConfig = YamlConfiguration.loadConfiguration(customYml);

        
        
        System.out.println("Plugin started");
        getServer().getPluginManager().registerEvents(this, this);
        this.getCommand("sendinput").setExecutor(new Commandinput());
        this.getCommand("startgame").setExecutor(new Startgame());
        this.getCommand("startduel").setExecutor(new Startduel());
        this.getCommand("duelinput").setExecutor(new Duelinput());
        this.getCommand("editpiece").setExecutor(new Editpiece());
        this.getCommand("skineditor").setExecutor(new OpenSkinEditor());
        getServer().getPluginManager().registerEvents(new SkinEditor(), this);
        //trash
        File f = new File("plugins\\tetr");
        numberofsongs=f.listFiles().length;

        System.out.print(numberofsongs+" song(s) loaded");
        
        pathnames=new String[numberofsongs];
        sarr=new Song[numberofsongs];
        pathnames = f.list();
        for(int i=0;i<numberofsongs;i++){
            xd="plugins\\tetr\\"+pathnames[i];
            sarr[i]=NBSDecoder.parse(new File(xd));
        }
        
        Table.slist=new Playlist(sarr);
        Table.rsp=new RadioSongPlayer(Table.slist);
        //tend
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        event.setJoinMessage("Welcome " + event.getPlayer().getName() + "!");
    }
    
    @EventHandler
    public void onItemHeld(PlayerItemHeldEvent event){
        int itemId = event.getNewSlot();
        ItemStack item = Pluginmain.sp.player.getInventory().getItem(itemId);
        if(item == null){
            return;
        }
        
        String name="invalid";
        
        if(item.hasItemMeta()){
            name=item.getItemMeta().getDisplayName();
            Pluginmain.sp.userInput(name);
            Pluginmain.sp.player.getInventory().setHeldItemSlot(8);
        }

    }
}