package nl.esciencecenter.e3dchem.knime.silicosit.preferences;

import org.eclipse.jface.preference.*;
import org.eclipse.ui.IWorkbenchPreferencePage;

import nl.esciencecenter.e3dchem.knime.silicosit.Activator;

import org.eclipse.ui.IWorkbench;

/**
 * This class represents a preference page that
 * is contributed to the Preferences dialog. By 
 * subclassing <samp>FieldEditorPreferencePage</samp>, we
 * can use the field support built into JFace that allows
 * us to create a page that is small and knows how to 
 * save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They
 * are stored in the preference store that belongs to
 * the main plug-in class. That way, preferences can
 * be accessed directly via the preference store.
 */

public class PreferencePage
	extends FieldEditorPreferencePage
	implements IWorkbenchPreferencePage {

	public PreferencePage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("Preferences for Silicos-it KNIME nodes");
	}
	
	/**
	 * Creates the field editors. Field editors are abstractions of
	 * the common GUI blocks needed to manipulate various types
	 * of preferences. Each field editor knows how to save and
	 * restore itself.
	 */
	public void createFieldEditors() {
		addField(new DirectoryFieldEditor(PreferenceConstants.SILICOSIT_BINDIR, 
				"Directory with Silicos-it binaries:", getFieldEditorParent()));
		// TODO validate SILICOSIT_BINDIR contains binaries
		addField(new DirectoryFieldEditor(PreferenceConstants.BABEL_LIBDIR, 
				"Directory with Open Babel plugin libraries:", getFieldEditorParent()));
		addField(new DirectoryFieldEditor(PreferenceConstants.BABEL_DATADIR, 
				"Directory with Open Babel data files:", getFieldEditorParent()));
	}

	@Override
	public void init(IWorkbench workbench) {
		// nothing to do here, already initialized in constructor
	}
}