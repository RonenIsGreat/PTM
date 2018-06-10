/**
 * 
 */
package server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * @author ronen
 *
 */
public class BestFirstSearch extends CommonSearcher{

	@Override
	public Solution Search(ISearchable searchable) {
		addToOpenList(searchable.GetStartState());
		HashSet<State> closedSet = new HashSet<State>();
		
		while(openList.size() > 0) {
			State currentState = PopOpenList(); //dequeue
			closedSet.add(currentState);
			
			if(searchable.IsGoalState(currentState)) {
				// back trace through the parents
				return backTrace(currentState, searchable.GetStartState());
			}
			
			List<State> successors = searchable.GetAllPossibleStates(currentState);
			
			for (State state : successors) {
				if(!closedSet.contains(state) && !openList.contains(state)) {
					state.SetCameFrom(currentState);
					state.SetCost(searchable.GetCost(state));
					addToOpenList(state);
				} else{
					//System.out.println("t");
				}
			}
		}
		
		// no solution found
		return new Solution();
	}

	private Solution backTrace(State goalState, State startState) {
		Solution solution = new Solution();
		LinkedList<String> backTraceChangesList = new LinkedList<String>();
		State state = goalState;
		
		while(!state.equals(startState)) {
			backTraceChangesList.add(state.GetStateMove());
			state = state.GetCameFrom();
		}
		
		Collections.reverse(backTraceChangesList);
		solution.SetSolutionStringLines(backTraceChangesList);
		return solution;
	}

}
