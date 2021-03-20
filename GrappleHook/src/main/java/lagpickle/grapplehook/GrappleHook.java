package lagpickle.grapplehook;

import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

public class GrappleHook extends JavaPlugin {

    private GrappleManager grappleManager;
    private GrappleListener grappleListener;
    private GrappleHookCommand grappleHookCommand;

    public GrappleHook() {
        this.grappleManager = new GrappleManager(this);
        this.grappleListener = new GrappleListener(this);
        this.grappleHookCommand = new GrappleHookCommand(this);
    }

    @Override
    public void onEnable() {
        this.readConfig();
        getServer().getPluginManager().registerEvents(this.grappleListener,this);
        getCommand("grapplehook-reload").setExecutor(this.grappleHookCommand);
    }

    public GrappleManager getGrappleManager() {
        return this.grappleManager;
    }
    public GrappleListener getGrappleListener() {
        return this.grappleListener;
    }

    private double maxTetherLength2;
    private Particle.DustOptions particleOptions;
    public void readConfig() {
        saveDefaultConfig();
        reloadConfig();

        ConfigurationSection config = this.getConfig();

        double maxTether = config.getDouble("max-tether-length");
        this.maxTetherLength2 = maxTether * maxTether;

        try {
            this.particleOptions = new Particle.DustOptions(
                Color.fromRGB(
                    config.getInt("particle-options.red"),
                    config.getInt("particle-options.green"),
                    config.getInt("particle-options.blue")
                ),
                Double.valueOf(config.getDouble("particle-options.size")).floatValue()
            );
        } catch (IllegalArgumentException e) {
            getLogger().warning("Particle options invalid, using defaults");
            this.particleOptions = new Particle.DustOptions(Color.GRAY,1);
        }
    }
    public int getHookTimeout() {
        return this.getConfig().getInt("hook-timeout");
    }
    public double getRetractionSpeed() {
        return getConfig().getDouble("retraction-speed");
    }
    public double getMinTetherLength() {
        return getConfig().getDouble("min-tether-length");
    }
    public double getMaxTetherLength2() {
        return this.maxTetherLength2;
    }
    public Particle.DustOptions getParticleOptions() {
        return this.particleOptions;
    }
    public double getParticleStep() {
        return this.getConfig().getDouble("particle-options.step");
    }
}
