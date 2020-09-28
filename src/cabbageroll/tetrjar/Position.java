package cabbageroll.tetrjar;

public class Position {
    public static int[][] stage=new int[21][12];
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
        
        for(i = 0; i < b_size; i += 1)
        {
            for(j = 0; j < b_size; j += 1)
            {
                if((y+i<0 || y+i>20) || (x+j<0 || x+j>11)) {
                    continue;
                }
                
                if(stage[y + i][x + j] > 0 && block[i][j] > 0)
                    return true;
            }
        }
        return false;
    }
}
