package tetr.shared;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;

import org.bukkit.entity.Player;

import tetr.minecraft.Main;

public class GameLogic {
    
    private Player player;

    public final Point[][][] pieces = Pieces.pieces;
    private final Point[][][] kicktable = Kicktable.kicktable_srsplus;
    private final int[][] garbagetable = Garbagetable.tetrio;
    
    public boolean gameover = false;
    
    public ArrayList<Integer> garbageToCome = new ArrayList<Integer>();
    private int garbageHole;
    private int garbageCap = 4;
    
    private int zonelines;
    private boolean zone;
    
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

    private void sendGarbage(int n) {
        if(n>0) {
            if(this.player == null) {
                receiveGarbage(n);
            }else {
                int random = (int)Math.random()*Main.inwhichroom.get(player).playerlist.size();
                Player target = Main.inwhichroom.get(player).playerlist.get(random);
                if(target!=player) {
                    Main.inwhichroom.get(player).forwardGarbage(n, target);
                }else {
                    sendGarbage(n);
                }
            }
        }
    }
    
    public void receiveGarbage(int n) {
        garbageToCome.add(n);
    }
    
    private void tryToPutGarbage() {
        for(int h=0;h<garbageCap;h++) {
            if(!garbageToCome.isEmpty()) {
                totalGarbageReceived++;
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
            switch(currentPieceRotation) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            }
            return true;
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
                
                currentPiecePosition = new Point(3, 20);
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
        currentPiecePosition = new Point(3, 20);
        currentPieceRotation = 0;
        currentPiece = nextPieces.get(0);
        nextPieces.remove(0);
        held = false;
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
    
    public void rotatePiece(int d) {
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
            if(!collides(currentPiecePosition.x + kicktable[pieceType][special][tries].x, currentPiecePosition.y - kicktable[pieceType][special][tries].y, newRotation)){
                movePiece(currentPiecePosition.x + kicktable[pieceType][special][tries].x, currentPiecePosition.y - kicktable[pieceType][special][tries].y, newRotation);
                break;
            }
        }
    }
    
    public boolean movePiece(int x, int y, int r) {
        if(!collides(x, y, r)) {
            currentPiecePosition.x = x;
            currentPiecePosition.y = y;
            currentPieceRotation = r;
            return true;
        }
        return false;
    }
    
    public void hardDropPiece() {
        int lines=0;
        while(!collides(currentPiecePosition.x, currentPiecePosition.y+lines+1, currentPieceRotation)) {
            lines++;
        }
        movePiece(currentPiecePosition.x, currentPiecePosition.y+lines, currentPieceRotation);
        score+=lines*2;
        placePiece();
    }
    
    public void placePiece() {
        for(Point point: getCurrentPiece()) {
            stage[currentPiecePosition.y + point.y][currentPiecePosition.x + point.x] = currentPiece;
        }
        if(zone) {
            clearLinesZone();
        }else {
            clearLines();
        }
        makeNextPiece();
    }
    
    public void clearLines() {
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
        

        System.out.println(numClears);
        switch (numClears) {
        case 1:
            score += 100;
            sendGarbage(garbagetable[0][combo+1]);
            break;
        case 2:
            score += 300;
            sendGarbage(garbagetable[1][combo+1]);
            break;
        case 3:
            score += 500;
            sendGarbage(garbagetable[2][combo+1]);
            break;
        case 4:
            score += 800;
            sendGarbage(garbagetable[3][combo+1]);
            break;
        default:
            tryToPutGarbage();
            break;
        }
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
    }

}
