package nl.esciencecenter.e3dchem.knime.silicosit.preferences;

import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeFalse;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SWTBotJunit4ClassRunner.class)
public class PreferencePageTest {
    private static SWTWorkbenchBot bot;

    @BeforeClass
    public static void setUp() {
        assumeFalse("Does not work on Mac", System.getProperty("os.name").contains("Mac"));
        bot = new SWTWorkbenchBot();
    }

    @AfterClass
    public static void sleep() {
        bot.closeAllShells();
    }

    @Test
    public void test_initialState() {
        String binDir = bot.textWithLabel("Directory with Silicos-it binaries:").getText();
        assertTrue("Silicos-it directory contains 'bin'", binDir.contains("bin"));

        String dataDir = bot.textWithLabel("Directory with Open Babel data files:").getText();
        assertTrue("Babel data dir contains 'data'", dataDir.contains("data"));

        String libDir = bot.textWithLabel("Directory with Open Babel plugin libraries:").getText();
        assertTrue("Babel lib dir contains 'lib/plugins'", libDir.contains("lib/plugins"));
    }

    @Before
    public void openPage() {
        bot.menu("Window").menu("Preferences").click();

        bot.shell("Preferences").activate();

        bot.tree().expandNode("KNIME", true).select("Silicos-it");
    }

    @After
    public void closePage() {
        bot.shell("Preferences").close();
    }
}
