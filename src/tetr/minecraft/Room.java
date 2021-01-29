package tetr.minecraft;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapPalette;
import org.bukkit.map.MapView;
import org.bukkit.map.MinecraftFont;
import org.bukkit.scheduler.BukkitRunnable;

import com.xxmicloxx.NoteBlockAPI.model.Playlist;
import com.xxmicloxx.NoteBlockAPI.model.RepeatMode;
import com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer;

import tetr.minecraft.constants.Constants;
import tetr.shared.GameLogic;

public class Room {
    
    private static String makeID() {
        String charSet=Constants.idCharSet;
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
    
    ItemStack mapItem;
    private static MapView mapView;

    public ArrayList<Player> playerlist=new ArrayList<Player>();
    public ArrayList<Player> spectators=new ArrayList<Player>();
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
    private boolean dontRender = false;
    
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
            name = "Singleplayer #" + id;
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
        
        mapView = Bukkit.createMap(Bukkit.getWorld("world"));
        ItemStack mapI = new ItemStack(Material.MAP, 1, mapView.getId());
        /*playerList.length==1 maxScale=4 showStats=true showOthers=true
            playerList.length>=2 maxScale=2 showStats=true showOthers=true
            playerList.length>=3 maxScale=2 showStats=false showOthers=true
            playerList.length>=5 maxScale=1 showStats=false showOthers=false*/
        
        mapView.getRenderers().clear();
        mapView.addRenderer(new Renderer() {
            @Override
            public void render(MapView map, MapCanvas mapCanvas, Player player) {
                if(dontRender) {
                    return;
                }
                int iter = 0;
                
                if(isSingleplayer) {
                    if(!playerboards.get(playerlist.get(0)).gl.gameover) {
                        GameLogic gl = playerboards.get(playerlist.get(iter)).gl;
                        int pixelSize = 4;
                        Point topLeftCorner = new Point(64-GameLogic.STAGESIZEX/2*pixelSize, 64-GameLogic.STAGESIZEY/4*pixelSize);
                        iter++;
                    
                        for(int i=20;i<40;i++) {
                            for(int j=0;j<10;j++) {
                                for(int k=0;k<pixelSize;k++) {
                                    for(int l=0;l<pixelSize;l++) {
                                        mapCanvas.setPixel(topLeftCorner.x + j*pixelSize + k, topLeftCorner.y + (i-20)*pixelSize + l, MapPalette.matchColor(tetr.normal.Main.intToColor(gl.stage[i][j])));
                                    }
                                }
                            }
                        }
                        
                        for(int i=0;i<GameLogic.NEXTPIECESMAX;i++) {
                            for(int j=0;j<4;j++) {
                                for(int k=0;k<4;k++) {
                                    for(int l=0;l<pixelSize;l++) {
                                        for(int m=0;m<pixelSize;m++) {
                                            mapCanvas.setPixel(topLeftCorner.x + GameLogic.STAGESIZEX * pixelSize + pixelSize * 3 + j * pixelSize + l, topLeftCorner.y + i * 4 * pixelSize + k * pixelSize + m, MapPalette.matchColor(tetr.normal.Main.intToColor(7)));
                                        }
                                    }       
                                }
                            }
                            
                            for(Point point: gl.pieces[gl.nextPieces.get(i)][0]) {
                                for(int j=0;j<pixelSize;j++) {
                                    for(int k=0;k<pixelSize;k++) {
                                        mapCanvas.setPixel(topLeftCorner.x + GameLogic.STAGESIZEX * pixelSize + pixelSize * 3 + point.x * pixelSize + j, topLeftCorner.y + i * 4 * pixelSize + point.y * pixelSize + k, MapPalette.matchColor(tetr.normal.Main.intToColor(gl.nextPieces.get(i))));
                                    }    
                                }    
                            }
                        }
                        mapCanvas.drawText(0, 0, MinecraftFont.Font, playerlist.get(0).getName());
                    }
                }else if(playersalive==2) {
                    for(Player player2: playerlist) {
                        if(!playerboards.get(player2).gl.gameover) {
                            GameLogic gl = playerboards.get(playerlist.get(iter)).gl;
                            int pixelSize = 2;
                            Point topLeftCorner = new Point(32+64*iter-GameLogic.STAGESIZEX/2*pixelSize, 64-GameLogic.STAGESIZEY/4*pixelSize);
                            iter++;
                        
                            for(int i=20;i<40;i++) {
                                for(int j=0;j<10;j++) {
                                    for(int k=0;k<pixelSize;k++) {
                                        for(int l=0;l<pixelSize;l++) {
                                            mapCanvas.setPixel(topLeftCorner.x + j*pixelSize + k, topLeftCorner.y + (i-20)*pixelSize + l, MapPalette.matchColor(tetr.normal.Main.intToColor(gl.stage[i][j])));
                                        }
                                    }
                                }
                            }
                            
                            for(int i=0;i<GameLogic.NEXTPIECESMAX;i++) {
                                for(int j=0;j<4;j++) {
                                    for(int k=0;k<4;k++) {
                                        for(int l=0;l<pixelSize;l++) {
                                            for(int m=0;m<pixelSize;m++) {
                                                mapCanvas.setPixel(topLeftCorner.x + GameLogic.STAGESIZEX * pixelSize + pixelSize * 3 + j * pixelSize + l, topLeftCorner.y + i * 4 * pixelSize + k * pixelSize + m, MapPalette.matchColor(tetr.normal.Main.intToColor(7)));
                                            }
                                        }       
                                    }
                                }
                                
                                for(Point point: gl.pieces[gl.nextPieces.get(i)][0]) {
                                    for(int j=0;j<pixelSize;j++) {
                                        for(int k=0;k<pixelSize;k++) {
                                            mapCanvas.setPixel(topLeftCorner.x + GameLogic.STAGESIZEX * pixelSize + pixelSize * 3 + point.x * pixelSize + j, topLeftCorner.y + i * 4 * pixelSize + point.y * pixelSize + k, MapPalette.matchColor(tetr.normal.Main.intToColor(gl.nextPieces.get(i))));
                                        }    
                                    }    
                                }
                            }
                        }
                    }
                }else if(playersalive==3) {
                    mapCanvas.drawText(0, 0, MinecraftFont.Font, "not implemented yet");
                    mapCanvas.drawText(30, 0, MinecraftFont.Font, "3 players alive");
                }else if(playersalive==4) {
                    mapCanvas.drawText(0, 0, MinecraftFont.Font, "not implemented yet");
                    mapCanvas.drawText(30, 0, MinecraftFont.Font, "4 players alive");
                }else if(playersalive==5) {
                    mapCanvas.drawText(0, 0, MinecraftFont.Font, "room is too big");
                    mapCanvas.drawText(30, 0, MinecraftFont.Font, "works only up to");
                    mapCanvas.drawText(60, 0, MinecraftFont.Font, "4 players");
                }

                dontRender = true;
            }
        });
        for(Player player: spectators) {
            player.getInventory().setItemInOffHand(mapI);
            player.sendMap(mapView);
        }
        
        new BukkitRunnable() {
            @Override
            public void run() {
                if(!running) {
                    this.cancel();
                }else {
                    dontRender = false;
                }
            }
        }.runTaskTimer(Main.plugin, 0, 40);
    
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
    
    public void addSpectator(Player player) {
        spectators.add(player);
        player.sendMessage("Added, but this feature is work in progress");
    }
    
    public void removeSpectator(Player player) {
        spectators.remove(player);
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
