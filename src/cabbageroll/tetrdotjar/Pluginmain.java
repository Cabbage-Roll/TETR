package cabbageroll.tetrdotjar;

import org.bukkit.plugin.java.JavaPlugin;

import com.xxmicloxx.NoteBlockAPI.model.Playlist;
import com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer;
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder;

import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import java.io.File;

import org.bukkit.event.EventHandler;


public class Pluginmain extends JavaPlugin implements Listener{

    public static Tplayer sp;
    public static Duel match;
    
    static JavaPlugin plugin;
    @Override
    public void onEnable() {
        plugin=this;
        System.out.println("Plugin started");
        getServer().getPluginManager().registerEvents(this, this);
        this.getCommand("sendinput").setExecutor(new Commandinput());
        this.getCommand("startgame").setExecutor(new Startgame());
        this.getCommand("startduel").setExecutor(new Startduel());
        this.getCommand("duelinput").setExecutor(new Duelinput());
        //trash
        Tplayer.sarr[0]=NBSDecoder.parse(new File("plugins\\Tetr\\song36.nbs"));
        Tplayer.sarr[1]=NBSDecoder.parse(new File("plugins\\Tetr\\metblast.nbs"));
        Tplayer.sarr[2]=NBSDecoder.parse(new File("plugins\\Tetr\\newgrass.nbs"));
        Tplayer.slist=new Playlist(Tplayer.sarr);
        Tplayer.rsp=new RadioSongPlayer(Tplayer.slist);
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