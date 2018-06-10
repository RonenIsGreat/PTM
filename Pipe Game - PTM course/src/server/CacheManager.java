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
public class CacheManager implements ICacheManager{
	
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
			List<String> solutionLines = solution.GetSolutionStringLines();
			Files.write(file, solutionLines, Charset.forName("UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private Path getPathFromHashCode(int hashCode) {
		final String solutionsFolder = "src\\server\\solutions";
		final String endOfTextFile = ".txt";
		String fileName = Integer.toString(hashCode) + endOfTextFile;
		return Paths.get(solutionsFolder, fileName);
	}
	
	private int getHashCodeOfView(Problem problem) {
		StringBuilder viewString = new StringBuilder();
		
		for (String viewRow : problem.GetProblemStringLines()) {
			viewString.append(viewRow);
		}
		
		return viewString.toString().hashCode();
	}

	// Convert display of cells that can rotate and become the same option
	private void convertToGenericViewString(StringBuilder viewString) {
		for (int index=0; index < viewString.length(); index++) {
			switch (viewString.charAt(index)) {
			case '|':
				viewString.setCharAt(index, '-');
				break;
			case '7':
				viewString.setCharAt(index, 'L');
				break;
			case 'J':
				viewString.setCharAt(index, 'L');
				break;
			case 'F':
				viewString.setCharAt(index, 'L');
				break;
			default:
				break;
			}
		}		
	}
}
