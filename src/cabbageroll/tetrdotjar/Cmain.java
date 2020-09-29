package cabbageroll.tetrdotjar;

import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class Cmain {    
    public static int lines=0;
    public static int next_blocks=7;
    public static boolean gameover = false;
    public static int score = 0;
    public static int counter = 0;
    public static int block_hold = -1;
    public static boolean held=false;
    public static World world;
    public static Player player;
    public static int combo=-1;
    public static boolean power=false;
    
    
    public static void updateScore(){
        held=false;
        
        if(Tspin.spun){
            score+=lines*1000;
            if(combo>4) {
                power=true;
            }
            player.playSound(player.getLocation(), Sound.ENTITY_LIGHTNING_THUNDER, 1f, 0.75f);
        }
        
        System.out.println("combo: "+combo);
        Tspin.spun=false;
        if(combo>=0) {
            if(power) {
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 1f, (float)Math.pow(2,(combo*2-12)/(double)12));
            }else {
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_HARP, 1f, (float)Math.pow(2,(combo*2-12)/(double)12));
            }
        }
    }
    
    public static void makenextblock() {
        Position.x = 4;
        Position.y = 20;
        Position.rotation = 0;
        ///the first block
        Position.block_current = Bags.bag1[0];
        if(Bags.bag1[0] == -1){
            Bags.shiftbag1();
        }

        Bags.shiftbag1();

        ///prints next blocks
        for(int k=0;k<next_blocks;k++){
            for(int i = 0; i < 4; i ++)
            {
                for(int j = 0; j < 4; j ++)
                {
                    if(Blocklist.block_list[Bags.bag1[k]][i][j] == 0)
                    {
                        Printing.colprintxy(j + 13,i + 0 + k*4,0,0);
                    }
                    else
                    {
                        Printing.colprintxy(j + 13,i + 0 + k*4,0,Blocklist.block_list[Bags.bag1[k]][i][j]);
                    }
                }
            }
        }

        if(Position.block_current == 2 || Position.block_current == 4)
            Position.block_size = 4;
        else
            Position.block_size = 3;
        ///spawn block
        for(int i = 0; i < Position.block_size; i += 1)
        {
            for(int j = 0; j < Position.block_size; j += 1)
            {
                Position.block[i][j] = Blocklist.block_list[Position.block_current][i][j];
            }
        }
        
        //check if its possible then print it
        for(int i = 0; i < Position.block_size; i += 1)
        {
            for(int j = 0; j < Position.block_size; j += 1)
            {
                if(Position.stage[i][j + Position.x] > 0 && Position.block[i][j] > 0)
                {
                    gameover = true;
                    return;
                }
                else if(Position.block[i][j] > 0)
                {
                    Printing.colprintxy(j + Position.x,i + Position.y,0,Position.block[i][j]);
                }
            }
        }
    }
    
    public static void initGame(){
        for(int y = 0; y < Position.STAGESIZEY; y ++){
            for(int x = 0; x < Position.STAGESIZEX; x ++){
                Position.stage[y][x] = 0;
                Printing.colprintxy(x, y, 0, 0);
            }
        }
        Tspin.spun=false;
        combo=-1;
        power=false;
        score = 0;
        held=false;
        gameover = false;
        block_hold = -1;
        Bags.generatebag2();
        for(int i=0;i<7;i++){
            Bags.bag1[i]=Bags.bag2[i];
        }
        Bags.generatebag2();
        updateScore();
        makenextblock();
        //make hold piece white
        for(int i = 0; i < 4; i += 1){
            for(int j = 0; j < 4; j += 1){
                Printing.colprintxy(j-7,i+3,15,0);
            }
        }
    }
    
    public static void dropBlock(){
        int lines = 0;
        while(!Position.isCollide(Position.x, Position.y + 1, Position.block_size))
        {
            lines += 1;
            Moveblock.moveBlock(Position.x, Position.y + 1);
        }
        score += lines;
        counter = 100;
    }
    
    public static void holdBlock(){
        int temp;

        if(held==false){

            //print current block into hold slot
            for(int i = 0; i < 4; i += 1)
            {
                for(int j = 0; j < 4; j += 1)
                {
                    if(Blocklist.block_list[Position.block_current][i][j] == 0)
                    {
                        Printing.colprintxy(j-7,i+3,0,0);
                    }
                    else
                    {
                        Printing.colprintxy(j-7,i+3,0,Blocklist.block_list[Position.block_current][i][j]);
                    }
                }
            }
            
            //erase current block from board
            for (int i = 0; i < 4; i+=1)
            {
                for (int j = 0; j < 4; j+=1)
                {
                    if(Position.block[i][j] > 0)
                    {
                        Printing.colprintxy(j+Position.x,i+Position.y,0,0);
                    }
                }
            }

            
            //first hold
            if(block_hold == -1){
                block_hold=Position.block_current;
                makenextblock();
            }else{
                //swap
                temp=Position.block_current;
                Position.block_current=block_hold;
                block_hold=temp;
                
                //spawn new block
                Position.x = 4;
                Position.y = 20;
                Position.rotation=0;
                if(Position.block_current == 2 || Position.block_current == 4)
                    Position.block_size = 4;
                else
                    Position.block_size = 3;
                
                for(int i = 0; i < Position.block_size; i++){
                    for(int j = 0; j < Position.block_size; j++){
                        Position.block[i][j] = Blocklist.block_list[Position.block_current][i][j];
                    }
                }
                
            }
        }else{
            //already held
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
        }
        held=true;
    }
    
    public static void userInput(String input)
    {
        switch (input)
        {
        case "right":
            if (!Position.isCollide(Position.x + 1, Position.y,Position.block_size))
            {
                Moveblock.moveBlock(Position.x + 1, Position.y);
            }
            break;
        case "left":
            if (!Position.isCollide(Position.x - 1, Position.y,Position.block_size))
            {
                Moveblock.moveBlock(Position.x - 1, Position.y);
            }
            break;
        case "down":
            if (!Position.isCollide(Position.x, Position.y + 1,Position.block_size))
            {
                Moveblock.moveBlock(Position.x, Position.y + 1);
            }
            break;
        case "y":
        case "Y":
        case "z":
        case "Z":
            Rotate.rotateBlock(Rotate.CCW);
            break;
        case "x":
        case "X":
            Rotate.rotateBlock(Rotate.CW);
            break;
        case "space":
            dropBlock();
            break;
        case "up":
            Rotate.rotateBlock(Rotate.R180);
            break;
        case "c":
        case "C":
            holdBlock();
            break;
        case "l":
        case "L":
            gameover=true;
            break;
        default:
            System.out.println("Nothing.");
        }
    }
    
    public static void checkPlaced(){
        boolean lineclean;
        lines = 0;
        ///suspicious condition
        for(int i = Position.y; i < Position.STAGESIZEY && i < (Position.y + Position.block_size); i++)
        {
            lineclean = true;
            for(int j = 0; j < Position.STAGESIZEX; j++)
            {
                if(Position.stage[i][j] == 0)
                {
                    lineclean = false;
                    break;
                }
            }
            if(lineclean)
            {
                //old problem fix
                for(int j=0;j<Position.STAGESIZEX;j++){
                    
                    Position.stage[0][j] = 0;
                    Printing.colprintxy(j,0,0,0);
                }
                //end
                
                lines++;
                for(int k = i; k > 0; k--)
                {
                    for(int j = 0; j <Position.STAGESIZEX; j ++)
                    {
                        Position.stage[k][j] = Position.stage[k - 1][j];
                        if(Position.stage[k][j] > 0)
                        {
                            Printing.colprintxy(j,k,0,Position.stage[k][j]);
                        }
                        else
                        {
                            Printing.colprintxy(j,k,0,0);
                        }
                    }
                }

            }
        }
        
        
        if(lines>0) {
            combo++;
        }else{
            combo=-1;
        }
        
        
        switch(lines)
        {
        case 1:
            score += 40;
            break;
        case 2:
            score += 100;
            break;
        case 3:
            score += 300;
            break;
        case 4:
            score += 1200;
            if(combo>4) {
                power=true;
            }
            break;
        }
        updateScore();
        }

    public static void placeBlock(){
        for(int i = 0; i < Position.block_size; i += 1)
        {
            for(int j = 0; j < Position.block_size; j += 1)
            {
                if(Position.block[i][j] > 0)
                {
                    Printing.colprintxy(j+Position.x,i+Position.y,0,Position.block[i][j]);
                    Position.stage[i + Position.y][j + Position.x] = Position.block[i][j];
                }
            }
        }
    }
    
    /*public static void cmain(){
        initGame();
        while(!gameover){
            Playgame.playgame();
        }
    }*/

    
}
