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
					state.SetPriority(searchable.CalculateStatePriority(state));
					
					if(state.GetPriority() > 0) {
						addToOpenList(state);
					}
				}
			}
		}
		
		// no solution found
		return new Solution();
	}
}
