/**
 * 
 */
package server;

import java.io.BufferedReader;
import java.io.PrintWriter;

/**
 * @author ronen
 *
 */
public interface IClientHandler {
	
	Problem getClientProblem(BufferedReader inFromClient, String doneStr);
	
	void solveClientProblem(Problem problem, PrintWriter outToClient, String doneStr);
	
}
