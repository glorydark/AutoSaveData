package glorydark.autosave;

import cn.nukkit.Server;
import cn.nukkit.event.Listener;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.plugin.PluginLogger;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;
import glorydark.autosave.utils.FileHandler;
import glorydark.autosave.utils.GetCalender;
import javafx.concurrent.Task;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

public class MainClass extends PluginBase implements Listener {
    public static String path = null;
    public static Integer saveInterval = null;
    public static PluginLogger logger;
    public static Boolean save = false;
    public static MainClass instance;
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
        logger = this.getLogger();
        instance = this;
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
        if(save == true){
            logger.warning("您还需要等待一段时间才可再次保存！");
            return;
        }else {
            save = true;
            List<String> SaveDirPathList = MainClass.getSaveDirList();
            File file = new File(MainClass.path + "/" + GetCalender.getDateString());
            for (String dirpath : SaveDirPathList) {
                String[] strings = dirpath.split("/");
                file.mkdir();
                String savePath = MainClass.path + "/" + GetCalender.getDateString() + "/" + strings[strings.length - 1] + "/";
                try {
                    FileHandler.copyDir(Server.getInstance().getDataPath() + "/" + dirpath, savePath);
                } catch (IOException e) {
                    logger.error(TextFormat.GREEN + "复制文件夹失败，请尝试过几秒后重试!");
                    e.printStackTrace();
                    Server.getInstance().getScheduler().scheduleDelayedTask(instance, () -> {
                        save = false;
                        logger.info("您可以开始下一次保存了！");
                    }, 20);
                }
                logger.info(TextFormat.GREEN + "保存文件夹【" + strings[strings.length - 1] + "】成功!");
            }
            try {
                FileHandler.generateFile(MainClass.path + "/" + GetCalender.getDateString(), "zip");
                logger.info(TextFormat.GREEN + "创建压缩文件【" + GetCalender.getDateString() + ".zip】成功!");
                FileHandler.delete(file);
                Server.getInstance().getScheduler().scheduleDelayedTask(instance, () -> {
                    save = false;
                    logger.info("您可以开始下一次保存了！");
                }, 20);
            } catch (Exception e) {
                logger.error(TextFormat.GREEN + "创建压缩文件失败，文件夹以为您保存到相应位置，请尝试过几秒后重试!");
                Server.getInstance().getScheduler().scheduleDelayedTask(instance, () -> {
                    save = false;
                    logger.info("您可以开始下一次保存了！");
                }, 20);
                e.printStackTrace();
            }
        }
    }
}
