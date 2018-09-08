/**
 * 
 */
package server;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ronen
 *
 */
public class CacheManager implements ICacheManager {
	
	private final String m_solutionsFolder = "solutions";


	/**
	 * Ctor
	 */
	public CacheManager() {

	}

	// Methods

	@Override
	public Solution GetSavedSolution(Problem problem) {
		Solution solution = new Solution();
		int viewHash = getHashCodeOfView(problem);
		Path file = getPathFromHashCode(viewHash);

		try {
			if (Files.exists(file)) {
				solution.SetSolutionStringLines(new ArrayList<String>(Files.readAllLines(file)));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return solution;
	}

	@Override
	public void SaveSolution(Solution solution, Problem problem) {
		try {	
			int viewHash = getHashCodeOfView(problem);
			Path file = getPathFromHashCode(viewHash);
			
			if(!Files.exists(Paths.get(m_solutionsFolder))) {
				Files.createDirectory(Paths.get(m_solutionsFolder));
			}
						
			List<String> solutionLines = solution.GetSolutionStringLines();
			Files.write(file, solutionLines, Charset.forName("UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Path getPathFromHashCode(int hashCode) {
		final String endOfTextFile = ".txt";
		String fileName = Integer.toString(hashCode) + endOfTextFile;
		return Paths.get(m_solutionsFolder, fileName);
	}

	private int getHashCodeOfView(Problem problem) {
		StringBuilder viewString = new StringBuilder();

		for (String viewRow : problem.GetProblemStringLines()) {
			viewString.append(viewRow);
		}

		return viewString.toString().hashCode();
	}
}
