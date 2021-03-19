package lagpickle.grapplehook;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class LocationGrapple extends BukkitRunnable {

    private static final double MAX_DISTANCE2 = 1024;
    private static final double MIN_LENGTH = 4;
    private static final double RETRACTION_SPEED = 0.4;

    private GrappleManager manager;
    private Player player;
    private Location hookLoc;
    private double length;
    private double length2;

    public LocationGrapple(GrappleManager manager,Player player,Location hookLoc) {
        this.manager = manager;
        this.player = player;
        this.hookLoc = hookLoc;
        this.length = player.getLocation().distance(hookLoc);
        this.length2 = this.length * this.length;
    }

    @Override
    public void run() {
        if (this.length > MIN_LENGTH) {
            this.length -= RETRACTION_SPEED;
            this.length2 = this.length * this.length;
        }
        // find distance to new player position
        double distance2;
        Location playerLoc = this.player.getLocation();
        Location testLoc = playerLoc.clone();
        testLoc.add(this.player.getVelocity());
        try {
            distance2 = testLoc.distanceSquared(this.hookLoc);
        } catch (IllegalArgumentException e) {
            distance2 = Double.MAX_VALUE;
        }
        // cancel grapple if player is too far
        if (distance2 > MAX_DISTANCE2) {
            this.manager.unGrapple(this.player);
            return;
        }
        // if player is "pulling" on the grapple line
        if (distance2 > this.length2) {
            Location newLoc = this.hookLoc.clone().add(
                testLoc.subtract(this.hookLoc).toVector()
                    .normalize()
                    .multiply(this.length)
            );
            Vector newVelocity = newLoc.subtract(playerLoc).toVector();
            this.player.setVelocity(newVelocity);
        }
    }

}
