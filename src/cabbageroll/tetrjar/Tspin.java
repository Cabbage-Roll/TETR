package cabbageroll.tetrjar;

public class Tspin {

    public static boolean spun=false;

    public static void tspin(){
        int truth=0;
        if(Position.stage[Position.y][Position.x]>0)
            truth++;
        if(Position.stage[Position.y+2][Position.x]>0)
            truth++;
        if(Position.stage[Position.y][Position.x+2]>0)
            truth++;
        if(Position.stage[Position.y+2][Position.x+2]>0)
            truth++;
        if(truth>=3){
            spun=true;
        }else{
            spun=false;
        }
    }
}