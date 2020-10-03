package cabbageroll.tetrdotjar;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Tplayer {
    /*
     bags-here done
     blocklist-its own file
     cmain-here done
     commandinput-unk
     kicktable-its own file
     moveblock-here done
     playgame-unk
     pluginmain-its own file
     position-here done
     printing-here done
     rotate-here done
     startgame-unk
     tspin-here done
    */
    public static final int CCW=0;
    public static final int CW=1;
    public static final int R180=2;
    public int gx=0;
    public int gy=0;
    public int gz=0;
    public int bag_counter=0;
    public int[] bag1=new int[7];
    public int[] bag2=new int[7];
    public int lines=0;
    public int next_blocks=7;
    public boolean gameover=false;
    public int score = 0;
    public int counter = 0;
    public int block_hold = -1;
    public boolean held=false;
    public World world;
    public Player player;
    public int combo=-1;
    public boolean power=false;
    public final int STAGESIZEX=10;
    public final int STAGESIZEY=40;
    public int[][] stage=new int[STAGESIZEY][STAGESIZEX];
    public int[][] block=new int[4][4];
    public int block_current=-1;
    public int x;
    public int y;
    public int block_size;
    public int rotation;
    public boolean spun=false;

    public Tplayer(){
        
    }
    
    public void shiftBag1(){
        if(bag_counter>6){
            generateBag2();
        }
        for(int i=0;i<6;i++){
            bag1[i]=bag1[i+1];
        }
        bag1[6]=bag2[0];
        for(int i=0;i<6;i++){
            bag2[i]=bag2[i+1];
        }
        bag_counter++;
    }
    
    public void generateBag2(){
        bag_counter=0;
        for(int i=0;i<7;i++){
            bag2[i]=(int)(Math.random()*7);
            for(int j=0;j<i;j++){
                if(bag2[i]==bag2[j]){
                    i--;
                }
            }
        }
    }
 
    public void makeNextBlock(){
        x = 3;
        y = 10;
        rotation = 0;
        ///the first block
        block_current = bag1[0];
        if(bag1[0] == -1){
            shiftBag1();
        }

        shiftBag1();

        ///prints next blocks
        for(int k=0;k<next_blocks;k++){
            for(int i=0;i<4;i++){
                for(int j=0;j<4;j++){
                    if(Blocklist.block_list[bag1[k]][i][j] == 0){
                        colPrint(j + 13,i + 0 + k*4,0,42);
                    }else{
                        colPrint(j + 13,i + 0 + k*4,0,Blocklist.block_list[bag1[k]][i][j]);
                    }
                }
            }
        }

        if(block_current==2 || block_current==4){
            block_size=4;
        }else{
            block_size=3;
        }
        
        ///spawn block
        for(int i=0;i<block_size;i++){
            for(int j=0;j<block_size;j++){
                block[i][j] = Blocklist.block_list[block_current][i][j];
            }
        }
        
        //check if its possible then print it
        for(int i=0;i<block_size;i++){
            for(int j=0;j<block_size;j++){
                if(stage[i+y][j+x]>0 && block[i][j]>0){
                    player.playSound(player.getLocation(), Sound.BLOCK_PISTON_CONTRACT, 1f, 1f);
                    gameover=true;
                    return;
                }
                else if(block[i][j]>0){
                    colPrint(j+x,i+y,0,block[i][j]);
                }
            }
        }
    }
    
    public boolean isCollide(int x, int y, int b_size){
        int collision;
        
        for(int i=0;i<b_size;i++){
            for(int j=0;j<b_size;j++){
                ///code fix that prevents OOBE and makes walls solid
                collision=1;
                if((0<=y+i && y+i<STAGESIZEY) && (0<=x+j && x+j<STAGESIZEX)){
                    collision=stage[y+i][x+j];
                }
                
                if(collision>0 && block[i][j]>0){
                    return true;
                }
            }
        }
        return false;
    }
    
    public void colPrint(int x, int y, int z, int color) {
        Block b=world.getBlockAt(gx+x,gy-y,gz+z);
        if(color==42){
            b.setType(Material.AIR);
        }else if(color==69){
            return;
        }else{
        b.setType(Material.CONCRETE);
        if(color==0)
            b.setData(DyeColor.BLACK.getWoolData());
        else if(color==4)
            b.setData(DyeColor.RED.getWoolData());
        else if(color==6)
            b.setData(DyeColor.ORANGE.getWoolData());
        else if(color==14)
            b.setData(DyeColor.YELLOW.getWoolData());
        else if(color==10)
            b.setData(DyeColor.LIME.getWoolData());
        else if(color==3)
            b.setData(DyeColor.LIGHT_BLUE.getWoolData());
        else if(color==1)
            b.setData(DyeColor.BLUE.getWoolData());
        else if(color==13)
            b.setData(DyeColor.PURPLE.getWoolData());
        else if(color==15)
            b.setData(DyeColor.WHITE.getWoolData());
        }
    }
    
    public void initGame(){
        for(int y=0;y<STAGESIZEY;y++){
            for(int x=0;x<STAGESIZEX;x++){
                stage[y][x]=0;
                colPrint(x, y, 0, 42);
            }
        }
        spun=false;
        combo=-1;
        power=false;
        score=0;
        held=false;
        gameover=false;
        block_hold=-1;
        generateBag2();
        for(int i=0;i<7;i++){
            bag1[i]=bag2[i];
        }
        generateBag2();
        updateScore();
        makeNextBlock();
        for(int i=0;i<4;i++){
            for(int j=0;j<4;j++){
                colPrint(j-7,i+3,0,42);
            }
        }
    }
    
    public void tSpin(){
        int truth=0;
        if(stage[y][x]>0)
            truth++;
        if(stage[y+2][x]>0)
            truth++;
        if(stage[y][x+2]>0)
            truth++;
        if(stage[y+2][x+2]>0)
            truth++;
        if(truth>=3){
            spun=true;
        }else{
            spun=false;
        }
    }
    
    public void updateScore(){
        held=false;
        
        if(spun){
            score+=lines*1000;
            if(combo>4){
                power=true;
            }
            player.playSound(player.getLocation(), Sound.ENTITY_LIGHTNING_THUNDER, 1f, 0.75f);
        }
        
        System.out.println("combo: "+combo);
        spun=false;
        if(combo>=0){
            if(power){
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 1f, (float)Math.pow(2,(combo*2-12)/(double)12));
            }else{
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_HARP, 1f, (float)Math.pow(2,(combo*2-12)/(double)12));
            }
        }
    }
    
    public void dropBlock(){
        int lines = 0;
        while(!isCollide(x, y+1, block_size)){
            lines++;
            moveBlock(x, y+1);
        }
        score+=lines;
        counter=100;
    }
    
    public void moveBlock(int x, int y){
        for (int i=0;i<block_size;i++){
            for (int j=0;j<block_size;j++){
                if(block[i][j] > 0){
                    colPrint(j+this.x, i+this.y, 0, 42);
                }
            }
        }
        this.x = x;
        this.y = y;

        for (int i=0;i<block_size;i++){
            for (int j=0;j<block_size;j++){
                if(block[i][j]>0){
                    colPrint(j+this.x, i+this.y, 0, block[i][j]);
                }
            }
        }
    }
    
    public void userInput(String input){
        switch (input){
        case "right":
            if (!isCollide(x+1, y, block_size)){
                moveBlock(x+1, y);
            }
            break;
        case "left":
            if (!isCollide(x-1, y, block_size)){
                moveBlock(x-1, y);
            }
            break;
        case "down":
            if (!isCollide(x, y+1, block_size)){
                moveBlock(x, y+1);
            }
            break;
        case "y":
        case "Y":
        case "z":
        case "Z":
            rotateBlock(CCW);
            break;
        case "x":
        case "X":
            rotateBlock(CW);
            break;
        case "space":
            dropBlock();
            break;
        case "up":
            rotateBlock(R180);
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
    
    public void holdBlock(){
        int temp;

        if(held==false){

            //print current block into hold slot
            for(int i=0;i<4;i++){
                for(int j=0;j<4;j++){
                    if(Blocklist.block_list[block_current][i][j]==0){
                        colPrint(j-7, i+3, 0, 42);
                    }else{
                        colPrint(j-7, i+3, 0, Blocklist.block_list[block_current][i][j]);
                    }
                }
            }
            
            //erase current block from board
            for (int i=0;i<4;i++){
                for(int j=0;j<4;j++){
                    if(block[i][j]>0){
                        colPrint(j+x, i+y, 0, 0);
                    }
                }
            }

            //first hold
            if(block_hold==-1){
                block_hold=block_current;
                makeNextBlock();
            }else{
                //swap
                temp=block_current;
                block_current=block_hold;
                block_hold=temp;
                
                //spawn new block
                x=3;
                y=10;
                rotation=0;
                if(block_current==2 || block_current==4){
                    block_size=4;
                }else{
                    block_size=3;
                }
                
                for(int i=0;i<block_size;i++){
                    for(int j=0;j<block_size;j++){
                        block[i][j] = Blocklist.block_list[block_current][i][j];
                    }
                }
                //////////////////////d9djtm ptimz the peice
            }
        }else{
            //already held
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
        }
        held=true;
    }
    
    public void rotateBlock(int d){
        int piece_type=block_size-3;
        int special=-1;
        int tries=0;
        int maxtries=5;
        int oldrotation=rotation;

        int[][] temp=new int[block_size][block_size];

        for(int i=0;i<block_size;i++)
            for(int j=0;j<block_size;j++)
                temp[i][j]=block[i][j];
        
        if(d==R180){
            if(block_current==6) {
                maxtries=6;
                piece_type=0;
            }else{
                maxtries=2;
                piece_type=1;
            }
            
            ///retarded 
            if(rotation==0)
                special=0;
            else if(rotation==1)
                special=1;
            else if(rotation==2)
                special=2;
            else if(rotation==3)
                special=3;
            
        }else{
            if(rotation==0 && d==CW)
                special=0;
            else if(rotation==1 && d==CCW)
                special=1;
            else if(rotation==1 && d==CW)
                special=2;
            else if(rotation==2 && d==CCW)
                special=3;
            else if(rotation==2 && d==CW)
                special=4;
            else if(rotation==3 && d==CCW)
                special=5;
            else if(rotation==3 && d==CW)
                special=6;
            else if(rotation==0 && d==CCW)
                special=7;
        }

        switch(d){
        case CCW:
            for(int i=0;i<block_size;i++){
                for(int j=0;j<block_size;j++){
                    block[i][j]=temp[j][block_size-1-i];
                }
            }

            rotation--;
            if(rotation<0){
                rotation+=4;
            }

            break;
        case CW:
            for(int i=0;i<block_size;i++){
                for(int j=0;j<block_size;j++){
                    block[i][j]=temp[block_size - 1 - j][i];
                }
            }

            rotation++;
            if(rotation>3){
                rotation-=4;
            }

            break;
        case R180:
            for(int i=0;i<block_size;i++){
                for(int j=0;j<block_size;j++){
                    block[i][j]=temp[block_size-1-i][block_size-1-j];
                }
            }
            rotation+=2;
            if(rotation>3){
                rotation-=4;
            }
            break;
        }

        for(tries=0;tries<maxtries;tries++){
            if(d==R180) {
                if(!(isCollide(
                    x+Kicktable.kicks_180[piece_type][0][special][tries],
                    y-Kicktable.kicks_180[piece_type][1][special][tries],
                    block_size
                    ))){
                        break;
                    }
            }else{
                if(!(isCollide(
                    x+Kicktable.kicks[piece_type][0][special][tries],
                    y-Kicktable.kicks[piece_type][1][special][tries],
                    block_size
                    ))){
                        break;
                    }
            }
            
            
            if(tries==maxtries-1){
                for(int i=0;i<block_size;i++){
                    for(int j=0;j<block_size;j++){
                        block[i][j]=temp[i][j];
                    }
                }
                rotation=oldrotation;
                System.out.println("All tests failed");
                return;
            }
        }
        
        for(int i=0;i<block_size;i++){
            for(int j=0;j<block_size;j++){
                if(temp[i][j]>0){
                    colPrint(j+x, i+y, 0, 42);
                }
            }
        }
        
        if(d==R180) {
            x+=Kicktable.kicks_180[piece_type][0][special][tries];
            y-=Kicktable.kicks_180[piece_type][1][special][tries];
        }else{
            x+=Kicktable.kicks[piece_type][0][special][tries];
            y-=Kicktable.kicks[piece_type][1][special][tries];
        }
        
        ///if it succeeds show it
        for(int i=0;i<block_size;i++){
            for(int j=0;j<block_size;j++){
                //OOBE FIX
                if((x+j<0 || STAGESIZEX<=x+j) || (y+i<0 || STAGESIZEY<=y+i)) {
                    colPrint(j+x, i+y, 0, 69);
                }else if(stage[y+i][x+j]==0 && block[i][j]==0){
                    colPrint(j+x, i+y, 0, 42);
                }else if(block[i][j] > 0){
                    colPrint(j+x, i+y, 0, block[i][j]);
                }
            }
        }

        if(block_current==6){
            tSpin();
        }
    }
    
    public void checkPlaced(){
        boolean lineclean;
        lines=0;
        
        ///suspicious condition
        for(int i=y;i<STAGESIZEY && i<(y+block_size);i++){
            lineclean=true;
            for(int j=0;j<STAGESIZEX;j++){
                if(stage[i][j] == 0){
                    lineclean=false;
                    break;
                }
            }
            if(lineclean){
                //old problem fix
                for(int j=0;j<STAGESIZEX;j++){
                    stage[0][j]=0;
                    colPrint(j, 0, 0, 42);
                }
                //end
                
                lines++;
                for(int k=i;k>0;k--){
                    for(int j=0;j<STAGESIZEX;j++){
                        stage[k][j]=stage[k-1][j];
                        if(stage[k][j] > 0){
                            colPrint(j, k, 0, stage[k][j]);
                        }else{
                            colPrint(j, k, 0, 42);
                        }
                    }
                }
            }
        }
        
        
        if(lines>0) {
            combo++;
        }else{
            combo=-1;
            power=false;
        }
        
        
        switch(lines){
        case 1:
            score+=40;
            break;
        case 2:
            score+=100;
            break;
        case 3:
            score+=300;
            break;
        case 4:
            score+=1200;
            if(combo>4){
                power=true;
            }
            break;
        }
        updateScore();
    }

    public void placeBlock(){
        for(int i=0;i<block_size;i++){
            for(int j=0;j<block_size;j++){
                if(block[i][j]>0){
                    colPrint(j+x, i+y, 0, block[i][j]);
                    stage[i+y][j+x]=block[i][j];
                }
            }
        }
    }
    
    public void playGame() {
        new BukkitRunnable(){ //BukkitRunnable, not Runnable
             @Override
             public void run() {
                if(counter>=100){
                    if(!isCollide(x, y+1, block_size)){
                        moveBlock(x, y+1);
                        }else{
                            placeBlock();
                            checkPlaced();
                            makeNextBlock();
                        }
                        counter=0;
                }
                counter+=0;
                
                if(gameover){
                    this.cancel();
                }
                System.out.println("loop");
             }
        }.runTaskTimer(Pluginmain.plugin, 0, 5); //Repeating task with 0 ticks initial delay, run once per 20 ticks (one second). Make sure you pass a valid instance of your plugin.
    }
}
