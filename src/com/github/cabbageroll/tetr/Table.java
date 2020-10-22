package com.github.cabbageroll.tetr;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.xxmicloxx.NoteBlockAPI.model.Playlist;
import com.xxmicloxx.NoteBlockAPI.model.RepeatMode;
import com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer;
import com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer;

import fr.minuskube.netherboard.Netherboard;
import fr.minuskube.netherboard.bukkit.BPlayerBoard;

public class Table {
    
    //ZLOSIJT,air,ghost,border
    public static ItemStack[] blocks={
        new ItemStack(Material.CONCRETE, 1, (short) 14),
        new ItemStack(Material.CONCRETE, 1, (short) 1),
        new ItemStack(Material.CONCRETE, 1, (short) 4),
        new ItemStack(Material.CONCRETE, 1, (short) 5),
        new ItemStack(Material.CONCRETE, 1, (short) 3),
        new ItemStack(Material.CONCRETE, 1, (short) 11),
        new ItemStack(Material.CONCRETE, 1, (short) 10),
        new ItemStack(Material.AIR),
        new ItemStack(Material.IRON_BLOCK),
        new ItemStack(Material.STAINED_GLASS, 1, (short) 14),
        new ItemStack(Material.STAINED_GLASS, 1, (short) 1),
        new ItemStack(Material.STAINED_GLASS, 1, (short) 4),
        new ItemStack(Material.STAINED_GLASS, 1, (short) 5),
        new ItemStack(Material.STAINED_GLASS, 1, (short) 3),
        new ItemStack(Material.STAINED_GLASS, 1, (short) 11),
        new ItemStack(Material.STAINED_GLASS, 1, (short) 10),
    };

    World world;
    Player player;
    int looptick;
    BukkitTask task;
    BPlayerBoard board;
    
    static final int CCW=0;
    static final int CW=1;
    static final int R180=2;
    
    int gx=0;
    int gy=0;
    int gz=0;
    int m1x=1;
    int m1y=0;
    int m2x=0;
    int m2y=-1;
    int m3x=0;
    int m3y=0;
    
    //printing variables
    int coni;
    int conj;
    int conk;
    
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
    int b2b=-1;
    
    int totallines=0;
    int totalblocks=0;
    
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
    boolean mini=false;
    boolean gameover=false;
    boolean held=false;
    boolean power=false;//spike
    
    //retarded
    static Playlist slist;
    static RadioSongPlayer rsp;
    
    
    //new
    @SuppressWarnings("deprecation")
    void printSingleBlock(int x, int y, int z, int color){
        Block b=world.getBlockAt(x, y, z);
        if(color==69){
            return;
        }
        
        b.setType(blocks[color].getType());

        b.setData(blocks[color].getData().getData());
        
        /*
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
            b.setData(DyeColor.WHITE.getWoolData());*/
    }
    
    //new
    void initScoreboard(){
        board=Netherboard.instance().createBoard(player, "Stats");
        
        board.clear();
        
        board.set(" ", 5);
        board.set("Lines: "+totallines, 4);
        board.set("Pieces: "+totalblocks, 3);
        board.set("Score: "+score, 2);
        board.set("", 1);
        board.set(""+b2b, 0);
        
    }
    
    //new
    void sendTheScoreboard(){
        if(b2b>0){
            board.set("§6§lB2B x"+b2b, 1);
        }else{
            board.set("", 1);
        }
        
        if(combo>0){
            board.set("COMBO "+combo, 5);
        }else{
            board.set(" ", 5);
        }
        
        board.set("Lines: "+totallines, 4);
        board.set("Pieces: "+totalblocks, 3);
        board.set("Score: "+score, 2);
    }
    
    //works
    void printStaticBlock(int x, int y, int block){
        for(int i=0;i<4;i++){
            for(int j=0;j<4;j++){
                colPrint(j+x, i+y, Blocklist.block_list[block][i][j]);
            }
        }
    }
    
    //works
    void sendTheTitle(){
        String s1="";
        String s2="";
        String s3="";
        String s4="         ";
        if(combo>=1){
            s2=String.valueOf(combo)+" COMBO";
        }
        
        if(spun){
            if(mini){
                s3="§5t-spin§r";
            }else{
                s3="§5T-SPIN§r";
            }
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
        
        if(lines==0 && spun){
            s1=" ";
        }
        
        if(totallines*10==totalblocks*4){
            s4="§6§lALL CLEAR§r";
        }
        
        //dont kill old title if its empty
        if(s1!="" || s2!=""){
        s1=s3+" "+s1+"       "+s4;
        s2=s2+"                                ";
        
            player.sendTitle(s1, s2, 0, 20, 10);
        }
    }

    //works
    void removeGhost(){
      //fill with air
        for(int i=0;i<block_size;i++){
            for(int j=0;j<block_size;j++){
                if(block[i][j]!=7){
                    colPrint(j+ghostx, i+ghosty, 7);
                }
            }
        }
    }
    
    //works
    void drawGhost(){
        ghosty=y;
        while(!isCollide(x, ghosty+1)){
            ghosty++;
        }
        
        //update ghost position
        ghostx=x;

        //print ghost
        for(int i=0;i<block_size;i++){
            for(int j=0;j<block_size;j++){
                if(block[i][j]!=7){
                    colPrint(j+ghostx, i+ghosty, 9+block[i][j]);
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
                if(stage[i+y][j+x]!=7 && block[i][j]!=7){
                    player.playSound(player.getLocation(), Sound.BLOCK_PISTON_CONTRACT, 1f, 1f);
                    gameover=true;
                    return;
                }
                colPrint(j+x, i+y, block[i][j]);
            }
        }
        
    }
    
    //works
    boolean isCollide(int x, int y){
        int temp;
        
        for(int i=0;i<block_size;i++){
            for(int j=0;j<block_size;j++){
                ///code fix that prevents OOBE and makes walls solid
                temp=9;
                
                if((0<=y+i && y+i<STAGESIZEY) && (0<=x+j && x+j<STAGESIZEX)){
                    temp=stage[y+i][x+j];
                }
                
                if(temp!=7 && block[i][j]!=7){
                    return true;
                }
            }
        }
        return false;
    }
    
    //IMPROVE
    void colPrint(int x, int y, int color){
        int ti;
        int tj;
        int tk;
        
        if(coni<0){
            ti=-1;
        }else if(coni>0){
            ti=1;
        }else{
            ti=0;
        }
        
        if(conj<0){
            tj=-1;
        }else if(conj>0){
            tj=1;
        }else{
            tj=0;
        }
        
        if(conk<0){
            tk=-1;
        }else if(conk>0){
            tk=1;
        }else{
            tk=0;
        }
        
        for(int i=0;i<=coni;i++){
            for(int j=0;j<=conj;j++){
                for(int k=0;k<=conk;k++){
                    printSingleBlock(gx+x*m1x+y*m1y+(i==coni?0:i)*ti, gy+x*m2x+y*m2y+(j==conj?0:j)*tj, gz+x*m3x+y*m3y+(k==conk?0:k)*tk, color);
                }
            }
        }
    }
    
    //improve
    void initGame(){
        
        if(task!=null){
            task.cancel();
        }
        
        /****trash******/
        int random=(int)(Math.random()*Pluginmain.numberofsongs);
        rsp.playSong(random);
        rsp.setRepeatMode(RepeatMode.ONE);
        if(rsp.isPlaying()==false) {
            rsp.setPlaying(true);
        }
        player.sendMessage("Playing: "+rsp.getSong().getPath());
        /***********trash ended************/
        
        coni=Math.max((int)Math.abs(m1x),(int)Math.abs(m1y));
        conj=Math.max((int)Math.abs(m2x),(int)Math.abs(m2y));
        conk=Math.max((int)Math.abs(m3x),(int)Math.abs(m3y));
        
        for(int y=0;y<STAGESIZEY;y++){
            for(int x=0;x<STAGESIZEX;x++){
                stage[y][x]=7;
                colPrint(x, y, 7);
            }
        }
        spun=false;
        gameover=false;
        held=false;
        power=false;
        
        combo=-1;
        score=0;
        block_hold=-1;
        b2b=-1;
        
        totallines=0;
        totalblocks=0;
        
        generateBag2();
        for(int i=0;i<7;i++){
            bag1[i]=bag2[i];
        }
        generateBag2();
        
        makeNextBlock();
        
        //fill hold place with air
        for(int i=0;i<4;i++){
            for(int j=0;j<4;j++){
                colPrint(j-7, i+20, 7);
            }
        }
        
        looptick=0;
        playGame();
        initScoreboard();
    }
    
    //works
    void tSpin(){
        int truth=0;
        if(stage[y][x]!=7){
            truth++;
        }
        if(stage[y][x+2]!=7){
            truth++;
        }
        if(stage[y+2][x]!=7){
            truth++;
        }
        if(stage[y+2][x+2]!=7){
            truth++;
        }
        if(truth>=3){
            spun=true;
            mini=true;
            if(rotation==0){
                if(stage[y][x]!=7 && stage[y][x+2]!=7){
                    mini=false;
                }
            }else if(rotation==1){
                if(stage[y][x+2]!=7 && stage[y+2][x+2]!=7){
                    mini=false;
                }
            }else if(rotation==2){
                if(stage[y+2][x+2]!=7 && stage[y+2][x]!=7){
                    mini=false;
                }
            }else if(rotation==3){
                if(stage[y+2][x]!=7 && stage[y][x]!=7){
                    mini=false;
                }
            }
        }else{
            spun=false;
            mini=false;
        }
    }
    
    //improve now
    void updateScore(){
        if((spun && lines>0) || lines==4){
            b2b++;
        }else if(lines>0){
            b2b=-1;
        }
        
        if(spun){
            score+=lines*1000;
            if(combo>3){
                power=true;
            }
            player.playSound(player.getLocation(), Sound.ENTITY_LIGHTNING_THUNDER, 1f, 0.75f);
        }
        
        if(combo>=0){
            if(power){
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 1f, (float)Math.pow(2,(combo*2-12)/(double)12));
            }else{
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_HARP, 1f, (float)Math.pow(2,(combo*2-12)/(double)12));
            }
        }

        sendTheTitle();
        sendTheScoreboard();
        
        spun=false;
        mini=false;
        held=false;
        /*
        SINGLE:100
        DOUBLE:300
        TRIPLE:500
        QUAD:800
        TSPIN_MINI:100
        TSPIN:400
        TSPIN_MINI_SINGLE:200
        TSPIN_SINGLE:800
        TSPIN_MINI_DOUBLE:400
        TSPIN_DOUBLE:1200
        TSPIN_TRIPLE:1600
        TSPIN_QUAD:2600
        BACKTOBACK_MULTIPLIER:1.5
        COMBO:50
        ALL_CLEAR:3500
        SOFTDROP:1
        HARDDROP:2
        */
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
                if(block[i][j]!=7){
                    colPrint(j+this.x, i+this.y, 7);
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
                if(block[i][j]!=7){
                    colPrint(j+this.x, i+this.y, block[i][j]);
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
                    if(block[i][j]!=7){
                        colPrint(j+x, i+y, 7);
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
                        if(stage[i+y][j+x]!=7 && block[i][j]!=7){
                            player.playSound(player.getLocation(), Sound.BLOCK_PISTON_CONTRACT, 1f, 1f);
                            gameover=true;
                            return;
                        }
                        colPrint(j+x, i+y, block[i][j]);
                    }
                }
                
            }
            
        }else{
            //already held
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
        }
        held=true;
    }
    
    //IMPROVE
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
            if(d==R180){
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
                if(temp[i][j]!=7){
                    colPrint(j+x, i+y, 7);
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
        
        drawGhost();
        ///if it succeeds show it
        for(int i=0;i<block_size;i++){
            for(int j=0;j<block_size;j++){
                //OOBE FIX
                if((x+j<0 || STAGESIZEX<=x+j) || (y+i<0 || STAGESIZEY<=y+i)) {
                    colPrint(j+x, i+y, 69);
                }else if(stage[y+i][x+j]==7 && block[i][j]==7){
                    colPrint(j+x, i+y, 7);
                }else if(block[i][j]!=7){
                    colPrint(j+x, i+y, block[i][j]);
                }
            }
        }
        
        
        if(block_current==6){
            if((d!=R180 && tries==4) && (special==0 || special==3 || special==4 || special==7)){
                spun=true;
                mini=false;
            }else{
            tSpin();
            }
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
                if(stage[i][j]==7){
                    lineclean=false;
                    break;
                }
            }
            if(lineclean){
                //old problem fix
                for(int j=0;j<STAGESIZEX;j++){
                    stage[0][j]=7;
                    colPrint(j, 0, 7);
                }
                //end
                
                lines++;
                for(int k=i;k>0;k--){
                    for(int j=0;j<STAGESIZEX;j++){
                        stage[k][j]=stage[k-1][j];
                        if(stage[k][j]!=7){
                            colPrint(j, k, stage[k][j]);
                        }else{
                            colPrint(j, k, 7);
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
        
        totallines+=lines;
        totalblocks+=1;
        updateScore();
    }

    //improve now
    void placeBlock(){
        for(int i=0;i<block_size;i++){
            for(int j=0;j<block_size;j++){
                if(block[i][j]!=7){
                    colPrint(j+x, i+y, block[i][j]);
                    stage[i+y][j+x]=block[i][j];
                }
            }
        }
    }
    
    //works
   	void playGame(){
   	    task=new BukkitRunnable(){
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
   	            
   	        board.set("Tick: "+looptick++%20, 0);
   	        }
   	    }.runTaskTimer(Pluginmain.plugin, 0, 0);
    }
}
