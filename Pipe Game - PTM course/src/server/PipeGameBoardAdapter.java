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
public class PipeGameBoardAdapter {
	
	// data members
	private PipeGameBoard m_pipeGameBoard;
	
	/**
	 * Ctor
	 */
	public PipeGameBoardAdapter(Problem problem) {
		m_pipeGameBoard = new PipeGameBoard();
		List<String> problemLines = problem.GetProblemStringLines();
		m_pipeGameBoard.SetBoard(convertToBoard(problemLines));
	}
	
	public PipeGameBoardAdapter(PipeGameBoardAdapter pipeGameBoardAdapter) {
		m_pipeGameBoard = new PipeGameBoard();
		ArrayList<ArrayList<Character>> newBoard = new ArrayList<>();
		
		for (ArrayList<Character> column : pipeGameBoardAdapter.m_pipeGameBoard.GetBoard()) {
			newBoard.add(new ArrayList<Character>(column));
		}
		
		m_pipeGameBoard.SetBoard(newBoard);
	}
	
	public int GetColumnsNumber() {
		return m_pipeGameBoard.GetBoard().size();
	}

	public int GetRowsNumber() {
		return m_pipeGameBoard.GetBoard().get(0).size();
	}
	
	@Override
	public boolean equals(Object other) {
		if(!(other instanceof PipeGameBoardAdapter)) {
			return false;
		}
		
		PipeGameBoardAdapter otherAdapter = (PipeGameBoardAdapter)other;
		
		if(m_pipeGameBoard.GetBoard().equals(otherAdapter.m_pipeGameBoard.GetBoard())) {
			return true;
		}else {
			return false;
		}	
	}
	
	public Location GetStartCellPosition() {
		return m_pipeGameBoard.GetStartCellPosition();
	}
	
	public char GetPipeChar(int column, int row) {
		return m_pipeGameBoard.GetBoard().get(column).get(row);
	}
	
	public char GetPipeChar(Location cell) {
		return m_pipeGameBoard.GetBoard().get(cell.col).get(cell.row);
	}
	
	public Location GetRotatedCell(PipeGameBoardAdapter cameFromAdapter) {
		for (int column = 0; column < GetColumnsNumber(); column++) {
			for (int row = 0; row < GetRowsNumber(); row++) {
				char cameFromChar = cameFromAdapter.GetPipeChar(column, row);
				char thisChar = this.GetPipeChar(column, row);
				
				if(cameFromChar != thisChar) {
					Location rotatedCell = new Location(column, row);
					return rotatedCell;
				}
			}
		}	
		
		return null;
	}
	
	public int GetNumberOfRotatesRight(Location rotatedCell, PipeGameBoardAdapter cameFromAdapter) {
		int rotatesRight;
		char thisChar = this.GetPipeChar(rotatedCell.col, rotatedCell.row);
		char cameFromChar = cameFromAdapter.GetPipeChar(rotatedCell.col, rotatedCell.row);
		
		if(thisChar == '|' || thisChar == '-') {
			rotatesRight = 1;
		}else {
			int cameFromRotating = symbolToRotationNumber(cameFromChar);
			int thisRotation = symbolToRotationNumber(thisChar);
			final int numberOfRotates = 4;
			rotatesRight = (thisRotation - cameFromRotating) % numberOfRotates;
			if (rotatesRight < 0) rotatesRight += numberOfRotates;
		}
		
		return rotatesRight;
	}
	
	public void SetPipeCellChar(int column, int row, char ch) {
		m_pipeGameBoard.GetBoard().get(column).set(row, ch);
	}
	
	private int symbolToRotationNumber(char cellSymbol) {
		int rotations = 0;
		
		switch (cellSymbol) {
		case 'J':
			rotations = 0;
			break;
		case 'L':
			rotations = 1;
			break;
		case 'F':
			rotations = 2;
			break;
		case '7':
			rotations = 3;
			break;
		default:
			break;
		}
		
		return rotations;
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
	
	@Override
	public int hashCode() {
		return m_pipeGameBoard.hashCode();
	}
}
