package nl.esciencecenter.e3dchem.knime.silicosit;

import org.eclipse.core.runtime.Plugin;

/**
 * This is the eclipse bundle activator.
 * Note: KNIME node developers probably won't have to do anything in here,
 * as this class is only needed by the eclipse platform/plugin mechanism.
 * If you want to move/rename this file, make sure to change the plugin.xml
 * file in the project root directory accordingly.
 *
 */
public class Activator extends Plugin {
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
