package tetr.minecraft.constants;

public class Blocklist {
    public static int[][][] block_list={
        //Z piece (3x3)
        {
            {0,0,7,7},
            {7,0,0,7},
            {7,7,7,7},
            {7,7,7,7}
        },
        
        //L piece (3x3)
        {
            {7,7,1,7},
            {1,1,1,7},
            {7,7,7,7},
            {7,7,7,7}
        },
        
        //O piece (2x2)
        {
            {2,2,7,7},
            {2,2,7,7},
            {7,7,7,7},
            {7,7,7,7}
        },
    
        //S piece (3x3)
        {
            {7,3,3,7},
            {3,3,7,7},
            {7,7,7,7},
            {7,7,7,7}
        },
    
        //I piece (4x4)
        {
            {7,7,7,7},
            {4,4,4,4},
            {7,7,7,7},
            {7,7,7,7}
        },
        
        //J piece (3x3)
        {
            {5,7,7,7},
            {5,5,5,7},
            {7,7,7,7},
            {7,7,7,7}
        },
    
        //T piece (3x3)
        {
            {7,6,7,7},
            {6,6,6,7},
            {7,7,7,7},
            {7,7,7,7}
        }
    };
}