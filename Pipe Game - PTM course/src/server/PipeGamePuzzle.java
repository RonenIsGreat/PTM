/**
 * 
 */
package server;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import javafx.util.Pair;

/**
 * @author ronen
 *
 */
public class PipeGamePuzzle implements ISearchable<PipeGameState> {

	// data members
	final private PipeGameState m_startState;
	
	/**
	 * Ctor
	 */
	public PipeGamePuzzle(PipeGameAdapter pipeGameObjectAdapter) {
		PipeGameBoard board = pipeGameObjectAdapter.GetPipeGameBoard();
		m_startState = new PipeGameState(board);
	}

	@Override
	public PipeGameState GetStartState() {
		return m_startState;
	}

	@Override
	public List<PipeGameState> GetAllPossibleStates(PipeGameState currentState) {
		List<PipeGameState> possibleStatesList = new LinkedList<PipeGameState>();
		ArrayList<ArrayList<Character>> board = currentState.GetState().GetBoard();
		
		for (int column = 0; column < board.size(); column++) {
			for (int row = 0; row < board.get(column).size(); row++) {
				char boardChar = board.get(column).get(row);
				
				if(isRotatingPipeCell(boardChar) && !(currentState.IsRotatedBoardCell(column, row))) {					
					if((boardChar == '|') || (boardChar == '-')) {
						PipeGameState newPipeGameState = new PipeGameState(rotateRightPipeCellBoard(board, column, row), currentState.GetRotatedBoardCells());
						newPipeGameState.SetRotatedBoardCell(column, row);
						possibleStatesList.add(newPipeGameState);
					} else {
						PipeGameState newPipeGameState = new PipeGameState(rotateRightPipeCellBoard(board, column, row), currentState.GetRotatedBoardCells());
						newPipeGameState.SetRotatedBoardCell(column, row);
						possibleStatesList.add(newPipeGameState);
						
						newPipeGameState = new PipeGameState(rotateRightPipeCellBoard(newPipeGameState.GetState().GetBoard(), column, row), currentState.GetRotatedBoardCells());
						newPipeGameState.SetRotatedBoardCell(column, row);
						possibleStatesList.add(newPipeGameState);
						
						newPipeGameState = new PipeGameState(rotateRightPipeCellBoard(newPipeGameState.GetState().GetBoard(), column, row), currentState.GetRotatedBoardCells());
						newPipeGameState.SetRotatedBoardCell(column, row);
						possibleStatesList.add(newPipeGameState);
					}
				}
			}
		}	
		
		return possibleStatesList;
	}

	private boolean isRotatingPipeCell(char boardChar) {
		return (boardChar == '-' || boardChar == 'J' || boardChar == '|' || boardChar == 'L'
				|| boardChar == 'F' || boardChar == '7');
	}
	

	private PipeGameBoard rotateRightPipeCellBoard(ArrayList<ArrayList<Character>> board, int column, int row) {
		ArrayList<ArrayList<Character>> oldBoardCopy = new ArrayList<ArrayList<Character>>(board);
		oldBoardCopy.set(column,  new ArrayList<Character>(board.get(column)));
		PipeGameBoard newRotatedBoard = new PipeGameBoard();
		newRotatedBoard.SetBoard(oldBoardCopy);
		char boardChar = newRotatedBoard.GetBoard().get(column).get(row);

		switch (boardChar) {
		case '-':
			newRotatedBoard.GetBoard().get(column).set(row, '|');
			break;
		case 'J':
			newRotatedBoard.GetBoard().get(column).set(row, 'L');
			break;
		case '|':
			newRotatedBoard.GetBoard().get(column).set(row, '-');
			break;
		case 'L':
			newRotatedBoard.GetBoard().get(column).set(row, 'F');
			break;
		case 'F':
			newRotatedBoard.GetBoard().get(column).set(row, '7');
			break;
		case '7':
			newRotatedBoard.GetBoard().get(column).set(row, 'J');
			break;
		}
		
		return newRotatedBoard;
	}

	@Override
	public boolean IsGoalState(PipeGameState state) {
		ArrayList<ArrayList<Character>> board = state.GetState().GetBoard();
		Pair<Integer, Integer> cellPlace = getStartCellOfBoard(board);
		List<Pair<Integer, Integer>> checkingCellsList = new LinkedList<>();
		checkingCellsList.add(cellPlace);
		HashSet<Pair<Integer, Integer>> checkedCellPlaces = new HashSet<>();
		boolean foundGoal = false;
		
		while(!checkingCellsList.isEmpty() && !foundGoal) {
			cellPlace = checkingCellsList.get(0);
			checkingCellsList.remove(0);
			List<Pair<Integer, Integer>> reachableCells = getReachableCells(board, cellPlace, checkedCellPlaces);
			checkingCellsList.addAll(reachableCells);
			
			if(getCellValue(board, cellPlace) == 'g') {
				foundGoal = true;
			}
		}
		
		if(getCellValue(board, cellPlace) == 'g') {
			return true;
		}
		else {
			return false;
		}
	}
	
	@Override
	// Cost it higher as the length of connected pipes
	public int GetCost(PipeGameState state) {
		int cost = 0;
		Pair<Integer, Integer> cellPlace;
		ArrayList<ArrayList<Character>> board = state.GetState().GetBoard();
		List<Pair<Integer, Integer>> checkingCellsList = new LinkedList<>();
		checkingCellsList.add(getStartCellOfBoard(board));
		HashSet<Pair<Integer, Integer>> checkedCellPlaces = new HashSet<>();
		
		while(!checkingCellsList.isEmpty()) {
			cellPlace = checkingCellsList.get(0);
			checkingCellsList.remove(0);
			List<Pair<Integer, Integer>> reachableCells = getReachableCells(board, cellPlace, checkedCellPlaces);
			checkingCellsList.addAll(reachableCells);
			cost++;
		}
		
		return cost;
	}

	private Pair<Integer, Integer> getStartCellOfBoard(ArrayList<ArrayList<Character>> board){
		for (int column = 0; column < board.size(); column++) {
			for (int row = 0; row < board.get(column).size(); row++) {
				char boardChar = board.get(column).get(row);
				
				if(boardChar == 's') {
					return new Pair<>(column, row);
				}
			}
		}	
		
		return null;
	}
	
	private char getCellValue(ArrayList<ArrayList<Character>> board, Pair<Integer, Integer> cellPlace) {
		int column = cellPlace.getKey();
		int row = cellPlace.getValue();
		return board.get(column).get(row);
	}
	
	private List<Pair<Integer, Integer>> getReachableCells(ArrayList<ArrayList<Character>> board, 
			Pair<Integer, Integer> cellPlace, HashSet<Pair<Integer, Integer>> checkedCellPlaces) {
		List<Pair<Integer, Integer>> reachableCells = new LinkedList<>();
		char cellChar = getCellValue(board, cellPlace);
		
		switch (cellChar) {
		case 's':
			addNearCellIfReachable(board, cellPlace, "up", reachableCells, checkedCellPlaces);
			addNearCellIfReachable(board, cellPlace, "down", reachableCells, checkedCellPlaces);
			addNearCellIfReachable(board, cellPlace, "left", reachableCells, checkedCellPlaces);
			addNearCellIfReachable(board, cellPlace, "right", reachableCells, checkedCellPlaces);
			break;
		case '-':
			addNearCellIfReachable(board, cellPlace, "left", reachableCells, checkedCellPlaces);
			addNearCellIfReachable(board, cellPlace, "right", reachableCells, checkedCellPlaces);
			break;
		case 'J':
			addNearCellIfReachable(board, cellPlace, "up", reachableCells, checkedCellPlaces);
			addNearCellIfReachable(board, cellPlace, "left", reachableCells, checkedCellPlaces);
			break;
		case '|':
			addNearCellIfReachable(board, cellPlace, "up", reachableCells, checkedCellPlaces);
			addNearCellIfReachable(board, cellPlace, "down", reachableCells, checkedCellPlaces);
			break;
		case 'L':
			addNearCellIfReachable(board, cellPlace, "up", reachableCells, checkedCellPlaces);
			addNearCellIfReachable(board, cellPlace, "right", reachableCells, checkedCellPlaces);
			break;
		case 'F':
			addNearCellIfReachable(board, cellPlace, "down", reachableCells, checkedCellPlaces);
			addNearCellIfReachable(board, cellPlace, "right", reachableCells, checkedCellPlaces);
			break;
		case '7':			
			addNearCellIfReachable(board, cellPlace, "left", reachableCells, checkedCellPlaces);
			addNearCellIfReachable(board, cellPlace, "down", reachableCells, checkedCellPlaces);
			break;
		}
		
		return reachableCells;
	}

	private void addNearCellIfReachable(ArrayList<ArrayList<Character>> board, Pair<Integer, Integer> cellPlace,
			String direction, List<Pair<Integer, Integer>> reachableCells,
			HashSet<Pair<Integer, Integer>> checkedCellPlaces) {
		if(cellPlace == null) {
			return;
		}
		
		int cellColumn = cellPlace.getKey();
		int cellRow = cellPlace.getValue();
		int lastColumnIndex = board.size() - 1;
		int lastRowIndex = board.get(0).size() - 1;
		
		switch (direction) {
		case "up":
			cellColumn--;
			Pair<Integer, Integer> upperCellPlace = new Pair<Integer, Integer>(cellColumn, cellRow);
			
			if(!checkedCellPlaces.contains(upperCellPlace) && (cellColumn >= 0)) {
				char upperCellChar = getCellValue(board, upperCellPlace);

				if(upperCellChar == 'g' || upperCellChar == '|' || upperCellChar == 'F' || upperCellChar == '7') {
					checkedCellPlaces.add(upperCellPlace);
					reachableCells.add(upperCellPlace);
				}
			}
			break;
		case "down":
			cellColumn++;
			Pair<Integer, Integer> lowerCellPlace = new Pair<Integer, Integer>(cellColumn, cellRow);
			
			if(!checkedCellPlaces.contains(lowerCellPlace) && (cellColumn <= lastColumnIndex)) {
				char lowerCellChar = getCellValue(board, lowerCellPlace);

				if(lowerCellChar == 'g' || lowerCellChar == '|' || lowerCellChar == 'J' || lowerCellChar == 'L') {
					checkedCellPlaces.add(lowerCellPlace);
					reachableCells.add(lowerCellPlace);
				}
			}
			break;
		case "left":
			cellRow--;
			Pair<Integer, Integer> leftCellPlace = new Pair<Integer, Integer>(cellColumn, cellRow);
			
			if(!checkedCellPlaces.contains(leftCellPlace) && (cellRow >= 0)) {
				char leftCellChar = getCellValue(board, leftCellPlace);

				if(leftCellChar == 'g' || leftCellChar == '-' || leftCellChar == 'L' || leftCellChar == 'F') {
					checkedCellPlaces.add(leftCellPlace);
					reachableCells.add(leftCellPlace);
				}
			}
			break;
		case "right":
			cellRow++;
			Pair<Integer, Integer> rightCellPlace = new Pair<Integer, Integer>(cellColumn, cellRow);
			
			if(!checkedCellPlaces.contains(rightCellPlace) && (cellRow <= lastRowIndex)) {
				char rightCellChar = getCellValue(board, rightCellPlace);

				if(rightCellChar == 'g' || rightCellChar == '-' || rightCellChar == 'J' || rightCellChar == '7') {
					checkedCellPlaces.add(rightCellPlace);
					reachableCells.add(rightCellPlace);
				}
			}
			break;
		}
	}
}
