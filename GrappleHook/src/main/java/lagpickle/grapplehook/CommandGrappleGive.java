package lagpickle.grapplehook;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class CommandGrappleGive implements CommandExecutor {

    private GrappleHook plugin;

    public CommandGrappleGive(GrappleHook plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender,Command command,String label,String[] args) {
        Player player;
        if (args.length < 1) {
            if (sender instanceof Player) {
                player = (Player)sender;
            } else {
                sender.sendMessage("Target is not a player");
                return true;
            }
        } else {
            player = this.plugin.getServer().getPlayer(args[0]);
            if (player == null) {
                sender.sendMessage("Player not found");
                return true;
            }
        }
        ItemStack item = player.getInventory().getItemInMainHand();
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.getPersistentDataContainer().set(
                this.plugin.getItemKey(),PersistentDataType.BYTE,(byte)1);
            item.setItemMeta(meta);
            sender.sendMessage("Turned item into grapple");
        } else {
            sender.sendMessage("Could not modify item");
        }
        return true;
    }

}
