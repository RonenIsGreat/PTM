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
		PipeGameBoardAdapter pipeGameObjectAdapter = new PipeGameBoardAdapter(problem);
		PipeGamePuzzle pipeGameSearchable = new PipeGamePuzzle(pipeGameObjectAdapter);
		return searcher.Search(pipeGameSearchable);		
	}	
	
}
