package tetr.minecraft;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.xxmicloxx.NoteBlockAPI.model.Playlist;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder;

import tetr.minecraft.functions.Functions;
import tetr.minecraft.functions.Functions_1_10_R1;
import tetr.minecraft.functions.Functions_1_11_R1;
import tetr.minecraft.functions.Functions_1_12_R1;
import tetr.minecraft.functions.Functions_1_13_R1;
import tetr.minecraft.functions.Functions_1_13_R2;
import tetr.minecraft.functions.Functions_1_14_R1;
import tetr.minecraft.functions.Functions_1_15_R1;
import tetr.minecraft.functions.Functions_1_16_R1;
import tetr.minecraft.functions.Functions_1_16_R2;
import tetr.minecraft.functions.Functions_1_16_R3;
import tetr.minecraft.functions.Functions_1_8_R1;
import tetr.minecraft.functions.Functions_1_8_R2;
import tetr.minecraft.functions.Functions_1_8_R3;
import tetr.minecraft.functions.Functions_1_9_R1;
import tetr.minecraft.functions.Functions_1_9_R2;
import tetr.minecraft.menus.Listen;
import tetr.shared.LoadConfig;
public class Main extends JavaPlugin implements Listener{
    
    public static JavaPlugin plugin;
    public static ConsoleCommandSender console;
    
    public static LinkedHashMap<String,Room> roommap = new LinkedHashMap<String,Room>();
    public static HashMap<Player,Room> inwhichroom = new HashMap<Player,Room>();

    public static HashMap<Player,String> lastui = new HashMap<Player,String>();
    public static HashMap<Player,Integer> joinroompage = new HashMap<Player,Integer>();
    
    public static HashMap<Player,Integer> skineditorver = new HashMap<Player,Integer>();
    public static HashMap<Player,ItemStack[]> skinmap = new HashMap<Player,ItemStack[]>();
    
    public static void saveCustomYml(FileConfiguration ymlConfig, File ymlFile) {
        try{
            ymlConfig.save(ymlFile);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
    public static Functions functions;

    public static int numberofsongs;
    String[] pathnames;
    String xd;
    static Song[] songarray;
    private static String version;
    
    @Override
    public void onEnable(){
        long timeStart = System.currentTimeMillis();
        
        plugin=this;
        
        try {
            LoadConfig.load(true);
        } catch (IOException e) {
            getLogger().warning("Error");
            // TODO Auto-generated catch block
            // e.printStackTrace();
        }
        
        console=getServer().getConsoleSender();
        this.getCommand("tetr").setExecutor(new OpenMenu());
        
        //detect events
        getServer().getPluginManager().registerEvents(new Listen(), this);
        getServer().getPluginManager().registerEvents(this, this);
        
        //trash
        File f = new File(this.getDataFolder()+"/songs");
        f.mkdirs();
        numberofsongs=f.listFiles().length;
        if(numberofsongs>0){
            
            getLogger().info(numberofsongs+" song(s) found");
            
            pathnames=new String[numberofsongs];
            songarray=new Song[numberofsongs];
            pathnames = f.list();
            for(int i=0;i<numberofsongs;i++){
                xd=this.getDataFolder()+"/songs/"+pathnames[i];
                songarray[i]=NBSDecoder.parse(new File(xd));
            }
            
            Room.slist=new Playlist(songarray);
            //tRASH end
        }else{
            getLogger().info("No songs detected. Please add some songs!");
        }
        
        getLogger().info("Split: " + (System.currentTimeMillis() - timeStart) + "ms");
        
        if (setupActionbar()) {

            Bukkit.getPluginManager().registerEvents(this, this);

            getLogger().info("TETR: success");
            

        } else {

            getLogger().severe("TETR: not");
            getLogger().severe("version: "+Bukkit.getServer().getClass().getPackage().getName());

            Bukkit.getPluginManager().disablePlugin(this);
        }
        
        for(Player player: Bukkit.getOnlinePlayers()) {
            lastui.put(player, "home");

            if(!Main.skineditorver.containsKey(player)) {
                Main.skineditorver.put(player, 0);
            }
            
            initSkin(player);
        }
        
        long timeEnd = System.currentTimeMillis();
        
        long timeElapsed = timeEnd - timeStart;
        
        getLogger().info("Done. Time elapsed: " + timeElapsed + "ms");
        
        //add update checker
        //https://www.spigotmc.org/wiki/creating-an-update-checker-that-checks-for-updates/
        
    }
    
    private boolean setupActionbar() {
        try {

            version=Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

        } catch (ArrayIndexOutOfBoundsException whatVersionAreYouUsingException) {
            return false;
        }

        getLogger().info("Your server is running version " + version);

        if(version.equals("v1_8_R1")){
            functions=new Functions_1_8_R1();
        }else if(version.equals("v1_8_R2")){
            functions=new Functions_1_8_R2();
        }else if(version.equals("v1_8_R3")){
            functions=new Functions_1_8_R3();
        }else if(version.equals("v1_9_R1")){
            functions=new Functions_1_9_R1();
        }else if(version.equals("v1_9_R2")){
            functions=new Functions_1_9_R2();
        }else if(version.equals("v1_10_R1")){
            functions=new Functions_1_10_R1();
        }else if(version.equals("v1_11_R1")){
            functions=new Functions_1_11_R1();
        }else if(version.equals("v1_12_R1")){
            functions=new Functions_1_12_R1();
        }else if(version.equals("v1_13_R1")){
            functions=new Functions_1_13_R1();
        }else if(version.equals("v1_13_R2")){
            functions=new Functions_1_13_R2();
        }else if(version.equals("v1_14_R1")){
            functions=new Functions_1_14_R1();
        }else if(version.equals("v1_15_R1")){
            functions=new Functions_1_15_R1();
        }else if(version.equals("v1_16_R1")){
            functions=new Functions_1_16_R1();
        }else if(version.equals("v1_16_R2")){
            functions=new Functions_1_16_R2();
        }else if(version.equals("v1_16_R3")){
            functions=new Functions_1_16_R3();
        }
        
        return functions!=null;
    }
    
    @Override
    public void onDisable(){
        plugin=null;
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        lastui.put(player, "home");
        initSkin(player);
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        if(lastui.containsKey(player)) {
            lastui.remove(player);
        }
        if(inwhichroom.containsKey(player)) {
            Main.inwhichroom.get(player).removePlayer(player);
        }
    }
    
    public void initSkin(Player player) {
        File customYml = new File(Main.plugin.getDataFolder() + "/userdata/" + player.getUniqueId() + ".yml");
        FileConfiguration customConfig = YamlConfiguration.loadConfiguration(customYml);
        ItemStack[] blocks = new ItemStack[17];
        blocks[0] = customConfig.getItemStack("blockZ");
        blocks[1] = customConfig.getItemStack("blockL");
        blocks[2] = customConfig.getItemStack("blockO");
        blocks[3] = customConfig.getItemStack("blockS");
        blocks[4] = customConfig.getItemStack("blockI");
        blocks[5] = customConfig.getItemStack("blockJ");
        blocks[6] = customConfig.getItemStack("blockT");
        blocks[7] = customConfig.getItemStack("background");
        blocks[8] = customConfig.getItemStack("garbage");
        blocks[9] = customConfig.getItemStack("ghostZ");
        blocks[10] = customConfig.getItemStack("ghostL");
        blocks[11] = customConfig.getItemStack("ghostO");
        blocks[12] = customConfig.getItemStack("ghostS");
        blocks[13] = customConfig.getItemStack("ghostI");
        blocks[14] = customConfig.getItemStack("ghostJ");
        blocks[15] = customConfig.getItemStack("ghostT");
        blocks[16] = customConfig.getItemStack("zone");
        skinmap.put(player, blocks);
        Main.skineditorver.put(player, customConfig.getInt("useSkinSlot"));
    }
}