package tetr;

import org.bukkit.Sound;

public class SoundUtil {
    public static Sound ORB_PICKUP, NOTE_HARP, NOTE_PLING, THUNDER, VILLAGER_NO;

    static{
        if(Main.version.contains("1_8")){
            ORB_PICKUP=Sound.valueOf("ORB_PICKUP");
            NOTE_HARP=Sound.valueOf("NOTE_PIANO");
            NOTE_PLING=Sound.valueOf("NOTE_PLING");
            THUNDER=Sound.valueOf("AMBIENCE_THUNDER");
            VILLAGER_NO=Sound.valueOf("VILLAGER_NO");
        }else if(Main.version.contains("1_9")){
        }else if(Main.version.contains("1_10")){
        }else if(Main.version.contains("1_11")){
        }else if(Main.version.contains("1_12")){
            ORB_PICKUP=Sound.valueOf("ENTITY_EXPERIENCE_ORB_PICKUP");
            NOTE_HARP=Sound.valueOf("BLOCK_NOTE_HARP");
            NOTE_PLING=Sound.valueOf("BLOCK_NOTE_PLING");
            THUNDER=Sound.valueOf("ENTITY_LIGHTNING_THUNDER");
            VILLAGER_NO=Sound.valueOf("ENTITY_VILLAGER_NO");
        }else if(Main.version.contains("1_13")){
            
        }else if(Main.version.contains("1_14")){
            
        }else if(Main.version.contains("1_15")){
            
        }else if(Main.version.contains("1_16")){
            ORB_PICKUP=Sound.valueOf("ENTITY_EXPERIENCE_ORB_PICKUP");
            NOTE_HARP=Sound.valueOf("BLOCK_NOTE_BLOCK_HARP");
            NOTE_PLING=Sound.valueOf("BLOCK_NOTE_BLOCK_PLING");
            THUNDER=Sound.valueOf("ENTITY_LIGHTNING_BOLT_THUNDER");
            VILLAGER_NO=Sound.valueOf("ENTITY_VILLAGER_NO");
        }else{
            ORB_PICKUP=null;
            NOTE_HARP=null;
            NOTE_PLING=null;
            THUNDER=null;
            VILLAGER_NO=null;
        }
    }
}