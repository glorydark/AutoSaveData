package glorydark.autosave;

import cn.nukkit.Server;
import cn.nukkit.event.Listener;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;
import glorydark.autosave.utils.FileHandler;
import glorydark.autosave.utils.GetCalender;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

public class MainClass extends PluginBase implements Listener {
    public static String path = null;
    public static Integer saveInterval = null;
    @Override
    public void onLoad(){
        this.getLogger().info(TextFormat.YELLOW+"AutoSaveAndRestart onLoad!");
    }

    @Override
    public void onEnable(){
        this.getLogger().info(TextFormat.GREEN+"AutoSaveAndRestart onEnable!");
        this.saveDefaultConfig();
        TimeZone timeZone = TimeZone.getTimeZone("GMT+8");
        TimeZone.setDefault(timeZone);
        path = this.getDataFolder().getPath();
        Config config = new Config(path+"/config.yml");
        saveInterval = config.getInt("AutoSaveIntervals",60);
        int restartInterval = config.getInt("RestartIntervals", 60);
        this.getServer().getScheduler().scheduleDelayedTask(new AutoSaveTask(),saveInterval*120); //20ticks = 1s  1min = 60s -> 1min = 120ticks
        this.getServer().getCommandMap().register("保存文件夹",new SaveDataCommand("saveAllData"));
    }

    @Override
    public void onDisable(){
        this.getLogger().info(TextFormat.RED+"AutoSaveAndRestart onDisable!");
    }

    public static List<String> getSaveDirList(){
        Config config = new Config(path+"/config.yml");
        return new ArrayList<String>(config.getStringList("SaveDir"));
    }

    public static void saveAllData(){
        List<String> SaveDirPathList = MainClass.getSaveDirList();
        for(String dirpath: SaveDirPathList){
            String[] strings = dirpath.split("/");
            File file = new File(MainClass.path+"/"+GetCalender.getDateString());
            file.mkdir();
            try {
                FileHandler.copyDir(Server.getInstance().getDataPath()+"/"+dirpath,MainClass.path+"/"+GetCalender.getDateString() + "/" + strings[strings.length-1] + "/");
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.print("Save dictionary successfully!");
    }
}
