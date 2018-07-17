/**
 * 
 */
package server;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * @author ronen
 *
 */
public class HillClimbing extends CommonSearcher{
	@Override
	public Solution Search(ISearchable searchable) {
		addToOpenList(searchable.GetStartState());
		HashSet<State> closedSet = new HashSet<State>();
		int maxPriority = 0;
		
		// Find the next best states
		while(openList.size() > 0) {
			List<State> bestSuccessorsStates = new LinkedList<>();
			
			for (State currentState : openList) {
				closedSet.add(currentState);
				
				if(searchable.IsGoalState(currentState)) {
					// back trace through the parents
					return backTrace(currentState, searchable.GetStartState());
				}
				
				List<State> successors = (searchable.GetAllPossibleStates(currentState));
				
				for (State successor : successors) {
					if(!closedSet.contains(successor) && !openList.contains(successor)) {
						successor.SetCameFrom(currentState);
						successor.SetPriority(searchable.GetStatePriority(successor));
						
						if(successor.GetPriority() > maxPriority) {
							bestSuccessorsStates.clear();
							bestSuccessorsStates.add(successor);
							maxPriority = successor.GetPriority();
						} else if(successor.GetPriority() == maxPriority) {
							bestSuccessorsStates.add(successor);
						}
					}
				}
			}
			
			// Clear old best states
			openList.clear();
			
			// Set the new best states
			for (State state : bestSuccessorsStates) {
				addToOpenList(state);
			}
		}
		
		// no solution found
		return new Solution();
	}
}
