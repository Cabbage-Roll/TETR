package tetr.minecraft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.bukkit.entity.Player;

import com.xxmicloxx.NoteBlockAPI.model.Playlist;
import com.xxmicloxx.NoteBlockAPI.model.RepeatMode;
import com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer;

import tetr.minecraft.constants.Constants;

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
    public String name;
    public Player host;
    public static Playlist slist;
    public RadioSongPlayer rsp;
    public boolean running;
    public int playersalive;
    public boolean backfire = false;
    public boolean isSingleplayer;
    
    public int index;
    
    public Room(Player player, boolean isSingleplayer){
        if(Main.numberofsongs>0){
            rsp=new RadioSongPlayer(slist);
            rsp.setVolume((byte)50);
        }
        
        String mkID;
        do {
            mkID=makeID();
        }while(!isUnique(mkID));
        id = mkID;
        
        host=player;
        addPlayer(player);
        Main.roommap.put(id, this);
        this.isSingleplayer = isSingleplayer;
        if(isSingleplayer) {
            name = "Singleplayer (work in progress)";
        }else {
            name = "Room #" + id;
        }
        index = -1;
    }
    
    public void stopRoom(){
        if(Main.numberofsongs>0){
            rsp.setPlaying(false);
        }
        
        for(Player player: playerlist){
            playerboards.get(player).setGameOver(true);
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
                table.getPlayer().sendMessage("[TETR] Playing: "+rsp.getSong().getPath().getName().replaceAll(".nbs$", ""));
            }
        }
        
        playersalive=playerlist.size();
        running=true;
    }
    
    public void addPlayer(Player player) {
        Main.inwhichroom.put(player, this);
        playerlist.add(player);
        Table table=new Table(player);
        playerboards.put(player,table);
        
        if(Main.numberofsongs>0){
            rsp.addPlayer(player);
        }
    }
    
    public void removePlayer(Player player) {
        if(Main.numberofsongs>0){
            rsp.removePlayer(player);
        }
        playerboards.get(player).destroyTable();
        playerboards.get(player).setGameOver(true);
        playersalive--;
        if(playersalive<2){
            stopRoom();
        }
        playerlist.remove(player);
        playerboards.remove(player);
        Main.inwhichroom.remove(player);
        if(player==host){
            if(playerlist.size()==0) {
                Main.roommap.remove(id);
            }else {
                host=playerlist.get(0);
                host.sendMessage("[TETR] Since the old room host left, you became the new host.");
            }
        }
    }
    
    public void forwardGarbage(int n, Player sender) {
        if(n>0) {
            int random = (int) (Math.random()*playerlist.size());
            Table table = playerboards.get(playerlist.get(random));
            Player receiver = table.getPlayer();
            if(receiver!=sender || (receiver==sender && backfire)) {
                if(!table.gl.gameover) {
                    table.gl.receiveGarbage(n);
                }else if(running){
                    forwardGarbage(n, sender);
                }
            }else if(!isSingleplayer){
                forwardGarbage(n, sender);
            }
        }
    }
    
}
