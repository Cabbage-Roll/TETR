package cabbageroll.tetr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.bukkit.entity.Player;

import com.xxmicloxx.NoteBlockAPI.model.Playlist;
import com.xxmicloxx.NoteBlockAPI.model.RepeatMode;
import com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer;

public class Room {
    
    static String makeID(){
        String abc="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder result=new StringBuilder(4);
        for(int i=0;i<4;i++){
            int index=(int)(abc.length()*Math.random());
            result.append(abc.charAt(index)); 
        }
        return result.toString();
    } 

    public ArrayList<Player> playerlist=new ArrayList<Player>();
    public Map<Player,Table> playerboards=new HashMap<Player,Table>();
    public String id;
    public Player host;
    static Playlist slist;
    public RadioSongPlayer rsp;
    public boolean running;
    public boolean multiplayer;
    public int playersalive;
    
    public Room(Player p){
        if(Main.numberofsongs>0){
            rsp=new RadioSongPlayer(slist);
        }
        id=makeID();
        host=p;
        addPlayer(p);
        Main.roommap.put(id, this);
        multiplayer=false;
    }
    
    public void stopRoom(){
        if(Main.numberofsongs>0){
            rsp.setPlaying(false);
        }
        
        for(Player player: playerlist){
            playerboards.get(player).gameover=true;
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
        
        Random x=new Random();
        long seed=x.nextInt();
        long seed2=x.nextInt();
        System.out.println(seed+","+seed2);
        
        for(Player player: playerlist){
            Table table=playerboards.get(player);
            table.whotosendblocksto=new ArrayList<Player>(playerlist);
            table.initGame(seed,seed2);
            
            if(Main.numberofsongs>0){
                table.player.sendMessage("Playing: "+rsp.getSong().getPath());
            }
        }
        
        playersalive=playerlist.size();
        running=true;
    }
    
    public void addPlayer(Player player){
        Table table=new Table(player);
        playerboards.put(player,table);
        playerlist.add(player);
        multiplayer=true;
        
        if(Main.numberofsongs>0){
            rsp.addPlayer(player);
        }
        
        Main.inwhichroom.put(player, id);
    }
    
    public void removePlayer(Player player){
        if(Main.numberofsongs>0){
            rsp.removePlayer(player);
        }
        
        playerboards.get(player).gameover=true;
        playerlist.remove(player);
        playerboards.remove(player);
        Main.inwhichroom.remove(player);
        if(player==host){
            if(playerlist.size()==0){
                Main.roommap.remove(id);
            }else{
                host=playerlist.get(0);
            }
        }
        if(playerlist.size()==1){
            multiplayer=false;
        }
    }
    
    public void forwardGarbage(int n, Player player){
        if(multiplayer){
            if(n>0){
                int rand=(int)(Math.random()*playerlist.size());
                if(playerboards.get(playerlist.get(rand)).player!=player){
                    if(!playerboards.get(playerlist.get(rand)).gameover){
                        playerboards.get(playerlist.get(rand)).receiveGarbage(n);
                    }else{
                        forwardGarbage(n, player);
                    }
                }else{
                    forwardGarbage(n, player);
                }
            }
        }
    }
    
}
