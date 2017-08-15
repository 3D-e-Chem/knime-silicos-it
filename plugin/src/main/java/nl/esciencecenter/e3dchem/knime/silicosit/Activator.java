package nl.esciencecenter.e3dchem.knime.silicosit;

import org.eclipse.ui.plugin.AbstractUIPlugin;

public class Activator extends AbstractUIPlugin {
	 // The plug-in ID
    public static final String PLUGIN_ID = "nl.esciencecenter.e3dchem.knime.silicosit.plugin";
    
    // The shared instance.
    private static Activator plugin;

    /**
     * The constructor.
     */
    public Activator() {
        super();
        plugin = this;
    }
    
    /**
     * Returns the shared instance.
     *
     * @return Singleton instance of the Plugin
     */
    public static Activator getDefault() {
        return plugin;
    }
}
