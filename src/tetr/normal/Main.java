package tetr.normal;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import tetr.shared.GameLogic;

public class Main extends JPanel {
    
    private final Color[] tetrominoColors = { Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.CYAN, Color.BLUE, Color.MAGENTA };
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
    
    GameLogic gl = new GameLogic();
    

    private static final long serialVersionUID = 1L;

    private static final int PIXELSIZE = 12;
    private static final int GRIDSIZE = 0;
    private static final Point TOPLEFTCORNER = new Point(PIXELSIZE*10, PIXELSIZE*3);
    
    private void drawHold(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(TOPLEFTCORNER.x -7 * PIXELSIZE, TOPLEFTCORNER.y + GameLogic.STAGESIZEY/2 * PIXELSIZE, PIXELSIZE * 4, PIXELSIZE * 4);
        if(gl.heldPiece!=-1) {
            g.setColor(tetrominoColors[gl.heldPiece]);
            for(Point point: gl.pieces[gl.heldPiece][0]) {
                g.fillRect(TOPLEFTCORNER.x + (-7 + point.x) * PIXELSIZE, TOPLEFTCORNER.y + (point.y + GameLogic.STAGESIZEY/2) * PIXELSIZE, PIXELSIZE, PIXELSIZE);
            }
        }
    }
    
    private void drawQueue(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(TOPLEFTCORNER.x + (3 + GameLogic.STAGESIZEX) * PIXELSIZE, TOPLEFTCORNER.y + GameLogic.STAGESIZEY/2 * PIXELSIZE, PIXELSIZE * 4, PIXELSIZE * 4 * 5);
        ///prints next blocks
        for(int i=0;i<5;i++){
            int piece = gl.nextPieces.get(i);
            g.setColor(tetrominoColors[piece]);
            for(Point point: gl.pieces[piece][0]) {
                g.fillRect(TOPLEFTCORNER.x + (3 + point.x + GameLogic.STAGESIZEX) * PIXELSIZE, TOPLEFTCORNER.y + (i * 4 + point.y + GameLogic.STAGESIZEY/2) * PIXELSIZE, PIXELSIZE-GRIDSIZE, PIXELSIZE-GRIDSIZE);
            }
        }
    }
    
    // Draw the falling piece
    private void drawPiece(Graphics g) {
        g.setColor(tetrominoColors[gl.currentPiece]);
        for (Point point: gl.pieces[gl.currentPiece][gl.currentPieceRotation]) {
            g.fillRect(TOPLEFTCORNER.x + (point.x + gl.currentPiecePosition.x) * PIXELSIZE, TOPLEFTCORNER.y + (point.y + gl.currentPiecePosition.y) * PIXELSIZE, PIXELSIZE-GRIDSIZE, PIXELSIZE-GRIDSIZE);
        }
    }
    
    @Override 
    public void paintComponent(Graphics g)
    {
        // garbage meter
        int total=0;
        for(int num: gl.garbageQueue) {
            total+=num;
        }
        
        g.setColor(Color.BLACK);
        g.fillRect(TOPLEFTCORNER.x - PIXELSIZE*2, TOPLEFTCORNER.y + GameLogic.STAGESIZEY/2*PIXELSIZE, PIXELSIZE-GRIDSIZE, (PIXELSIZE-GRIDSIZE)*20);
        
        for(int i=0;i<total;i++) {
            g.setColor(intToColor((i/(GameLogic.STAGESIZEY/2))%7));
            g.fillRect(TOPLEFTCORNER.x - PIXELSIZE*2, TOPLEFTCORNER.y + (GameLogic.STAGESIZEY - 1 - i%20)*PIXELSIZE, PIXELSIZE-GRIDSIZE, PIXELSIZE-GRIDSIZE);
        }
        
        // Paint the well
        for(int i=GameLogic.STAGESIZEY-GameLogic.VISIBLEROWS;i<GameLogic.STAGESIZEY;i++) {
            for(int j=0;j<GameLogic.STAGESIZEX;j++) {
                g.setColor(intToColor(gl.stage[i][j]));
                g.fillRect(TOPLEFTCORNER.x + PIXELSIZE*j, TOPLEFTCORNER.y + PIXELSIZE*i, PIXELSIZE-GRIDSIZE, PIXELSIZE-GRIDSIZE);
            }
        }
        
        // Display the score
        g.setColor(Color.WHITE);
        g.drawString("points: " + gl.score, 19*6, PIXELSIZE-GRIDSIZE);

        g.setColor(Color.WHITE);
        g.drawString("alpha", 20*12, 20*2);
        
        //show controls

        g.setColor(Color.WHITE);
        g.drawString("Controls:", 50, 80);
        g.drawString("move left/right - left/right arrow\n", 50, 90);
        g.drawString("rotate counterclockwise: z/y\n", 50, 100);
        g.drawString("rotate clockwise: x\n", 50, 110);
        g.drawString("rotate 180: arrow up\n", 50, 120);
        g.drawString("hold: c\n", 50, 130);
        g.drawString("hard drop: space\n", 50, 140);
        g.drawString("soft drop: arrown down", 50, 150);
        g.drawString("zone: shift", 50, 160);
        g.drawString(gl.magicString, 120, 200);
        
        
        drawPiece(g);
        drawQueue(g);
        drawHold(g);
    }
    

    public static void main(String[] args) {
        try {
            Clip music = AudioSystem.getClip();

            music.open(AudioSystem.getAudioInputStream(new File("song36.wav")));

            music.start();
            
        } catch (Exception e) {
            System.out.println(e);
        }
        
        JFrame f = new JFrame("TETR");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(PIXELSIZE * 30 + 16, PIXELSIZE * 46 + 39);
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - f.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - f.getHeight()) / 2);
        f.setLocation(x, y);
        f.setBackground(new Color((int)(Math.random()*256),(int)(Math.random()*256),(int)(Math.random()*256)));
        
        final Main game = new Main();
        game.gl.initGame();
        f.add(game);
        
        
        f.setVisible(true);
        // Keyboard controls
        f.addKeyListener(new KeyListener() {
            
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    game.gl.movePiece(game.gl.currentPiecePosition.x - 1, game.gl.currentPiecePosition.y, game.gl.currentPieceRotation);
                    break;
                case KeyEvent.VK_RIGHT:
                    game.gl.movePiece(game.gl.currentPiecePosition.x + 1, game.gl.currentPiecePosition.y, game.gl.currentPieceRotation);
                    break;
                case KeyEvent.VK_DOWN:
                    game.gl.movePiece(game.gl.currentPiecePosition.x, game.gl.currentPiecePosition.y + 1, game.gl.currentPieceRotation);
                    break;
                case KeyEvent.VK_SPACE:
                    game.gl.hardDropPiece();
                    break;
                case KeyEvent.VK_Z:
                case KeyEvent.VK_Y:
                    game.gl.rotatePiece(-1);
                    break;
                case KeyEvent.VK_X:
                    game.gl.rotatePiece(+1);
                    break;
                case KeyEvent.VK_UP:
                    //180
                    game.gl.rotatePiece(+2);
                    break;
                case KeyEvent.VK_C:
                    game.gl.holdPiece();
                    break;
                case KeyEvent.VK_SHIFT:
                    game.gl.startZone();
                    break;
                } 
            }
            

            @Override
            public void keyTyped(KeyEvent e) {
            }


            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        
        //random colors
        new Thread() {
            @Override
            public void run() {
                while(!game.gl.gameover) {
                    try {
                        Thread.sleep(10);
                            Color color = new Color(
                                                    Math.abs((f.getBackground().getRed()+(int)(Math.random()*5)-2)%256),
                                                    Math.abs((f.getBackground().getGreen()+(int)(Math.random()*5)-2)%256),
                                                    Math.abs((f.getBackground().getBlue()+(int)(Math.random()*5)-2)%256)
                                                                                                               );
                        f.setBackground(color);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                JOptionPane.showMessageDialog(f, "Hi, I am a familiar yet legally distinct block stacker game but because of poor technology in my country unfortunately I am not able to continue operating. \nPlease be so kind to restart the game yourself if you want to play again. \nMany thanks for your cooperation! \nBest regards, The familiar yet legally distinct block stacker game\n\n\nBe sure to also try out the minecraft version!");
            }
        }.start();
        
    }
    
}