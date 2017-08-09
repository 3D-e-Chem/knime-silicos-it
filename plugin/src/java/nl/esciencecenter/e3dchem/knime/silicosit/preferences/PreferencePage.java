package nl.esciencecenter.e3dchem.knime.silicosit.preferences;

import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import nl.esciencecenter.e3dchem.knime.silicosit.Activator;

public class PreferencePage
	extends FieldEditorPreferencePage
	implements IWorkbenchPreferencePage {

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
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("Preferences for Silicos-it KNIME nodes");
	}
}