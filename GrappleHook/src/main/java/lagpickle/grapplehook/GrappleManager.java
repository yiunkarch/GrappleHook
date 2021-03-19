package lagpickle.grapplehook;

import java.util.HashMap;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class GrappleManager {

    private GrappleHook plugin;
    private HashMap<Player,BukkitTask> grapples;

    public GrappleManager(GrappleHook plugin) {
        this.plugin = plugin;
        this.grapples = new HashMap<>();
    }

    public boolean isGrappled(Player player) {
        return this.grapples.containsKey(player);
    }

    public void grappleLocation(Player player, Location location) {
        this.grapples.put(player,null);
    }

    public void unGrapple(Player player) {
        if (this.grapples.containsKey(player)) {
            this.grapples.remove(player);
        }
    }
}
