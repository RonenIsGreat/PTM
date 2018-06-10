package server;

import java.util.List;

public abstract class State<T> implements Comparable<State>{

	private T m_state;
	protected int m_cost;
	protected State<T> m_cameFrom;
	protected String m_stateMove;
	protected boolean[][] m_rotatedBoardCells;
	
	/**
	 * Ctor
	 */
	public State(T state) {
		m_state = state;
		m_cameFrom = null;
		m_cost = 0;
	}
	
	public abstract void SetCameFrom(State<T> cameFrom);
			
	public T GetState() {
		return m_state;
	}
	
	public State<T> GetCameFrom() {
		return m_cameFrom;
	}
	
	public int GetCost() {
		return m_cost;
	}
	
	public void SetCost(int cost) {
		m_cost = cost;
	}

	public String GetStateMove() {
		return m_stateMove;
	}
	
	@Override
	public int compareTo(State state) {
		return (state.GetCost() - m_cost);
	}

	public boolean[][] GetRotatedBoardCells() {
		return m_rotatedBoardCells;
	}
}
