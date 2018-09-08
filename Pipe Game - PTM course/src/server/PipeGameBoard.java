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
	
	public Location GetStartCellPosition(){
		boolean flag = false;
		Location startCellPosition = null;
		
		for (int column = 0; (column < m_board.size()) && !flag; column++) {
			for (int row = 0; (row < m_board.get(column).size()) && !flag; row++) {
				char c = m_board.get(column).get(row);
				
				if(c == 's') {
					startCellPosition = new Location(column, row);
					flag = true;
				}
			}
		}	
		return startCellPosition;
	}
	
	@Override
	public int hashCode() {
		StringBuilder boardString = new StringBuilder();
		
		for (int column = 0; column < m_board.size(); column++) {
			for (int row = 0; row < m_board.get(column).size(); row++) {
				char c = m_board.get(column).get(row);
				boardString.append(c);
			}
		}	
		
		return boardString.toString().hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		PipeGameBoard other = (PipeGameBoard) o;
		
		if(other.GetBoard().size() != m_board.size()) {
			return false;
		}

		for (int column = 0; (column < m_board.size()); column++) {
			if(other.GetBoard().get(column).size() != m_board.get(column).size()) {
				return false;
			}
			
			for (int row = 0; (row < m_board.get(column).size()); row++) {
				char c = m_board.get(column).get(row);
				char otherC = other.GetBoard().get(column).get(row);
				
				if(c != otherC) {
					return false;
				}
			}
		}	

		return true;
	}
}
