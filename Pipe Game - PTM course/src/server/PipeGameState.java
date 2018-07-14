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
public class PipeGameState extends State<PipeGameBoard> {
	
	protected Pair<Integer, Integer> m_ConnectedTo;
		
	public PipeGameState(PipeGameBoard state) {
		super(state);
		m_rotatedBoardCells = new boolean[state.GetBoard().size()][state.GetBoard().get(0).size()];
	}
	
	public PipeGameState(PipeGameBoard state, boolean[][] rotatedBoardCells) {
		super(state);
		m_rotatedBoardCells = new boolean[rotatedBoardCells.length][];
		
		for(int i = 0; i < rotatedBoardCells.length; i++) {
			m_rotatedBoardCells[i] = rotatedBoardCells[i].clone();
		}
	}

	@Override
	public void SetCameFrom(State<PipeGameBoard> cameFrom) {
		m_cameFrom = cameFrom;
		SetStateMove(cameFrom);
	}

	@Override
	public boolean equals(Object other) {
		if(!(other instanceof PipeGameState)) {
			return false;
		}
		
		PipeGameState otherState = (PipeGameState)other;
		ArrayList<ArrayList<Character>> otherBoard = otherState.GetState().GetBoard();
		ArrayList<ArrayList<Character>> ourBoard = this.GetState().GetBoard();
		
		if(ourBoard.equals(otherBoard)) {
			return true;
		}else {
			return false;
		}		
	}
	
	public Pair<Integer, Integer> GetRotatedCellPosition() {
		Pair<Integer, Integer> RotatedCellPosition;
		
		if(m_stateMove == null) {
			RotatedCellPosition = GetState().GetStartCellPosition();
		} else {
			String[] splittedStateMove = m_stateMove.split(",");
			int column = Integer.parseInt(splittedStateMove[0]);
			int row = Integer.parseInt(splittedStateMove[1]);
			RotatedCellPosition = new Pair<>(column, row);
		}
		return RotatedCellPosition;
	}
	
	public boolean IsRotatedBoardCell(int column, int row) {
		return m_rotatedBoardCells[column][row];
	}
	
	public void SetRotatedBoardCell(int column, int row) {
		m_rotatedBoardCells[column][row] = true;
	}
	
	private void SetStateMove(State<PipeGameBoard> cameFrom) {
		String stateMove = null;
		Boolean flag = false;
		ArrayList<ArrayList<Character>> cameFromBoard = cameFrom.GetState().GetBoard();
		ArrayList<ArrayList<Character>> thisBoard = GetState().GetBoard();
		
		for (int column = 0; (column < cameFromBoard.size()) && !flag; column++) {
			for (int row = 0; (row < cameFromBoard.get(column).size()) && !flag; row++) {
				char cameFromChar = cameFromBoard.get(column).get(row);
				char thisChar = thisBoard.get(column).get(row);
				
				if(cameFromChar != thisChar) {
					stateMove = column + "," + row + "," + getNumberOfRotatesRight(cameFromChar, thisChar);
					flag = true;
				}
			}
		}		
		
		m_stateMove = stateMove;
	}
	
	private int getNumberOfRotatesRight(char cameFromChar, char thisChar) {
		int rotatesRight;
		
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
}
