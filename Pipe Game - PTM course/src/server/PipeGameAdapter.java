/**
 * 
 */
package server;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ronen
 *
 */
public class PipeGameAdapter {
	
	// data member
	PipeGameBoard m_board;
	
	/**
	 * Ctor
	 */
	public PipeGameAdapter(Problem problem) {
		m_board = new PipeGameBoard();
		List<String> problemLines = problem.GetProblemStringLines();
		m_board.SetBoard(convertToBoard(problemLines));
	}

	public PipeGameBoard GetPipeGameBoard() {
		return m_board;
	}
	
	private ArrayList<ArrayList<Character>> convertToBoard(List<String> problemLines){
		ArrayList<ArrayList<Character>> board = new ArrayList<ArrayList<Character>>();
		
		for (String line : problemLines) {
			board.add(convertStringToChars(line));
		}
		
		return board;
	}
	
	private ArrayList<Character> convertStringToChars(String string){
		ArrayList<Character> charArray = new ArrayList<Character>();
		
		for (char c : string.toCharArray()) {
			charArray.add(c);
		}
			
		return charArray;
	}
}
