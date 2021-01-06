package tetr.minecraft;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Location;
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
import tetr.minecraft.functions.SendBlockChangeCustom;
import tetr.shared.GameLogic;

public class Table {
    
    GameLogic gl = new GameLogic();

    public static boolean transparent=false;
    boolean destroying = false;
    
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
    
    private int lins;
    
    public boolean ULTRAGRAPHICS = true;
    
    //if counter > gravity^-1  fall
    private int counter = 0;//gravity variable
    private double startingGravity = 20;
    private int gravityIncreaseDelay = 600;
    private double gravityIncrease = 1 / 20;
    private int lockDelay = 20;
    private int timesMoved = 0;
    private static final int MAXIMUMMOVES = 15;
    
    private boolean spun = false;//tspin
    private boolean mini = false;
    
    //garbage
    private ArrayList<Integer> garbo = new ArrayList<Integer>();
    private Random garbagegen;
    private int well;
    private double startingGarbageCap = 4;
    private double garbageCapIncreaseDelay = 1200;
    private double garbageCapIncrease = 1 / 20;
    
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
            moveTable(location.getBlockX()-gl.STAGESIZEY, location.getBlockY()+gl.STAGESIZEY-gl.VISIBLEROWS/2, location.getBlockZ()+gl.STAGESIZEX/2);
        }else if(135<=yaw && yaw<225) {
            moveTable(location.getBlockX()-gl.STAGESIZEX/2, location.getBlockY()+gl.STAGESIZEY-gl.VISIBLEROWS/2, location.getBlockZ()-gl.STAGESIZEY);
        }else if(225<=yaw && yaw<315) {
            rotateTable("Y");
            moveTable(location.getBlockX()+gl.STAGESIZEY, location.getBlockY()+gl.STAGESIZEY-gl.VISIBLEROWS/2, location.getBlockZ()-gl.STAGESIZEX/2);
        }else if((315<=yaw && yaw<360) || (0<=yaw && yaw<45)) {
            rotateTable("Y");
            rotateTable("Y");
            moveTable(location.getBlockX()+gl.STAGESIZEX/2, location.getBlockY()+gl.STAGESIZEY-gl.VISIBLEROWS/2, location.getBlockZ()+gl.STAGESIZEY);
        }
        gl.gameover=true;
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
    
    /*
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
        //updateScore();

        for(int i=0;i<zonelines/2+1;i++) {
            player.playSound(player.getEyeLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1f, 0.5f);
        }
        
        sendGarbage(zonelines*2);
        zone = false;
        zonelines = 0;
        lines = 0;
                
    }
    */
    
    public void startZone() {
        if(!zone) {
            zone = true;
        }
    }
    
    public void setGameOver() {
        gl.gameover = true;
    }
    
    public boolean getGameOver() {
        return gl.gameover;
    }

    //v0 - delayed
    private void sendGarbage(int n) {
        player.sendMessage(garbo.toString());
        Main.inwhichroom.get(player).forwardGarbage(n, player);
        //todo
        for(int h=0;h<startingGarbageCap;h++) {
            if(!garbo.isEmpty()) {
                gl.totalGarbageReceived++;
                for(int i=0;i<gl.STAGESIZEY-1;i++) {
                    for(int j=0;j<gl.STAGESIZEX;j++) {
                        gl.stage[i][j]=gl.stage[i+1][j];
                    }
                }
                for(int j=0;j<gl.STAGESIZEX;j++) {
                    if(j==well) {
                        gl.stage[gl.STAGESIZEY-1][j]=7;
                    }else{
                        gl.stage[gl.STAGESIZEY-1][j]=8;
                    }
                }
                
                garbo.set(0, garbo.get(0)-1);
                if(garbo.get(0)<=0) {
                    garbo.remove(0);
                    well=garbagegen.nextInt(gl.STAGESIZEX);
                }
            }
        }
    }
    
    //v0 - delayed
    public void receiveGarbage(int n) {
        garbo.add(n);
    }
    
    //v0 - delayed
    private void putGarbage() {
        System.out.println("putGarbage()");
        for(int h=0;h<startingGarbageCap;h++) {
            if(!garbo.isEmpty()) {
                gl.totalGarbageReceived++;
                for(int i=0;i<gl.STAGESIZEY-1;i++) {
                    for(int j=0;j<gl.STAGESIZEX;j++) {
                        gl.stage[i][j]=gl.stage[i+1][j];
                    }
                }
                for(int j=0;j<gl.STAGESIZEX;j++) {
                    if(j==well) {
                        gl.stage[gl.STAGESIZEY-1][j]=7;
                    }else{
                        gl.stage[gl.STAGESIZEY-1][j]=8;
                    }
                }
                
                garbo.set(0, garbo.get(0)-1);
                if(garbo.get(0)<=0) {
                    garbo.remove(0);
                    well=garbagegen.nextInt(gl.STAGESIZEX);
                }
            }
        }
    }

    //v3
    private void makeNextPiece() {
        gl.makeNextPiece();
    }
    
    //v3
    private boolean collides(int x, int y, int rotation) {
        return gl.collides(x, y, rotation);
    }
    
    //v1
    public void initGame(long seed, long seed2) {
        garbo.clear();
        gen=new Random(seed);
        garbagegen=new Random(seed2);
        well = garbagegen.nextInt(gl.STAGESIZEX);
        
        coni=Math.max(Math.abs(m1x),Math.abs(m1y));
        conj=Math.max(Math.abs(m2x),Math.abs(m2y));
        conk=Math.max(Math.abs(m3x),Math.abs(m3y));
        
        spun=false;
        
        player.getInventory().setHeldItemSlot(8);
        
        zone = false;
        zonelines = 0;
        
        looptick = 0;
        
        gl.initGame();
        initScoreboard();
        gameLoop();
    }
    
    //v0 - delayed
    private void tSpin() {
        /*
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
        */
    }
    
    //v0 - delayed
    private void updateScore() {
        /*
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
        */
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
    
    //v3
    private void hardDropPiece() {
        gl.hardDropPiece();
    }
    
    //v3
    private boolean movePiece(int x, int y, int r) {
        return gl.movePiece(x, y, r);
    }
    
    //v3
    private boolean holdPiece() {
        return gl.holdPiece();
    }
    
    //v3
    private void rotatePiece(int d) {
        gl.rotatePiece(d);
    }
    
    //v0 - delayed
    private void clearRows() {
        /*
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
            */
    }
    

    //v3
    private void placePiece() {
        gl.placePiece();
    }
    
    double maxvelocity=0;
    long startTime;
    boolean moving=false;
    String direction;
    boolean singlemove;
    
   	//unique functions
    
    private void gameLoop() {
        //thread unsafe code
        new BukkitRunnable() {
            @Override
            public void run() {
                if(destroying) {
                    this.cancel();
                }else if(gl.gameover) {
                    boolean ot = transparent;
                    transparent = true;
                    for(int i=0;i<gl.STAGESIZEY;i++) {
                        for(int j=0;j<gl.STAGESIZEX;j++) {
                            colPrintNewRender(j, i, 7);
                        }
                    }
                    transparent = ot;
                    
                    for(int i=gl.STAGESIZEY-gl.VISIBLEROWS;i<gl.STAGESIZEY;i++) {
                        for(int j=0;j<gl.STAGESIZEX;j++) {
                            turnToFallingBlock(j, i, 1);
                        }
                    }
                    
                    this.cancel();
                }else {
                    looptick++;
                    render();
                }
            }
        }.runTaskTimer(Main.plugin, 0, 1);
        
        //thread safe code
        new Thread() {
            @Override
            public void run() {
                while(!gl.gameover) {
                    if(counter>=100) {
                        if(!movePiece(gl.currentPiecePosition.x, gl.currentPiecePosition.y+1, gl.currentPieceRotation)){
                            placePiece();
                        }else {
                            counter = 0;   
                        }
                    }
    
                    counter+=(gl.totalLinesCleared+4)/4;
                    
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                if(Main.inwhichroom.get(player) != null) {
                    if(Main.roommap.containsKey(Main.inwhichroom.get(player).id)) {
                        Main.inwhichroom.get(player).playersalive--;
                        if(Main.inwhichroom.get(player).playersalive<=1) {
                            Main.inwhichroom.get(player).stopRoom();
                        }
                    }
                }
            }
        }.start();
    }
   	
    @SuppressWarnings("deprecation")
    private void turnToFallingBlock(int x, int y, double d) {
        if(ULTRAGRAPHICS == true) {
            int tex, tey, tez;
            ItemStack blocks[] = Blocks.blocks;
            int color = gl.stage[y][x];
            for(int i=0;i<(coni!=0?coni:thickness);i++) {
                tex = gx+x*m1x+y*m1y+i;
                for(int j=0;j<(conj!=0?conj:thickness);j++) {
                    tey = gy+x*m2x+y*m2y+j;
                    for(int k=0;k<(conk!=0?conk:thickness);k++) {
                        tez = gz+x*m3x+y*m3y+k;
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
    }
    
    private void sendScoreboard() {
        
        if(gl.combo>0) {
            board.set("Combo: " + gl.combo, 6);
        }else{
            board.set("     ", 6);
        }
        
        board.set("Lines: " + gl.totalLinesCleared, 5);
        board.set("Pieces: " + gl.totalPiecesPlaced, 4);
        board.set("Score: " + gl.score, 3);
        
        if(gl.b2b>0) {
            board.set("Back to back: " + gl.b2b, 2);
        }else{
            board.set(" ", 2);
        }

        board.set("Time: " + looptick, 1);
        board.set("Counter: " + counter, 0);
    }
    
    private void sendTitleAndActionBar() {
        /*
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
                
                if((gl.totalLinesCleared-gl.totalGarbageReceived)*gl.STAGESIZEX+gl.totalGarbageReceived==gl.totalPiecesPlaced*4) {
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
        */
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
            if(!collides(gl.currentPiecePosition.x-1, gl.currentPiecePosition.y, gl.currentPieceRotation)) {
                movePiece(gl.currentPiecePosition.x-1, gl.currentPiecePosition.y, gl.currentPieceRotation);
                counter=0;
            }
            break;
        case "right":
            if(!collides(gl.currentPiecePosition.x+1, gl.currentPiecePosition.y, gl.currentPieceRotation)) {
                movePiece(gl.currentPiecePosition.x+1, gl.currentPiecePosition.y, gl.currentPieceRotation);
                counter=0;
            }
            break;
            
        case "up":
            rotatePiece(+2);
            counter=0;
            break;
        case "down":
            if(!collides(gl.currentPiecePosition.x, gl.currentPiecePosition.y+1, gl.currentPieceRotation)) {
                movePiece(gl.currentPiecePosition.x, gl.currentPiecePosition.y+1, gl.currentPieceRotation);
                counter=0;
                gl.score+=1;
            }
            break;
        
        case "space":
            hardDropPiece();
            break;
        case "l":
            gl.gameover=true;
            break;
        case "instant":
            int temp = gl.currentPiecePosition.y;
            while(!collides(gl.currentPiecePosition.x, temp+1, gl.currentPieceRotation)) {
                temp++;
            }
            movePiece(gl.currentPiecePosition.x, temp, gl.currentPieceRotation);
            break;
            
        default:
            System.out.println("wee woo wee woo");
        }
    }
   	
    private void debug(String s) {
        System.out.println(s);
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
        if(y>=gl.STAGESIZEY-gl.VISIBLEROWS) {
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
        for(int i=0;i<gl.STAGESIZEY;i++) {
            for(int j=0;j<gl.STAGESIZEX;j++) {
                colPrintNewRender(j, i, 7);
            }
        }
        transparent = ot;
        gl.gameover = true;
        board.delete();
        board = null;
        destroying = true;
    }
    
    public void moveTable(int x, int y, int z) {
        boolean ot = transparent;
        transparent = true;
        for(int i=0;i<gl.STAGESIZEY;i++) {
            for(int j=0;j<gl.STAGESIZEX;j++) {
                colPrintNewRender(j, i, 7);
            }
        }
        gx = x;
        gy = y;
        gz = z;
        for(int i=0;i<gl.STAGESIZEY;i++) {
            for(int j=0;j<gl.STAGESIZEX;j++) {
                colPrintNewRender(j, i, 16);
            }
        }
        transparent = ot;
    }
    
    public void rotateTable(String input) {
        boolean ot = transparent;
        transparent = true;
        for(int i=0;i<gl.STAGESIZEY;i++) {
            for(int j=0;j<gl.STAGESIZEX;j++) {
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
        
        for(int i=0;i<gl.STAGESIZEY;i++) {
            for(int j=0;j<gl.STAGESIZEX;j++) {
                colPrintNewRender(j, i, 16);
            }
        }
        transparent = ot;
    }
   	
   	private void render() {
   	    //print board
   	    for(int i=0;i<gl.STAGESIZEY;i++) {
   	        for(int j=0;j<gl.STAGESIZEX;j++) {
   	            colPrintNewRender(j, i, gl.stage[i][j]);
   	        }
   	    }
   	    
   	    //print next queue
        for(int i=0;i<gl.next_blocks;i++) {
            printStaticPieceNewRender(gl.STAGESIZEX+3, gl.STAGESIZEY/2+i*4, gl.nextPieces.get(i));
        }
        
        //print held piece
        printStaticPieceNewRender(-7, gl.STAGESIZEY/2, gl.heldPiece);
        
        //print ghost
        int ghosty=gl.currentPiecePosition.y;
        while(!collides(gl.currentPiecePosition.x, ghosty+1, gl.currentPieceRotation)) {
            ghosty++;
        }

        for(Point point: gl.pieces[gl.currentPiece][gl.currentPieceRotation]) {
            colPrintNewRender(point.x+gl.currentPiecePosition.x, point.y+ghosty, 9+gl.currentPiece);
        }
        
        //print current piece
        for(Point point: gl.pieces[gl.currentPiece][gl.currentPieceRotation]) {
            colPrintNewRender(point.x + gl.currentPiecePosition.x, point.y + gl.currentPiecePosition.y, gl.currentPiece);
        }
        
        //print garbage meter
        int total=0;
        for(int num: garbo) {
            total+=num;
        }
        
        for(int i=0;i<gl.STAGESIZEY/2;i++) {
            colPrintNewRender(-2, gl.STAGESIZEY-1-i, 7);
        }
        
        for(int i=0;i<total;i++) {
            colPrintNewRender(-2, gl.STAGESIZEY-1-i%(gl.STAGESIZEY/2), (i/(gl.STAGESIZEY/2))%7);
        }
        
        //send scoreboard

        sendScoreboard();
   	    
   	}
}
