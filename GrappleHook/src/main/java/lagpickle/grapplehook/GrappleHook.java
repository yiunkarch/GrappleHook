package lagpickle.grapplehook;

import org.bukkit.plugin.java.JavaPlugin;

public class GrappleHook extends JavaPlugin {

    private GrappleManager grappleManager;
    private GrappleListener grappleListener;

    public GrappleHook() {
        this.grappleManager = new GrappleManager(this);
        this.grappleListener = new GrappleListener(this);
    }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this.grappleListener,this);
    }

    public GrappleManager getGrappleManager() {
        return this.grappleManager;
    }
    public GrappleListener getGrappleListener() {
        return this.grappleListener;
    }

}
