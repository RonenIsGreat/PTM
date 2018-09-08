/**
 * 
 */
package server;

/**
 * @author ronen
 *
 */
public class PipeGameState extends State<PipeGameBoardAdapter> {
	
	protected boolean[][] m_rotatedBoardCells;
			
	public PipeGameState(PipeGameBoardAdapter adapter) {
		super(adapter);
		m_rotatedBoardCells = new boolean[adapter.GetColumnsNumber()][adapter.GetRowsNumber()];
	}
	
	public PipeGameState(PipeGameBoardAdapter state, boolean[][] rotatedBoardCells) {
		super(state);
		m_rotatedBoardCells = new boolean[rotatedBoardCells.length][];
		
		for(int i = 0; i < rotatedBoardCells.length; i++) {
			m_rotatedBoardCells[i] = rotatedBoardCells[i].clone();
		}
	}

	@Override
	public void SetCameFrom(State<PipeGameBoardAdapter> cameFrom) {
		m_cameFrom = cameFrom;
		SetStateMove(cameFrom);
	}

	@Override
	public boolean equals(Object other) {
		if(!(other instanceof PipeGameState)) {
			return false;
		}
		
		PipeGameState otherState = (PipeGameState)other;
		PipeGameBoardAdapter otherBoard = otherState.GetState();
		PipeGameBoardAdapter ourBoard = this.GetState();
		
		return ourBoard.equals(otherBoard);
	}
	
	public Location GetRotatedCellPosition() {
		Location RotatedCellPosition;
		
		if(m_stateMove == null) {
			RotatedCellPosition = GetState().GetStartCellPosition();
			
			if(RotatedCellPosition == null) {
				RotatedCellPosition = new Location(0,0);
			}
		} else {
			String[] splittedStateMove = m_stateMove.split(",");
			int column = Integer.parseInt(splittedStateMove[0]);
			int row = Integer.parseInt(splittedStateMove[1]);
			RotatedCellPosition = new Location(column, row);
		}
		return RotatedCellPosition;
	}
	
	public boolean IsRotatedBoardCell(int column, int row) {
		return m_rotatedBoardCells[column][row];
	}
	
	public void SetRotatedBoardCell(int column, int row) {
		m_rotatedBoardCells[column][row] = true;
	}
	
	public boolean[][] GetRotatedBoardCells() {
		return m_rotatedBoardCells;
	}
	
	private void SetStateMove(State<PipeGameBoardAdapter> cameFrom) {
		String stateMove = null;
		
		PipeGameBoardAdapter cameFromAdapter = cameFrom.GetState();
		Location rotatedCell = this.GetState().GetRotatedCell(cameFromAdapter);
		
		if(rotatedCell != null) {
			stateMove = rotatedCell.col + "," + rotatedCell.row + "," + 
					this.GetState().GetNumberOfRotatesRight(rotatedCell, cameFromAdapter);	
		}
			
		m_stateMove = stateMove;
	}

	@Override
	public int hashCode() {
		return this.GetState().hashCode();
	}
}
