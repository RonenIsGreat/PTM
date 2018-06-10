/**
 * 
 */
package server;

/**
 * @author ronen
 *
 */
public class PipeGameSolver implements ISolver{

	/**
	 * Ctor
	 */
	public PipeGameSolver() {
		
	}

	// Methods
	
	@Override
	public Solution GetSolution(Problem problem, ISearcher searcher) {
		PipeGameAdapter pipeGameObjectAdapter = new PipeGameAdapter(problem);
		PipeGamePuzzle pipeGameSearchable = new PipeGamePuzzle(pipeGameObjectAdapter);
		return searcher.Search(pipeGameSearchable);		
	}	
	
}
