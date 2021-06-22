package glorydark.autosave;

import cn.nukkit.Server;
import cn.nukkit.scheduler.Task;
import cn.nukkit.utils.TextFormat;

public class AutoSaveTask extends Task implements Runnable {
    public void onRun(int i) {
        MainClass.saveAllData();
        Server.getInstance().getScheduler().scheduleDelayedTask(new Task() {
            public void onRun(int i) {
                MainClass.saveAllData();
                Server.getInstance().getLogger().info(TextFormat.YELLOW+"[AutoSaveOrRestart]自动保存需保存文件夹");
            }
        },60*MainClass.saveInterval);
    }
}
