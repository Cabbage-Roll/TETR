package cabbageroll.tetrdotjar;

import java.util.ArrayList;
import java.util.List;

public class Duel {
    public List<Tplayer> plist=new ArrayList<>();
    public static int num=2;
    
    Duel(Tplayer x, Tplayer y){
        plist.add(x);
        x.initGame();
        x.playGame();
        plist.add(y);
        y.initGame();
        y.playGame();
    }
    
    public void stop(){
        for(int g=0;g<num;g++) {
            plist.get(0).gameover=true;
            plist.remove(0);
        }
    }
    
}
