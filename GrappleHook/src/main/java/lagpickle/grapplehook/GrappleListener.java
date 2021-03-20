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
            switch (event.getAction()) {
                case LEFT_CLICK_AIR:
                case LEFT_CLICK_BLOCK:
                    RayTraceResult ray = player.rayTraceBlocks(
                        this.plugin.getMaxTetherLength());
                    if (ray != null) {
                        this.plugin.getGrappleManager().grappleLocation(
                            player,ray.getHitBlock().getLocation());
                    }
                    break;
                case RIGHT_CLICK_AIR:
                case RIGHT_CLICK_BLOCK:
                    this.plugin.getGrappleManager().unGrapple(player);
                    break;
            }
        }
    }

}
