package cabbageroll.tetr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.bukkit.entity.Player;

import com.xxmicloxx.NoteBlockAPI.model.Playlist;
import com.xxmicloxx.NoteBlockAPI.model.RepeatMode;
import com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer;

import cabbageroll.tetr.constants.Constants;

public class Room {
    
    private static String makeID() {
        String charSet=Constants.charSet;
        StringBuilder result=new StringBuilder(Constants.idLength);
        for(int i=0;i<Constants.idLength;i++){
            int index = (int) (charSet.length()*Math.random());
            result.append(charSet.charAt(index)); 
        }
        return result.toString();
    }
    
    private static boolean isUnique(String id) {
        Object[] keys = Main.roommap.keySet().toArray();
        for(int i=0;i<keys.length;i++) {
            String key = (String) keys[i];
            if(id.equals(key)) {
                return false;
            }
        }
        return true;
    }

    public ArrayList<Player> playerlist=new ArrayList<Player>();
    public Map<Player,Table> playerboards=new HashMap<Player,Table>();
    public String id;
    public Player host;
    public static Playlist slist;
    public RadioSongPlayer rsp;
    public boolean running;
    public boolean multiplayer;
    public int playersalive;
    public boolean backfire = false;
    public boolean unlisted;
    
    public int index;
    
    public Room(Player player, boolean unlisted){
        if(Main.numberofsongs>0){
            rsp=new RadioSongPlayer(slist);
        }
        
        String mkID;
        do {
            mkID=makeID();
        }while(!isUnique(mkID));
        id = mkID;
        
        host=player;
        addPlayer(player);
        Main.roommap.put(id, this);
        multiplayer=false;
        this.unlisted = unlisted;
        index = -1;
    }
    
    public void stopRoom(){
        if(Main.numberofsongs>0){
            rsp.setPlaying(false);
        }
        
        for(Player player: playerlist){
            playerboards.get(player).setGameOver();
        }
        
        running=false;
    }
    
    public void startRoom(){
        if(Main.numberofsongs>0){
            if(index==-1) {
                int random=(int)(Math.random()*Main.numberofsongs);
                rsp.playSong(random);
            }else {
                rsp.playSong(index);
            }
            rsp.setRepeatMode(RepeatMode.ONE);
            if(rsp.isPlaying()==false){
                rsp.setPlaying(true);
            }
        }
        
        Random x=new Random();
        long seed=x.nextInt();
        long seed2=x.nextInt();
        
        for(Player player: playerlist){
            Table table=playerboards.get(player);
            table.initGame(seed,seed2);
            
            if(Main.numberofsongs>0){
                table.getPlayer().sendMessage("Playing: "+rsp.getSong().getPath().getName().replaceAll(".nbs$", ""));
            }
        }
        
        playersalive=playerlist.size();
        running=true;
    }
    
    public void addPlayer(Player player) {
        Table table=new Table(player);
        playerboards.put(player,table);
        playerlist.add(player);
        multiplayer=true;
        
        if(Main.numberofsongs>0){
            rsp.addPlayer(player);
        }
        
        Main.inwhichroom.put(player, this);
    }
    
    public void removePlayer(Player player) {
        if(Main.numberofsongs>0){
            rsp.removePlayer(player);
        }
        playerboards.get(player).setGameOver();
        playersalive--;
        if(playersalive<=1){
            stopRoom();
        }
        
        if(playerboards.get(player).getBoard()!=null) {
            playerboards.get(player).getBoard().delete();
        }
        playerlist.remove(player);
        playerboards.remove(player);
        Main.inwhichroom.remove(player);
        if(player==host){
            if(playerlist.size()==0) {
                Main.roommap.remove(id);
            }else {
                host=playerlist.get(0);
            }
        }
        if(playerlist.size()==1){
            multiplayer=false;
        }
    }
    
    public void forwardGarbage(int n, Player player) {
        if(n>0) {
            int rand = (int) (Math.random()*playerlist.size());
            if(playerboards.get(playerlist.get(rand)).getPlayer()!=player || (playerboards.get(playerlist.get(rand)).getPlayer()==player && backfire)) {
                if(!playerboards.get(playerlist.get(rand)).getGameOver()) {
                    playerboards.get(playerlist.get(rand)).receiveGarbage(n);
                }else {
                    forwardGarbage(n, player);
                }
            }else if(multiplayer) {
                forwardGarbage(n, player);
            }
        }
    }
    
}
