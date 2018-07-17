/**
 * 
 */
package server;

import java.util.Collections;
import java.util.LinkedList;
import java.util.PriorityQueue;

/**
 * @author ronen
 *
 */
public abstract class CommonSearcher implements ISearcher {
	
	protected PriorityQueue<State> openList;
	private int m_evaluatedNodes;
	
	/**
	 * Ctor
	 */
	public CommonSearcher() {
		openList = new PriorityQueue<State>();
		m_evaluatedNodes = 0;
	} 
	
	@Override
	public abstract Solution Search(ISearchable searchable);

	@Override
	public int GetNumberOfNodesEvaluated() {
		return m_evaluatedNodes;
	}

	protected State PopOpenList() {
		m_evaluatedNodes++;
		return openList.poll();
	}
	
	protected void addToOpenList(State state) {
		openList.add(state);
	}
	
	protected Solution backTrace(State goalState, State startState) {
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
