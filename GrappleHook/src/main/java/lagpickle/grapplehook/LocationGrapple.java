package lagpickle.grapplehook;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class LocationGrapple extends BukkitRunnable {

    private GrappleHook plugin;
    private Player player;
    private Location hookLoc;
    private double length;
    private double length2;
    private int grappleTicks;

    public LocationGrapple(GrappleHook plugin,Player player,Location hookLoc) {
        this.plugin = plugin;
        this.player = player;
        this.hookLoc = hookLoc;
        this.length = player.getLocation().distance(hookLoc);
        this.length2 = this.length * this.length;
        this.grappleTicks = 0;
    }

    public void setGrappleTicks(int ticks) {
        this.grappleTicks = ticks;
    }

    @Override
    public void run() {
        // ungrapple after delay
        if (--this.grappleTicks < 0) {
            this.plugin.getGrappleManager().unGrapple(this.player);
            return;
        }
        Location playerLoc = this.player.getLocation();
        double distance2 = playerLoc.distanceSquared(this.hookLoc);
        // cancel grapple if player is too far
        if (distance2 >
                this.length2
                    + this.length*this.plugin.getTetherStretchLimit()
        ) {
            this.plugin.getGrappleManager().unGrapple(this.player);
            return;
        }
        // retract tether
        if (this.length > this.plugin.getMinTetherLength()
                && this.length2*this.plugin.getTetherStretchiness()
                    >= distance2
        ) {
            this.length -= this.plugin.getRetractionSpeed();
            this.length2 = this.length * this.length;
        }
        // find distance to new player position
        Location testLoc = playerLoc.clone();
        testLoc.add(this.player.getVelocity());
        try {
            distance2 = testLoc.distanceSquared(this.hookLoc);
        } catch (IllegalArgumentException e) {
            distance2 = Double.MAX_VALUE;
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
        // particles
        Location loc = player.getEyeLocation().add(0,0.5,0);
        Vector stepVec = this.hookLoc.clone().subtract(loc).toVector();
        double stepSize = this.plugin.getParticleStep();
        stepVec.normalize().multiply(stepSize);
        for (double i = 0; i < this.length; i += stepSize) {
            loc.getWorld().spawnParticle(
                Particle.REDSTONE, loc, 1, 0,0,0,
                this.plugin.getParticleOptions());
            loc.add(stepVec);
        }
    }

}
