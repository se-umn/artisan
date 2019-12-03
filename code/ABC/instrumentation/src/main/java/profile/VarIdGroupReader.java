package profile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/** Helper class used to write a map of var group ids to their corresponding list of var ids */
public class VarIdGroupReader {
	private static VarIdGroupReader inst = null;
	public static VarIdGroupReader inst() { if (inst == null) inst = new VarIdGroupReader("varidgroups.out"); return inst; }
	
	private List<List<Integer>> groupsToVarIds = new ArrayList<List<Integer>>();
	
	public int getNumGroups() { return groupsToVarIds.size(); }
	public List<Integer> getVarIds(int groupId) { return groupsToVarIds.get(groupId); }
	
	/** @param filename Is the name of the file to read, assumed to be in the current directory */
	private VarIdGroupReader(String filename) {
		try {
			// read file and create group_id -> var_ids map
			File fIn = new File(filename);
			BufferedReader reader = new BufferedReader(new FileReader(fIn));
			
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.equals("DEFINITIONS") || line.equals("USES"))
					continue; // ignore these section headers, only intended for readability
				
				// get group id
				final int end = line.indexOf(':');
				final int groupId = Integer.parseInt(line.substring(0, end));
				assert line.charAt(end+1) == ' ';
				List<Integer> varIds = dua.util.Util.parseIntList(line.substring(end+2));
				
				groupsToVarIds.add(varIds);
				assert groupsToVarIds.size() == groupId + 1;
			}
			
			reader.close();
		}
		catch (IOException e) { System.err.println("Couldn't read var group id file:" + e); }
	}
}
