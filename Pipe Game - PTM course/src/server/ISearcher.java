/**
 * 
 */
package server;

/**
 * @author ronen
 *
 */
public interface ISearcher {

	Solution Search(ISearchable searchable);
	int GetNumberOfNodesEvaluated();
	
}
