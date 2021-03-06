package nl.esciencecenter.e3dchem.knime.silicosit.preferences;

import java.io.IOException;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.osgi.framework.Bundle;

import nl.esciencecenter.e3dchem.knime.silicosit.Activator;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		
		String silicositBinDir = findPathInBundle("bin");
		store.setDefault(PreferenceConstants.SILICOSIT_BINDIR, silicositBinDir);
		String babelLibDir = findPathInBundle("lib/plugins");
		store.setDefault(PreferenceConstants.BABEL_LIBDIR, babelLibDir);
		String babelDataDir = findPathInBundle("data");
		store.setDefault(PreferenceConstants.BABEL_DATADIR, babelDataDir);
	}

	private String findPathInBundle(String path) {
		Bundle bundle = Platform.getBundle(Activator.PLUGIN_ID);
		try {
			return FileLocator.toFileURL(FileLocator.find(bundle, new Path(path), null)).getPath();
		} catch (IOException e) {
			return "";
		}
	}
}
