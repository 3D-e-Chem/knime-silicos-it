package nl.esciencecenter.e3dchem.knime.silicosit.preferences;

import java.io.File;
import java.util.Collection;

import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.swt.widgets.Composite;

public class DirectoryWithExecutablesFieldEditor extends DirectoryFieldEditor {
    private Collection<String> executables;

    /**
     * 
     * @param name
     *            the name of the preference this field editor works on
     * @param labelText
     *            the label text of the field editor
     * @param parent
     *            the parent of the field editor's control
     * @param executables
     *            All executable filenames expected in the directory
     */
    public DirectoryWithExecutablesFieldEditor(String name, String labelText, Composite parent, Collection<String> executables) {
        super(name, labelText, parent);
        this.executables = executables;
    }

    @Override
    protected boolean doCheckState() {
        boolean isValid = super.doCheckState();
        if (!isValid) {
            // don't check executables when field is invalid
            return isValid;
        }
        File dir = new File(getStringValue());
        isValid = executables.stream().map(e -> new File(dir, e)).allMatch(c -> c.canExecute());
        if (!isValid) {
            setErrorMessage(String.format("Could not find all executables (%s) in directory",
                    String.join(", ", executables.toArray(new String[] {}))));
        }
        return isValid;
    }
}
