package cabbageroll.tetr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.block.Block;
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
    
    Room(String name, Player p){
        rsp=new RadioSongPlayer(slist);
        this.name=name;
        host=p;
        addPlayer(p);
    }
    
    public void stopRoom(){
        rsp.setPlaying(false);
        for(Table table: playerlist.values()){
            table.gameover=true;
        }
    }
    
    public void startRoom(){
        if(Pluginmain.numberofsongs>0){
            int random=(int)(Math.random()*Pluginmain.numberofsongs);
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
            
            if(Pluginmain.numberofsongs>0)
                table.player.sendMessage("Playing: "+rsp.getSong().getPath());
        }
    }
    
    //doesnt check for duplicates
    public void addPlayer(Player p){
        playerlist.put(p,new Table(p,playerlist.size()));
        rsp.addPlayer(p);
    }
    
    //doesnt check for host
    public void removePlayer(Player p){
        playerlist.remove(p);
    }
    
}
