package tetr.shared;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class GameLogic {

    private final Color[] tetrominoColors = {
            Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.CYAN, Color.BLUE, Color.MAGENTA
        };
    
    public final Point[][][] pieces = Pieces.pieces;
    private final Point[][][] kicktable = Kicktable.kicktable_srsplus;
    
    public final int STAGESIZEX = 10;
    public final int STAGESIZEY = 40;

    public Point pieceOrigin;
    public int currentPiece;
    public int heldPiece = -1;
    public int rotation;
    private boolean held;
    public ArrayList<Integer> nextPieces = new ArrayList<Integer>();
    
    public long score;
    public int[][] stage;
    
    private Random gen = new Random((int)(Math.random()*Integer.MAX_VALUE));
    
    private Point[] getCurrentPiece() {
        return pieces[currentPiece][rotation];
    }
    
    public Color intToColor(int number) {
        if(number == 0){
            return tetrominoColors[0];
        }else if(number == 1){
            return tetrominoColors[1];
        }else if(number == 2){
            return tetrominoColors[2];
        }else if(number == 3){
            return tetrominoColors[3];
        }else if(number == 4){
            return tetrominoColors[4];
        }else if(number == 5){
            return tetrominoColors[5];
        }else if(number == 6){
            return tetrominoColors[6];
        }else if(number == 7){
            return Color.BLACK;
        }else if(number == 8){
            return Color.GRAY;
        }else if(number == 16){
            return Color.WHITE;
        }
        return null;
    }
    
    //TETR FUNCTION
    //REQUIRES PRINTING
    public void initGame() {
        stage = new int[STAGESIZEY][STAGESIZEX];
        for(int i=0;i<STAGESIZEY;i++) {
            for(int j=0;j<STAGESIZEX;j++) {
                stage[i][j] = 7;
            }
        }
        makeNextBlock();
    }
    
    //TETR FUNCTION
    //REQUIRES PRINTING
    public void holdBlock() {
        if(!held) {
            int temp;
            
            //print current block into hold slot
            
            //erase current block from board

            //if first hold
            if(heldPiece==-1) {
                heldPiece = currentPiece;
                makeNextBlock();
            }else {
                //swap
                temp = currentPiece;
                currentPiece = heldPiece;
                heldPiece = temp;
                
                //spawn new block
                pieceOrigin = new Point(3, 1);
                rotation = 0;
            }
            
            held = true;
        }
    }
    
    //TETR FUNCTION
    //retarded bag generator
    private void makeNextBlock() {
        if(nextPieces.size() <= 7) {
            Integer[] bag2 = new Integer[7];
            for(int i=0;i<7;i++){
                bag2[i]=(int)(gen.nextInt(7));
                for(int j=0;j<i;j++){
                    if(bag2[i]==bag2[j]){
                        i--;
                    }
                }
            }
            nextPieces.addAll(Arrays.asList(bag2));
        }
        
        spawnBlock();
    }
    
    //TETR FUNCTION
    //REQUIRES PRINTING
    private void spawnBlock() {
        pieceOrigin = new Point(3, 1);
        rotation = 0;
        currentPiece = nextPieces.get(0);
        nextPieces.remove(0);
        held = false;
    }
    
    //TETR FUNCTION
    public boolean collides(int x, int y, int rotation) {
        for(Point point: pieces[currentPiece][rotation]) {

            System.out.println("A");
            //first we check if the piece is inside borders
            if((0<=point.y+y && point.y+y<STAGESIZEY) && (0<=point.x+x && point.x+x<STAGESIZEX)) {

                System.out.println("B");
                //check for the collision with other pieces
                if(stage[point.y+y][point.x+x] != 7) {
                    System.out.println("C");
                    return true;
                }
                System.out.println("OK");
            }else {
                
                System.out.println("OUT OF BOUNDS!"+(point.y+y)+","+(point.x+x));
                return true;
            }
        }

        System.out.println("E");
        return false;
    }
    
    //TETR FUNCTION
    //REQUIRES PRINTING
    public void rotate(int d) {
        int newRotation = (rotation + d + 4) % 4;
        
        int special = -1;
        
        if(rotation==0 && newRotation==1) {
            special = 0;
        }else if(rotation==1 && newRotation==0) {
            special = 1;
        }else if(rotation==1 && newRotation==2) {
            special = 2;
        }else if(rotation==2 && newRotation==1) {
            special = 3;
        }else if(rotation==2 && newRotation==3) {
            special = 4;
        }else if(rotation==3 && newRotation==2) {
            special = 5;
        }else if(rotation==3 && newRotation==0) {
            special = 6;
        }else if(rotation==0 && newRotation==3) {
            special = 7;
        }else if(rotation==0 && newRotation==2) {
            special = 8;
        }else if(rotation==2 && newRotation==0) {
            special = 9;
        }else if(rotation==1 && newRotation==3) {
            special = 10;
        }else if(rotation==3 && newRotation==1) {
            special = 11;
        }
        
        int pieceType = 0;
        
        int maxtries = kicktable[pieceType][special].length;

        for(int tries=0;tries<maxtries;tries++) {
            if(!collides(pieceOrigin.x + kicktable[pieceType][special][tries].x, pieceOrigin.y - kicktable[pieceType][special][tries].y, newRotation)){
                pieceOrigin.x += kicktable[pieceType][special][tries].x;
                pieceOrigin.y -= kicktable[pieceType][special][tries].y;
                rotation = newRotation;
                break;
            }
        }
    }
    
    // Move the piece left or right
    //REQUIRES PRINTING
    public void move(int offsetX, int offsetY) {
        if(!collides(pieceOrigin.x + offsetX, pieceOrigin.y + offsetY, rotation)) {
            pieceOrigin.x += offsetX;
            pieceOrigin.y += offsetY;
        }
    }
    
    
    // Make the dropping piece part of the well, so it is available for
    // collision detection.
    //REQUIRES PRINTING
    public void fixToWell() {
        for (Point point: getCurrentPiece()) {
            stage[pieceOrigin.y + point.y][pieceOrigin.x + point.x] = currentPiece;
        }
        clearRows();
        makeNextBlock();
    }
    

    //REQUIRES PRINTING
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
    
    // Clear completed rows from the field and award score according to
    // the number of simultaneously cleared rows.

    //REQUIRES PRINTING
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
