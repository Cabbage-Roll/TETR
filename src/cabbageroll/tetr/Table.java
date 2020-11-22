package cabbageroll.tetr;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import cabbageroll.tetr.constants.Blocklist;
import cabbageroll.tetr.constants.Garbagetable;
import cabbageroll.tetr.constants.Kicktable;
import cabbageroll.tetr.functions.SendBlockChangeCustom;
import fr.minuskube.netherboard.Netherboard;
import fr.minuskube.netherboard.bukkit.BPlayerBoard;
import net.md_5.bungee.api.ChatColor;

public class Table {
    
    public static boolean transparent=false;
    
    private World world;
    public Player player;
    private int looptick;
    public BukkitTask task;
    private BPlayerBoard board;
    ArrayList<Player> whotosendblocksto;
    
    private static final int CCW=0;
    private static final int CW=1;
    private static final int R180=2;
    
    public int gx=100;
    public int gy=50;
    public int gz=0;
    public int m1x=1;
    public int m1y=0;
    public int m2x=0;
    public int m2y=-1;
    public int m3x=0;
    public int m3y=0;
    
    
    //intermediate variables
    private int coni;
    private int conj;
    private int conk;
    
    //bag variables
    private Random gen;
    private int bag_counter=0;
    private int[] bag1=new int[7];
    private int[] bag2=new int[7];
    private int next_blocks=5;
    private int block_hold=-1;
    private int block_current=-1;
    
    private int lines=0;
    private int score=0;
    private int counter=0;//gravity variable
    private int combo=-1;
    private int b2b=-1;
    
    private int totallines=0;
    private int totalblocks=0;
    
    //board variables
    private final int STAGESIZEX=10;
    private final int STAGESIZEY=40;
    private final int VISIBLEROWS=40;
    private int[][] stage=new int[STAGESIZEY][STAGESIZEX];
    private int[][] block=new int[4][4];
    
    //piece variables
    private int x;
    private int y;
    private int block_size;
    private int rotation;
    private int ghostx;
    private int ghosty;
    
    private boolean spun=false;//tspin
    private boolean mini=false;
    boolean gameover=false;
    private boolean held=false;
    private boolean power=false;//spike
    
    //garbage
    private ArrayList<Integer> garbo=new ArrayList<Integer>();
    private Random garbagegen;
    private int well;
    private int cap=4;
    private int totalgarbage;
    
    //handling
    private final int DAS=4;
    private final int ARR=0;
    private final int SDF=40;
    private boolean dasing;
    private int dura;
    private double oldx;
    private double oldy;
    private double oldz;
    
    //zone
    private int zonelines;
    private boolean zone;
    
    Table(Player p){
        player=p;
        world=p.getWorld();
    }
    
    private void stopZone() {
        boolean lineclean;
        lines = 0;
        
        for(int i=0;i<STAGESIZEY;i++){
            lineclean=true;
            for(int j=0;j<STAGESIZEX;j++){
                if(stage[i][j]!=16){
                    lineclean=false;
                    break;
                }
            }

            if(lineclean){
                for(int j=0;j<STAGESIZEX;j++) {
                    stage[0][j]=7;
                    colPrint(j, 0, 7);
                }
                
                lines++;
                for(int k=i;k>0;k--) {
                    for(int j=0;j<STAGESIZEX;j++) {
                        stage[k][j] = stage[k-1][j];
                        colPrint(j, k, stage[k][j]);
                    }
                }
            }
        }

        player.sendTitle("", ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "" + lines + " LINE" + (lines==1?"":"S"), 20, 40, 20);
        updateScore();

        sendGarbage(lines*2);
        zone = false;
        zonelines = 0;
    }
    
    public void startZone() {
        if(!zone) {
            zone = true;
            
        }
    }
    
    private void topOutCheck() {
        for(int i=0;i<block_size;i++){
            for(int j=0;j<block_size;j++){
                if(stage[i+y][j+x]!=7 && block[i][j]!=7){
                    if(!zone) {
                        player.playSound(player.getEyeLocation(), SoundUtil.ORB_PICKUP, 1f, 1f);
                        gameover=true;
                    }else {
                        stopZone();
                    }
                    return;
                }
            }
        }
    }
    
    public void rotateTable(String input) {
        int temp;
        switch(input) {
        case "X":
            temp=-m3x;
            m3x=m2x;
            m2x=temp;
            temp=-m3y;
            m3y=m2y;
            m2y=temp;
            break;
        case "Y":
            temp=-m3x;
            m3x=m1x;
            m1x=temp;
            temp=-m3y;
            m3y=m1y;
            m1y=temp;
            break;
        case "Z":
            temp=-m2x;
            m2x=m1x;
            m1x=temp;
            temp=-m2y;
            m2y=m1y;
            m1y=temp;
            break;
        }
        
    }
    
    private void sendGarbage(int n) {
        Main.roommap.get(Main.inwhichroom.get(player)).forwardGarbage(n, player);
    }
    
    public void receiveGarbage(int n) {
        garbo.add(n);
        printLava();
    }
    
    private void printLava() {
        int total=0;
        for(int num: garbo){
            total+=num;
        }
        
        for(int i=0;i<STAGESIZEY;i++) {
            colPrint(-2, STAGESIZEY-1-i, 7);
        }
        
        for(int i=0;i<total;i++) {
            colPrint(-2, STAGESIZEY-1-i%20, (i/20)%7);
        }
    }
    
    private void putGarbage(){
        for(int h=0;h<cap;h++){
            if(!garbo.isEmpty()){
                totalgarbage++;
                for(int i=0;i<STAGESIZEY-1;i++){
                    for(int j=0;j<STAGESIZEX;j++){
                        stage[i][j]=stage[i+1][j];
                        colPrint(j, i, stage[i][j]);
                    }
                }
                for(int j=0;j<STAGESIZEX;j++){
                    if(j==well){
                        stage[STAGESIZEY-1][j]=7;
                        colPrint(j, STAGESIZEY-1, 7);
                    }else{
                        stage[STAGESIZEY-1][j]=8;
                        colPrint(j, STAGESIZEY-1, 8);
                    }
                }
                
                garbo.set(0, garbo.get(0)-1);
                if(garbo.get(0)<=0){
                    garbo.remove(0);
                    well=garbagegen.nextInt(10);
                }
            }
        }

        printLava();
    }
    
    //works
    private int getBlockSize(int block){
        if(block==4){
            return 4;
        }else if(block==2){
            return 2;
        }else{
            return 3;
        }
    }
    
    //works
    private void printSingleBlock(int x, int y, int z, int color){
        if(color==7 && transparent){
            Block b=world.getBlockAt(x, y, z);
            for(Player player: whotosendblocksto){
                SendBlockChangeCustom.sendBlockChangeCustom(player, new Location(world, x, y, z), b);
            }
            return;
        }
        
        for(Player player: whotosendblocksto){
            SendBlockChangeCustom.sendBlockChangeCustom(player, new Location(world, x, y, z), color);
        }
    }
    
    //new
    private void initScoreboard(){
        board=Netherboard.instance().createBoard(player, "Stats");
        
        board.clear();
        
        board.set(" ", 5);
        board.set("Lines: "+totallines, 4);
        board.set("Pieces: "+totalblocks, 3);
        board.set("Score: "+score, 2);
        board.set("", 1);
        
    }
    
    //new
    private void sendTheScoreboard(){
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
    
    //improve
    private void printStaticBlock(int x, int y, int block){
        for(int i=0;i<4;i++){
            for(int j=0;j<4;j++){
                colPrint(j+x, i+y, 7);
            }
        }
        
        for(int i=0;i<(getBlockSize(block)==4?3:getBlockSize(block));i++){
            for(int j=0;j<getBlockSize(block);j++){
                switch(block){
                case 2:
                    colPrint(j+x+1, i+y+1, Blocklist.block_list[block][i][j]);
                    break;
                case 0:
                case 1:
                case 3:
                case 5:
                case 6:
                    ///somethin g wrong
                    colPrint(j+x+0.5f, i+y+1, Blocklist.block_list[block][i][j]);
                    break;
                case 4:
                    colPrint(j+x, i+y+0.5f, Blocklist.block_list[block][i][j]);
                    break;
                }
            }
        }
    }
    
    //works
    private void sendTheTitle(){
        if(!zone) {
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
            
            if((totallines-totalgarbage)*10+totalgarbage==totalblocks*4){
                s4="§6§lALL CLEAR§r";
            }
            
            //dont kill old title if its empty
            if(s1!="" || s2!=""){
            s1=s3+" "+s1+"       "+s4;
            s2=s2+"                                ";
            
                //Main.functions.sendTitle(player, s1, s2, 0, 20, 10);
                player.sendTitle(s1, s2, 0, 20, 10);
            }
        }
    }

    //works
    private void removeGhost(){
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
    private void drawGhost(){
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
    private void shiftBag1(){
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
    private void generateBag2(){
        bag_counter=0;
        for(int i=0;i<7;i++){
            bag2[i]=(int)(gen.nextInt(7));
            for(int j=0;j<i;j++){
                if(bag2[i]==bag2[j]){
                    i--;
                }
            }
        }
    }

    //works
    private void spawnBlock(){
        x=3;
        y=20;
        rotation=0;
        
        if(block_current==4){
            block_size=4;
        }else if(block_current==2){
            block_size=2;
        }else{
            block_size=3;
        }
        
        for(int i=0;i<block_size;i++){
            for(int j=0;j<block_size;j++){
                block[i][j] = Blocklist.block_list[block_current][i][j];
            }
        }
        
        spun=false;
        mini=false;
    }
    
    //works
    private void makeNextBlock(){
        block_current=bag1[0];
        shiftBag1();

        ///prints next blocks
        for(int i=0;i<next_blocks;i++){
            printStaticBlock(13, 20+i*4, bag1[i]);
        }
        
        spawnBlock();
        drawGhost();
        
        //check if its possible then print it (at same time)
        
        
        topOutCheck();
        for(int i=0;i<block_size;i++){
            for(int j=0;j<block_size;j++){
                if(block[i][j]!=7){
                    colPrint(j+x, i+y, block[i][j]);
                }
            }
        }
    }
    
    //works
    private boolean isCollide(int x, int y){
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
    
    //works
    private void colPrint(float x, float y, int color){
        if(y>=STAGESIZEY-VISIBLEROWS) {
            for(int i=0;i<=coni;i++) {
                for(int j=0;j<=conj;j++) {
                    for(int k=0;k<=conk;k++) {
                        printSingleBlock(
                        gx+(int)(x*m1x)+(int)(y*m1y)+(i==coni?0:i),
                        gy+(int)(x*m2x)+(int)(y*m2y)+(j==conj?0:j),
                        gz+(int)(x*m3x)+(int)(y*m3y)+(k==conk?0:k),
                        color
                        );
                    }
                }
            }
        }
    }
    
    //improve
    public void initGame(long seed,long seed2){

        player.setWalkSpeed(0.2f);
        garbo.clear();
        gen=new Random(seed);
        garbagegen=new Random(seed2);
        well=garbagegen.nextInt(10);
        totalgarbage=0;
        
        
        if(task!=null){
            task.cancel();
        }
        
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
        player.getInventory().setHeldItemSlot(8);
        printLava();
        
        oldx=player.getLocation().getX();
        oldy=player.getLocation().getY();
        oldz=player.getLocation().getZ();
        zone=false;
        zonelines = 0;
    }
    
    //works
    private void tSpin(){
        int truth=0;
        boolean wall=false;
        if((y<0 || STAGESIZEY<=y) || (x<0 || STAGESIZEX<=x)){
            truth++;
            wall=true;
        }else if(stage[y][x]!=7){
            truth++;
        }
        
        if((y<0 || STAGESIZEY<=y) || (x+2<0 || STAGESIZEX<=x+2)){
            truth++;
            wall=true;
        }else if(stage[y][x+2]!=7){
            truth++;
        }
        
        if((y+2<0 || STAGESIZEY<=y+2) || (x<0 || STAGESIZEX<=x)){
            truth++;
            wall=true;
        }else if(stage[y+2][x]!=7){
            truth++;
        }
        
        if((y+2<0 || STAGESIZEY<=y+2) || (x+2<0 || STAGESIZEX<=x+2)){
            truth++;
            wall=true;
        }else if(stage[y+2][x+2]!=7){
            truth++;
        }
        
        if(truth>=3 && wall){
            spun=true;
            mini=true;
            return;
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
    private void updateScore(){
        if(!zone) {
            
            if((spun && lines>0) || lines==4){
                b2b++;
            }else if(lines>0){
                b2b=-1;
            }
            
            if(lines>0){
                combo++;
            }else{
                putGarbage();
                combo=-1;
                power=false;
            }
            
            if(spun){
                player.playSound(player.getEyeLocation(), SoundUtil.THUNDER, 1f, 0.75f);
                if(mini){
                    switch(lines){
                    case 0:
                        score+=100;
                        break;
                    case 1:
                        score+=200*(b2b>0?1.5:1);
                        break;
                    case 2:
                        score+=400*(b2b>0?1.5:1);
                        break;
                    }
                }else{
                    switch(lines){
                    case 0:
                        score+=400;
                        break;
                    case 1:
                        score+=800*(b2b>0?1.5:1);
                        break;
                    case 2:
                        score+=1200*(b2b>0?1.5:1);
                        break;
                    case 3:
                        score+=1600*(b2b>0?1.5:1);
                        break;
                    }
                }
                
            }else{
                switch(lines){
                case 1:
                    score+=100;
                    break;
                case 2:
                    score+=300;
                    break;
                case 3:
                    score+=500;
                    break;
                case 4:
                    score+=800*(b2b>0?1.5:1);
                    break;
                }
            }
            
            //condition for triggering power...
            if(combo>3){
                power=true;
            }
            
            if(combo>=0){
                if(power){
                    player.playSound(player.getEyeLocation(), SoundUtil.NOTE_PLING, 1f, (float)Math.pow(2,(combo*2-16)/(double)16));
                }else{
                    player.playSound(player.getEyeLocation(), SoundUtil.NOTE_HARP, 1f, (float)Math.pow(2,(combo*2-16)/(double)16));
                }
                score+=combo*50;
            }
            
            if((totallines-totalgarbage)*10+totalgarbage==totalblocks*4){
                score+=3500;
                sendGarbage(10);
            }
    
            sendTheTitle();
            sendTheScoreboard();
            
            ///sendgarbage
            if(lines>0){
                int temp=0;
                if(spun==false){
                    temp=lines-1;
                }else if(mini==true){
                    if(lines==1){
                        temp=4;
                    }else{
                        temp=6;
                    }
                }else{
                    if(lines==1){
                        temp=5;
                    }else if(lines==2){
                        temp=7;
                    }else{
                        temp=8;
                    }
                }
                
                sendGarbage(Garbagetable.garbage_table[temp][combo]);
            }
        }
        
        spun=false;
        mini=false;
        held=false;
        
    }
        /*scoring:
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
    
    //improve
    private void dropBlock(){
        int lines=0;
        while(!isCollide(x, y+lines+1)){
            lines++;
        }
        moveBlock(x, y+lines);
        score+=lines*2;
        counter=100;
    }
    
    //works
    private void moveBlock(int x, int y){
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
    public void userInput(String input){
        switch(input){
        case "y":
            rotateBlock(CCW);
            counter=0;
            break;
        case "x":
            rotateBlock(CW);
            counter=0;
            break;
        case "c":
            holdBlock();
            counter=0;
            break;
            
        case "left":
            if(!isCollide(x-1, y)){
                moveBlock(x-1, y);
                counter=0;
            }
            break;
        case "right":
            if(!isCollide(x+1, y)){
                moveBlock(x+1, y);
                counter=0;
            }
            break;
            
        case "up":
            rotateBlock(R180);
            counter=0;
            break;
        case "down":
            if(!isCollide(x, y+1)){
                moveBlock(x, y+1);
                counter=0;
                score+=1;
                sendTheScoreboard();
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
                userInput("down");
            }
            break;
            
        default:
            System.out.println("Wrong input");
        }
    }

    //improve now
    private void holdBlock(){
        int temp;

        if(!held){
            removeGhost();
            //print current block into hold slot
            printStaticBlock(-7, 20, block_current);
            
            //erase current block from board
            for (int i=0;i<block_size;i++){
                for(int j=0;j<block_size;j++){
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
                
                topOutCheck();
                for(int i=0;i<block_size;i++){
                    for(int j=0;j<block_size;j++){
                        if(block[i][j]!=7){
                            colPrint(j+x, i+y, block[i][j]);
                        }
                    }
                }
                
            }
            
        }else{
            //already held
            player.playSound(player.getEyeLocation(), SoundUtil.VILLAGER_NO, 1f, 1f);
        }
        held=true;
    }
    
    //IMPROVE
    private void rotateBlock(int d){
        int piece_type=block_size==4?1:0;
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
                if(block[i][j]!=7){
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
    private void checkPlaced(){
        boolean lineclean;
        lines=0;
        int hr=0;
        
        //find highest row
        for(int k=0;k<STAGESIZEY;k++){
            for(int j=0;j<STAGESIZEX;j++){
                if(stage[k][j]!=7){
                    hr=k;
                    k=STAGESIZEY;
                    break;
                }
            }
        }
        
        for(int i=y;i<STAGESIZEY;i++){
            lineclean=true;
            for(int j=0;j<STAGESIZEX;j++){
                if(stage[i][j]==7 || stage[i][j]==16){
                    lineclean=false;
                    break;
                }
            }
            if(lineclean){
                lines++;
                
                    for(int j=0;j<STAGESIZEX;j++){
                        stage[0][j] = 7;
                        colPrint(j, 0, 7);
                    }
                    for(int k=i;k>hr-lines;k--){
                        for(int j=0;j<STAGESIZEX;j++){
                            stage[k][j] = stage[k-1][j];
                            colPrint(j, k, stage[k][j]);
                        }
                    }
                    
                if(zone){
                    zonelines++;
                    for(int k=0;k<STAGESIZEY-zonelines;k++){
                        for(int j=0;j<STAGESIZEX;j++){
                            stage[k][j] = stage[k+1][j];
                            colPrint(j, k, stage[k][j]);
                        }
                    }
                    for(int j=0;j<STAGESIZEX;j++){
                        stage[STAGESIZEY-zonelines][j] = 16;
                        colPrint(j, STAGESIZEY-zonelines, 16);
                    }
                }
                i--;
            }
        }
        
        
        totallines+=lines;
        totalblocks+=1;
        updateScore();
        if(zone && lines>0) {
            player.playSound(player.getEyeLocation(), SoundUtil.NOTE_PLING, 1f, (float)Math.pow(2,(zonelines*2-16)/(double)16));
        }
        makeNextBlock();
    }

    //improve now
    private void placeBlock(){
        for(int i=0;i<block_size;i++){
            for(int j=0;j<block_size;j++){
                if(block[i][j]!=7){
                    colPrint(j+x, i+y, block[i][j]);
                    stage[i+y][j+x]=block[i][j];
                }
            }
        }
        checkPlaced();
    }
    

    double maxvelocity=0;
    long startTime;
    boolean moving=false;
    String direction;
    boolean singlemove;
    
   	private void playGame(){
   	    task=new BukkitRunnable(){
   	        @Override
   	        public void run() {
   	            if(counter>=100){
   	                if(!isCollide(x, y+1)){
   	                    moveBlock(x, y+1);
   	                    }else{
   	                        placeBlock();
   	                    }
   	                    counter=0;
   	            }
   	            counter+=totallines/4;
   	            
   	            if(gameover){
   	                player.setWalkSpeed(0.2f);
   	                task.cancel();
   	                task=null;
   	                if(Main.roomlist.contains(Main.inwhichroom.get(player))){
       	                Main.roommap.get(Main.inwhichroom.get(player)).playersalive--;
       	                if(Main.roommap.get(Main.inwhichroom.get(player)).playersalive<=1){
       	                    Main.roommap.get(Main.inwhichroom.get(player)).stopRoom();
       	                }
   	                }
   	            }
   	            
       	        board.set("TIME "+looptick, 0);
       	        looptick++;
    
                /*PacketPlayOutUpdateHealth test;
                test=new PacketPlayOutUpdateHealth((float)player.getHealth(), 2, player.getSaturation());
                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(test);*/
       	        
       	        //movement code
       	        /*
       	        Vector vel = new Vector(player.getLocation().getX()-oldx, 
                       	                player.getLocation().getY()-oldy, 
                       	                player.getLocation().getZ()-oldz);
    
       	        if(Math.abs(vel.getX())>0 && !singlemove) {
       	            if(vel.getX()<0) {
       	                direction="left";
       	            }else {
       	                direction="right";
       	            }
       	            userInput(direction);
       	            singlemove=true;
       	            player.sendMessage("single move");
       	        }
       	        
       	        if(maxvelocity<Math.abs(vel.getX()) && !moving && Math.abs(vel.getX())>0.05f*player.getWalkSpeed()*5) {
       	            startTime = System.nanoTime();
       	            moving=true;
       	            if(vel.getX()<0) {
       	                direction="left";
       	            }else {
       	                direction="right";
       	            }
       	            dura=looptick;
       	        }
    
       	        if(maxvelocity>Math.abs(vel.getX()) && moving && Math.abs(vel.getX())<0.2f*player.getWalkSpeed()*5) {
       	            singlemove=false;
       	            moving=false;
       	            dasing=false;
       	        }
       	        
       	        board.set(""+vel.getX(), -10);
       	        
       	        maxvelocity=Math.abs(vel.getX());
       	        
    
       	        oldx=player.getLocation().getX();
       	        oldy=player.getLocation().getY();
       	        oldz=player.getLocation().getZ();
       	        
       	        if(ARR>0) {
           	        if(dasing && (looptick-dura)%ARR==0) {
           	            userInput(direction);
           	        }
       	        }else {
       	            if(dasing) {
       	                userInput(direction);
       	                userInput(direction);
       	                userInput(direction);
       	                userInput(direction);
       	                userInput(direction);
       	                userInput(direction);
       	                userInput(direction);
       	                userInput(direction);
       	                userInput(direction);
       	                userInput(direction);
       	            }
       	        }
       	        
       	        if(looptick-dura>=DAS && moving) {
       	            dasing=true;
       	        }
   	        */
   	        
   	        }
   	    }.runTaskTimer(Main.plugin, 0, 0);
    }
}
