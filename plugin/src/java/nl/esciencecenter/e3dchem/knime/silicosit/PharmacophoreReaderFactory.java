package nl.esciencecenter.e3dchem.knime.silicosit;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "PharmacophoreReader" Node.
 *
 */
public class PharmacophoreReaderFactory
        extends NodeFactory<PharmacophoreReaderModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public PharmacophoreReaderModel createNodeModel() {
        return new PharmacophoreReaderModel();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNrNodeViews() {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NodeView<PharmacophoreReaderModel> createNodeView(final int viewIndex,
            final PharmacophoreReaderModel nodeModel) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasDialog() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NodeDialogPane createNodeDialogPane() {
        return new PharmacophoreReaderDialog();
    }

}
