/**
 * 
 */
package server;

import java.util.ArrayList;

/**
 * @author ronen
 *
 */
public class PipeGameBoard {

	// Data member
	private ArrayList<ArrayList<Character>> m_board;
	
	/**
	 * Ctor
	 */
	public PipeGameBoard() {
		m_board = null;
	}
	
	public ArrayList<ArrayList<Character>> GetBoard() {
		return m_board;
	}
	
	public void SetBoard(ArrayList<ArrayList<Character>> board) {
		m_board = board;
	}
	
}
