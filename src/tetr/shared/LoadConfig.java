package tetr.shared;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import tetr.minecraft.xseries.XMaterial;

public class LoadConfig {
    
    static InputStream file = LoadConfig.class.getResourceAsStream("stupidconfig.txt");
    
    public static void load(boolean mc) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(file));
        tetr.shared.GameLogic.STAGESIZEX = Integer.valueOf(br.readLine());
        tetr.shared.GameLogic.STAGESIZEY = Integer.valueOf(br.readLine());
        tetr.shared.GameLogic.VISIBLEROWS = Integer.valueOf(br.readLine());
        tetr.shared.GameLogic.NEXTPIECESMAX = Integer.valueOf(br.readLine());
        tetr.normal.Constants.idLength = Integer.valueOf(br.readLine());
        tetr.normal.Constants.idCharSet = String.valueOf(br.readLine());
        if(mc) {
            for(int i=0;i<17;i++) {
                tetr.minecraft.constants.Blocks.blocks[i] = XMaterial.valueOf(br.readLine()).parseItem();
            }
        }
    }
}