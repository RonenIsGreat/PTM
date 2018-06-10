/**
 * 
 */
package server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javafx.util.Pair;

/**
 * @author ronen
 *
 */
public class PipeGameState extends State<PipeGameBoard, Pair<Integer, Integer>> {
	
	public PipeGameState(PipeGameBoard state) {
		super(state);
	}
	
	@Override
	public void SetCameFrom(State<PipeGameBoard, Pair<Integer, Integer>> cameFrom) {
		m_cameFrom = cameFrom;
		m_cost = cameFrom.GetCost() + 1;
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
	
	@Override
	public List<String> GetBackTraceLines(State startState) {
		LinkedList<Pair<Integer, Integer>> stateMovesList = new LinkedList<>();
		stateMovesList.add(GetStateMove());
		PipeGameState state = this;
		
		while(!state.equals(startState)) {
			stateMovesList.add(state.GetStateMove());
			state = (PipeGameState) state.GetCameFrom();
		}
		
		Collections.reverse(stateMovesList);
		return convertStateMovesToBackTraceLines(stateMovesList);
	}
	
	private List<String> convertStateMovesToBackTraceLines(LinkedList<Pair<Integer, Integer>> stateMovesList) {
		HashMap<Pair<Integer, Integer>, Integer> stateMovesCount = new HashMap<>(); 
		
	}

	private void SetStateMove(State<PipeGameBoard, Pair<Integer, Integer>> cameFrom) {
		Pair<Integer, Integer> stateMove = null;
		Boolean flag = false;
		ArrayList<ArrayList<Character>> cameFromBoard = cameFrom.GetState().GetBoard();
		ArrayList<ArrayList<Character>> thisBoard = GetState().GetBoard();
		
		for (int column = 0; (column < cameFromBoard.size()) && !flag; column++) {
			for (int row = 0; (row < cameFromBoard.get(column).size()) && !flag; row++) {
				char cameFromChar = cameFromBoard.get(column).get(row);
				char thisChar = thisBoard.get(column).get(row);
				
				if(cameFromChar != thisChar) {
					stateMove = new Pair<Integer, Integer>(column, row);
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
