package tetr.minecraft;

import java.awt.Point;

import org.bukkit.ChatColor;
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
import tetr.minecraft.constants.Blocks;
import tetr.minecraft.xseries.XSound;
import tetr.shared.GameLogic;

public class Table {

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
    
    public int[][] lastStageState = new int[GameLogic.STAGESIZEY][GameLogic.STAGESIZEX];
    
    public boolean ULTRAGRAPHICS = true;
    
    public GameLogic gl;
    
    Table(Player p) {
        gl = new GameLogic(p);
        player=p;
        world=p.getWorld();
        Location location=player.getLocation();
        float yaw = player.getLocation().getYaw();
        yaw = (yaw % 360 + 360) % 360;
        if(45<=yaw && yaw<135) {
            rotateTable("Y");
            rotateTable("Y");
            rotateTable("Y");
            moveTable(location.getBlockX()-GameLogic.STAGESIZEY, location.getBlockY()+GameLogic.STAGESIZEY-GameLogic.VISIBLEROWS/2, location.getBlockZ()+GameLogic.STAGESIZEX/2);
        }else if(135<=yaw && yaw<225) {
            moveTable(location.getBlockX()-GameLogic.STAGESIZEX/2, location.getBlockY()+GameLogic.STAGESIZEY-GameLogic.VISIBLEROWS/2, location.getBlockZ()-GameLogic.STAGESIZEY);
        }else if(225<=yaw && yaw<315) {
            rotateTable("Y");
            moveTable(location.getBlockX()+GameLogic.STAGESIZEY, location.getBlockY()+GameLogic.STAGESIZEY-GameLogic.VISIBLEROWS/2, location.getBlockZ()-GameLogic.STAGESIZEX/2);
        }else if((315<=yaw && yaw<360) || (0<=yaw && yaw<45)) {
            rotateTable("Y");
            rotateTable("Y");
            moveTable(location.getBlockX()+GameLogic.STAGESIZEX/2, location.getBlockY()+GameLogic.STAGESIZEY-GameLogic.VISIBLEROWS/2, location.getBlockZ()+GameLogic.STAGESIZEY);
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
    
    public Player getPlayer() {
        return player;
    }
    
    public void setGameOver(boolean value) {
        gl.gameover = value;
    }
    
    public void initGame(long seed, long seed2) {
        coni=Math.max(Math.abs(m1x),Math.abs(m1y));
        conj=Math.max(Math.abs(m2x),Math.abs(m2y));
        conk=Math.max(Math.abs(m3x),Math.abs(m3y));
        
        player.getInventory().setHeldItemSlot(8);
        
        looptick = 0;
        
        gl.initGame();
        initScoreboard();
        gameLoop();
    }

    /*
    scoring:
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
                    for(int i=0;i<GameLogic.STAGESIZEY;i++) {
                        for(int j=0;j<GameLogic.STAGESIZEX;j++) {
                            colPrintNewRender(j, i, 7);
                        }
                    }
                    transparent = ot;
                    
                    for(int i=GameLogic.STAGESIZEY-GameLogic.VISIBLEROWS;i<GameLogic.STAGESIZEY;i++) {
                        for(int j=0;j<GameLogic.STAGESIZEX;j++) {
                            turnToFallingBlock(j, i, 1);
                        }
                    }
                    
                    this.cancel();
                }else {
                    render();
                    looptick++;
                }
            }
        }.runTaskTimer(Main.plugin, 0, 1);
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
                        //lol.addScoreboardTag("sand");
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
            board.set("Combo: " + gl.combo, 7);
        }else{
            board.set("     ", 6);
        }

        board.set("Garbage received: " + gl.totalGarbageReceived, 6);
        board.set("Lines: " + gl.totalLinesCleared, 5);
        board.set("Pieces: " + gl.totalPiecesPlaced, 4);
        board.set("Score: " + gl.score, 3);
        
        if(gl.b2b>0) {
            board.set("Back to back: " + gl.b2b, 2);
        }else{
            board.set(" ", 2);
        }

        board.set("Time: " + looptick, 1);
        board.set("gl.counter: " + gl.counter, 0);
    }
    
    public boolean userInput(String input) {
        if(!gl.gameover) {
            switch(input) {
            case "y":
                if(gl.rotatePiece(-1)) {
                    gl.counter=0;
                }
                break;
            case "x":
                if(gl.rotatePiece(+1)) {
                    gl.counter=0;
                }
                break;
            case "c":
                if(gl.holdPiece()) {
                    gl.counter=0;   
                }else {
                    player.playSound(player.getEyeLocation(), XSound.ENTITY_SPLASH_POTION_BREAK.parseSound(), 1f, 1f);
                }
                break;
                
            case "left":
                if(gl.movePiece(gl.currentPiecePosition.x-1, gl.currentPiecePosition.y, gl.currentPieceRotation)) {
                    gl.counter=0;
                }
                break;
            case "right":
                if(gl.movePiece(gl.currentPiecePosition.x+1, gl.currentPiecePosition.y, gl.currentPieceRotation)) {
                    gl.counter=0;
                }
                break;
                
            case "up":
                if(gl.rotatePiece(+2)) {
                    gl.counter=0;
                }
                break;
            case "down":
                if(gl.movePiece(gl.currentPiecePosition.x, gl.currentPiecePosition.y+1, gl.currentPieceRotation)) {
                    gl.counter=0;
                    gl.score+=1;
                }
                break;
            
            case "space":
                gl.hardDropPiece();
                break;
            case "l":
                gl.gameover=true;
                break;
            case "instant":
                int temp = gl.currentPiecePosition.y;
                while(!gl.collides(gl.currentPiecePosition.x, temp+1, gl.currentPieceRotation)) {
                    temp++;
                }
                gl.movePiece(gl.currentPiecePosition.x, temp, gl.currentPieceRotation);
                break;
            case "shift":
                gl.startZone();
            default:
                System.out.println("wee woo wee woo");
            }
        }
        return false;
    }
   	
    @SuppressWarnings("unused")
    private void debug(String s) {
        System.out.println(s);
    }
    
   	//rendering functions
   	
    private void printSingleBlock(int x, int y, int z, int color) {
        if(color==7 && transparent) {
            Block b=world.getBlockAt(x, y, z);
            for(Player player: Main.inwhichroom.get(player).playerlist) {
                Main.functions.sendBlockChangeCustom(player, new Location(world, x, y, z), b);
            }
            return;
        }
        
        for(Player player: Main.inwhichroom.get(player).playerlist) {
            Main.functions.sendBlockChangeCustom(player, new Location(world, x, y, z), color);
        }
    }
   	
   	private void colPrintNewRender(float x, float y, int color) {
        int tex, tey, tez;
        if(y>=GameLogic.STAGESIZEY-GameLogic.VISIBLEROWS) {
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
                colPrintNewRender(x + point.x, y + point.y, block);
            }
        }
    }
   	
   	public void destroyTable() {
   	    boolean ot = transparent;
        transparent = true;
        for(int i=0;i<GameLogic.STAGESIZEY;i++) {
            for(int j=0;j<GameLogic.STAGESIZEX;j++) {
                colPrintNewRender(j, i, 7);
            }
        }
        transparent = ot;
        gl.gameover = true;
        if(board!=null)
        board.delete();
        board = null;
        destroying = true;
    }
    
    public void moveTable(int x, int y, int z) {
        boolean ot = transparent;
        transparent = true;
        for(int i=0;i<GameLogic.STAGESIZEY;i++) {
            for(int j=0;j<GameLogic.STAGESIZEX;j++) {
                colPrintNewRender(j, i, 7);
            }
        }
        gx = x;
        gy = y;
        gz = z;
        for(int i=0;i<GameLogic.STAGESIZEY;i++) {
            for(int j=0;j<GameLogic.STAGESIZEX;j++) {
                colPrintNewRender(j, i, 16);
            }
        }
        transparent = ot;
    }
    
    public void rotateTable(String input) {
        boolean ot = transparent;
        transparent = true;
        for(int i=0;i<GameLogic.STAGESIZEY;i++) {
            for(int j=0;j<GameLogic.STAGESIZEX;j++) {
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
        
        for(int i=0;i<GameLogic.STAGESIZEY;i++) {
            for(int j=0;j<GameLogic.STAGESIZEX;j++) {
                colPrintNewRender(j, i, 16);
            }
        }
        transparent = ot;
    }
    
   	private void render() {
   	    int[][] newStageState = new int[40][10];
   	    //update stage
   	    for(int i=0;i<GameLogic.STAGESIZEY;i++) {
   	        for(int j=0;j<GameLogic.STAGESIZEX;j++) {
   	            newStageState[i][j] = gl.stage[i][j];
   	        }
   	    }
   	    
   	    //print next queue
        for(int i=0;i<GameLogic.NEXTPIECESMAX;i++) {
            printStaticPieceNewRender(GameLogic.STAGESIZEX+3, GameLogic.STAGESIZEY/2+i*4, gl.nextPieces.get(i));
        }
        
        //print held piece
        printStaticPieceNewRender(-7, GameLogic.STAGESIZEY/2, gl.heldPiece);
        
        //update ghost
        int ghosty=gl.currentPiecePosition.y;
        while(!gl.collides(gl.currentPiecePosition.x, ghosty+1, gl.currentPieceRotation)) {
            ghosty++;
        }

        for(Point point: gl.pieces[gl.currentPiece][gl.currentPieceRotation]) {
            newStageState[point.y + ghosty][point.x + gl.currentPiecePosition.x] = 9+gl.currentPiece;
        }
        
        //update current piece
        for(Point point: gl.pieces[gl.currentPiece][gl.currentPieceRotation]) { 
            newStageState[point.y + gl.currentPiecePosition.y][point.x + gl.currentPiecePosition.x] = gl.currentPiece;
        }
        
        //print garbage meter
        int total=0;
        for(int num: gl.garbageQueue) {
            total+=num;
        }
        
        for(int i=0;i<GameLogic.STAGESIZEY/2;i++) {
            colPrintNewRender(-2, GameLogic.STAGESIZEY-1-i, 7);
        }
        
        for(int i=0;i<total;i++) {
            colPrintNewRender(-2, GameLogic.STAGESIZEY-1-i%(GameLogic.STAGESIZEY/2), (i/(GameLogic.STAGESIZEY/2))%7);
        }
        
        
        //print stage+piece+ghost
        for(int i=0;i<GameLogic.STAGESIZEY;i++) {
            for(int j=0;j<GameLogic.STAGESIZEX;j++) {
                lastStageState[i][j] = newStageState[i][j];
                colPrintNewRender(j, i, newStageState[i][j]);
            }
        }
        
        //send scoreboard

        sendScoreboard();
   	    
        //send magic string action bar
        Main.functions.sendActionBarCustom(player, (gl.zone==true?(ChatColor.DARK_GREEN + "" + ChatColor.BOLD):"") + gl.magicString);
   	}
}
