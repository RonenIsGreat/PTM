/**
 * 
 */
package server;

import java.util.HashSet;
import java.util.List;

/**
 * @author ronen
 *
 */
public class DFS extends CommonSearcher{
	@Override
	public Solution Search(ISearchable searchable) {
		addToOpenList(searchable.GetStartState());
		HashSet<State> closedSet = new HashSet<State>();
		
		while(openList.size() > 0) {
			State currentState = PopOpenList(); //dequeue
			closedSet.add(currentState);
			int priority = 0;
			
			if(searchable.IsGoalState(currentState)) {
				// back trace through the parents
				return backTrace(currentState, searchable.GetStartState());
			}
			
			List<State> successors = searchable.GetAllPossibleStates(currentState);
			
			for (State state : successors) {
				if(!closedSet.contains(state) && !openList.contains(state)) {
					state.SetCameFrom(currentState);
					int currentStatePrio = state.GetPriority() + priority;
					priority++;
					state.SetPriority(priority);
					addToOpenList(state);
				}
			}
		}
		
		// no solution found
		return new Solution();
	}
}
