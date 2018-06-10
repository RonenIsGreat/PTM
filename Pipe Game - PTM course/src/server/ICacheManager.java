/**
 * 
 */
package server;

/**
 * @author ronen
 *
 */
public interface ICacheManager {

	Solution GetSavedSolution(Problem problem);
	void SaveSolution(Solution solution, Problem problem);
	
}
