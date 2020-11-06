package cabbageroll.tetr.functions;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import cabbageroll.tetr.Main;
import cabbageroll.tetr.Table;

public class SendBlockChangeCustom {

    @SuppressWarnings("deprecation")
    public static void sendBlockChangeCustom(Player player, Location loc, int color){
        if(Main.version.contains("1_8") || Main.version.contains("1_9") || Main.version.contains("1_10") || Main.version.contains("1_11") || Main.version.contains("1_12")){
            player.sendBlockChange(loc, Table.blocks[color].getType(), Table.blocks[color].getData().getData());
        }else{
            player.sendBlockChange(loc, Table.blocks[color].getType().createBlockData());
        }
    }
    
    @SuppressWarnings("deprecation")
    public static void sendBlockChangeCustom(Player player, Location loc, Block block){
        if(Main.version.contains("1_8") || Main.version.contains("1_9") || Main.version.contains("1_10") || Main.version.contains("1_11") || Main.version.contains("1_12")){
            player.sendBlockChange(loc, block.getType(), block.getData());
        }else{
            player.sendBlockChange(loc, block.getBlockData());
        }
    }
    
    
}
