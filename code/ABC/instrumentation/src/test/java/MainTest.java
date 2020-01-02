import java.io.File;
import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.xmlpull.v1.XmlPullParserException;

import de.unipassau.abc.instrumentation.Main;

public class MainTest {

	@Rule
	public TemporaryFolder temporaryFolder = new TemporaryFolder();
	
	@Test
	public void testMain() throws IOException, XmlPullParserException {
		
		File outputDIR = temporaryFolder.newFolder();
		
		File theAPK = new File("src/test/resources/org.ametro_40.apk");
		File androidJAR = new File("src/test/resources/soot-infoflow-android.jar");
		String[] args = new String[] { "--apk", theAPK.getAbsolutePath(),//
				"--android-jar", androidJAR.getAbsolutePath(), //
				"--output-to", outputDIR.getAbsolutePath()
				};
		
		Main m = new Main();
		m.main(args);
	}
}
