/**
 * 
 */
package server;

import java.util.List;

/**
 * @author ronen
 *
 */
public class Solution {

	// Data member
		private List<String> m_solution;
		
		/**
		 * Ctor
		 */
		public Solution() {
			m_solution = null;
		}
		
		public List<String> GetSolutionStringLines() {
			return m_solution;
		}
		
		public void SetSolutionStringLines(List<String> solution) {
			m_solution = solution;
		}
	
}
