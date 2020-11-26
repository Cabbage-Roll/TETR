package cabbageroll.tetr.constants;

public class Kicktable {

    public static int[][][][] kicks={  
        {//J, L, S, T, Z Tetromino Wall Kick Data
            {{ 0, 0},{-1, 0},{-1,+1},{ 0,-2},{-1,-2}},
            {{ 0, 0},{+1, 0},{+1,-1},{ 0,+2},{+1,+2}},
            {{ 0, 0},{+1, 0},{+1,-1},{ 0,+2},{+1,+2}},
            {{ 0, 0},{-1, 0},{-1,+1},{ 0,-2},{-1,-2}},
            {{ 0, 0},{+1, 0},{+1,+1},{ 0,-2},{+1,-2}},
            {{ 0, 0},{-1, 0},{-1,-1},{ 0,+2},{-1,+2}},
            {{ 0, 0},{-1, 0},{-1,-1},{ 0,+2},{-1,+2}},
            {{ 0, 0},{+1, 0},{+1,+1},{ 0,-2},{+1,-2}},
        },
        {//I Tetromino Wall Kick Data
            {{ 0, 0},{+1, 0},{-2, 0},{-2,-1},{+1,+2}},
            {{ 0, 0},{-1, 0},{+2, 0},{-1,-2},{+2,+1}},
            {{ 0, 0},{-1, 0},{+2, 0},{-1,+2},{+2,-1}},
            {{ 0, 0},{-2, 0},{+1, 0},{-2,+1},{+1,-2}},
            {{ 0, 0},{+2, 0},{-1, 0},{+2,+1},{-1,-2}},
            {{ 0, 0},{+1, 0},{-2, 0},{+1,+2},{-2,-1}},
            {{ 0, 0},{+1, 0},{-2, 0},{+1,-2},{-2,+1}},
            {{ 0, 0},{-1, 0},{+2, 0},{+2,-1},{-1,+2}},
        }
    };
    
    public static int[][][][] kicks_180={
        {
            {{ 0, 0},{ 0,+1},{+1,+1},{-1,+1},{+1, 0},{-1, 0}},
            {{ 0, 0},{ 0,-1},{-1,-1},{+1,-1},{-1, 0},{+1, 0}},
            {{ 0, 0},{+1, 0},{+1,+2},{+1,+1},{ 0,+2},{ 0,+1}},
            {{ 0, 0},{-1, 0},{-1,+2},{-1,+1},{ 0,+2},{ 0,+1}},
        },
        {
            {{ 0, 0},{ 0,+1}},
            {{ 0, 0},{+1, 0}},
            {{ 0, 0},{ 0,-1}},
            {{ 0, 0},{-1, 0}},
        }
    };
    
}
