package tetr.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.cryptomorin.xseries.XMaterial;

public class LoadConfig {
    
    static InputStream file = LoadConfig.class.getResourceAsStream("stupidconfig.txt");
    
    public static void load(boolean mc) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(file));
        tetr.core.GameLogic.STAGESIZEX = Integer.valueOf(br.readLine());
        tetr.core.GameLogic.STAGESIZEY = Integer.valueOf(br.readLine());
        tetr.core.GameLogic.VISIBLEROWS = Integer.valueOf(br.readLine());
        tetr.core.GameLogic.NEXTPIECESMAX = Integer.valueOf(br.readLine());
        tetr.core.Constants.idLength = Integer.valueOf(br.readLine());
        tetr.core.Constants.idCharSet = String.valueOf(br.readLine());
        if(mc) {
            for(int i=0;i<17;i++) {
                tetr.core.minecraft.Blocks.blocks[i] = XMaterial.valueOf(br.readLine()).parseItem();
            }
        }
    }
}