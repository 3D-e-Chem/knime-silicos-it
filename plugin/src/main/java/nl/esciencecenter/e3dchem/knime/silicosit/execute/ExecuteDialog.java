package nl.esciencecenter.e3dchem.knime.silicosit.execute;

import org.knime.core.data.collection.ListDataValue;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentColumnNameSelection;
import org.knime.core.node.defaultnodesettings.DialogComponentStringSelection;

public class ExecuteDialog extends DefaultNodeSettingsPane {

	@SuppressWarnings("unchecked")
	public ExecuteDialog() {
		super();
		
		ExecuteConfig config = new ExecuteConfig();
		
        addDialogComponent(new DialogComponentStringSelection(config.getProgram(), "Program", ExecuteConfig.PROGRAMS));
        addDialogComponent(new DialogComponentColumnNameSelection(config.getArgumentsColumn(), "Column with arguments", 0,
                ListDataValue.class));
	}

}
