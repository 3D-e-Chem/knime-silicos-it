package nl.esciencecenter.e3dchem.knime.silicosit;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ActivatorTest {
	@Test
	public void test() {
		String expected = "nl.esciencecenter.e3dchem.knime.silicosit.plugin";
		assertEquals(expected, Activator.PLUGIN_ID);
	}
}
