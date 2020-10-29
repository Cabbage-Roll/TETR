package cabbageroll.tetr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.xxmicloxx.NoteBlockAPI.model.Playlist;
import com.xxmicloxx.NoteBlockAPI.model.RepeatMode;
import com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer;

public class Room {

    public Map<Player, Table> playerlist=new HashMap<Player, Table>();
    public String name;
    public Player host;
    static Playlist slist;
    public RadioSongPlayer rsp;
    public boolean running;
    
    public Room(String name, Player p){
        if(Main.numberofsongs>0){
            rsp=new RadioSongPlayer(slist);
        }
        this.name=name;
        host=p;
        addPlayer(p);
    }
    
    public void stopRoom(){
        if(Main.numberofsongs>0){
            rsp.setPlaying(false);
        }
        for(Table table: playerlist.values()){
            table.gameover=true;
        }
        
        running=false;
    }
    
    public void startRoom(){
        if(Main.numberofsongs>0){
            int random=(int)(Math.random()*Main.numberofsongs);
            rsp.playSong(random);
            rsp.setRepeatMode(RepeatMode.ONE);
            if(rsp.isPlaying()==false){
                rsp.setPlaying(true);
            }
        }
        
        long seed=System.currentTimeMillis();
        for(Table table: playerlist.values()){
            table.whotosendblocksto=new ArrayList<Player>(playerlist.keySet());
            table.initGame(seed);
            
            if(Main.numberofsongs>0){
                table.player.sendMessage("Playing: "+rsp.getSong().getPath());
            }
        }
        
        running=true;
    }
    
    //doesnt check for duplicates
    public void addPlayer(Player p){
        Table table=new Table(p,playerlist.size());
        playerlist.put(p,table);
        if(Main.numberofsongs>0){
            rsp.addPlayer(p);
        }
        Main.inwhichroom.put(p, name);
        Bukkit.getServer().getPluginManager().registerEvents(table, Main.plugin);
    }
    
    //doesnt check for host
    public void removePlayer(Player p){
        playerlist.remove(p);
    }
    
}
