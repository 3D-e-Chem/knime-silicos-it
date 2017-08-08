package nl.esciencecenter.e3dchem.knime.silicosit.execute;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

public class ExecuteConfig {
	public static final Set<String> PROGRAMS = new HashSet<>(Arrays.asList("align-it", "filter-it", "shape-it", "strip-it"));
	public SettingsModelString program = new SettingsModelString("program", null);
	public SettingsModelString argumentsColumn = new SettingsModelString("arguments", null);
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
}
