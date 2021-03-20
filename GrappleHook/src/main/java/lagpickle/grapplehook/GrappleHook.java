package lagpickle.grapplehook;

import org.bukkit.Color;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

public class GrappleHook extends JavaPlugin {

    private GrappleManager grappleManager;
    private GrappleListener grappleListener;
    private CommandReload commandReload;
    private CommandGrappleGive commandGive;

    private final NamespacedKey itemKey;

    public GrappleHook() {
        this.grappleManager = new GrappleManager(this);
        this.grappleListener = new GrappleListener(this);
        this.commandReload = new CommandReload(this);
        this.commandGive = new CommandGrappleGive(this);
        this.itemKey = new NamespacedKey(this,"GrappleHook");
    }

    @Override
    public void onEnable() {
        this.readConfig();
        getServer().getPluginManager().registerEvents(this.grappleListener,this);
        getCommand("grapplehook-reload").setExecutor(this.commandReload);
        getCommand("grapplehook-give").setExecutor(this.commandGive);
    }

    public GrappleManager getGrappleManager() {
        return this.grappleManager;
    }
    public GrappleListener getGrappleListener() {
        return this.grappleListener;
    }

    private Particle.DustOptions particleOptions;
    public void readConfig() {
        saveDefaultConfig();
        reloadConfig();

        ConfigurationSection config = this.getConfig();

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
    public int getUngrappleDelay() {
        return getConfig().getInt("ungrapple-delay");
    }
    public double getRetractionSpeed() {
        return getConfig().getDouble("retraction-speed");
    }
    public double getMinTetherLength() {
        return getConfig().getDouble("min-tether-length");
    }
    public double getMaxTetherLength() {
        return getConfig().getDouble("max-tether-length");
    }
    public double getTetherStretchiness() {
        return getConfig().getDouble("tether-stretchiness");
    }
    public double getTetherStretchLimit() {
        return this.getConfig().getDouble("tether-stretch-limit");
    }
    public Particle.DustOptions getParticleOptions() {
        return this.particleOptions;
    }
    public double getParticleStep() {
        return this.getConfig().getDouble("particle-options.step");
    }

    public NamespacedKey getItemKey() {
        return this.itemKey;
    }
}
