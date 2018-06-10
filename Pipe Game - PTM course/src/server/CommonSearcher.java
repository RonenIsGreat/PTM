/**
 * 
 */
package server;

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
}
