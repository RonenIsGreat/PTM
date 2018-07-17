/**
 * 
 */
package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * @author ronen
 *
 */
public class ClientHandler implements IClientHandler{

	// Data Members
	private ICacheManager m_cacheManager;
	private ISolver m_solver;
	
	/**
	 * Ctor
	 */
	public ClientHandler(ICacheManager cacheManager, ISolver solver) {
		this.m_cacheManager = cacheManager;
		this.m_solver = solver;
	}
	
	// Methods
	
	@Override
	public void handleClient(BufferedReader inFromClient, PrintWriter outToClient, String doneStr) {
		Problem problem = getProblem(inFromClient, doneStr);
		long startTime = System.currentTimeMillis();
		Solution solution = getSolution(problem);
		long endTime = System.currentTimeMillis();
		System.out.println("Solve took: " + (endTime - startTime) + " miliseconds");
		sendSolution(solution, outToClient, doneStr);
	}
	
	private Problem getProblem(BufferedReader inFromClient, String doneStr){
		Problem problem = new Problem();
		ArrayList<String> problemList = new ArrayList<String>();

		try {
			String line;
			while(!(line=inFromClient.readLine()).equals(doneStr)) {
				problemList.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		problem.SetProblemStringLines(problemList);
		return problem;
	}
	
	private Solution getSolution(Problem problem) {
		Solution solution = m_cacheManager.GetSavedSolution(problem);
		
		if(solution.GetSolutionStringLines().isEmpty()) {
			solution = m_solver.GetSolution(problem, new BestFirstSearch());
			m_cacheManager.SaveSolution(solution, problem);
		}
		
		return solution;
	}
	
	private void sendSolution(Solution solution, PrintWriter outToClient, String doneStr) {
		for (String line : solution.GetSolutionStringLines()) {
			outToClient.println(line);
			outToClient.flush();
		}
		
		outToClient.println(doneStr);
		outToClient.flush();
	}
}
