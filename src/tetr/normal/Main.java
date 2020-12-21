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
import javax.swing.JPanel;

import tetr.shared.GameLogic;

public class Main extends JPanel {
    
    GameLogic gl = new GameLogic();

    private static final long serialVersionUID = 1L;

    private static final int PIXELSIZE = 20;
    private static final int GRIDSIZE = 0;
    private static final Point TOPLEFTCORNER = new Point(PIXELSIZE*10, PIXELSIZE*3);
    
    private final Color[] tetrominoColors = {
        Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.CYAN, Color.BLUE, Color.MAGENTA
    };
    
    private void drawHold(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(TOPLEFTCORNER.x -7 * PIXELSIZE, TOPLEFTCORNER.y, PIXELSIZE * 4, PIXELSIZE * 4);
        if(gl.heldPiece!=-1) {
            g.setColor(tetrominoColors[gl.heldPiece]);
            for(Point point: gl.pieces[gl.heldPiece][0]) {
                g.fillRect(TOPLEFTCORNER.x + (-7 + point.x) * PIXELSIZE, TOPLEFTCORNER.y + point.y * PIXELSIZE, PIXELSIZE, PIXELSIZE);
            }
        }
    }
    
    private void drawQueue(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(TOPLEFTCORNER.x + (3 + gl.STAGESIZEX) * PIXELSIZE, TOPLEFTCORNER.y, PIXELSIZE * 4, PIXELSIZE * 4 * 5);
        ///prints next blocks
        for(int i=0;i<5;i++){
            int piece = gl.nextPieces.get(i);
            g.setColor(tetrominoColors[piece]);
            for(Point point: gl.pieces[piece][0]) {
                g.fillRect(TOPLEFTCORNER.x + (3 + point.x + gl.STAGESIZEX) * PIXELSIZE, TOPLEFTCORNER.y + (i * 4 + point.y) * PIXELSIZE, PIXELSIZE-GRIDSIZE, PIXELSIZE-GRIDSIZE);
            }
        }
    }
    
    // Draw the falling piece
    private void drawPiece(Graphics g) {
        g.setColor(tetrominoColors[gl.currentPiece]);
        for (Point point: gl.pieces[gl.currentPiece][gl.rotation]) {
            g.fillRect(TOPLEFTCORNER.x + (point.x + gl.pieceOrigin.x) * PIXELSIZE, TOPLEFTCORNER.y + (point.y + gl.pieceOrigin.y) * PIXELSIZE, PIXELSIZE-GRIDSIZE, PIXELSIZE-GRIDSIZE);
        }
    }
    
    @Override 
    public void paintComponent(Graphics g)
    {
        // Paint the well
        g.fillRect(TOPLEFTCORNER.x, TOPLEFTCORNER.y, PIXELSIZE*gl.STAGESIZEX, PIXELSIZE*gl.STAGESIZEY);
        for(int i=0;i<gl.STAGESIZEY;i++) {
            for(int j=0;j<gl.STAGESIZEX;j++) {
                g.setColor(gl.intToColor(gl.stage[i][j]));
                g.fillRect(TOPLEFTCORNER.x + PIXELSIZE*j, TOPLEFTCORNER.y + PIXELSIZE*i, PIXELSIZE-GRIDSIZE, PIXELSIZE-GRIDSIZE);
            }
        }
        
        // Display the score
        g.setColor(Color.WHITE);
        g.drawString("Bruh points: " + gl.score, 19*6, PIXELSIZE-GRIDSIZE);

        g.setColor(Color.WHITE);
        g.drawString("alpha", 20*12, 20*2);
        
        
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
        f.setSize(PIXELSIZE * 30 + 16, PIXELSIZE * 26 + 39);
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
                    game.gl.move(-1, 0);
                    break;
                case KeyEvent.VK_RIGHT:
                    game.gl.move(+1, 0);
                    break;
                case KeyEvent.VK_DOWN:
                    game.gl.move(0, +1);
                    break;
                case KeyEvent.VK_SPACE:
                    while (!game.gl.collides(game.gl.pieceOrigin.x, game.gl.pieceOrigin.y + 1, game.gl.rotation)) {
                        game.gl.pieceOrigin.y += 1;
                    }
                        game.gl.fixToWell();
                    break;
                case KeyEvent.VK_Y:
                    game.gl.rotate(-1);
                    break;
                case KeyEvent.VK_X:
                    game.gl.rotate(+1);
                    break;
                case KeyEvent.VK_UP:
                    //180
                    game.gl.rotate(+2);
                    break;
                case KeyEvent.VK_C:
                    game.gl.holdBlock();
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
        
        //gravity
        new Thread() {
            @Override public void run() {
                while (true) {
                    try {
                        Thread.sleep(100000);
                        game.gl.move(0, +1);
                    } catch ( InterruptedException e ) {}
                }
            }
        }.start();

        //lock delay
        
        
        //random colors
        new Thread() {
            @Override
            public void run() {
                while(true) {
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
            }
        }.start();
        
    }
    
}