package glorydark.autosave;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class SaveDataCommand extends Command {
    public SaveDataCommand(String s) {
        super(s,"自动保存特定文件夹!","saveAllData");
    }

    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        if(commandSender instanceof Player) {
            if(commandSender.isOp()){
                MainClass.saveAllData();
                commandSender.sendMessage("正在执行保存命令！");
            }else{
                commandSender.sendMessage("您没有权限！");
            }
        }else{
            MainClass.saveAllData();
        }
        return true;
    }
}
