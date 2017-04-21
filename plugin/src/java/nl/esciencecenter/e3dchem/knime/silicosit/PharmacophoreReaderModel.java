package nl.esciencecenter.e3dchem.knime.silicosit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.RowKey;
import org.knime.core.data.def.DefaultRow;
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
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.util.FileUtil;

/**
 * This is the model implementation of PharmacophoreReader.
 *
 */
public class PharmacophoreReaderModel extends NodeModel {
	public static final String CFGKEY_FILENAME = "phar_filename";
	private static final DataColumnSpecCreator CREATOR = new DataColumnSpecCreator("Pharmacophore", StringCell.TYPE);
	private static final DataTableSpec SPEC = new DataTableSpec(CREATOR.createSpec());

	private final SettingsModelString pharFilename = new SettingsModelString(CFGKEY_FILENAME, null);

	/**
	 * Constructor for the node model.
	 */
	protected PharmacophoreReaderModel() {
		super(0, 1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec)
			throws Exception {

		BufferedDataContainer container = exec.createDataContainer(SPEC);

		String filename = pharFilename.getStringValue();
		InputStream inStream;
		try {
			inStream = new URL(filename).openStream();
		} catch (MalformedURLException e) {
			inStream = new FileInputStream(filename);
		}

		BufferedReader in = new BufferedReader(new InputStreamReader(inStream));
		String line;
		StringBuilder buf = new StringBuilder(4096);
		String name = null;
		String sep = System.getProperty("line.separator");
		while ((line = in.readLine()) != null) {
			exec.checkCanceled();

			if (line.startsWith("$$$$")) {
				buf.append(line).append(sep);
				addPharmacophore(name, buf, container);
				buf.setLength(0);
			} else if (line.startsWith("#")) {
				// Skip comments
			} else {
				if (line.split("\\s+").length < 9) {
					// pharmacophore name
					name = line;
				}
				buf.append(line).append(sep);
			}
		}

		// once we are done, we close the container and return its table
		inStream.close();
		container.close();
		BufferedDataTable out = container.getTable();
		return new BufferedDataTable[] { out };
	}

	private void addPharmacophore(String name, StringBuilder buf, BufferedDataContainer container) {
		DataRow row = new DefaultRow(new RowKey(name), StringCellFactory.create(buf.toString()));
		container.addRowToTable(row);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected DataTableSpec[] configure(final DataTableSpec[] inSpecs) throws InvalidSettingsException {

		String filename = pharFilename.getStringValue();
		if (filename == null) {
			throw new InvalidSettingsException("No *.phar file specified");
		}

		try {
			URL url = new URL(filename);
			if ("file".equals(url.getProtocol())) {
				checkFile(FileUtil.getFileFromURL(url));
			}
		} catch (MalformedURLException e) {
			checkFile(new File(filename));
		}

		return new DataTableSpec[] { SPEC };
	}

	private void checkFile(File f) throws InvalidSettingsException {
		if (!f.exists()) {
			throw new InvalidSettingsException("File '" + f.getAbsolutePath() + "' does not exist.");
		}
		if (!f.isFile()) {
			throw new InvalidSettingsException("The path '" + f.getAbsolutePath() + "' is not a file.");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {
		pharFilename.saveSettingsTo(settings);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
		pharFilename.loadSettingsFrom(settings);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
		pharFilename.validateSettings(settings);
		if (settings.getString(CFGKEY_FILENAME) == null) {
			throw new InvalidSettingsException("No phar file specified");
		}
	}

	@Override
	protected void loadInternals(File nodeInternDir, ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
		String filename = pharFilename.getStringValue();
		if (filename == null) {
			return;
		}

		try {
			URL url = new URL(filename);
			if ("file".equals(url.getProtocol())) {
				checkFile(FileUtil.getFileFromURL(url));
			}
		} catch (MalformedURLException e) {
			try {
				checkFile(new File(filename));
			} catch (InvalidSettingsException e1) {
				setWarningMessage(e.getMessage());
			}
		} catch (InvalidSettingsException e) {
			setWarningMessage(e.getMessage());
		}
	}

	@Override
	protected void saveInternals(File nodeInternDir, ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
		// Auto-generated method stub

	}

	@Override
	protected void reset() {
		// Auto-generated method stub
	}
}
