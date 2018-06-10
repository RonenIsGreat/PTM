/**
 * 
 */
package server;

import java.util.List;

/**
 * @author ronen
 *
 */
public interface ISearchable<T extends State> {

	T GetStartState();
	List<T> GetAllPossibleStates(T currentState);
	boolean IsGoalState(T state);
	int GetCost(T state);

}
