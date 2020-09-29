
package cabbageroll.tetrdotjar;

public class Position {
    public static final int STAGESIZEX=10;
    public static final int STAGESIZEY=40;
    public static int[][] stage=new int[STAGESIZEY][STAGESIZEX];
    public static int[][] block=new int[4][4];

    public static int block_current=-1;

    public static int x;
    public static int y;
    public static int block_size;
    public static int rotation;

    public static boolean isCollide(int x,int y,int b_size)
    {
        int i;
        int j;
        int collision;
        
        for(i = 0; i < b_size; i += 1)
        {
            for(j = 0; j < b_size; j += 1)
            {
                ///code fix that prevents OOBE and makes walls solid
                collision=1;
                if((0<=y+i && y+i<STAGESIZEY) && (0<=x+j && x+j<STAGESIZEX)) {
                    collision=stage[y + i][x + j];
                }
                
                if(collision > 0 && block[i][j] > 0) {
                    return true;
                }
            }
        }
        return false;
    }
}
