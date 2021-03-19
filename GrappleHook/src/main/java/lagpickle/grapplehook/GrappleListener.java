package lagpickle.grapplehook;

import java.util.HashMap;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class GrappleListener implements Listener {

    private static final int HOOK_TIMEOUT = 10;

    private GrappleHook plugin;
    private HashMap<Projectile,Player> hooks;

    public GrappleListener(GrappleHook plugin) {
        this.plugin = plugin;
        this.hooks = new HashMap<>();
    }

    private void launchHook(Player player) {
        Arrow hook = player.launchProjectile(Arrow.class);
        hook.setPickupStatus(Arrow.PickupStatus.DISALLOWED);
        this.hooks.put(hook,player);
        // remove hook after delay
        this.plugin.getServer().getScheduler().runTaskLater(
            this.plugin, () -> this.removeHook(hook), HOOK_TIMEOUT);
    }

    private void removeHook(Projectile hook) {
        this.hooks.remove(hook);
        hook.remove();
    }

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        if (event.hasItem()) {
            switch (event.getAction()) {
                case LEFT_CLICK_AIR:
                case LEFT_CLICK_BLOCK:
                    Player player = event.getPlayer();
                    GrappleManager gm = this.plugin.getGrappleManager();
                    // relaunch hook if hook is still flying
                    if (this.hooks.containsValue(player)) {
                        Projectile hook = this.hooks.entrySet().stream()
                            .filter(e -> e.getValue() == player)
                            .map(e -> e.getKey())
                            .findFirst()
                            .orElse(null);
                        this.removeHook(hook);
                    }
                    // toggle grapple state
                    if (gm.isGrappled(player)) {
                        gm.unGrapple(player);
                    } else {
                        launchHook(player);
                    }
            }
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        Projectile projectile = event.getEntity();
        // if projectile is a grappling hook
        if (this.hooks.containsKey(projectile)) {
            // if projectile hit a block
            if (event.getHitBlock() != null) {
                this.plugin.getGrappleManager().grappleLocation(
                    this.hooks.get(projectile),
                    event.getHitBlock().getLocation()
                );
            }
            // remove the hook
            this.removeHook(projectile);
            event.setCancelled(true);
        }
    }

}
