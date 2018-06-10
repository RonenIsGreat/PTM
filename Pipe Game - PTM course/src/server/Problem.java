/**
 * 
 */
package server;

import java.util.List;

/**
 * @author ronen
 *
 */
public class Problem {

	// Data member
	private List<String> m_problem;
	
	/**
	 * Ctor
	 */
	public Problem() {
		m_problem = null;
	}
	
	public List<String> GetProblemStringLines() {
		return m_problem;
	}
	
	public void SetProblemStringLines(List<String> solution) {
		m_problem = solution;
	}
	
}
