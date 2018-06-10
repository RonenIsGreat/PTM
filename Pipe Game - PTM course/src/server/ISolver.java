/**
 * 
 */
package server;

/**
 * @author ronen
 *
 */
public interface ISolver {

	Solution GetSolution(Problem problem, ISearcher searcher);

}
