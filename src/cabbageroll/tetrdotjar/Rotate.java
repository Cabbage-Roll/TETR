package cabbageroll.tetrdotjar;

public class Rotate {

    public static final int CCW=0;
    public static final int CW=1;
    public static final int R180=2;
    
    public static void rotateBlock(int d){
        int i;
        int j;
        int piece_type=Position.block_size-3;
        int special=-1;
        int tries=0;
        int oldrotation=Position.rotation;

        int[][] temp=new int[Position.block_size][Position.block_size];

        for(i = 0; i < Position.block_size; i += 1)
            for(j = 0; j < Position.block_size; j += 1)
                temp[i][j] = Position.block[i][j];

        if(Position.rotation==0 && d==CW)
            special=0;
        else if(Position.rotation==1 && d==CCW)
            special=1;
        else if(Position.rotation==1 && d==CW)
            special=2;
        else if(Position.rotation==2 && d==CCW)
            special=3;
        else if(Position.rotation==2 && d==CW)
            special=4;
        else if(Position.rotation==3 && d==CCW)
            special=5;
        else if(Position.rotation==3 && d==CW)
            special=6;
        else if(Position.rotation==0 && d==CCW)
            special=7;


        switch(d){
        case CCW:
            for(i = 0; i < Position.block_size; i += 1){
                for(j = 0; j < Position.block_size; j += 1){
                    Position.block[i][j] = temp[j][Position.block_size - 1 - i];
                }
            }

            Position.rotation--;
            if(Position.rotation<0){
                Position.rotation+=4;
            }

            break;
        case CW:
            for(i = 0; i < Position.block_size; i += 1){
                for(j = 0; j < Position.block_size; j += 1){
                    Position.block[i][j] = temp[Position.block_size - 1 - j][i];
                }
            }

            Position.rotation++;
            if(Position.rotation>3){
                Position.rotation-=4;
            }

            break;
        case R180:
            for(i = 0; i < Position.block_size; i += 1){
                for(j = 0; j < Position.block_size; j += 1){
                    Position.block[i][j] = temp[Position.block_size-1-i][Position.block_size-1-j];
                }
            }
            Position.rotation+=2;
            if(Position.rotation>3){
                Position.rotation-=4;
            }
            break;
        }

        for(tries=0;tries<5;tries++){
            if(!(Position.isCollide(
                    Position.x+Kicktable.kicks[piece_type][0][special][tries],
                    Position.y-Kicktable.kicks[piece_type][1][special][tries],
                    Position.block_size)
                )){
                break;
            }

            if(tries==4){
                for (i = 0; i < Position.block_size; i+=1)
                {
                    for (j = 0; j < Position.block_size; j+=1)
                    {
                        Position.block[i][j] = temp[i][j];
                    }
                }
                Position.rotation=oldrotation;
                System.out.println("All tests failed");
                return;
            }
        }
        
        Moveblock.moveBlock(
                  Position.x+Kicktable.kicks[piece_type][0][special][tries],
                  Position.y-Kicktable.kicks[piece_type][1][special][tries]
                  );

        ///if it succeeds show it
        for (i = 0; i < Position.block_size; i+=1)
        {
            for (j = 0; j < Position.block_size; j+=1)
            {
                if(Position.stage[Position.y + i][Position.x + j] == 0 && Position.block[i][j] == 0)
                {
                    Printing.colprintxy(j + Position.x,i + Position.y,0,0);
                }
                else if(Position.block[i][j] > 0)
                {
                    Printing.colprintxy(j + Position.x,i + Position.y,0,Position.block[i][j]);
                }
            }
        }

        if(Position.block_current==6){
            Tspin.tspin();
        }
    }
}
