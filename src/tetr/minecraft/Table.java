package tetr.minecraft;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import fr.minuskube.netherboard.Netherboard;
import fr.minuskube.netherboard.bukkit.BPlayerBoard;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import tetr.minecraft.constants.Blocks;
import tetr.minecraft.constants.Garbagetable;
import tetr.minecraft.functions.SendBlockChangeCustom;
import tetr.shared.GameLogic;

public class Table {
    
    GameLogic gl = new GameLogic();

    public static boolean transparent=false;
    
    private World world;
    private Player player;
    private int looptick;
    private BPlayerBoard board;
    
    private int gx = 100;
    private int gy = 50;
    private int gz = 0;
    public int m1x = 1;
    public int m1y = 0;
    public int m2x = 0;
    public int m2y = -1;
    public int m3x = 0;
    public int m3y = 0;
    public int thickness = 1;

    //intermediate variables
    private int coni;
    private int conj;
    private int conk;
    
    //bag variables
    private Random gen;
    private ArrayList<Integer> nextPieces = new ArrayList<Integer>();
    
    private int lines;
    private int score;
    private int combo;
    private int b2b;
    
    public boolean ULTRAGRAPHICS = true;
    
    //if counter > gravity^-1  fall
    private int counter = 0;//gravity variable
    private double startingGravity = 20;
    private int gravityIncreaseDelay = 600;
    private double gravityIncrease = 1 / 20;
    private int lockDelay = 20;
    private int timesMoved = 0;
    private static final int MAXIMUMMOVES = 15;
    
    private int totallines;
    private int totalblocks;
    
    //board variables
    private final int STAGESIZEX = 10;
    private final int STAGESIZEY = 40;
    private final int VISIBLEROWS = 24;
    private int[][] stage = new int[gl.STAGESIZEY][gl.STAGESIZEX];
    
    //piece variables
    private Point currentPiecePosition;
    private int currentPieceRotation;
    
    private boolean spun = false;//tspin
    private boolean mini = false;
    private boolean gameover = false;
    
    //garbage
    private ArrayList<Integer> garbo = new ArrayList<Integer>();
    private Random garbagegen;
    private int well;
    private double startingGarbageCap = 4;
    private double garbageCapIncreaseDelay = 1200;
    private double garbageCapIncrease = 1 / 20;
    private int totalgarbage;
    
    //handling
    /*
    private final int DAS=4;
    private final int ARR=0;
    private final int SDR=1;
    private boolean dasing;
    private int dura;
    private double oldx;
    private double oldy;
    private double oldz;
    */
    
    //zone
    private int zonelines;
    private boolean zone;
    
    Table(Player p) {
        player=p;
        world=p.getWorld();
        Location location=player.getLocation();
        float yaw = player.getLocation().getYaw();
        if(45<=yaw && yaw<135) {
            rotateTable("Y");
            rotateTable("Y");
            rotateTable("Y");
            moveTable(location.getBlockX()-STAGESIZEY, location.getBlockY()+STAGESIZEY-VISIBLEROWS/2, location.getBlockZ()+STAGESIZEX/2);
        }else if(135<=yaw && yaw<225) {
            moveTable(location.getBlockX()-STAGESIZEX/2, location.getBlockY()+STAGESIZEY-VISIBLEROWS/2, location.getBlockZ()-STAGESIZEY);
        }else if(225<=yaw && yaw<315) {
            rotateTable("Y");
            moveTable(location.getBlockX()+STAGESIZEY, location.getBlockY()+STAGESIZEY-VISIBLEROWS/2, location.getBlockZ()-STAGESIZEX/2);
        }else if((315<=yaw && yaw<360) || (0<=yaw && yaw<45)) {
            rotateTable("Y");
            rotateTable("Y");
            moveTable(location.getBlockX()+STAGESIZEX/2, location.getBlockY()+STAGESIZEY-VISIBLEROWS/2, location.getBlockZ()+STAGESIZEY);
        }
        gameover=true;
    }
    
    public int getGx() {
        return gx;
    }
    
    public int getGy() {
        return gy;
    }
    
    public int getGz() {
        return gz;
    }
    
    public BPlayerBoard getBoard() {
        return board;
    }
    
    public Player getPlayer() {
        return player;
    }
    
    private void stopZone() {

        for(int i=STAGESIZEY-zonelines;i<STAGESIZEY;i++) {
            for(int j=0;j<STAGESIZEX;j++) {
                turnToFallingBlock(j, i, 0.5);
            }
        }
        
        for(int i=0;i<STAGESIZEY;i++) {
            for(int j=0;j<STAGESIZEX;j++) {
                if(STAGESIZEY-zonelines-1-i>=0) {
                    stage[STAGESIZEY-1-i][j] = stage[STAGESIZEY-zonelines-1-i][j];
                }
            }
        }
            
        if(!Main.version.contains("1_8")) {
            player.sendTitle("", ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "" + zonelines + " LINE" + (zonelines==1?"":"S"), 20, 40, 20);
        }
        updateScore();

        for(int i=0;i<zonelines/2+1;i++) {
            player.playSound(player.getEyeLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1f, 0.5f);
        }
        
        sendGarbage(zonelines*2);
        zone = false;
        zonelines = 0;
        lines = 0;
                
    }
    
    public void startZone() {
        if(!zone) {
            zone = true;
        }
    }
    
    public void setGameOver() {
        gameover = true;
    }
    
    public boolean getGameOver() {
        return gameover;
    }
    
    //v1.1
    private void topOutCheck() {
        gl.currentPieceRotation = currentPieceRotation;
        gl.currentPiecePosition = currentPiecePosition;
        gl.stage = stage;
        
        if(gl.topOutCheck()) {
            if(!zone) {
                player.playSound(player.getEyeLocation(), SoundUtil.ORB_PICKUP, 1f, 1f);
                setGameOver();
            }else {
                stopZone();
            }
            return;
        }
    }
    
    //v0
    private void sendGarbage(int n) {
        player.sendMessage(garbo.toString());
        Main.inwhichroom.get(player).forwardGarbage(n, player);
        //todo
        for(int h=0;h<startingGarbageCap;h++) {
            if(!garbo.isEmpty()) {
                totalgarbage++;
                for(int i=0;i<STAGESIZEY-1;i++) {
                    for(int j=0;j<STAGESIZEX;j++) {
                        stage[i][j]=stage[i+1][j];
                    }
                }
                for(int j=0;j<STAGESIZEX;j++) {
                    if(j==well) {
                        stage[STAGESIZEY-1][j]=7;
                    }else{
                        stage[STAGESIZEY-1][j]=8;
                    }
                }
                
                garbo.set(0, garbo.get(0)-1);
                if(garbo.get(0)<=0) {
                    garbo.remove(0);
                    well=garbagegen.nextInt(STAGESIZEX);
                }
            }
        }
    }
    
    //v0
    public void receiveGarbage(int n) {
        garbo.add(n);
    }
    
    //v0
    private void putGarbage() {
        System.out.println("putGarbage()");
        for(int h=0;h<startingGarbageCap;h++) {
            if(!garbo.isEmpty()) {
                totalgarbage++;
                for(int i=0;i<STAGESIZEY-1;i++) {
                    for(int j=0;j<STAGESIZEX;j++) {
                        stage[i][j]=stage[i+1][j];
                    }
                }
                for(int j=0;j<STAGESIZEX;j++) {
                    if(j==well) {
                        stage[STAGESIZEY-1][j]=7;
                    }else{
                        stage[STAGESIZEY-1][j]=8;
                    }
                }
                
                garbo.set(0, garbo.get(0)-1);
                if(garbo.get(0)<=0) {
                    garbo.remove(0);
                    well=garbagegen.nextInt(STAGESIZEX);
                }
            }
        }
    }

    //v0
    private void spawnPiece() {
        currentPiecePosition = new Point(3, 20);
        currentPieceRotation = 0;
        gl.currentPiece = nextPieces.get(0);
        nextPieces.remove(0);
        
        spun=false;
        mini=false;
        counter = 0;
    }
    
    //v0 - urgent
    private void makeNextPiece() {
        if(nextPieces.size() <= 7) {
            ArrayList<Integer> bag = new ArrayList<Integer>();
            for(int i=0;i<7;i++) {
                bag.add(i);
            }
            Collections.shuffle(bag);
            nextPieces.addAll(bag);
        }
        
        spawnPiece();
        
        topOutCheck();
        
    }
    
    //v2
    private boolean collides(int x, int y, int rotation) {
        gl.stage = stage;
        return gl.collides(x, y, rotation);
    }
    
    //v0 - urgent
    public void initGame(long seed, long seed2) {

        garbo.clear();
        nextPieces.clear();
        gen=new Random(seed);
        garbagegen=new Random(seed2);
        well = garbagegen.nextInt(STAGESIZEX);
        
        if(!getGameOver()) {
            setGameOver();
        }
        
        coni=Math.max((int)Math.abs(m1x),(int)Math.abs(m1y));
        conj=Math.max((int)Math.abs(m2x),(int)Math.abs(m2y));
        conk=Math.max((int)Math.abs(m3x),(int)Math.abs(m3y));
        
        for(int y=0;y<STAGESIZEY;y++) {
            for(int x=0;x<STAGESIZEX;x++) {
                stage[y][x] = 7;
            }
        }
        
        spun=false;
        gameover=false;
        gl.held=false;
        
        combo=-1;
        score=0;
        gl.heldPiece=-1;
        b2b=-1;
        
        totallines = 0;
        totalblocks = 0;
        totalgarbage = 0;
        
        makeNextPiece();
        
        
        gameLoop();
        initScoreboard();
        player.getInventory().setHeldItemSlot(8);
        
        /*oldx=player.getLocation().getX();
        oldy=player.getLocation().getY();
        oldz=player.getLocation().getZ();
        */
        zone = false;
        zonelines = 0;
        
    }
    
    //v0
    /*
    private void tSpin() {
        int truth=0;
        boolean wall=false;
        if((currentPiecePosition.y<0 || STAGESIZEY<=currentPiecePosition.y) || (currentPiecePosition.x<0 || STAGESIZEX<=currentPiecePosition.x)) {
            truth++;
            wall=true;
        }else if(stage[currentPiecePosition.y][currentPiecePosition.x]!=7) {
            truth++;
        }
        
        if((currentPiecePosition.y<0 || STAGESIZEY<=currentPiecePosition.y) || (currentPiecePosition.x+2<0 || STAGESIZEX<=currentPiecePosition.x+2)) {
            truth++;
            wall=true;
        }else if(stage[currentPiecePosition.y][currentPiecePosition.x+2]!=7) {
            truth++;
        }
        
        if((currentPiecePosition.y+2<0 || STAGESIZEY<=currentPiecePosition.y+2) || (currentPiecePosition.x<0 || STAGESIZEX<=currentPiecePosition.x)) {
            truth++;
            wall=true;
        }else if(stage[currentPiecePosition.y+2][currentPiecePosition.x]!=7) {
            truth++;
        }
        
        if((currentPiecePosition.y+2<0 || STAGESIZEY<=currentPiecePosition.y+2) || (currentPiecePosition.x+2<0 || STAGESIZEX<=currentPiecePosition.x+2)) {
            truth++;
            wall=true;
        }else if(stage[currentPiecePosition.y+2][currentPiecePosition.x+2]!=7) {
            truth++;
        }
        
        if(truth>=3 && wall) {
            spun=true;
            mini=true;
            return;
        }
        
        if(truth>=3) {
            spun=true;
            mini=true;
            if(rotation==0) {
                if(stage[currentPiecePosition.y][currentPiecePosition.x]!=7 && stage[currentPiecePosition.y][currentPiecePosition.x+2]!=7) {
                    mini=false;
                }
            }else if(rotation==1) {
                if(stage[currentPiecePosition.y][currentPiecePosition.x+2]!=7 && stage[currentPiecePosition.y+2][currentPiecePosition.x+2]!=7) {
                    mini=false;
                }
            }else if(rotation==2) {
                if(stage[currentPiecePosition.y+2][currentPiecePosition.x+2]!=7 && stage[currentPiecePosition.y+2][currentPiecePosition.x]!=7) {
                    mini=false;
                }
            }else if(rotation==3) {
                if(stage[currentPiecePosition.y+2][currentPiecePosition.x]!=7 && stage[currentPiecePosition.y][currentPiecePosition.x]!=7) {
                    mini=false;
                }
            }
        }else{
            spun=false;
            mini=false;
        }
    }*/
    
    //v0
    private void updateScore() {
        if(!zone) {
            
            if((spun && lines>0) || lines==4) {
                b2b++;
            }else if(lines>0) {
                b2b=-1;
            }
            
            if(lines>0) {
                combo++;
            }else{
                putGarbage();
                combo=-1;
            }
            
            if(spun) {
                player.playSound(player.getEyeLocation(), SoundUtil.THUNDER, 1f, 0.75f);
                if(mini) {
                    switch(lines) {
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
                    switch(lines) {
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
                switch(lines) {
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
            
            
            if(combo>=0) {
                player.playSound(player.getEyeLocation(), SoundUtil.NOTE_HARP, 1f, (float)Math.pow(2,(combo*2-16)/(double)16));
                score+=combo*50;
            }
            
            if((totallines-totalgarbage)*STAGESIZEX+totalgarbage==totalblocks*4) {
                score+=3500;
                sendGarbage(STAGESIZEX);
            }
    
            sendTheTitle();
            sendTheScoreboard();
            
            ///sendgarbage
            if(lines>0) {
                int temp=0;
                if(spun==false) {
                    temp=lines-1;
                }else if(mini==true) {
                    if(lines==1) {
                        temp=4;
                    }else{
                        temp=6;
                    }
                }else{
                    if(lines==1) {
                        temp=5;
                    }else if(lines==2) {
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
        gl.held=false;
        
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
    
    //v0
    private void hardDropPiece() {
        int lines=0;
        while(!collides(currentPiecePosition.x, currentPiecePosition.y+lines+1, currentPieceRotation)) {
            lines++;
        }
        movePiece(currentPiecePosition.x, currentPiecePosition.y+lines, currentPieceRotation);
        score+=lines*2;
        placePiece();
    }
    
    //v2
    private void movePiece(int x, int y, int r) {
        gl.currentPieceRotation = currentPieceRotation;
        gl.currentPiecePosition = currentPiecePosition;
        gl.movePiece(x, y, r);
        
        currentPieceRotation = gl.currentPieceRotation;
    }
    
    //v2
    private boolean holdPiece() {
        gl.currentPiecePosition = currentPiecePosition;
        gl.currentPieceRotation = currentPieceRotation;
        if(gl.holdPiece()) {
            currentPiecePosition = gl.currentPiecePosition;
            currentPieceRotation = gl.currentPieceRotation;
            return true;
        }
        return false;
    }
    
    //v2
    private void rotatePiece(int d) {
        gl.currentPieceRotation = currentPieceRotation;
        gl.currentPiecePosition = currentPiecePosition;
        gl.rotatePiece(d);
        
        currentPieceRotation = gl.currentPieceRotation;
    }
    
    //resudual zone code
    /*
    private void clearRows() {
        int highestRow = STAGESIZEY;
        int linesCleared = 0;
        ArrayList<Integer> temp = new ArrayList<Integer>();
        
        //find highest row
        for(int i=0;i<STAGESIZEY;i++) {
            for(int j=0;j<STAGESIZEX;j++) {
                if(stage[i][j]!=7) {
                    highestRow = i;
                    i = STAGESIZEY;
                    break;
                }
            }
        }
        
        temp.add(highestRow);
        
        for(int i=highestRow, j;i<STAGESIZEY;i++) {
            for(j=0;j<STAGESIZEX;j++) {
                if(stage[i][j]==7 || stage[i][j]==16) {
                    break;
                }
            }

            if(j==STAGESIZEX) {
                linesCleared++;
                temp.add(i);
                if(!zone) {
                    for(int k=0;k<STAGESIZEX;k++) {
                        turnToFallingBlock(k, i, 0.3);
                    }
                }
            }
        }
        
        temp.add(STAGESIZEY-zonelines);
        //player.sendMessage(temp.toString());
        
        if(zone) {
            zonelines += linesCleared;
        }else {
            lines = linesCleared;
        }
        final int lc = linesCleared;
        final int hr = highestRow;
        
        if(zone) {
            for(int i=1;i<temp.size()-1;i++) {
                for(int j=temp.get(1);j<temp.get(i+1);j++) {
                    for(int k=0;k<STAGESIZEX;k++) {
                        //if(k==0)
                            //player.sendMessage("i="+i+" j="+j+"stage["+j+"]=stage["+(j+i)+"]");
                            
                        if(j+i<STAGESIZEY) {
                            stage[j][k] = stage[j+i][k];
                        }
                    }
                }
            }
                
            for(int i=temp.get(temp.size()-1)-lc;i<temp.get(temp.size()-1);i++) {
                for(int j=0;j<STAGESIZEX;j++) {
                    stage[i][j] = 16;
                }
            }
        }else {
            for(int i=1;i<temp.size()-1;i++) {
                for(int j=temp.get(temp.size()-i-1)+i-1;j>temp.get(temp.size()-i-2)+i-1;j--) {
                    for(int k=0;k<STAGESIZEX;k++) {
                        //if(k==0)
                            //player.sendMessage("i="+i+" j="+j+" stage["+j+"]=stage["+(j-i)+"]");
                            
                        if(j<STAGESIZEY) {
                            stage[j][k] = stage[j-i][k];
                        }
                    }
                }
            }
            
            for(int i=hr;i<hr+lc;i++) {

                //player.sendMessage("stage["+i+"]=7");
                for(int j=0;j<STAGESIZEX;j++) {
                    stage[i][j] = 7;
                }
            }
        }


        totallines+=lc;
        totalblocks+=1;
        updateScore();
        if(zone && lc>0) {
            for(int i=0;i<20*zonelines;i++)
            player.playSound(player.getEyeLocation(), SoundUtil.NOTE_PLING, 1f, (float)Math.pow(2,(zonelines*2-16)/(double)16));
        }
            
    }
    */

    //v2
    private void placePiece() {
        gl.nextPieces = nextPieces;
        gl.stage = stage;
        gl.currentPiecePosition = currentPiecePosition;
        gl.currentPieceRotation = currentPieceRotation;
        gl.placePiece();
        stage = gl.stage;
        currentPiecePosition = gl.currentPiecePosition;
        currentPieceRotation = gl.currentPieceRotation;
    }
    
    double maxvelocity=0;
    long startTime;
    boolean moving=false;
    String direction;
    boolean singlemove;
    
   	//unique functions
    
    private void gameLoop() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if(counter>=100) {
                    if(!collides(currentPiecePosition.x, currentPiecePosition.y+1, currentPieceRotation)) {
                        movePiece(currentPiecePosition.x, currentPiecePosition.y+1, currentPieceRotation);
                    }else{
                        placePiece();
                    }

                    counter = 0;
                }

                counter+=(totallines+4)/4;
                
                if(gameover) {
                    
                    boolean ot = transparent;
                    transparent = true;
                    for(int i=0;i<STAGESIZEY;i++) {
                        for(int j=0;j<STAGESIZEX;j++) {
                            colPrintNewRender(j, i, 7);
                        }
                    }
                    transparent = ot;
                    
                    for(int i=STAGESIZEY-VISIBLEROWS;i<STAGESIZEY;i++) {
                        for(int j=0;j<STAGESIZEX;j++) {
                            turnToFallingBlock(j, i, 1);
                        }
                    }     
                                     
                    this.cancel();
                    if(Main.roommap.containsKey(Main.inwhichroom.get(player).id)) {
                        Main.inwhichroom.get(player).playersalive--;
                        if(Main.inwhichroom.get(player).playersalive<=1) {
                            Main.inwhichroom.get(player).stopRoom();
                        }
                    }
                }
                
                board.set("TIME "+looptick, 0);
                looptick++;
                render();
    
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
   	
    @SuppressWarnings("deprecation")
    private void turnToFallingBlock(int x, int y, double d) {
        if(ULTRAGRAPHICS == true) {
            int tex, tey, tez;
            ItemStack blocks[] = Blocks.blocks;
            int color = stage[y][x];
            for(int i=0;i<(coni!=0?coni:thickness);i++) {
                tex = gx+(int)(x*m1x)+(int)(y*m1y)+i;
                for(int j=0;j<(conj!=0?conj:thickness);j++) {
                    tey = gy+(int)(x*m2x)+(int)(y*m2y)+j;
                    for(int k=0;k<(conk!=0?conk:thickness);k++) {
                        tez = gz+(int)(x*m3x)+(int)(y*m3y)+k;
                        FallingBlock lol = world.spawnFallingBlock(new Location(world, tex, tey, tez), blocks[color].getType(), blocks[color].getData().getData());
                        lol.setVelocity(new Vector(d*(2-Math.random()*4),d*(5-Math.random()*10),d*(2-Math.random()*4)));
                        lol.setDropItem(false);
                        lol.addScoreboardTag("sand");
                    }
                }
            }
        }
    }
    
    private void initScoreboard() {
        board=Netherboard.instance().createBoard(player, "Stats");
        
        board.clear();
        
        board.set(" ", 5);
        board.set("Lines: "+totallines, 4);
        board.set("Pieces: "+totalblocks, 3);
        board.set("Score: "+score, 2);
        board.set("", 1);
    }
    
    private void sendTheScoreboard() {
        if(b2b>0) {
            board.set("§6§lB2B x"+b2b, 1);
        }else{
            board.set("", 1);
        }
        
        if(combo>0) {
            board.set("COMBO "+combo, 5);
        }else{
            board.set(" ", 5);
        }
        
        board.set("Lines: "+totallines, 4);
        board.set("Pieces: "+totalblocks, 3);
        board.set("Score: "+score, 2);
    }
    
    private void sendTheTitle() {
        if(!Main.version.contains("1_8")) {
            if(!zone) {
                String s1="";
                String s3="";
                
                if(spun) {
                    if(mini) {
                        s3="§5t-spin§r";
                    }else{
                        s3="§5T-SPIN§r";
                    }
                }
                
                if(lines==1) {
                    s1="SINGLE";
                }else if(lines==2) {
                    s1="DOUBLE";
                }else if(lines==3) {
                    s1="TRIPLE";
                }else if(lines==4) {
                    s1="QUAD";
                }
                
                if(lines==0 && spun) {
                    s1=" ";
                }
                
                if((totallines-totalgarbage)*STAGESIZEX+totalgarbage==totalblocks*4) {
                    player.sendTitle("", ChatColor.GOLD + "" + ChatColor.BOLD + "ALL CLEAR", 20, 40, 20);
                }
                
                //dont kill old title if its empty
                if(s1!="") {
                s1=s3+" "+s1;
                
                    //Main.functions.sendTitle(player, s1, s2, 0, 20, 10);
                    
                    //player.sendTitle(s1, s2, 0, 20, 10);
    
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new ComponentBuilder(s1).create());
                }
            }
        }
    }
    
    public void userInput(String input) {
        switch(input) {
        case "y":
            rotatePiece(-1);
            counter=0;
            break;
        case "x":
            rotatePiece(+1);
            counter=0;
            break;
        case "c":
            if(holdPiece()==true) {
                counter=0;   
            }else {
                player.playSound(player.getEyeLocation(), SoundUtil.VILLAGER_NO, 1f, 1f);
            }
            break;
            
        case "left":
            if(!collides(currentPiecePosition.x-1, currentPiecePosition.y, currentPieceRotation)) {
                movePiece(currentPiecePosition.x-1, currentPiecePosition.y, currentPieceRotation);
                counter=0;
            }
            break;
        case "right":
            if(!collides(currentPiecePosition.x+1, currentPiecePosition.y, currentPieceRotation)) {
                movePiece(currentPiecePosition.x+1, currentPiecePosition.y, currentPieceRotation);
                counter=0;
            }
            break;
            
        case "up":
            rotatePiece(+2);
            counter=0;
            break;
        case "down":
            if(!collides(currentPiecePosition.x, currentPiecePosition.y+1, currentPieceRotation)) {
                movePiece(currentPiecePosition.x, currentPiecePosition.y+1, currentPieceRotation);
                counter=0;
                score+=1;
                sendTheScoreboard();
            }
            break;
        
        case "space":
            hardDropPiece();
            break;
        case "l":
            gameover=true;
            break;
        case "instant":
            int temp = currentPiecePosition.y;
            while(!collides(currentPiecePosition.x, temp+1, currentPieceRotation)) {
                temp++;
            }
            movePiece(currentPiecePosition.x, temp, currentPieceRotation);
            break;
            
        default:
            System.out.println("wee woo wee woo");
        }
    }
   	
   	//rendering functions
   	
    private void printSingleBlock(int x, int y, int z, int color) {
        if(color==7 && transparent) {
            Block b=world.getBlockAt(x, y, z);
            for(Player player: Main.inwhichroom.get(player).playerlist) {
                SendBlockChangeCustom.sendBlockChangeCustom(player, new Location(world, x, y, z), b);
            }
            return;
        }
        
        for(Player player: Main.inwhichroom.get(player).playerlist) {
            SendBlockChangeCustom.sendBlockChangeCustom(player, new Location(world, x, y, z), color);
        }
    }
   	
   	private void colPrintNewRender(float x, float y, int color) {
        int tex, tey, tez;
        if(y>=STAGESIZEY-VISIBLEROWS) {
            for(int i=0;i<(coni!=0?coni:thickness);i++) {
                tex = gx+(int)Math.floor(x*m1x)+(int)Math.floor(y*m1y)+i;
                for(int j=0;j<(conj!=0?conj:thickness);j++) {
                    tey = gy+(int)Math.floor(x*m2x)+(int)Math.floor(y*m2y)+j;
                    for(int k=0;k<(conk!=0?conk:thickness);k++) {
                        tez = gz+(int)Math.floor(x*m3x)+(int)Math.floor(y*m3y)+k;
                        printSingleBlock(tex, tey, tez, color);
                        //debug
                        //player.sendMessage("i="+i+",j="+j+",k="+k+",tex="+tex+",tey="+tey+",tez="+tez+";");
                    }
                }
            }
        }
    }
   	
   	private void printStaticPieceNewRender(int x, int y, int block) {
        for(int i=0;i<4;i++) {
            for(int j=0;j<4;j++) {
                colPrintNewRender(j+x, i+y, 7);
            }
        }
        
        if(block != -1) {
            for(Point point: gl.pieces[block][0]) {
                switch(block) {
                case 2:
                    colPrintNewRender(point.x+x+1, point.y+y+1, block);
                    break;
                case 0:
                case 1:
                case 3:
                case 5:
                case 6:
                    ///something wrong
                    colPrintNewRender(point.x+x+0.5f, point.y+y+1, block);
                    break;
                case 4:
                    colPrintNewRender(point.x+x, point.y+y+0.5f, block);
                    break;
                }
            }
        }
    }
   	
   	public void destroyTable() {
        boolean ot = transparent;
        transparent = true;
        for(int i=0;i<STAGESIZEY;i++) {
            for(int j=0;j<STAGESIZEX;j++) {
                colPrintNewRender(j, i, 7);
            }
        }
        transparent = ot;
    }
    
    public void moveTable(int x, int y, int z) {
        boolean ot = transparent;
        transparent = true;
        for(int i=0;i<STAGESIZEY;i++) {
            for(int j=0;j<STAGESIZEX;j++) {
                colPrintNewRender(j, i, 7);
            }
        }
        gx = x;
        gy = y;
        gz = z;
        for(int i=0;i<STAGESIZEY;i++) {
            for(int j=0;j<STAGESIZEX;j++) {
                colPrintNewRender(j, i, 16);
            }
        }
        transparent = ot;
    }
    
    public void rotateTable(String input) {
        boolean ot = transparent;
        transparent = true;
        for(int i=0;i<STAGESIZEY;i++) {
            for(int j=0;j<STAGESIZEX;j++) {
                colPrintNewRender(j, i, 7);
            }
        }
        
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
        
        for(int i=0;i<STAGESIZEY;i++) {
            for(int j=0;j<STAGESIZEX;j++) {
                colPrintNewRender(j, i, 16);
            }
        }
        transparent = ot;
    }
   	
   	private void render() {
   	    //print board
   	    for(int i=0;i<STAGESIZEY;i++) {
   	        for(int j=0;j<STAGESIZEX;j++) {
   	            colPrintNewRender(j, i, stage[i][j]);
   	        }
   	    }
   	    
   	    //print next queue
        for(int i=0;i<gl.next_blocks;i++) {
            printStaticPieceNewRender(STAGESIZEX+3, STAGESIZEY/2+i*4, nextPieces.get(i));
        }
        
        //print held piece
        printStaticPieceNewRender(-7, STAGESIZEY/2, gl.heldPiece);
        
        //print ghost
        int ghosty=currentPiecePosition.y;
        while(!collides(currentPiecePosition.x, ghosty+1, currentPieceRotation)) {
            ghosty++;
        }

        for(Point point: gl.pieces[gl.currentPiece][currentPieceRotation]) {
            colPrintNewRender(point.x+currentPiecePosition.x, point.y+ghosty, 9+gl.currentPiece);
        }
        
        //print current piece
        for(Point point: gl.pieces[gl.currentPiece][currentPieceRotation]) {
            colPrintNewRender(point.x + currentPiecePosition.x, point.y + currentPiecePosition.y, gl.currentPiece);
        }
        
        //print garbage meter
        int total=0;
        for(int num: garbo) {
            total+=num;
        }
        
        for(int i=0;i<STAGESIZEY/2;i++) {
            colPrintNewRender(-2, STAGESIZEY-1-i, 7);
        }
        
        for(int i=0;i<total;i++) {
            colPrintNewRender(-2, STAGESIZEY-1-i%(STAGESIZEY/2), (i/(STAGESIZEY/2))%7);
        }
   	    
   	}
}
