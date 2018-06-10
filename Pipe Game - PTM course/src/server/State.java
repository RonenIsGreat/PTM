package server;

import java.util.List;

public abstract class State<T,G> implements Comparable<State>{

	private T m_state;
	protected int m_cost;
	protected State<T,G> m_cameFrom;
	protected G m_stateMove;
	
	/**
	 * Ctor
	 */
	public State(T state) {
		m_state = state;
		m_cameFrom = null;
		m_cost = 0;
	}
	
	public abstract void SetCameFrom(State<T,G> cameFrom);
		
	public T GetState() {
		return m_state;
	}
	
	public State<T,G> GetCameFrom() {
		return m_cameFrom;
	}
	
	public int GetCost() {
		return m_cost;
	}

	public G GetStateMove() {
		return m_stateMove;
	}
	
	@Override
	public int compareTo(State state) {
		return (m_cost - state.GetCost());
	}
	
	public abstract List<String> GetBackTraceLines(State startState);
}
