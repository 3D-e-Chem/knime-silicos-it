package nl.esciencecenter.e3dchem.knime.silicosit.execute;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.eclipse.jface.preference.IPreferenceStore;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.RowKey;
import org.knime.core.data.collection.ListCell;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.IntCell;
import org.knime.core.data.def.StringCell;
import org.knime.core.data.def.StringCell.StringCellFactory;
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
        int argumentsIndex = inSpec.findColumnIndex(config.getArgumentsColumnValue());
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
		IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
		String silicositBinDir = preferenceStore.getString(PreferenceConstants.SILICOSIT_BINDIR);
        String executable = config.getProgramValue();
		if ("".equals(silicositBinDir)) {
			this.getLogger().warn("Directory with Silicos-it binaries not set, expecting binaries to be in PATH");
		} else {
			executable = new File(
					silicositBinDir,
                    config.getProgramValue()
			).getCanonicalPath();
			this.getLogger().debug("Directory with Silicos-it binaries: " + silicositBinDir + " - " + executable);
		}
		commands.add(executable);
		for (String argument : arguments) {
			commands.add(argument);
		}
	
		ProcessBuilder pb = new ProcessBuilder(commands);
		Map<String, String> env = pb.environment();
		String babelLibDir = preferenceStore.getString(PreferenceConstants.BABEL_LIBDIR);
		if (!env.containsKey("BABEL_LIBDIR")) {
			if ("".equals(babelLibDir)) {
				this.getLogger().info("Directory with Open Babel plugin libraries not set, reading/writing of Chemical file formats can be severly impared");
			} else {
				env.put("BABEL_LIBDIR", babelLibDir);
			}
		}
		String babelDataDir = preferenceStore.getString(PreferenceConstants.BABEL_DATADIR);
		if (!env.containsKey("BABEL_DATADIR")) {
			if ("".equals(babelDataDir)) {
				this.getLogger().info("Directory with Open Babel data files not set, falling back to Open Babel default data directory");
			} else {
				env.put("BABEL_DATADIR", babelDataDir);
			}
		}
		Process process = pb.start();
        StreamCollector stdout = new StreamCollector(process.getInputStream());
        Thread stdoutT = new Thread(stdout);
        stdoutT.start();
        StreamCollector stderr = new StreamCollector(process.getErrorStream());
        Thread stderrT = new Thread(stderr);
        stderrT.start();

		int exitValue = process.waitFor();
		if (exitValue != 0) {
			setWarningMessage("Some rows failed to execute correctly");
		}
        stdoutT.join();
        stderrT.join();

		StringCellFactory factory = new StringCell.StringCellFactory();
        return new DefaultRow(rowKey, new IntCell(exitValue), factory.createCell(stdout.getContent()),
                factory.createCell(stderr.getContent()));
	}

	private DataTableSpec createOutputSpec() {
		return new DataTableSpec(
				new DataColumnSpecCreator("exit value", IntCell.TYPE).createSpec(),
				new DataColumnSpecCreator("standard output", StringCell.TYPE).createSpec(),
				new DataColumnSpecCreator("standard error", StringCell.TYPE).createSpec()
		);
	}

	@Override
	protected DataTableSpec[] configure(DataTableSpec[] inSpecs) throws InvalidSettingsException {
		DataTableSpec appendedSpec = createOutputSpec();
		DataTableSpec outputSpec = new DataTableSpec(inSpecs[0], appendedSpec);
		return new DataTableSpec[] { outputSpec };
	}
}
