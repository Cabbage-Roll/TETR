package tetr.minecraft.functions;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import tetr.minecraft.functions.sendBlockChangeCustom.SendBlockChangeCustom_V2;

public class Functions_1_15_R1 implements Functions {

    @Override
    public void sendTitleCustom(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
    }

    @Override
    public void sendBlockChangeCustom(Player player, Location loc, int color) {
        SendBlockChangeCustom_V2.sendBlockChangeCustom(player, loc, color);
    }

    @Override
    public void sendBlockChangeCustom(Player player, Location loc, Block block) {
        SendBlockChangeCustom_V2.sendBlockChangeCustom(player, loc, block);
    }

    @Override
    public void sendActionBarCustom(Player player, String message) {
        // TODO Auto-generated method stub
        
    }
}
