package profile;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/** Helper class used to write a map of var group ids to their corresponding list of var ids */
public class VarIdGroupWriter {
	private BufferedWriter writer;
	
	/** @param filename Is the name of the file to write, without path (it goes into base out bath) */
	public VarIdGroupWriter(String filename) {
		try {
			File fOut = new File(dua.util.Util.getCreateBaseOutPath() + filename);
			writer = new BufferedWriter(new FileWriter(fOut));
		}
		catch (IOException e) { System.err.println("Couldn't write var group id file:" + e); }
	}
	
	/** Simply to help read the file by using sections with headers. A \n will be added at the end. */
	public void writeSectionHeader(String header) {
		try {
			writer.write(header + "\n");
		} catch (IOException e) { System.err.println("Couldn't write to var group id file:" + e); }
	}
	
	public void writeVarIdGroup(int varGroupId, List<Integer> varIds) {
		try {
			writer.write(varGroupId + ": " + varIds + "\n");
		} catch (IOException e) { System.err.println("Couldn't write to var group id file:" + e); }
	}
	
	public void finishWriting() {
		try {
			writer.flush();
			writer.close();
		}
		catch (IOException e) { System.err.println("Couldn't finish var group id file:" + e); }
	}
}
