package cabbageroll.tetrdotjar;

public class Moveblock {

    public static void moveBlock(int x, int y)
    {
        int i;
        int j;
        
        for (i = 0; i < Position.block_size; i+=1)
        {
            for (j = 0; j < Position.block_size; j+=1)
            {
                if(Position.block[i][j] > 0)
                {
                    Printing.colprintxy(j + Position.x,i + Position.y,0,0);
                }
            }
        }
        Position.x = x;
        Position.y = y;

        for (i = 0; i < Position.block_size; i++)
        {
            for (j = 0; j < Position.block_size; j++)
            {
                if(Position.block[i][j] > 0)
                {
                    Printing.colprintxy(j + Position.x,i + Position.y,0,Position.block[i][j]);
                }
            }
        }
    }

}
