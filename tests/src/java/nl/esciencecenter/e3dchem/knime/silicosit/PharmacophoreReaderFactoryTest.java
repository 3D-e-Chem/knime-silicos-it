package nl.esciencecenter.e3dchem.knime.silicosit;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PharmacophoreReaderFactoryTest {
	
	@Test
    public void testGetNrNodeViews() {
		PharmacophoreReaderFactory factory = new PharmacophoreReaderFactory();
		
		int nrviews = factory.getNrNodeViews();
		
		assertEquals(1, nrviews);
	}
}