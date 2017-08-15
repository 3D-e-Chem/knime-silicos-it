package nl.esciencecenter.e3dchem.knime.silicosit.execute;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

public class ExecuteFactory extends NodeFactory<ExecuteNode> {

	@Override
	public ExecuteNode createNodeModel() {
		return new ExecuteNode();
	}

	@Override
	protected int getNrNodeViews() {
		return 0;
	}

	@Override
	protected boolean hasDialog() {
		return true;
	}

	@Override
	public NodeView<ExecuteNode> createNodeView(int viewIndex, ExecuteNode nodeModel) {
		return null;
	}

	@Override
	protected NodeDialogPane createNodeDialogPane() {
		return new ExecuteDialog();
	}

}
