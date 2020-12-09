package tetr.functions;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import tetr.Main;
import tetr.constants.Blocks;

public class SendBlockChangeCustom {
    
    @SuppressWarnings("deprecation")
    public static void sendBlockChangeCustom(Player player, Location loc, int color){
        ItemStack blocks[] = Main.skinmap.get(player);
        
        if(Main.version.contains("1_8") || Main.version.contains("1_9") || Main.version.contains("1_10") || Main.version.contains("1_11") || Main.version.contains("1_12")){
            if(Main.skineditorver.get(player)==1) {
                player.sendBlockChange(loc, blocks[color].getType(), blocks[color].getData().getData());
            }else if(Main.skineditorver.get(player)==0) {
                player.sendBlockChange(loc, Blocks.blocks[color].getType(), Blocks.blocks[color].getData().getData());
            }
        }else{
            if(Main.skineditorver.get(player)==1) {
                player.sendBlockChange(loc, blocks[color].getType().createBlockData());
            }else if(Main.skineditorver.get(player)==0) 
                player.sendBlockChange(loc, Blocks.blocks[color].getType().createBlockData());
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
