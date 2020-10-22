package cabbageroll.tetr;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

public class Room {

    public List<Table> plist=new ArrayList<>();
    public String name;
    public Player host;
    
    Room(String name, Player p){
        this.name=name;
        host=p;
    }
    
    public void stopRoom(){
        for(int i=0;i<plist.size();i++) {
            plist.get(i).gameover=true;
        }
    }
    
    public void startRoom(){
        for(int i=0;i<plist.size();i++) {
            plist.get(i).initGame();
            plist.get(i).playGame();
        }
    }
    
    //doesnt check for duplicates
    public void addPlayer(Player p){
        Table temp=new Table();
        temp.player=p;
        plist.add(plist.size(),temp);
    }
    
    //doesnt check for host
    public void removePlayer(Player p){
        Table temp;
        for(int i=0;i<plist.size();i++){
            temp=plist.get(i);
            if(temp.player==p){
                plist.remove(i);
            }
        }
    }
}
