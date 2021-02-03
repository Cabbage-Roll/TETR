package tetr.normal;

import java.awt.Color;

import tetr.shared.GameLogic;

public class Table extends GameLogic {
    private static final Color[] tetrominoColors = {
            Color.RED,
            Color.ORANGE,
            Color.YELLOW,
            Color.GREEN,
            Color.CYAN,
            Color.BLUE,
            Color.MAGENTA
    };

    public static Color intToColor(int number) {
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
}
