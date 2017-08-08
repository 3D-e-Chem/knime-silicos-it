package nl.esciencecenter.e3dchem.knime.silicosit.execute;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.RowKey;
import org.knime.core.data.collection.ListCell;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.IntCell;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import nl.esciencecenter.e3dchem.knime.silicosit.Activator;
import nl.esciencecenter.e3dchem.knime.silicosit.preferences.PreferenceConstants;

public class ExecuteNode extends NodeModel {
	private ExecuteConfig config = new ExecuteConfig();

	protected ExecuteNode() {
		super(1, 1);
	}

	@Override
	protected void loadInternals(File nodeInternDir, ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
		// nothing to do, no internals
	}

	@Override
	protected void saveInternals(File nodeInternDir, ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
		// nothing to do, no internals
	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings) {
		config.saveSettingsTo(settings);
	}

	@Override
	protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {
		config.validateSettings(settings);
	}

	@Override
	protected void loadValidatedSettingsFrom(NodeSettingsRO settings) throws InvalidSettingsException {
		config.loadValidatedSettingsFrom(settings);
	}

	@Override
	protected void reset() {
		// nothing to do, no internals
	}

	@Override
	protected BufferedDataTable[] execute(BufferedDataTable[] inData, ExecutionContext exec) throws Exception {
		BufferedDataTable inTable = inData[0];
		DataTableSpec inSpec = inTable.getSpec();
		int argumentsIndex = inSpec.findColumnIndex(config.argumentsColumn.getStringValue());
		BufferedDataContainer container = exec.createDataContainer(createOutputSpec());
		long rowCount = inTable.size();
		long currentRow = 0;
		for (DataRow inRow : inTable) {
			ListCell argumentsCell = ((ListCell) inRow.getCell(argumentsIndex));
			List<String> arguments = StreamSupport
					.stream(argumentsCell.spliterator(), false)
					.map(c -> ((StringCell) c).getStringValue()).collect(Collectors.toList());
			DataRow row = process(inRow.getKey(), arguments);
			container.addRowToTable(row);
			exec.checkCanceled();
			exec.setProgress(0.9 * currentRow / rowCount, " processing row " + currentRow);
			currentRow++;
		}
		container.close();
		BufferedDataTable outTable = container.getTable();
		BufferedDataTable out = exec.createJoinedTable(inTable, outTable, exec.createSubProgress(0.1));
		return new BufferedDataTable[] { out };
	}

	private DataRow process(RowKey rowKey, List<String> arguments) throws IOException, InterruptedException {
		List<String> commands = new ArrayList<>(arguments.size() + 1);
		String executable = new File(
				Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.SILICOSIT_BINDIR),
				config.program.getStringValue()
		).getCanonicalPath();
		commands.add(executable);
		for (String argument : arguments) {
			commands.add(argument);
		}
	
		ProcessBuilder pb = new ProcessBuilder(commands).inheritIO();
		Map<String, String> env = pb.environment();
		String babelLibDir = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.BABEL_LIBDIR);
		env.put("BABEL_LIBDIR", babelLibDir);
		String babelDataDir = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.BABEL_DATADIR);
		env.put("BABEL_DATADIR", babelDataDir);
		int exitValue = pb.start().waitFor();
		if (exitValue != 0) {
			setWarningMessage("Some rows failed to execute correctly");
		}
		DataCell cell = new IntCell(exitValue);
		return new DefaultRow(rowKey, cell);
	}

	private DataTableSpec createOutputSpec() {
		DataColumnSpecCreator colSpecCreator = new DataColumnSpecCreator("exit value", IntCell.TYPE);
		DataColumnSpec colSpec = colSpecCreator.createSpec();
		return new DataTableSpec(colSpec);
	}

	@Override
	protected DataTableSpec[] configure(DataTableSpec[] inSpecs) throws InvalidSettingsException {
		DataTableSpec appendedSpec = createOutputSpec();
		DataTableSpec outputSpec = new DataTableSpec(inSpecs[0], appendedSpec);
		return new DataTableSpec[] { outputSpec };
	}
}
