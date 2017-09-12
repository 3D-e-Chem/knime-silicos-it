package nl.esciencecenter.e3dchem.knime.silicosit.execute;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

public class ExecuteConfig {
    public static final Set<String> PROGRAMS = Collections
            .unmodifiableSet(new TreeSet<>(Arrays.asList("align-it", "filter-it", "shape-it", "strip-it")));
    private SettingsModelString program = new SettingsModelString("program", "align-it");
    private SettingsModelString argumentsColumn = new SettingsModelString("arguments", null);
	public void saveSettingsTo(NodeSettingsWO settings) {
		program.saveSettingsTo(settings);
		argumentsColumn.saveSettingsTo(settings);
		
	}
	public void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {
		program.validateSettings(settings);
		argumentsColumn.validateSettings(settings);
		
	}
	public void loadValidatedSettingsFrom(NodeSettingsRO settings) throws InvalidSettingsException {
		program.loadSettingsFrom(settings);
		argumentsColumn.loadSettingsFrom(settings);
	}

    public SettingsModelString getProgram() {
        return program;
    }

    public SettingsModelString getArgumentsColumn() {
        return argumentsColumn;
    }

    public String getProgramValue() {
        return program.getStringValue();
    }

    public String getArgumentsColumnValue() {
        return argumentsColumn.getStringValue();
    }

}
