package tetr.shared;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;

public class GameLogic {

    public final Point[][][] pieces = Pieces.pieces;
    private final Point[][][] kicktable = Kicktable.kicktable_srsplus;
    
    public final int STAGESIZEX = 10;
    public final int STAGESIZEY = 40;
    public final int VISIBLEROWS = 24;
    public final int next_blocks = 5;

    public Point currentPiecePosition;
    public int currentPieceRotation;
    public int currentPiece;
    public int heldPiece = -1;
    public boolean held;
    public ArrayList<Integer> nextPieces = new ArrayList<Integer>();
    
    public long score;
    public int[][] stage;
    
    private Point[] getCurrentPiece() {
        return pieces[currentPiece][currentPieceRotation];
    }
    
    public boolean topOutCheck() {
        for(Point point: pieces[currentPiece][currentPieceRotation]) {
            if(stage[point.y+currentPiecePosition.y][point.x+currentPiecePosition.x]!=7){
                return true;
            }
        }
        return false;
    }
    
    public void initGame() {
        stage = new int[STAGESIZEY][STAGESIZEX];
        for(int i=0;i<STAGESIZEY;i++) {
            for(int j=0;j<STAGESIZEX;j++) {
                stage[i][j] = 7;
            }
        }
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
    
    private void spawnPiece() {
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
    
    public void movePiece(int x, int y, int r) {
        if(!collides(x, y, r)) {
            currentPiecePosition.x = x;
            currentPiecePosition.y = y;
            currentPieceRotation = r;
        }
    }
    
    public void placePiece() {
        for(Point point: getCurrentPiece()) {
            stage[currentPiecePosition.y + point.y][currentPiecePosition.x + point.x] = currentPiece;
        }
        clearRows();
        makeNextPiece();
    }
    
    public void deleteRow(int row) {

        System.out.println("Clearing row "+row);
        
        for(int j=0;j<STAGESIZEX;j++) {
            stage[0][j] = 7;
        }
        
        for(int i=row;i>0;i--) {
            for(int j=0;j<STAGESIZEX;j++) {
                stage[i][j] = stage[i-1][j];
            }
        }
    }
    
    public void clearRows() {
        boolean gap;
        int numClears = 0;
        

        for(int i=STAGESIZEY-1;i>0;i--) {
            gap = false;
            for(int j=0;j<STAGESIZEX;j++) {
                if (stage[i][j] == 7) {
                    gap = true;
                    break;
                }
            }
            if (!gap) {
                deleteRow(i);
                i += 1;
                numClears += 1;
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
    }
}
