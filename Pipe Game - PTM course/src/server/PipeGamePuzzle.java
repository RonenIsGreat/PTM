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
	public PipeGamePuzzle(PipeGameBoardAdapter pipeGameObjectAdapter) {
		//PipeGameBoard board = pipeGameObjectAdapter.GetPipeGameBoard();
		m_startState = new PipeGameState(pipeGameObjectAdapter);
	}

	@Override
	public PipeGameState GetStartState() {
		return m_startState;
	}

	@Override
	public List<PipeGameState> GetAllPossibleStates(PipeGameState currentState) {
		List<PipeGameState> possibleStatesList = new LinkedList<PipeGameState>();
		PipeGameBoardAdapter boardAdapter = currentState.GetState();
		
		for (int column = 0; column < boardAdapter.GetColumnsNumber(); column++) {
			for (int row = 0; row < boardAdapter.GetRowsNumber(); row++) {
				char boardChar = boardAdapter.GetPipeChar(column, row);
				
				if(isRotatingPipeCell(boardChar) && !(currentState.IsRotatedBoardCell(column, row))) {					
					if((boardChar == '|') || (boardChar == '-')) {
						PipeGameState newPipeGameState = new PipeGameState(rotatedRightPipeCellBoardAdapter(boardAdapter, column, row), currentState.GetRotatedBoardCells());
						newPipeGameState.SetRotatedBoardCell(column, row);
						possibleStatesList.add(newPipeGameState);
					} else {
						PipeGameState newPipeGameState = new PipeGameState(rotatedRightPipeCellBoardAdapter(boardAdapter, column, row), currentState.GetRotatedBoardCells());
						newPipeGameState.SetRotatedBoardCell(column, row);
						possibleStatesList.add(newPipeGameState);
						
						newPipeGameState = new PipeGameState(rotatedRightPipeCellBoardAdapter(newPipeGameState.GetState(), column, row), currentState.GetRotatedBoardCells());
						newPipeGameState.SetRotatedBoardCell(column, row);
						possibleStatesList.add(newPipeGameState);
						
						newPipeGameState = new PipeGameState(rotatedRightPipeCellBoardAdapter(newPipeGameState.GetState(), column, row), currentState.GetRotatedBoardCells());
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
	

	private PipeGameBoardAdapter rotatedRightPipeCellBoardAdapter(PipeGameBoardAdapter boardAdapter, int column, int row) {
		PipeGameBoardAdapter newPipeGameAdapter = new PipeGameBoardAdapter(boardAdapter);
		char boardChar = newPipeGameAdapter.GetPipeChar(column, row);

		switch (boardChar) {
		case '-':
			newPipeGameAdapter.SetPipeCellChar(column, row, '|');
			break;
		case 'J':
			newPipeGameAdapter.SetPipeCellChar(column, row, 'L');
			break;
		case '|':
			newPipeGameAdapter.SetPipeCellChar(column, row, '-');
			break;
		case 'L':
			newPipeGameAdapter.SetPipeCellChar(column, row, 'F');
			break;
		case 'F':
			newPipeGameAdapter.SetPipeCellChar(column, row, '7');
			break;
		case '7':
			newPipeGameAdapter.SetPipeCellChar(column, row, 'J');
			break;
		}
		
		return newPipeGameAdapter;
	}

	@Override
	public boolean IsGoalState(PipeGameState state) {
		PipeGameBoardAdapter boardAdapter = state.GetState();
		Pair<Integer, Integer> cellPlace = state.GetState().GetStartCellPosition();
		List<Pair<Integer, Integer>> checkingCellsList = new LinkedList<>();
		HashSet<Pair<Integer, Integer>> checkedCellPlaces = new HashSet<>();
		
		if(cellPlace == null) {
			return false;
		}
		
		checkingCellsList.add(cellPlace);
		
		while(!checkingCellsList.isEmpty()) {
			cellPlace = checkingCellsList.get(0);
			checkingCellsList.remove(0);
			List<Pair<Integer, Integer>> reachableCells = getReachableCells(boardAdapter, cellPlace, checkedCellPlaces);
			checkingCellsList.addAll(reachableCells);
			
			if(boardAdapter.GetPipeChar(cellPlace) == 'g') {
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	// Priority it higher as the length of connected pipes, and the state can be connected to start state
	public int GetStatePriority(PipeGameState state) {
		int priority = 0;
		List<Pair<Integer, Integer>> connectedPipes = getConnectedPipesFromStart(state);

		if(canBeConnectedToStartState(state, connectedPipes)) {
			priority =  connectedPipes.size();
		}
		
		return priority;
	}
	
	private boolean canBeConnectedToStartState(PipeGameState state, List<Pair<Integer, Integer>> connectedPipes) {
		boolean canBeConnectedToLastState = false;
		Pair<Integer, Integer> currentRotatedCell = state.GetRotatedCellPosition();
		char currentRotatedCellChar = state.GetState().GetPipeChar(currentRotatedCell);
		PipeGameBoardAdapter currentPipeGameBoardAdapter = state.GetState();
		
		
		// Check if there are near connected to start pipe cells
		List<Pair<Integer, Integer>> nearConnectedToStartPipes = getNearConnectedToStartPipes(connectedPipes, currentRotatedCell);
		
		if(nearConnectedToStartPipes.isEmpty()) {
			return false;
		}
		
		for (Pair<Integer, Integer> nearConnectedToStartPipeCell : nearConnectedToStartPipes) {
			char nearCellChar = currentPipeGameBoardAdapter.GetPipeChar(nearConnectedToStartPipeCell);
			
			if(nearCellChar == 's') {
				canBeConnectedToLastState = true;
			}else {
				List<Pair<Integer, Integer>> connectedToNearPipeAndStartPipes = getNearConnectedToStartPipes(connectedPipes,
																									nearConnectedToStartPipeCell);
				
				for (Pair<Integer, Integer> connectedToNearPipeCell : connectedToNearPipeAndStartPipes) {
					char connectedToNearCellChar = currentPipeGameBoardAdapter.GetPipeChar(connectedToNearPipeCell);
					char[] possibleConnectionStatesOfLastRotatedCell = getPossibleConnectionStatesOfPipeCell(nearConnectedToStartPipeCell,
							connectedToNearPipeCell, nearCellChar, connectedToNearCellChar);
					
					for (char stateOfLastRotatedCell : possibleConnectionStatesOfLastRotatedCell) {
						char[] possibleConnectionStatesOfCurrentRotatedCell = getPossibleConnectionStatesOfPipeCell(currentRotatedCell,
								nearConnectedToStartPipeCell, currentRotatedCellChar, stateOfLastRotatedCell);
						
						if(possibleConnectionStatesOfCurrentRotatedCell != null) {
							return true;
						}
					}
				}
			}
		}
		
		return canBeConnectedToLastState;
	}

	private List<Pair<Integer, Integer>> getNearConnectedToStartPipes(List<Pair<Integer, Integer>> connectedPipes,
			Pair<Integer, Integer> currentRotatedCell) {
		List<Pair<Integer, Integer>> nearConnectedToStartPipes = new LinkedList<>();
		Pair<Integer, Integer> upperCell = new Pair<>(currentRotatedCell.getKey() - 1, currentRotatedCell.getValue());
		Pair<Integer, Integer> belowCell = new Pair<>(currentRotatedCell.getKey() + 1, currentRotatedCell.getValue());
		Pair<Integer, Integer> leftCell = new Pair<>(currentRotatedCell.getKey(), currentRotatedCell.getValue() - 1);
		Pair<Integer, Integer> rightCell = new Pair<>(currentRotatedCell.getKey(), currentRotatedCell.getValue() + 1);
		
		for (Pair<Integer, Integer> pipeCell : connectedPipes) {
			if(compareCells(pipeCell, upperCell) || compareCells(pipeCell, belowCell) ||
					compareCells(pipeCell, leftCell) || compareCells(pipeCell, rightCell)) {
				nearConnectedToStartPipes.add(pipeCell);
			}
		}
		
		return nearConnectedToStartPipes;
	}

	private char[] getPossibleConnectionStatesOfPipeCell(Pair<Integer, Integer> rotatingPipeCell,
					Pair<Integer, Integer> fixedPipeCell, char rotatingPipeCellChar, char fixedPipeCellChar) {
		char[] possibleConnectionStatesOfPipeCell = "".toCharArray();
		int columnDif = rotatingPipeCell.getKey() - fixedPipeCell.getKey();
		int rowDif = rotatingPipeCell.getValue() - fixedPipeCell.getValue();
		
		if(columnDif == 1 && rowDif == 0) {
			if(fixedPipeCellChar == 's' || fixedPipeCellChar == '|' || fixedPipeCellChar == 'F' || fixedPipeCellChar == '7') {
				if(rotatingPipeCellChar == '-' || rotatingPipeCellChar == '|') {
					possibleConnectionStatesOfPipeCell = "|".toCharArray();
				}else {
					possibleConnectionStatesOfPipeCell = "JL".toCharArray();
				}
			}
		}else if(columnDif == -1 && rowDif == 0) {
			if(fixedPipeCellChar == 's' || fixedPipeCellChar == '|' || fixedPipeCellChar == 'J' || fixedPipeCellChar == 'L') {
				if(rotatingPipeCellChar == '-' || rotatingPipeCellChar == '|') {
					possibleConnectionStatesOfPipeCell = "|".toCharArray();
				}else {
					possibleConnectionStatesOfPipeCell = "F7".toCharArray();
				}
			}
		}else if(columnDif == 0 && rowDif == 1) {
			if(fixedPipeCellChar == 's' || fixedPipeCellChar == '-' || fixedPipeCellChar == 'F' || fixedPipeCellChar == 'L') {
				if(rotatingPipeCellChar == '-' || rotatingPipeCellChar == '|') {
					possibleConnectionStatesOfPipeCell = "-".toCharArray();
				}else {
					possibleConnectionStatesOfPipeCell = "J7".toCharArray();
				}
			}
		}else if(columnDif == 0 && rowDif == -1) {
			if(fixedPipeCellChar == 's' || fixedPipeCellChar == '-' || fixedPipeCellChar == 'J' || fixedPipeCellChar == '7') {
				if(rotatingPipeCellChar == '-' || rotatingPipeCellChar == '|') {
					possibleConnectionStatesOfPipeCell = "-".toCharArray();
				}else {
					possibleConnectionStatesOfPipeCell = "FL".toCharArray();
				}
			}
		}
		
		return possibleConnectionStatesOfPipeCell;
	}
	
	private boolean compareCells(Pair<Integer, Integer> cell, Pair<Integer, Integer> otherCell) {
		if((cell.getKey() == otherCell.getKey()) && (cell.getValue() == otherCell.getValue())) {
			return true;
		}
		
		return false;
	}

	private List<Pair<Integer, Integer>> getConnectedPipesFromStart(PipeGameState state) {
		List<Pair<Integer, Integer>> connectedPipes = new LinkedList<>();
		List<Pair<Integer, Integer>> checkingCellsList = new LinkedList<>();
		HashSet<Pair<Integer, Integer>> checkedCellPlaces = new HashSet<>();
		PipeGameBoardAdapter boardAdapter = state.GetState();
		Pair<Integer, Integer> startCellPlace = state.GetState().GetStartCellPosition();
		
		if(startCellPlace != null) {
			checkingCellsList.add(startCellPlace);
			connectedPipes.add(startCellPlace);
			
			while(!checkingCellsList.isEmpty()) {
				Pair<Integer, Integer> cellPlace = checkingCellsList.get(0);
				checkingCellsList.remove(0);
				
				List<Pair<Integer, Integer>> reachableCells = getReachableCells(boardAdapter, cellPlace, checkedCellPlaces);
				
				checkingCellsList.addAll(reachableCells);
				connectedPipes.addAll(reachableCells);
			}
		}
		
		return connectedPipes;
	}
	
	private List<Pair<Integer, Integer>> getReachableCells(PipeGameBoardAdapter boardAdapter, 
			Pair<Integer, Integer> cellPlace, HashSet<Pair<Integer, Integer>> checkedCellPlaces) {
		List<Pair<Integer, Integer>> reachableCells = new LinkedList<>();
		char cellChar = boardAdapter.GetPipeChar(cellPlace);
		
		switch (cellChar) {
		case 's':
			addNearCellIfReachable(boardAdapter, cellPlace, "up", reachableCells, checkedCellPlaces);
			addNearCellIfReachable(boardAdapter, cellPlace, "down", reachableCells, checkedCellPlaces);
			addNearCellIfReachable(boardAdapter, cellPlace, "left", reachableCells, checkedCellPlaces);
			addNearCellIfReachable(boardAdapter, cellPlace, "right", reachableCells, checkedCellPlaces);
			break;
		case '-':
			addNearCellIfReachable(boardAdapter, cellPlace, "left", reachableCells, checkedCellPlaces);
			addNearCellIfReachable(boardAdapter, cellPlace, "right", reachableCells, checkedCellPlaces);
			break;
		case 'J':
			addNearCellIfReachable(boardAdapter, cellPlace, "up", reachableCells, checkedCellPlaces);
			addNearCellIfReachable(boardAdapter, cellPlace, "left", reachableCells, checkedCellPlaces);
			break;
		case '|':
			addNearCellIfReachable(boardAdapter, cellPlace, "up", reachableCells, checkedCellPlaces);
			addNearCellIfReachable(boardAdapter, cellPlace, "down", reachableCells, checkedCellPlaces);
			break;
		case 'L':
			addNearCellIfReachable(boardAdapter, cellPlace, "up", reachableCells, checkedCellPlaces);
			addNearCellIfReachable(boardAdapter, cellPlace, "right", reachableCells, checkedCellPlaces);
			break;
		case 'F':
			addNearCellIfReachable(boardAdapter, cellPlace, "down", reachableCells, checkedCellPlaces);
			addNearCellIfReachable(boardAdapter, cellPlace, "right", reachableCells, checkedCellPlaces);
			break;
		case '7':			
			addNearCellIfReachable(boardAdapter, cellPlace, "left", reachableCells, checkedCellPlaces);
			addNearCellIfReachable(boardAdapter, cellPlace, "down", reachableCells, checkedCellPlaces);
			break;
		}
		
		return reachableCells;
	}

	private void addNearCellIfReachable(PipeGameBoardAdapter boardAdapter, Pair<Integer, Integer> cellPlace,
			String direction, List<Pair<Integer, Integer>> reachableCells,
			HashSet<Pair<Integer, Integer>> checkedCellPlaces) {
		if(cellPlace == null) {
			return;
		}
		
		int cellColumn = cellPlace.getKey();
		int cellRow = cellPlace.getValue();
		int lastColumnIndex = boardAdapter.GetColumnsNumber() - 1;
		int lastRowIndex = boardAdapter.GetRowsNumber() - 1;
		
		switch (direction) {
		case "up":
			cellColumn--;
			Pair<Integer, Integer> upperCellPlace = new Pair<Integer, Integer>(cellColumn, cellRow);
			
			if(!checkedCellPlaces.contains(upperCellPlace) && (cellColumn >= 0)) {
				char upperCellChar = boardAdapter.GetPipeChar(upperCellPlace);

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
				char lowerCellChar = boardAdapter.GetPipeChar(lowerCellPlace);

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
				char leftCellChar = boardAdapter.GetPipeChar(leftCellPlace);

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
				char rightCellChar = boardAdapter.GetPipeChar(rightCellPlace);

				if(rightCellChar == 'g' || rightCellChar == '-' || rightCellChar == 'J' || rightCellChar == '7') {
					checkedCellPlaces.add(rightCellPlace);
					reachableCells.add(rightCellPlace);
				}
			}
			break;
		}
	}
}
