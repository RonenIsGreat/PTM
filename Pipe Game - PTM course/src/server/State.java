package server;

public abstract class State<T> implements Comparable<State>{

	private T m_state;
	protected int m_priority;
	protected State<T> m_cameFrom;
	protected String m_stateMove;
	
	/**
	 * Ctor
	 */
	public State(T state) {
		m_state = state;
		m_cameFrom = null;
		m_priority = 0;
	}
	
	public abstract void SetCameFrom(State<T> cameFrom);
	
	@Override
	public abstract boolean equals(Object other);
	
	@Override
	public abstract int hashCode();
			
	public T GetState() {
		return m_state;
	}
	
	public void SetState(T state) {
		m_state = state;
	}
	
	public State<T> GetCameFrom() {
		return m_cameFrom;
	}
	
	public int GetPriority() {
		return m_priority;
	}
	
	public void SetPriority(int cost) {
		m_priority = cost;
	}

	public String GetStateMove() {
		return m_stateMove;
	}
	
	@Override
	public int compareTo(State state) {
		return (state.GetPriority() - m_priority);
	}
}
