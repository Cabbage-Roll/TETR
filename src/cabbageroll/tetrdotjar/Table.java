package cabbageroll.tetrdotjar;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.xxmicloxx.NoteBlockAPI.model.Playlist;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer;

public class Table {

    World world;
    Player player;
    
    static final int CCW=0;
    static final int CW=1;
    static final int R180=2;
    
    int gx=0;
    int gy=0;
    int gz=0;
    
    //bag variables
    int bag_counter=0;
    int[] bag1=new int[7];
    int[] bag2=new int[7];
    int next_blocks=5;
    int block_hold=-1;
    int block_current=-1;
    
    int lines=0;
    int score=0;//unused for now
    int counter=0;//gravity variable
    int combo=-1;
    
    //board variables
    final int STAGESIZEX=10;
    final int STAGESIZEY=40;
    int[][] stage=new int[STAGESIZEY][STAGESIZEX];
    int[][] block=new int[4][4];
    
    //piece variables
    int x;
    int y;
    int block_size;
    int rotation;
    int ghostx;
    int ghosty;
    
    boolean spun=false;//tspin
    boolean gameover=false;
    boolean held=false;
    boolean power=false;//spike
    
    //retarded
    static Playlist slist;
    static RadioSongPlayer rsp;
    static Song[] sarr=new Song[3];
    
    
    public Table(){
        //???
    }
    
    //works
    void printStaticBlock(int x, int y, int block){
        for(int i=0;i<4;i++){
            for(int j=0;j<4;j++){
                if(Blocklist.block_list[block][i][j]==0){
                    colPrint(j+x, i+y, 0, 42);
                }else{
                    colPrint(j+x, i+y, 0, Blocklist.block_list[block][i][j]);
                }
            }
        }
    }
    
    //works
    void sendTheTitle(){
        String s1="";
        String s2="";
        String s3="";
        if(combo>=1){
                s2=String.valueOf(combo)+" COMBO";
        }
        
        if(spun){
            s3="§5T-SPIN§r";
        }
        
        if(lines==1){
            s1="SINGLE";
        }else if(lines==2){
            s1="DOUBLE";
        }else if(lines==3){
            s1="TRIPLE";
        }else if(lines==4){
            s1="QUAD";
        }
        
        s1=s3+" "+s1+"                ";
        s2=s2+"                                ";
        
        player.sendTitle(s1, s2, 0, 20, 10);
    }
    
    //works
    void removeGhost(){
      //fill with air
        for(int i=0;i<block_size;i++){
            for(int j=0;j<block_size;j++){
                if(block[i][j]>0){
                    colPrint(j+ghostx, i+ghosty, 0, 42);
                }
            }
        }
    }
    
    //works
    void drawGhost(){
        int temp=y;
        while(!isCollide(x, temp+1)){
            temp++;
        }
        
        //update ghost position
        ghostx=x;
        ghosty=temp;

        //print white ghost
        for(int i=0;i<block_size;i++){
            for(int j=0;j<block_size;j++){
                if(block[i][j]>0){
                    colPrint(j+ghostx, i+ghosty, 0, 15);
                }
            }
        }
    }
    
    //works
    void shiftBag1(){
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
    
    //works
    void generateBag2(){
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

    //works
    void spawnBlock(){
        x=3;
        y=20;
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
    }
    
    //works
    void makeNextBlock(){
        block_current=bag1[0];
        shiftBag1();

        ///prints next blocks
        for(int i=0;i<next_blocks;i++){
            printStaticBlock(13, 20+i*4, bag1[i]);
        }
        
        spawnBlock();
        drawGhost();
        
        //check if its possible then print it (at same time)
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
    
    //works
    boolean isCollide(int x, int y){
        int temp;
        
        for(int i=0;i<block_size;i++){
            for(int j=0;j<block_size;j++){
                ///code fix that prevents OOBE and makes walls solid
                temp=1;
                
                if((0<=y+i && y+i<STAGESIZEY) && (0<=x+j && x+j<STAGESIZEX)){
                    temp=stage[y+i][x+j];
                }
                
                if(temp>0 && block[i][j]>0){
                    return true;
                }
            }
        }
        return false;
    }
    
    //improve
    void colPrint(int x, int y, int z, int color){
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
    
    //improve
    void initGame(){
        
        /****trash******/
        int random=(int)(Math.random()*3);
        rsp.playSong(random);
        if(rsp.isPlaying()==false) {
            rsp.setPlaying(true);
        }
        /***********trash ended************/
        
        for(int y=0;y<STAGESIZEY;y++){
            for(int x=0;x<STAGESIZEX;x++){
                stage[y][x]=0;
                colPrint(x, y, 0, 42);
            }
        }
        spun=false;
        gameover=false;
        held=false;
        power=false;
        
        combo=-1;
        score=0;
        block_hold=-1;
        
        generateBag2();
        for(int i=0;i<7;i++){
            bag1[i]=bag2[i];
        }
        generateBag2();
        
        
        makeNextBlock();
        
        //fill hold place with air
        for(int i=0;i<4;i++){
            for(int j=0;j<4;j++){
                colPrint(j-7,i+20,0,42);
            }
        }
    }
    
    //improve now
    void tSpin(){
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
    
    //improve now
    void updateScore(){
        if(spun){
            score+=lines*1000;
            if(combo>3){
                power=true;
            }
            player.playSound(player.getLocation(), Sound.ENTITY_LIGHTNING_THUNDER, 1f, 0.75f);
        }
        
        System.out.println("combo: "+combo);
        if(combo>=0){
            if(power){
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 1f, (float)Math.pow(2,(combo*2-12)/(double)12));
            }else{
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_HARP, 1f, (float)Math.pow(2,(combo*2-12)/(double)12));
            }
        }

        sendTheTitle();
        spun=false;
        held=false;
    }
    
    //improve
    void dropBlock(){
        int lines=0;
        while(!isCollide(x, y+1)){
            lines++;
            moveBlock(x, y+1);
        }
        score+=lines;
        counter=100;
    }
    
    //works
    void moveBlock(int x, int y){
        //fill with air
        for(int i=0;i<block_size;i++){
            for(int j=0;j<block_size;j++){
                if(block[i][j] > 0){
                    colPrint(j+this.x, i+this.y, 0, 42);
                }
            }
        }
        
        //update position
        this.x=x;
        this.y=y;

        removeGhost();
        drawGhost();
        //print piece
        for(int i=0;i<block_size;i++){
            for(int j=0;j<block_size;j++){
                if(block[i][j]>0){
                    colPrint(j+this.x, i+this.y, 0, block[i][j]);
                }
            }
        }
    }
    
    //works
    void userInput(String input){
        switch(input){
        case "y":
            rotateBlock(CCW);
            break;
        case "x":
            rotateBlock(CW);
            break;
        case "c":
            holdBlock();
            break;
            
        case "left":
            if(!isCollide(x-1, y)){
                moveBlock(x-1, y);
            }
            break;
        case "right":
            if(!isCollide(x+1, y)){
                moveBlock(x+1, y);
            }
            break;
            
        case "up":
            rotateBlock(R180);
            break;
        case "down":
            if(!isCollide(x, y+1)){
                moveBlock(x, y+1);
            }
            break;
        
        case "space":
            dropBlock();
            break;
        case "l":
            gameover=true;
            break;
        case "instant":
            while(!isCollide(x, y+1)){
                moveBlock(x, y+1);
            }
            break;
            
        default:
            System.out.println("Wrong input");
        }
    }

    //improve now
    void holdBlock(){
        int temp;

        if(!held){
            removeGhost();
            //print current block into hold slot
            printStaticBlock(-7, 20, block_current);
            
            //erase current block from board
            for (int i=0;i<4;i++){
                for(int j=0;j<4;j++){
                    if(block[i][j]>0){
                        colPrint(j+x, i+y, 0, 42);
                    }
                }
            }

            //if first hold
            if(block_hold==-1){
                block_hold=block_current;
                makeNextBlock();
            }else{
                //swap
                temp=block_current;
                block_current=block_hold;
                block_hold=temp;
                
                //spawn new block
                
                spawnBlock();
                
                //check if its possible then print it
                drawGhost();
                
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
            
        }else{
            //already held
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
        }
        held=true;
    }
    
    //improve now
    void rotateBlock(int d){
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

        removeGhost();
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
                    block[i][j]=temp[block_size-1-j][i];
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
                    y-Kicktable.kicks_180[piece_type][1][special][tries]
                    ))){
                        break;
                    }
            }else{
                if(!(isCollide(
                    x+Kicktable.kicks[piece_type][0][special][tries],
                    y-Kicktable.kicks[piece_type][1][special][tries]
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
                drawGhost();
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
        
        if(d==R180){
            x+=Kicktable.kicks_180[piece_type][0][special][tries];
            y-=Kicktable.kicks_180[piece_type][1][special][tries];
        }else{
            x+=Kicktable.kicks[piece_type][0][special][tries];
            y-=Kicktable.kicks[piece_type][1][special][tries];
        }
        
        ///if it succeeds show it
        drawGhost();
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
    
    //improve now
    void checkPlaced(){
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

    //improve now
    void placeBlock(){
        for(int i=0;i<block_size;i++){
            for(int j=0;j<block_size;j++){
                if(block[i][j]>0){
                    colPrint(j+x, i+y, 0, block[i][j]);
                    stage[i+y][j+x]=block[i][j];
                }
            }
        }
    }
    
    //improve
   	void playGame(){
        new BukkitRunnable(){ //BukkitRunnable, not Runnable
             @Override
             public void run() {
                if(counter>=100){
                    if(!isCollide(x, y+1)){
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
        }.runTaskTimer(Pluginmain.plugin, 0, 0); //Repeating task with 0 ticks initial delay, run once per 20 ticks (one second). Make sure you pass a valid instance of your plugin.
    }
}
