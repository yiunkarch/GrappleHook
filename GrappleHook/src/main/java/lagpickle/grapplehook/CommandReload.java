package lagpickle.grapplehook;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandReload implements CommandExecutor {

    private GrappleHook plugin;

    public CommandReload(GrappleHook plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender,Command command,String label,String[] args) {
        this.plugin.readConfig();
        sender.sendMessage("Reloading plugin config!");
        return true;
    }

}
