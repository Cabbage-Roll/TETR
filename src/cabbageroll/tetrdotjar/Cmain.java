package cabbageroll.tetrdotjar;

import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class Cmain {    
    public static int lines=0;
    public static int next_blocks=5;
    public static boolean gameover = false;
    public static int score = 0;
    public static int counter = 0;
    public static int block_hold = -1;
    public static boolean held=false;
    public static World world;
    public static Player player;
    public static int combo=-1;
    
    
    public static void updateScore(){
        held=false;
        
        if(Tspin.spun){
            score+=lines*1000;
        }
        System.out.println("combo: "+combo);
        Tspin.spun=false;
        if(combo>=0) {
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_HARP, 1f, (float)Math.pow(2,(combo*2-12)/(double)12));
        }
    }
    
    ///title unfinished
    public static String title(String input){/*
        do{
            input = getch();
        }while(input!= '1' && input != '2' && input !='3');
*/
        return input;
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


        for(int k=0;k<next_blocks;k++){
            for(int i = 0; i < 4; i += 1)
            {
                for(int j = 0; j < 4; j += 1)
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

        ///i or o
        ///iojstzl
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
        ///block out
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
        for(int y = 0; y < 41; y += 1)
        {
            for(int x = 0; x < 12; x += 1)
            {
                if(x == 0 || x == 11 || y == 40)
                {
                    Position.stage[y][x] = 1;
                    Printing.colprintxy(x, y, 0, 15);
                }
                else
                {
                    Position.stage[y][x] = 0;
                    Printing.colprintxy(x, y, 0, 0);
                }
            }
        }
        combo=-1;
        score = 0;
        held=false;
        gameover = false;
        Bags.bag1[0] = -1;
        block_hold = -1;
        Bags.generatebag2();
        for(int i=0;i<7;i++){
            Bags.bag1[i]=Bags.bag2[i];
        }
        Bags.generatebag2();
        updateScore();
        makenextblock();
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

            if(block_hold == -1)
            {
                block_hold=Position.block_current;
                makenextblock();
            }
            else
            {
                temp=Position.block_current;
                Position.block_current=block_hold;
                block_hold=temp;
                Position.x = 4;
                Position.y = 20;
                Position.rotation=0;
                if(Position.block_current == 2 || Position.block_current == 4)
                    Position.block_size = 4;
                else
                    Position.block_size = 3;
                for(int i = 0; i < Position.block_size; i += 1){
                    for(int j = 0; j < Position.block_size; j += 1){
                        Position.block[i][j] = Blocklist.block_list[Position.block_current][i][j];
                    }
                }
            }
        }else{

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
        for(int i = Position.y; i < 40 && i < (Position.y + Position. block_size); i += 1)
        {
            lineclean = true;
            for(int j = 1; j < 11; j += 1)
            {
                if(Position.stage[i][j] == 0)
                {
                    lineclean = false;
                    break;
                }
            }
            if(lineclean)
            {
                for(int j=1;j<11;j++){
                    Position.stage[0][j] = 0;
                    Printing.colprintxy(j,0,0,0);
                }
                lines += 1;
                for(int k = i; k > 0; k -= 1)
                {
                    for(int j = 1; j < 11; j += 1)
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
        }else {
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
    
    public static void cmain(){
        do{
            initGame();
            while(!gameover)
            {
                Playgame.playgame();
            }
        }while(true);
    }

    
}
