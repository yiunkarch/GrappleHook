package lagpickle.grapplehook;

import java.util.HashMap;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.RayTraceResult;

public class GrappleListener implements Listener {

    private GrappleHook plugin;

    public GrappleListener(GrappleHook plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        if (event.hasItem()) {
            Player player = event.getPlayer();
            GrappleManager gm = this.plugin.getGrappleManager();
            switch (event.getAction()) {
                case RIGHT_CLICK_AIR:
                case RIGHT_CLICK_BLOCK:
                    // renew grapple duration or launch new grapple
                    if (gm.isGrappled(player)) {
                        gm.renewGrapple(player);
                    } else {
                        RayTraceResult ray = player.rayTraceBlocks(
                            this.plugin.getMaxTetherLength());
                        if (ray != null) {
                            gm.grappleLocation(
                                player,ray.getHitBlock().getLocation());
                        }
                    }
            }
        }
    }

}
