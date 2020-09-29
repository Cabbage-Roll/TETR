package cabbageroll.tetrdotjar;

import org.bukkit.scheduler.BukkitRunnable;

public class Playgame {
    public static void playgame() {
        new BukkitRunnable(){ //BukkitRunnable, not Runnable
             @Override
             public void run() {
                if(Cmain.counter >= 100){
                    if(!Position.isCollide(Position.x, Position.y + 1, Position.block_size)){
                        Moveblock.moveBlock(Position.x, Position.y + 1);
                        }else{
                            Cmain.placeBlock();
                            Cmain.checkPlaced();
                            Cmain.makenextblock();
                        }
                        Cmain.counter=0;
                }
                Cmain.counter+=0;
                
                if(Cmain.gameover) {
                    this.cancel();
                }
             }
        }.runTaskTimer(Pluginmain.plugin, 0, 5); //Repeating task with 0 ticks initial delay, run once per 20 ticks (one second). Make sure you pass a valid instance of your plugin.
    }
}
