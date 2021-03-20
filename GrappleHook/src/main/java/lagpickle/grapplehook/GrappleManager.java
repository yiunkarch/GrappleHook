package lagpickle.grapplehook;

import java.util.HashMap;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class GrappleManager {

    private GrappleHook plugin;
    private HashMap<Player,LocationGrapple> grapples;

    public GrappleManager(GrappleHook plugin) {
        this.plugin = plugin;
        this.grapples = new HashMap<>();
    }

    public boolean isGrappled(Player player) {
        return this.grapples.containsKey(player);
    }

    public void grappleLocation(Player player, Location location) {
        // cancel running grapple task if one exists
        if (this.grapples.containsKey(player)) {
            this.grapples.get(player).cancel();
        }
        LocationGrapple grapple = new LocationGrapple(
            this.plugin,player,location);
        this.grapples.put(player,grapple);
        this.renewGrapple(player);
        grapple.runTaskTimer(this.plugin,0,1);
    }

    public void unGrapple(Player player) {
        if (this.grapples.containsKey(player)) {
            this.grapples.get(player).cancel();
            this.grapples.remove(player);
        }
    }

    public void renewGrapple(Player player) {
        if (this.grapples.containsKey(player)) {
            this.grapples.get(player).setGrappleTicks(
                this.plugin.getUngrappleDelay());
        }
    }
}
