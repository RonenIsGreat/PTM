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
			long startTime = System.currentTimeMillis();
			
			for (State state : successors) {
				if(!closedSet.contains(state) && !openList.contains(state)) {
					state.SetCameFrom(currentState);
					addToOpenList(state);
				} else{
					//System.out.println("t");
				}
			}
			
			long endTime = System.currentTimeMillis();
			System.out.println("successors took: " + (endTime - startTime) + " miliseconds");
		}
		// no solution found
		return null;
	}

	private Solution backTrace(State goalState, State startState) {
		Solution solution = new Solution();
		solution.SetSolutionStringLines(goalState.GetBackTraceLines(startState));
		return solution;
	}

}
