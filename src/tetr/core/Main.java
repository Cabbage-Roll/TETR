package tetr.core;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import tetr.core.normal.Table;
import tetr.core.normal.Window;

public class Main {
    public static void main(String[] args) throws IOException {
        Table table = new Table();

        try {
            LoadConfig.load(false);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (Constants.iKnowWhatIAmDoing) {
            try {
                Clip music = AudioSystem.getClip();

                //music.open(AudioSystem.getAudioInputStream(new File("song36.wav")));

                music.start();

            } catch (Exception e) {
                System.out.println(e);
            }
        }

        table.initGame();
        Window window = new Window(table);
        // Keyboard controls
        window.getFrame().addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    table.movePieceRelative(-1, 0);
                    break;
                case KeyEvent.VK_RIGHT:
                    table.movePieceRelative(+1, 0);
                    break;
                case KeyEvent.VK_DOWN:
                    table.movePieceRelative(0, +1);
                    break;
                case KeyEvent.VK_SPACE:
                    table.hardDropPiece();
                    break;
                case KeyEvent.VK_Z:
                case KeyEvent.VK_Y:
                    table.rotatePiece(-1);
                    break;
                case KeyEvent.VK_X:
                    table.rotatePiece(+1);
                    break;
                case KeyEvent.VK_UP:
                    // 180
                    table.rotatePiece(+2);
                    break;
                case KeyEvent.VK_C:
                    table.holdPiece();
                    break;
                case KeyEvent.VK_SHIFT:
                    table.startZone();
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
    }
}
