/**
 * 
 */
package server;

import java.util.ArrayList;

import javafx.util.Pair;

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
	
	public Pair<Integer, Integer> GetStartCellPosition(){
		boolean flag = false;
		Pair<Integer, Integer> startCellPosition = null;
		
		for (int column = 0; (column < m_board.size()) && !flag; column++) {
			for (int row = 0; (row < m_board.get(column).size()) && !flag; row++) {
				char c = m_board.get(column).get(row);
				
				if(c == 's') {
					startCellPosition = new Pair<>(column, row);
					flag = true;
				}
			}
		}	
		return startCellPosition;
	}
}
