package tetr.shared;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;

import org.bukkit.entity.Player;

import tetr.minecraft.Main;
import tetr.minecraft.xseries.XSound;

public class GameLogic {
    
    private Player player;
    public String magicString;
    private int magicStringsActive = 0;

    public final Point[][][] pieces = Pieces.pieces;
    private final Point[][][] kicktable = Kicktable.kicktable_srsplus;
    private final int[][] garbagetable = Garbagetable.tetrio;
    
    public boolean gameover = false;
    
    public ArrayList<Integer> garbageToCome = new ArrayList<Integer>();
    private int garbageHole;
    private int garbageCap = 4;
    
    private int zonelines;
    public boolean zone;
    
    public final int STAGESIZEX = 10;
    public final int STAGESIZEY = 40;
    public final int VISIBLEROWS = 24;
    public final int next_blocks = 5;

    public int currentPiece;
    public Point currentPiecePosition;
    public int currentPieceRotation;
    public int heldPiece = -1;
    public boolean held;
    public ArrayList<Integer> nextPieces = new ArrayList<Integer>();
    
    private boolean tSpin;
    private boolean tSpinMini;
    public int combo;
    public int b2b;
    public long score;
    public long totalLinesCleared = 0;
    public long totalPiecesPlaced = 0;
    public long totalGarbageReceived = 0;
    public int[][] stage = new int[STAGESIZEY][STAGESIZEX];
    
    public GameLogic(Player player) {
        this.player = player;
    }
    
    public GameLogic() {
        this.player = null;
    }

    private boolean checkOOBE(int x, int y) {
        if(y<0 || STAGESIZEY<=y || x<0 || STAGESIZEX<=x) {
            return true;
        }
        return false;
    }
    
    public int forGarbageTable(int l) {
        if(l==1 && tSpin==false) {
            setMagicString("single");
            return 0;
        }else if (l==2 && tSpin==false){
            setMagicString("double");
            return 1;
        }else if (l==3 && tSpin==false){
            setMagicString("triple");
            return 2;
        }else if (l==4 && tSpin==false){
            setMagicString("quad");
            return 3;
        }else if (l==1 && tSpin==true && tSpinMini == true){
            setMagicString("t spin mini single");
            return 4;
        }else if (l==1 && tSpin==true){
            setMagicString("t spin single");
            return 5;
        }else if (l==2 && tSpin==true && tSpinMini == true){
            setMagicString("t spin mini double");
            return 6;
        }else if (l==2 && tSpin==true){
            setMagicString("t spin double");
            return 7;
        }else if (l==3 && tSpin==true){
            setMagicString("t spin triple");
            return 8;
        }else {
            return -1;
        }
    }
    
    private void sendGarbage(int n) {
        int garbageRemaining = n;
        while(!garbageToCome.isEmpty() && garbageRemaining>0) {
            garbageToCome.set(0, garbageToCome.get(0)-1);
            if(garbageToCome.get(0)==0) {
                garbageToCome.remove(0);
                garbageHole=(int)(Math.random() * STAGESIZEX);
            }
            garbageRemaining--;
        }
        
        if(garbageRemaining>0) {
            if(this.player == null) {
                receiveGarbage(garbageRemaining);
            }else {
                Main.inwhichroom.get(player).forwardGarbage(garbageRemaining, player);
            }
        }
    }
    
    public void receiveGarbage(int n) {
        garbageToCome.add(n);
    }
    
    private void tryToPutGarbage() {
        for(int h=0;h<garbageCap;h++) {
            if(!garbageToCome.isEmpty()) {
                putGarbageLine(garbageHole);
                
                garbageToCome.set(0, garbageToCome.get(0)-1);
                if(garbageToCome.get(0)==0) {
                    garbageToCome.remove(0);
                    garbageHole=(int)(Math.random() * STAGESIZEX);
                }
            }
        }
    }
    
    private void putGarbageLine(int hole) {
        for(int i=0;i<STAGESIZEY-1;i++) {
            for(int j=0;j<STAGESIZEX;j++) {
                stage[i][j]=stage[i+1][j];
            }
        }
        for(int j=0;j<STAGESIZEX;j++) {
            if(j==garbageHole) {
                stage[STAGESIZEY-1][j]=7;
            }else{
                stage[STAGESIZEY-1][j]=8;
            }
        }
        
        totalGarbageReceived++;
    }
    
    private Point[] getCurrentPiece() {
        return pieces[currentPiece][currentPieceRotation];
    }
    
    public boolean topOutCheck() {
        for(Point point: pieces[currentPiece][currentPieceRotation]) {
            if(stage[point.y+currentPiecePosition.y][point.x+currentPiecePosition.x]!=7){
                if(zone) {
                    stopZone();
                }else {
                    gameover=true;   
                }
                return true;
            }
        }
        return false;
    }
    
    public void startZone() {
        zone = true;
        magicString = "Zone activated";
    }
    
    private void stopZone() {
        zone = false;
        for(int i=0;i<STAGESIZEY;i++) {
            for(int j=0;j<STAGESIZEX;j++) {
                if(STAGESIZEY-zonelines-1-i>=0) {
                    stage[STAGESIZEY-1-i][j] = stage[STAGESIZEY-zonelines-1-i][j];
                }
            }
        }
        zonelines = 0;
    }
    
    public boolean checkTSpin() {
        if(currentPiece == 6) {
            boolean corners[] = {true, true, true, true};
            if(!checkOOBE(currentPiecePosition.x, currentPiecePosition.y)) {
                if(stage[currentPiecePosition.y][currentPiecePosition.x]==7) {
                    corners[0] = false;
                }
            }
            
            if(!checkOOBE(currentPiecePosition.x+2, currentPiecePosition.y)) {
                if(stage[currentPiecePosition.y][currentPiecePosition.x+2]==7) {
                    corners[1] = false;
                }
            }
            
            if(!checkOOBE(currentPiecePosition.x, currentPiecePosition.y+2)) {
                if(stage[currentPiecePosition.y+2][currentPiecePosition.x]==7) {
                    corners[2] = false;
                }
            }
            
            if(!checkOOBE(currentPiecePosition.x+2, currentPiecePosition.y+2)) {
                if(stage[currentPiecePosition.y+2][currentPiecePosition.x+2]==7) {
                    corners[3] = false;
                }
            }
            
            int cornersFilled = 0;
            for(int i=0;i<4;i++) {
                if(corners[i]==true) {
                    cornersFilled++;
                }
            }
            
            if(cornersFilled>=3) {

                if(player!=null) {
                    for(int i=0;i<3;i++) {
                        player.playSound(player.getEyeLocation(), XSound.BLOCK_END_PORTAL_FRAME_FILL.parseSound(), 1f, 1f);
                    }
                }
                tSpinMini = true;
                
                switch(currentPieceRotation) {
                case 0:
                    if(corners[0] && corners[1]) {
                        tSpinMini=false;
                    }
                    break;
                case 1:
                    if(corners[1] && corners[3]) {
                        tSpinMini=false;
                    }
                    break;
                case 2:
                    if(corners[3] && corners[2]) {
                        tSpinMini=false;
                    }
                    break;
                case 3:
                    if(corners[2] && corners[0]) {
                        tSpinMini=false;
                    }
                    break;
                }
                return true;
            }
        }
        return false;
    }
    
    public void initGame() {
        for(int i=0;i<STAGESIZEY;i++) {
            for(int j=0;j<STAGESIZEX;j++) {
                stage[i][j] = 7;
            }
        }
        gameover = false;
        nextPieces.clear();
        heldPiece = -1;
        held = false;
        score = 0;
        combo = -1;
        b2b = -1;
        
        totalLinesCleared = 0;
        totalPiecesPlaced = 0;
        totalGarbageReceived = 0;
        
        zone = false;
        zonelines = 0;
        
        garbageToCome.clear();
        garbageHole=(int)(Math.random() * STAGESIZEX);
        
        magicString = "";
        
        makeNextPiece();
    }
    
    public boolean holdPiece() {
        if(!held) {
            int temp;
            
            //if first hold
            if(heldPiece==-1) {
                heldPiece = currentPiece;
                makeNextPiece();
            }else {
                //swap
                temp = currentPiece;
                currentPiece = heldPiece;
                heldPiece = temp;
                
                currentPiecePosition = new Point(3, 17);
                currentPieceRotation = 0;
                
                topOutCheck();
            }
            
            held = true;
            return true;
        }else {
            return false;
        }
    }
    
    public void makeNextPiece() {
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
    
    public void spawnPiece() {
        currentPiecePosition = new Point(3, 17);
        currentPieceRotation = 0;
        currentPiece = nextPieces.get(0);
        nextPieces.remove(0);
        held = false;
        tSpin = false;
    }
    
    public boolean collides(int x, int y, int rotation) {
        for(Point point: pieces[currentPiece][rotation]) {
            //first we check if the piece is inside borders
            if((0<=point.y+y && point.y+y<STAGESIZEY) && (0<=point.x+x && point.x+x<STAGESIZEX)) {
                //check for the collision with other pieces
                if(stage[point.y+y][point.x+x] != 7) {
                    return true;
                }
            }else {
                return true;
            }
        }
        return false;
    }
    
    public boolean rotatePiece(int d) {
        int newRotation = (currentPieceRotation + d + 4) % 4;
        
        int special = -1;
        
        if(currentPieceRotation==0 && newRotation==1) {
            special = 0;
        }else if(currentPieceRotation==1 && newRotation==0) {
            special = 1;
        }else if(currentPieceRotation==1 && newRotation==2) {
            special = 2;
        }else if(currentPieceRotation==2 && newRotation==1) {
            special = 3;
        }else if(currentPieceRotation==2 && newRotation==3) {
            special = 4;
        }else if(currentPieceRotation==3 && newRotation==2) {
            special = 5;
        }else if(currentPieceRotation==3 && newRotation==0) {
            special = 6;
        }else if(currentPieceRotation==0 && newRotation==3) {
            special = 7;
        }else if(currentPieceRotation==0 && newRotation==2) {
            special = 8;
        }else if(currentPieceRotation==2 && newRotation==0) {
            special = 9;
        }else if(currentPieceRotation==1 && newRotation==3) {
            special = 10;
        }else if(currentPieceRotation==3 && newRotation==1) {
            special = 11;
        }

        int pieceType = currentPiece==4?1:0;
        
        int maxtries = kicktable[pieceType][special].length;

        for(int tries=0;tries<maxtries;tries++) {
            if(movePiece(currentPiecePosition.x + kicktable[pieceType][special][tries].x, currentPiecePosition.y - kicktable[pieceType][special][tries].y, newRotation)){
                tSpin = checkTSpin();
                return true;
            }
        }
        
        return false;
    }
    
    public boolean movePiece(int newX, int newY, int newR) {
        if(!collides(newX, newY, newR)) {
            currentPiecePosition.x = newX;
            currentPiecePosition.y = newY;
            currentPieceRotation = newR;
            tSpin = false;
            return true;
        }
        return false;
    }
    
    public void hardDropPiece() {
        int lines=0;
        while(!collides(currentPiecePosition.x, currentPiecePosition.y+lines+1, currentPieceRotation)) {
            lines++;
        }
        if(lines>0) {
            movePiece(currentPiecePosition.x, currentPiecePosition.y+lines, currentPieceRotation);
            score+=lines*2;
        }
        placePiece();
    }
    
    public void placePiece() {

        totalPiecesPlaced++;
        
        for(Point point: getCurrentPiece()) {
            stage[currentPiecePosition.y + point.y][currentPiecePosition.x + point.x] = currentPiece;
        }
        
        if(zone) {
            clearLinesZone();
        }else {
            int linesCleared = clearLines();
            
            if(linesCleared>0) {
                combo++;
                if(player!=null) {
                    for(int i=0;i<linesCleared*2;i++) {
                        player.playSound(player.getEyeLocation(), XSound.BLOCK_NOTE_BLOCK_HARP.parseSound(), 1f, (float)Math.pow(2,(double)(combo*2-12)/12));
                    }
                    if(tSpin) {
                        for(int i=0;i<linesCleared*2;i++) {
                            player.playSound(player.getEyeLocation(), XSound.ENTITY_FIREWORK_ROCKET_BLAST.parseSound(), 1f, 0.5f);
                        }
                    }
                }
                if((totalLinesCleared-totalGarbageReceived)*STAGESIZEX+totalGarbageReceived==totalPiecesPlaced*4) {
                    if(player!=null) {
                        player.playSound(player.getEyeLocation(), XSound.BLOCK_ANVIL_LAND.parseSound(), 1f, 0.5f);
                    }
                    sendGarbage(garbagetable[forGarbageTable(linesCleared)][combo]+10);
                }else {
                    sendGarbage(garbagetable[forGarbageTable(linesCleared)][combo]);
                }
            }else {
                combo = -1;
                tryToPutGarbage();
            }
            
            debug("tspin="+tSpin+";combo="+combo+";linescleared="+linesCleared);
            
        }
        
        makeNextPiece();
    }
    
    private void debug(String s) {
    }
    
    public int clearLines() {
        int numClears = 0;
        boolean yes;
        for(int i=STAGESIZEY-1;i>0;i--) {
            yes = true;
            for(int j=0;j<STAGESIZEX;j++) {
                if (stage[i][j] == 7) {
                    yes = false;
                    break;
                }
            }
            if(yes) {
                clearLine(i);
                i++;
                numClears++;
            }
        }
        
        switch (numClears) {
        case 1:
            score += 100;
            break;
        case 2:
            score += 300;
            break;
        case 3:
            score += 500;
            break;
        case 4:
            score += 800;
            break;
        }
        
        return numClears;
    }
    
    public void clearLine(int line) {
        
        for(int j=0;j<STAGESIZEX;j++) {
            stage[0][j] = 7;
        }
        
        for(int i=line;i>0;i--) {
            for(int j=0;j<STAGESIZEX;j++) {
                stage[i][j] = stage[i-1][j];
            }
        }
        
        totalLinesCleared++;
    }
    
    public void clearLinesZone() {

        boolean yes;
        for(int i=STAGESIZEY-1-zonelines;i>0;i--) {
            yes = true;
            for(int j=0;j<STAGESIZEX;j++) {
                if (stage[i][j] == 7) {
                    yes = false;
                    break;
                }
            }
            if(yes) {
                zonelines++;
                clearLineZone(i);
            }
        }
    }
    
    public void clearLineZone(int line) {
        int gap = STAGESIZEY - (line + zonelines);
        for(int i=line;i<line+gap;i++) {
            for(int j=0;j<STAGESIZEX;j++) {
                stage[i][j] = stage[i+1][j];
            }
        }
        for(int j=0;j<STAGESIZEX;j++) {
            stage[STAGESIZEY-zonelines][j] = 16;
        }
        
        magicString = zonelines + " LINES";
    }
    
    private void setMagicString(String s) {
        new Thread() {
            @Override
            public void run() {
                magicStringsActive++;
                magicString = s + " " + combo + " combo";
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if(magicStringsActive == 1) {
                    magicString = "";
                }
                magicStringsActive--;
                this.interrupt();
            }
        }.start();
    }

}
